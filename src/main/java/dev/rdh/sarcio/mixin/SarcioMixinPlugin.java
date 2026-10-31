package dev.rdh.sarcio.mixin;

import dev.rdh.sarcio.Asm;
import dev.rdh.sarcio.Feature;
import dev.rdh.sarcio.Features;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Set;

public class SarcioMixinPlugin implements IMixinConfigPlugin {
	private static final Logger LOGGER = LogManager.getLogger("Sarcio");

	private static final String FEATURE_DESC = Type.getDescriptor(Feature.class);

	private final Set<String> reported = new ObjectOpenHashSet<>();

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		if (!Features.anyDisabled()) return true;

		ClassNode[] nodes = classNodes(mixinClassName);
		String feature = feature(nodes[0].invisibleAnnotations);
		String by = feature == null ? null : Features.disabledBy(feature);
		if (by != null) {
			if (this.reported.add(mixinClassName)) LOGGER.info("Not applying mixin {}: feature {} disabled by {}", mixinClassName, feature, by);
			return false;
		}

		for (ClassNode node : nodes) {
			strip(node);
		}
		return true;
	}

	private static String feature(List<AnnotationNode> annotations) {
		if (annotations == null) return null;
		for (AnnotationNode annotation : annotations) {
			if (FEATURE_DESC.equals(annotation.desc)) return (String) annotation.values.get(1);
		}
		return null;
	}

	private static boolean disabled(List<AnnotationNode> annotations) {
		String feature = feature(annotations);
		return feature != null && !Features.isEnabled(feature);
	}

	private static void strip(ClassNode node) {
		node.methods.removeIf(method -> disabled(method.invisibleAnnotations));
		node.fields.removeIf(field -> disabled(field.invisibleAnnotations) && !isInitialized(node, field));
	}

	private static boolean isInitialized(ClassNode node, FieldNode field) {
		for (MethodNode method : node.methods) {
			if (!method.name.equals("<init>") && !method.name.equals("<clinit>")) continue;
			for (AbstractInsnNode insn : method.instructions) {
				if ((insn.getOpcode() == Opcodes.PUTFIELD || insn.getOpcode() == Opcodes.PUTSTATIC) && ((FieldInsnNode) insn).name.equals(field.name)) {
					return true;
				}
			}
		}
		return false;
	}

	private static ClassNode[] classNodes(String mixinClassName) {
		try {
			Object mixin = (Object) MixinInternals.MIXIN.invokeExact((Object) ClassInfo.forName(mixinClassName));
			Object state = (Object) MixinInternals.PENDING_STATE.invokeExact(mixin);
			return new ClassNode[] {
				(ClassNode) (Object) MixinInternals.CLASS_NODE.invokeExact(state),
				(ClassNode) (Object) MixinInternals.VALIDATION_CLASS_NODE.invokeExact(state),
			};
		} catch (Throwable throwable) {
			throw new IllegalStateException("Could not apply disabled features to mixin " + mixinClassName, throwable);
		}
	}

	private static final class MixinInternals {
		private static final MethodHandle MIXIN;
		private static final MethodHandle PENDING_STATE;
		private static final MethodHandle CLASS_NODE;
		private static final MethodHandle VALIDATION_CLASS_NODE;

		static {
			try {
				Class<?> mixinInfo = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo");
				Class<?> state = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo$State");
				Class<?> mixinClassNode = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo$MixinClassNode");
				MIXIN = getter(ClassInfo.class, "mixin", mixinInfo);
				PENDING_STATE = getter(mixinInfo, "pendingState", state);
				CLASS_NODE = getter(state, "classNode", ClassNode.class);
				VALIDATION_CLASS_NODE = getter(state, "validationClassNode", mixinClassNode);
			} catch (ReflectiveOperationException exception) {
				throw new ExceptionInInitializerError(exception);
			}
		}

		private static MethodHandle getter(Class<?> owner, String name, Class<?> type) throws ReflectiveOperationException {
			return MethodHandles.privateLookupIn(owner, MethodHandles.lookup())
				.findGetter(owner, name, type)
				.asType(MethodType.methodType(Object.class, Object.class));
		}
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
		if (mixinClassName.endsWith("bugfix.LazySupplierMixin")) {
			Asm.asmLazyLoadBase(targetClass);
		}
	}
}

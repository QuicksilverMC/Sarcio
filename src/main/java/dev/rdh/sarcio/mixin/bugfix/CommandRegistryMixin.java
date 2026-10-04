package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Locale;
import java.util.Map;
import net.minecraft.server.command.handler.CommandRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandRegistry.class)
public class CommandRegistryMixin {
	@WrapOperation(
		method = {"run(Lnet/minecraft/server/command/source/CommandSource;Ljava/lang/String;)I", "getSuggestions"},
		at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;")
	)
	private Object sarcio$ignoreCommandCase(Map<?, ?> commands, Object name, Operation<Object> original) {
		Object command = original.call(commands, name);
		return command != null ? command : original.call(commands, ((String) name).toLowerCase(Locale.ROOT));
	}
}

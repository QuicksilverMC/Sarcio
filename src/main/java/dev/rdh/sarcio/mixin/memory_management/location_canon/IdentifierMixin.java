package dev.rdh.sarcio.mixin.memory_management.location_canon;

import com.llamalad7.mixinextras.expression.Definition;import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Identifier.class)
abstract class IdentifierMixin {
	@ModifyExpressionValue(method = "<init>(I[Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;toLowerCase()Ljava/lang/String;"))
	private String internDomain(String s) {
		return s.intern();
	}

	@Definition(id = "resourcePath", field = "Lnet/minecraft/resource/Identifier;path:Ljava/lang/String;")
	@Expression("this.resourcePath = @(?)")
	@ModifyExpressionValue(method = "<init>(I[Ljava/lang/String;)V", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private String internPath(String s) {
		return s == null ? null : s.intern();
	}
}

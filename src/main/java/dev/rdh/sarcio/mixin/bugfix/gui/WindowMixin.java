package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.Window;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Window.class)
public class WindowMixin {
    @Shadow
    private int scale;

    @WrapOperation(method = "<init>(Lnet/minecraft/client/Minecraft;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/Window;scale:I", opcode = Opcodes.PUTFIELD))
    private void sarcio$roundUnicodeScaleUp(Window window, int value, Operation<Void> original) {
        original.call(window, value == this.scale - 1 ? value + 2 : value);
    }

    @Definition(id = "scale", field = "Lnet/minecraft/client/render/Window;scale:I")
    @Expression("this.scale != 1")
    @ModifyExpressionValue(method = "<init>(Lnet/minecraft/client/Minecraft;)V", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean sarcio$roundScaleOneUp(boolean original) {
        return true;
    }
}

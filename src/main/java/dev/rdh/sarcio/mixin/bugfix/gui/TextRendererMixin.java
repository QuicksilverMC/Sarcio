package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextRenderer;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {
    @Shadow
    private void reset() {
        throw new AssertionError();
    }

    @Inject(method = "draw(Ljava/lang/String;FFIZ)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/TextRenderer;drawLayer(Ljava/lang/String;FFIZ)I", ordinal = 0, shift = At.Shift.AFTER))
    private void sarcio$resetStyleBetweenPasses(CallbackInfoReturnable<Integer> cir) {
        this.reset();
    }

    @Inject(method = {"setUnicode", "reload"}, at = @At("TAIL"))
    private void sarcio$rewrapChat(CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.gui != null && mc.textRenderer == (Object) this) {
            mc.gui.getChat().reset();
        }
    }

    @Definition(id = "glyphSizes", field = "Lnet/minecraft/client/render/TextRenderer;glyphSizes:[B")
    @Definition(id = "chr", local = @Local(type = char.class, argsOnly = true))
    @Expression("this.glyphSizes[chr] >>> 4")
    @ModifyExpressionValue(method = {"drawUnicodeGlyph", "getWidth(C)I"}, at = @At("MIXINEXTRAS:EXPRESSION"))
    private int sarcio$unsignedGlyphStart(int original) {
        return original & 0xF;
    }
}

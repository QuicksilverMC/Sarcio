package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {
    @Shadow
    private void resetStyles() {
        throw new AssertionError();
    }

    @Inject(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;renderString(Ljava/lang/String;FFIZ)I", ordinal = 0, shift = At.Shift.AFTER))
    private void sarcio$resetStyleBetweenPasses(CallbackInfoReturnable<Integer> cir) {
        this.resetStyles();
    }

    @Inject(method = {"setUnicodeFlag", "onResourceManagerReload"}, at = @At("TAIL"))
    private void sarcio$rewrapChat(CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.ingameGUI != null && mc.fontRendererObj == (Object) this) {
            mc.ingameGUI.getChatGUI().refreshChat();
        }
    }
}

package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.game.BookEditScreen;
import net.minecraft.client.render.platform.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {
    @WrapMethod(method = "render")
    private void sarcio$blendBook(int mouseX, int mouseY, float partialTicks, Operation<Void> original) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        original.call(mouseX, mouseY, partialTicks);
        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void sarcio$drawBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.renderBackground();
    }
}

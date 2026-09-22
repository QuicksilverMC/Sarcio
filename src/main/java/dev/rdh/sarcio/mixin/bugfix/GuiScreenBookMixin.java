package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.GuiScreenBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenBook.class)
public abstract class GuiScreenBookMixin extends GuiScreen {
    @WrapMethod(method = "drawScreen")
    private void sarcio$blendBook(int mouseX, int mouseY, float partialTicks, Operation<Void> original) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        original.call(mouseX, mouseY, partialTicks);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void sarcio$drawBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.drawWorldBackground(0);
    }
}

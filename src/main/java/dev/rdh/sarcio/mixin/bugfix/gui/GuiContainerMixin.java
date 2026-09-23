package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin {
    @Shadow
    private Slot theSlot;

    @Shadow
    protected abstract void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType);

    @Shadow
    protected abstract boolean checkHotbarKeys(int keyCode);

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void sarcio$handleMouseBoundKeys(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        int keyCode = mouseButton - 100;
        if (keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) {
            mc.thePlayer.closeScreen();
            ci.cancel();
        } else if (this.checkHotbarKeys(keyCode)) {
            ci.cancel();
        } else if (keyCode == mc.gameSettings.keyBindDrop.getKeyCode() && this.theSlot != null && this.theSlot.getHasStack()) {
            this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, GuiScreen.isCtrlKeyDown() ? 1 : 0, 4);
            ci.cancel();
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawGuiContainerBackgroundLayer(FII)V"))
    private void sarcio$blendBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/inventory/GuiContainer;drawTexturedModalRect(IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;II)V"))
    private void sarcio$blendSlot(Slot slotIn, CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }

    @Shadow
    protected boolean dragSplitting;

    @Shadow
    private int dragSplittingLimit;

    @Shadow
    private int dragSplittingRemnant;

    @WrapOperation(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    void sarcio$closeMenu(Minecraft instance, GuiScreen guiScreenIn, Operation<Void> original) {
        instance.thePlayer.closeScreen();
    }

    @Inject(method = "updateDragSplitting", at = @At("TAIL"))
    private void sarcio$fixCloneDragCount(CallbackInfo ci) {
        ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventory.getItemStack();
        if (itemStack != null && this.dragSplitting && this.dragSplittingLimit == 2) {
            this.dragSplittingRemnant = itemStack.getMaxStackSize();
        }
    }
}

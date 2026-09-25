package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.game.inventory.InventoryMenuScreen;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.inventory.slot.InventorySlot;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenuScreen.class)
public abstract class InventoryMenuScreenMixin {
    @Shadow
    private InventorySlot hoveredSlot;

    @Shadow
    protected abstract void clickSlot(InventorySlot slotIn, int slotId, int clickedButton, int clickType);

    @Shadow
    protected abstract boolean moveHoveredSlotToHotbar(int keyCode);

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void sarcio$handleMouseBoundKeys(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        int keyCode = mouseButton - 100;
        if (keyCode == mc.options.inventoryKey.getKeyCode()) {
            mc.player.closeMenu();
            ci.cancel();
        } else if (this.moveHoveredSlotToHotbar(keyCode)) {
            ci.cancel();
        } else if (keyCode == mc.options.dropKey.getKeyCode() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            this.clickSlot(this.hoveredSlot, this.hoveredSlot.index, Screen.isControlDown() ? 1 : 0, 4);
            ci.cancel();
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/game/inventory/InventoryMenuScreen;renderMenuBackground(FII)V"))
    private void sarcio$blendBackground(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }

    @Inject(method = "renderSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/game/inventory/InventoryMenuScreen;drawSprite(IILnet/minecraft/client/render/texture/TextureAtlasSprite;II)V"))
    private void sarcio$blendSlot(InventorySlot slotIn, CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
    }

    @Shadow
    protected boolean isDraggingItem;

    @Shadow
    private int clickDragMode;

    @Shadow
    private int draggedItemRemainder;

    @WrapOperation(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    void sarcio$closeMenu(Minecraft instance, Screen guiScreenIn, Operation<Void> original) {
        instance.player.closeMenu();
    }

    @Inject(method = "updateDraggedStackRemainder", at = @At("TAIL"))
    private void sarcio$fixCloneDragCount(CallbackInfo ci) {
        ItemStack itemStack = Minecraft.getInstance().player.inventory.getCursorItem();
        if (itemStack != null && this.isDraggingItem && this.clickDragMode == 2) {
            this.draggedItemRemainder = itemStack.getMaxSize();
        }
    }
}

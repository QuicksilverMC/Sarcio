package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.sound.instance.SimpleSoundInstance;
import net.minecraft.inventory.menu.InventoryMenu;
import net.minecraft.inventory.slot.InventorySlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public class InventoryMenuMixin {
    @Inject(method = "setItem", at = @At("HEAD"))
    private void sarcio$playArmorBreakSound(int slotID, ItemStack stack, CallbackInfo ci) {
        if (stack != null || slotID < 5 || slotID > 8) {
            return;
        }

        LocalClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null || player.playerMenu != (Object) this) {
            return;
        }

        InventorySlot slot = ((InventoryMenu) (Object) this).getSlot(slotID);
        ItemStack worn = slot == null ? null : slot.getItem();
        if (worn != null && worn.getItem() instanceof ArmorItem && worn.getDamage() > worn.getMaxDamage() - 2) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.of(new Identifier("random.break"), 1.0F));
        }
    }
}

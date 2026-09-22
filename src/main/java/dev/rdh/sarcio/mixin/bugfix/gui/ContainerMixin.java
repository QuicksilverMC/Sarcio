package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Container.class)
public class ContainerMixin {
    @Inject(method = "putStackInSlot", at = @At("HEAD"))
    private void sarcio$playArmorBreakSound(int slotID, ItemStack stack, CallbackInfo ci) {
        if (stack != null || slotID < 5 || slotID > 8) {
            return;
        }

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.inventoryContainer != (Object) this) {
            return;
        }

        Slot slot = ((Container) (Object) this).getSlot(slotID);
        ItemStack worn = slot == null ? null : slot.getStack();
        if (worn != null && worn.getItem() instanceof ItemArmor && worn.getItemDamage() > worn.getMaxDamage() - 2) {
            Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.break"), 1.0F));
        }
    }
}

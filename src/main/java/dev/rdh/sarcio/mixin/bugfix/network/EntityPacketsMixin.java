package dev.rdh.sarcio.mixin.bugfix.network;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityEventS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityHeadAnglesS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityMoveS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityMoveS2CPacket.class, EntityHeadAnglesS2CPacket.class, EntityEventS2CPacket.class})
public class EntityPacketsMixin {
    @Inject(method = "getEntity", at = @At("HEAD"), cancellable = true)
    private void sarcio$noWorldNoEntity(World world, CallbackInfoReturnable<Entity> cir) {
        if (world == null) {
            cir.setReturnValue(null);
        }
    }
}

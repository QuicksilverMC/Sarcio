package dev.rdh.sarcio.mixin.bugfix.network;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.handler.PacketHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.LoginS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.PacketUtils$48215440")
public class PacketThreadUtilRunnableMixin {
    @Shadow(remap = false) @Final private Packet<?> f_54564080; // packet, unnamed in feather

    @Feature("bugfix.network.drop_closed_connection_packets")
    @WrapWithCondition(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;handle(Lnet/minecraft/network/handler/PacketHandler;)V"))
    private boolean sarcio$dropPacketsFromClosedConnections(Packet<?> packet, PacketHandler handler) {
		return !(handler instanceof ClientPlayNetworkHandler client) || client.getConnection().isConnected();
	}

    @Feature("bugfix.network.drop_packets_without_world")
    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void dropPacketsWithoutWorld(CallbackInfo ci) {
        if (Minecraft.getInstance().world == null && !(this.f_54564080 instanceof LoginS2CPacket)) {
            ci.cancel();
        }
    }
}

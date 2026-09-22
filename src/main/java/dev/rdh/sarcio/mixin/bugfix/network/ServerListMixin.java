package dev.rdh.sarcio.mixin.bugfix.network;

import java.util.List;

import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerList.class)
public class ServerListMixin {
    @Shadow
    @Final
    private List<ServerData> servers;

    @Inject(method = "getServerData", at = @At("HEAD"), cancellable = true)
    private void sarcio$dontReadOutOfBounds(int index, CallbackInfoReturnable<ServerData> cir) {
        if (index < 0 || index >= this.servers.size()) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "removeServerData", at = @At("HEAD"), cancellable = true)
    private void sarcio$dontRemoveOutOfBounds(int index, CallbackInfo ci) {
        if (index < 0 || index >= this.servers.size()) {
            ci.cancel();
        }
    }
}

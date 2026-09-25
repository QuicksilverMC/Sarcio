package dev.rdh.sarcio.mixin.bugfix.network;

import java.util.List;
import net.minecraft.client.options.ServerList;
import net.minecraft.client.options.ServerListEntry;
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
    private List<ServerListEntry> entries;

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void sarcio$dontReadOutOfBounds(int index, CallbackInfoReturnable<ServerListEntry> cir) {
        if (index < 0 || index >= this.entries.size()) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "remove", at = @At("HEAD"), cancellable = true)
    private void sarcio$dontRemoveOutOfBounds(int index, CallbackInfo ci) {
        if (index < 0 || index >= this.entries.size()) {
            ci.cancel();
        }
    }
}

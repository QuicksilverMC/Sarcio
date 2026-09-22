package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerListEntryNormal.class)
public class ServerListEntryNormalMixin {
    @Shadow
    @Final
    private ServerData server;

    @WrapOperation(method = "drawEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ServerListEntryNormal;prepareServerIcon()V"))
    private void sarcio$preventIconCrash(ServerListEntryNormal instance, Operation<Void> original) {
        try {
            original.call(instance);
        } catch (Exception e) {
            this.server.setBase64EncodedIconData(null);
        }
    }
}

package dev.rdh.sarcio.mixin.bugfix.network;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerAddress.class)
public class ServerAddressMixin {
    @WrapMethod(method = "getAddress")
    private String sarcio$preventCrash(Operation<String> original) {
        try {
            return original.call();
        } catch (Exception _) {
            return "";
        }
    }
}

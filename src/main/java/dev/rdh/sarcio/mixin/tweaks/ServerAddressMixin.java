package dev.rdh.sarcio.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rdh.sarcio.util.AsyncServerPinger;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerAddress.class)
public class ServerAddressMixin {
    @WrapMethod(method = "parseAddress")
    private static String[] sarcio$deferSrvLookup(String address, Operation<String[]> original) {
        AsyncServerPinger.Ping ping = AsyncServerPinger.CURRENT.get();
        if (ping == null) {
            return original.call(address);
        }
        ping.srvHost = address;
        return new String[]{address, "25565"};
    }
}

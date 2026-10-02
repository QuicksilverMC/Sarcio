package dev.rdh.sarcio.mixin.tweaks;

import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerAddress.class)
public interface ServerAddressAccessor {
    @Invoker("<init>")
    static ServerAddress create(String address, int port) {
        throw new AssertionError();
    }
}

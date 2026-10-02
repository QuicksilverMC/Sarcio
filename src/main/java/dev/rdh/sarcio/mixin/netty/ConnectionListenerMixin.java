package dev.rdh.sarcio.mixin.netty;

import io.netty.channel.EventLoopGroup;
import net.minecraft.server.network.ConnectionListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ConnectionListener.class)
public class ConnectionListenerMixin {
    @ModifyArg(method = "bindLocal", at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/ServerBootstrap;group(Lio/netty/channel/EventLoopGroup;)Lio/netty/bootstrap/ServerBootstrap;"))
    private EventLoopGroup sarcio$useLocalGroup(EventLoopGroup nio) {
        return ConnectionListener.LOCAL_NETWORK_GROUP.get();
    }
}

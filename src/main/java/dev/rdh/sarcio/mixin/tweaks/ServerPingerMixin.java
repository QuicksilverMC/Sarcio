package dev.rdh.sarcio.mixin.tweaks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerListEntryNormal.class)
public class ServerPingerMixin {
    @Shadow
    @Final
    private ServerData server;

    @Unique
    private static final int SARCIO$PING_TIMEOUT_SECONDS = 4;

    @Unique
    private static final int SARCIO$MAX_PINGS_IN_FLIGHT = 64;

    @Unique
    private static final ExecutorService SARCIO$PINGERS = Executors.newThreadPerTaskExecutor(
        Thread.ofVirtual().name("Server Pinger #", 0).factory()
    );

    @Unique
    private static final AtomicInteger SARCIO$IN_FLIGHT = new AtomicInteger();

    @WrapOperation(method = "drawEntry", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ThreadPoolExecutor;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;"))
    private Future<?> sarcio$pingOnVirtualThread(ThreadPoolExecutor vanillaPingers, Runnable ping, Operation<Future<?>> original) {
        if (SARCIO$IN_FLIGHT.get() >= SARCIO$MAX_PINGS_IN_FLIGHT) {
            this.sarcio$failWith(EnumChatFormatting.GRAY + "Spamming...");
            return CompletableFuture.completedFuture(null);
        }

        SARCIO$IN_FLIGHT.incrementAndGet();
        return SARCIO$PINGERS.submit(() -> {
            Future<?> pinging = SARCIO$PINGERS.submit(ping);
            try {
                pinging.get(SARCIO$PING_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException timeout) {
                pinging.cancel(true);
                this.sarcio$failWith(EnumChatFormatting.RED + "Timed out");
            } catch (Exception ignored) {
                // the vanilla task reports its own failures
            } finally {
                SARCIO$IN_FLIGHT.decrementAndGet();
            }
        });
    }

    @Unique
    private void sarcio$failWith(String reason) {
        this.server.pingToServer = -1L;
        this.server.serverMOTD = reason;
    }
}

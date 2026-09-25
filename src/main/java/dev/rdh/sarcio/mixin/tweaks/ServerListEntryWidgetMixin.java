package dev.rdh.sarcio.mixin.tweaks;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.gui.widget.ServerListEntryWidget;
import net.minecraft.client.options.ServerListEntry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Formatting;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerListEntryWidget.class)
public class ServerListEntryWidgetMixin {
    @Shadow
    @Final
    private ServerListEntry entry;

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

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ThreadPoolExecutor;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;"))
    private Future<?> sarcio$pingOnVirtualThread(ThreadPoolExecutor vanillaPingers, Runnable ping) {
        if (SARCIO$IN_FLIGHT.get() >= SARCIO$MAX_PINGS_IN_FLIGHT) {
            this.sarcio$failWith(Formatting.GRAY + "Spamming...");
            return CompletableFuture.completedFuture(null);
        }

        SARCIO$IN_FLIGHT.incrementAndGet();
        return SARCIO$PINGERS.submit(() -> {
            Future<?> pinging = SARCIO$PINGERS.submit(ping);
            try {
                pinging.get(SARCIO$PING_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException timeout) {
                pinging.cancel(true);
                this.sarcio$failWith(Formatting.RED + I18n.translate("disconnect.timeout"));
            } catch (Exception ignored) {
                // the vanilla task reports its own failures
            } finally {
                SARCIO$IN_FLIGHT.decrementAndGet();
            }
        });
    }

    @Unique
    private void sarcio$failWith(String reason) {
        this.entry.ping = -1L;
        this.entry.motd = reason;
    }
}

package dev.rdh.sarcio.mixin.tweaks;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rdh.sarcio.util.AsyncServerPinger;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.client.gui.widget.ServerListEntryWidget;
import net.minecraft.client.options.ServerListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerListEntryWidget.class)
public class ServerListEntryWidgetMixin {
    @Shadow
    @Final
    private ServerListEntry entry;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ThreadPoolExecutor;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/Future;"))
    private Future<?> sarcio$pingAsync(ThreadPoolExecutor vanillaPingers, Runnable ping, Operation<Future<?>> original) {
        AsyncServerPinger.ping(this.entry, ping);
        return CompletableFuture.completedFuture(null);
    }
}

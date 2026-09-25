package dev.rdh.sarcio.mixin.bugfix;

import java.util.concurrent.FutureTask;
import net.minecraft.util.Utils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Utils.class)
public class UtilsMixin {
    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false), cancellable = true)
    private static <V> void sarcio$stopLogSpam(FutureTask<V> task, Logger logger, CallbackInfoReturnable<V> cir) {
        cir.setReturnValue(null);
    }
}

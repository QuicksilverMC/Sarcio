package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.util.Utils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Utils.class)
public class UtilsMixin {
    @WrapWithCondition(method = "run", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;fatal(Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false))
    private static boolean sarcio$stopLogSpam(Logger logger, String message, Throwable error) {
        return false;
    }
}

package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.rdh.sarcio.Feature;
import net.minecraft.server.GameProfileCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collections;

@Feature("bugfix.empty_user_cache")
@Mixin(GameProfileCache.class)
public class GameProfileCacheMixin {
	@ModifyExpressionValue(method = "load", at = @At(value = "INVOKE", target = "Lcom/google/gson/Gson;fromJson(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;", remap = false))
	private Object sarcio$replaceNullEntries(Object entries) {
		return entries == null ? Collections.emptyList() : entries;
	}
}

package dev.rdh.sarcio.mixin.resourcepacks;

import dev.rdh.sarcio.util.LenientJson;

import net.ornithemc.osl.resource.loader.impl.resource.JsonResourceMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.IOException;
import java.io.InputStream;

@Mixin(JsonResourceMetadata.class)
abstract class JsonResourceMetadataMixin {
    @ModifyVariable(
            method = "fromInputStream(Ljava/io/InputStream;)Lnet/ornithemc/osl/resource/loader/impl/resource/JsonResourceMetadata;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static InputStream sarcio$dropUnknownEscapes(InputStream is) throws IOException {
        return LenientJson.dropUnknownEscapes(is);
    }
}

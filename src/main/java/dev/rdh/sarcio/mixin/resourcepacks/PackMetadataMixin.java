package dev.rdh.sarcio;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.resource.pack.CustomResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.JsonResourceMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({CustomResourcePack.class, JsonResourceMetadata.class})
public abstract class PackMetadataMixin {
    @ModifyVariable(
        method = {
            "getMetadataSection",
            "fromInputStream(Ljava/io/InputStream;)Lnet/ornithemc/osl/resource/loader/impl/resource/JsonResourceMetadata;"
        },
        at = @At("HEAD"),
        argsOnly = true
    )
    private static InputStream sarcio$dropUnknownEscapes(InputStream in) throws IOException {
        try (InputStream stream = in) {
            String json = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            return new ByteArrayInputStream(json.replaceAll("(\\\\[\"\\\\/bfnrtu'\\n])|\\\\", "$1").getBytes(StandardCharsets.UTF_8));
        }
    }
}

package dev.rdh.sarcio.mixin.opt.resourcepacks;

import dev.rdh.sarcio.util.LenientJson;

import net.minecraft.client.resource.pack.CustomResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.IOException;
import java.io.InputStream;

@Mixin(CustomResourcePack.class)
abstract class CustomResourcePackMetadataMixin {
    @ModifyVariable(
            method = "getMetadataSection(Lnet/minecraft/client/resource/metadata/ResourceMetadataSerializers;Ljava/io/InputStream;Ljava/lang/String;)Lnet/minecraft/client/resource/metadata/ResourceMetadataSection;",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static InputStream sarcio$dropUnknownEscapes(InputStream in) throws IOException {
        return LenientJson.dropUnknownEscapes(in);
    }
}

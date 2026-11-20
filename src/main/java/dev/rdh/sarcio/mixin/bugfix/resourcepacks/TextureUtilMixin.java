package dev.rdh.sarcio.mixin.bugfix.resourcepacks;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rdh.sarcio.Feature;
import net.minecraft.client.render.texture.TextureUtil;
import org.spongepowered.asm.mixin.Mixin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Feature("bugfix.resourcepacks.unreadable_images")
@Mixin(TextureUtil.class)
abstract class TextureUtilMixin {
    // callers only handle IOException, so a broken image could crash the texture manager
    @WrapMethod(method = "readImage")
    private static BufferedImage sarcio$wrapReadErrors(InputStream in, Operation<BufferedImage> original) throws IOException {
        try {
            return original.call(in);
        } catch (RuntimeException e) {
            throw new IOException(e);
        }
    }
}

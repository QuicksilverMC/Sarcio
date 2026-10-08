package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.model.block.ItemModelGenerator;
import net.minecraft.client.render.texture.TextureAtlasSprite;
import net.minecraft.client.resource.model.BlockElement;
import net.minecraft.client.resource.model.BlockElementFace;
import net.minecraft.client.resource.model.BlockElementTexture;
import net.minecraft.util.math.Direction;
import org.lwjgl.util.vector.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mixin(ItemModelGenerator.class)
public abstract class ItemModelGeneratorMixin {
    @Shadow
    protected abstract boolean isTransparent(int[] pixels, int x, int y, int width, int height);

    // Fixes holes in item models disabling pixels in textures that contain <=0.1F transparency like OptiFine does.
    @Expression("@(?) == 0")
    @ModifyExpressionValue(method = "isTransparent", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int sarcio$adjustTransparencyAllowed(final int original) {
        return original <= 25 ? 0 : original;
    }

    @WrapMethod(method = "addSideElements")
    private List<BlockElement> sarcio$addSideElements(final TextureAtlasSprite sprite, final String key, final int layer, final Operation<List<BlockElement>> original) {
        final int width = sprite.getWidth();
        final int height = sprite.getHeight();
        final int frameCount = sprite.getFrameCount();
        final int[][] frames = new int[frameCount][];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = sprite.getFrame(i)[0];
        }

        final List<BlockElement> elements = new ArrayList<>();
        this.sarcio$scan(elements, layer, key, frames, width, height, false, Direction.DOWN, 0, 1, Direction.UP, 0, -1);
        this.sarcio$scan(elements, layer, key, frames, width, height, true, Direction.EAST, 1, 0, Direction.WEST, -1, 0);
        return elements;
    }

    @ModifyReturnValue(method = "processFrames", at = @At("RETURN"))
    private List<BlockElement> sarcio$unlerpUvs(final List<BlockElement> elements, @Local(argsOnly = true) final TextureAtlasSprite sprite) {
        final float du = 0.02F / sprite.getWidth(), dv = 0.02F / sprite.getHeight();
        for (final BlockElement element : elements) {
            for (final BlockElementFace face : element.faces.values()) {
                final float[] uv = face.textureCoords.coordinates;
                uv[0] = (uv[0] - du * 8) / (1 - du);
                uv[2] = (uv[2] - du * 8) / (1 - du);
                uv[1] = (uv[1] - dv * 8) / (1 - dv);
                uv[3] = (uv[3] - dv * 8) / (1 - dv);
            }
        }
        return elements;
    }

    @Unique
    private void sarcio$scan(final List<BlockElement> elements, final int layer, final String key, final int[][] frames, final int width, final int height, final boolean columns,
							 final Direction dirA, final int dxA, final int dyA, final Direction dirB, final int dxB, final int dyB) {
        final int outer = columns ? width : height;
        final int inner = columns ? height : width;
        for (int o = 0; o < outer; o++) {
            int firstA = -1, lastA = -1, firstB = -1, lastB = -1;
            for (int i = 0; i <= inner; i++) {
                final int x = columns ? o : i;
                final int y = columns ? i : o;
                if (i < inner && !this.sarcio$alwaysTransparent(frames, x, y, width, height)) {
                    if (this.sarcio$hasEdge(frames, x, y, dxA, dyA, width, height)) {
                        if (firstA == -1) firstA = i;
                        lastA = i;
                    }
                    if (this.sarcio$hasEdge(frames, x, y, dxB, dyB, width, height)) {
                        if (firstB == -1) firstB = i;
                        lastB = i;
                    }
                    continue;
                }
                if (firstA != -1) {
                    elements.add(sarcio$sideElement(dirA, layer, key, firstA, lastA, o, width, height, columns));
                    firstA = -1;
                }
                if (firstB != -1) {
                    elements.add(sarcio$sideElement(dirB, layer, key, firstB, lastB, o, width, height, columns));
                    firstB = -1;
                }
            }
        }
    }

    @Unique
    private static BlockElement sarcio$sideElement(final Direction dir, final int layer, final String key, final int start, final int end, final int o, final int width, final int height, final boolean column) {
        final float xf = width / 16.0F;
        final float yf = height / 16.0F;
        final Map<Direction, BlockElementFace> faces = new EnumMap<>(Direction.class);
        if (column) {
            faces.put(dir, new BlockElementFace(null, layer, key, new BlockElementTexture(new float[]{(o + 1) / xf, start / yf, o / xf, (end + 1) / yf}, 0)));
            return new BlockElement(new Vector3f(o / xf, (height - (end + 1)) / yf, 7.5F), new Vector3f((o + 1) / xf, (height - start) / yf, 8.5F), faces, null, true);
        }
        faces.put(dir, new BlockElementFace(null, layer, key, new BlockElementTexture(new float[]{start / xf, o / yf, (end + 1) / xf, (o + 1) / yf}, 0)));
        return new BlockElement(new Vector3f(start / xf, (height - (o + 1)) / yf, 7.5F), new Vector3f((end + 1) / xf, (height - o) / yf, 8.5F), faces, null, true);
    }

    @Unique
    private boolean sarcio$alwaysTransparent(final int[][] frames, final int x, final int y, final int width, final int height) {
        for (final int[] frame : frames) {
            if (!this.isTransparent(frame, x, y, width, height)) return false;
        }
        return true;
    }

    @Unique
    private boolean sarcio$hasEdge(final int[][] frames, final int x, final int y, final int dx, final int dy, final int width, final int height) {
        final int nx = x + dx, ny = y + dy;
        if (nx < 0 || ny < 0 || nx >= width || ny >= height) return true;
        for (final int[] frame : frames) {
            if (!this.isTransparent(frame, x, y, width, height) && this.isTransparent(frame, nx, ny, width, height)) return true;
        }
        return false;
    }
}

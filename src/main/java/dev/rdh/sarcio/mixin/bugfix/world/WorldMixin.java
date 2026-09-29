package dev.rdh.sarcio.mixin.bugfix.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow
    public abstract boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty);
    @Unique
    private int sarcio$range = 17;

    @Inject(method = "updateLight", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", ordinal = 0))
    private void sarcio$updateRange(LightType lightType, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        this.sarcio$range = this.isAreaLoaded(pos, 18, false) ? 17 : 15;
    }

    @ModifyExpressionValue(method  = "updateLight", at = @At(value = "CONSTANT", args = "intValue=17", ordinal = 0))
    private int sarcio$setStaticRange(int original) {
        return 16;
    }

    @ModifyExpressionValue(method  = "updateLight", at = {@At(value = "CONSTANT", args = "intValue=17", ordinal = 1), @At(value = "CONSTANT", args = "intValue=17", ordinal = 2)})
    private int setVariableRange(int original) {
        return this.sarcio$range;
    }

    @ModifyArg(method = "tickEntities", at = @At(value = "INVOKE", target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z"))
    private Collection<BlockEntity> sarcio$hashRemovedBlockEntities(@NotNull Collection<BlockEntity> removed) {
        return new ObjectOpenHashSet<>(removed);
    }
}

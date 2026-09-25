package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NbtCompound.class)
public class NbtCompoundMixin {
    @Inject(method = "put", at = @At("HEAD"))
    private void sarcio$failFast(String key, NbtElement value, CallbackInfo ci) {
        if (value == null) {
            throw new IllegalArgumentException("Invalid null NBT value with key " + key);
        }
    }
}

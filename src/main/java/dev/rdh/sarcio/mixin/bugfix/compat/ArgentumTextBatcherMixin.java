package dev.rdh.sarcio.mixin.bugfix.compat;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "dev.rdh.argentum.impl.render.gui.TextBatcher", remap = false)
public abstract class ArgentumTextBatcherMixin {
    @ModifyVariable(method = {"charWidth", "drawUnicodeGlyph"}, at = @At("STORE"), name = "left", require = 0)
    private int sarcio$unsignedGlyphStart(int left) {
        return left & 0xF;
    }
}

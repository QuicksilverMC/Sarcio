package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.screen.menu.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MultiplayerServerListWidget.class)
public class MultiplayerServerListWidgetMixin {
    @Shadow
    @Final
    private EntryListWidget.Entry scanningWidget;

    @WrapMethod(method = "getEntry")
    private EntryListWidget.Entry sarcio$preventIndexCrash(int index, Operation<EntryListWidget.Entry> original) {
        try {
            return original.call(index);
        } catch (IndexOutOfBoundsException e) {
            return this.scanningWidget;
        }
    }
}

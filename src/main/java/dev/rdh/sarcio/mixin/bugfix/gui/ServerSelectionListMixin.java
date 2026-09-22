package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.ServerSelectionList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerSelectionList.class)
public class ServerSelectionListMixin {
    @Shadow
    @Final
    private GuiListExtended.IGuiListEntry lanScanEntry;

    @WrapMethod(method = "getListEntry")
    private GuiListExtended.IGuiListEntry sarcio$preventIndexCrash(int index, Operation<GuiListExtended.IGuiListEntry> original) {
        try {
            return original.call(index);
        } catch (IndexOutOfBoundsException e) {
            return this.lanScanEntry;
        }
    }
}

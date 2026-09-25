package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.widget.ServerListEntryWidget;
import net.minecraft.client.options.ServerListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerListEntryWidget.class)
public class ServerListEntryWidgetMixin {
    @Shadow
    @Final
    private ServerListEntry entry;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/ServerListEntryWidget;loadServerIcon()V"))
    private void sarcio$preventIconCrash(ServerListEntryWidget instance, Operation<Void> original) {
        try {
            original.call(instance);
        } catch (Exception e) {
            this.entry.setIcon(null);
        }
    }
}

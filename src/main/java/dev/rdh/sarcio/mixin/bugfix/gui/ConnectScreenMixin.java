package dev.rdh.sarcio.mixin.bugfix.gui;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.menu.multiplayer.ConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Feature("bugfix.gui.connect_text_position")
@Mixin(ConnectScreen.class)
public class ConnectScreenMixin extends Screen {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/menu/multiplayer/ConnectScreen;drawCenteredString(Lnet/minecraft/client/render/TextRenderer;Ljava/lang/String;III)V"), index = 3)
    private int sarcio$keepTextAboveCancelButton(int y) {
        return Math.min(y, this.height / 4 + 120 + 12 - 20);
    }
}

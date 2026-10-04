package dev.rdh.sarcio.mixin.tweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.rdh.sarcio.SarcioMod;
import net.minecraft.client.gui.screen.game.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @ModifyExpressionValue(method = "init", at = @At(value = "CONSTANT", args = "intValue=100"))
    private int sarcio$longChatInput(int original) {
        return SarcioMod.chatLimit();
    }
}

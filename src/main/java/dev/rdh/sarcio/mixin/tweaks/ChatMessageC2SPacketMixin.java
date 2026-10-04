package dev.rdh.sarcio.mixin.tweaks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.rdh.sarcio.SarcioMod;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatMessageC2SPacket.class)
public class ChatMessageC2SPacketMixin {
    @ModifyExpressionValue(method = "<init>(Ljava/lang/String;)V", at = @At(value = "CONSTANT", args = "intValue=100"))
    private int sarcio$longChatPacket(int original) {
        return SarcioMod.chatLimit();
    }
}

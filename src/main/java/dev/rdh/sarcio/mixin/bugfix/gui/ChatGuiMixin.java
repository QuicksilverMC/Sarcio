package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.objectweb.asm.*;
import org.spongepowered.asm.mixin.Mixin;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatMessage;
import net.minecraft.client.gui.chat.ChatGui;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatGui.class)
public abstract class ChatGuiMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    public abstract int getVisibleLineCount();

    @Inject(method = "getMessageAt", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/chat/ChatGui;scroll:I", opcode = Opcodes.GETFIELD), cancellable = true)
    private void sarcio$ignoreClicksBelowChat(int mouseX, int mouseY, CallbackInfoReturnable<Text> cir, @Local(ordinal = 2) int y) {
        if (y / this.minecraft.textRenderer.fontHeight >= this.getVisibleLineCount()) {
            cir.setReturnValue(null);
        }
    }

    @WrapOperation(method = "removeMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ChatMessage;getId()I"))
    private int sarcio$skipNullChatLine(ChatMessage line, Operation<Integer> original) {
        return line == null ? -1 : original.call(line);
    }
}

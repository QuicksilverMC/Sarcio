package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;

import org.objectweb.asm.*;
import org.spongepowered.asm.mixin.Mixin;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GuiNewChat.class)
public abstract class GuiNewChatMixin {
    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    public abstract int getLineCount();

    @Inject(method = "getChatComponent", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiNewChat;scrollPos:I", opcode = Opcodes.GETFIELD), cancellable = true)
    private void sarcio$ignoreClicksBelowChat(int mouseX, int mouseY, CallbackInfoReturnable<IChatComponent> cir, @Local(ordinal = 2) int y) {
        if (y / this.mc.fontRendererObj.FONT_HEIGHT >= this.getLineCount()) {
            cir.setReturnValue(null);
        }
    }

    @WrapOperation(method = "deleteChatLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ChatLine;getChatLineID()I"))
    private int sarcio$skipNullChatLine(ChatLine line, Operation<Integer> original) {
        return line == null ? -1 : original.call(line);
    }
}

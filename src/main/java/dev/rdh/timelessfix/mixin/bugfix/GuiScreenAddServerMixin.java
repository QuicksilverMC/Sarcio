package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.GuiScreenAddServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiScreenAddServer.class)
public class GuiScreenAddServerMixin {
    @Definition(id = "serverIP", field = "Lnet/minecraft/client/multiplayer/ServerData;serverIP:Ljava/lang/String;")
    @Expression("?.serverIP = @(?)")
    @ModifyExpressionValue(method = "actionPerformed", at = @At("MIXINEXTRAS:EXPRESSION"))
    private String tf$trimIp(String original) {
        return original.trim();
    }
}

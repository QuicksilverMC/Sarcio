package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.menu.multiplayer.AddServerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AddServerScreen.class)
public class AddServerScreenMixin {
    @Definition(id = "serverIP", field = "Lnet/minecraft/client/options/ServerListEntry;ip:Ljava/lang/String;")
    @Expression("?.serverIP = @(?)")
    @ModifyExpressionValue(method = "buttonClicked", at = @At("MIXINEXTRAS:EXPRESSION"))
    private String sarcio$trimIp(String original) {
        return original.trim();
    }
}

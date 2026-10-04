package dev.rdh.sarcio.mixin.tweaks;

import net.minecraft.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.ProgressRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.twitch.ErrorTwitchStream;
import net.minecraft.client.twitch.TwitchStream;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    private TwitchStream twitchStream;

    @Inject(method = "initTwitchStream", at = @At("HEAD"), cancellable = true)
    private void sarcio$skipTwitchInit(CallbackInfo ci) {
        this.twitchStream = new ErrorTwitchStream(null);
        ci.cancel();
    }

    @WrapWithCondition(method = "runGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;renderStreamOverlay(F)V"))
    private boolean sarcio$removeStreamIndicator(GameRenderer instance, float partialTicks) {
        return false;
    }

    @WrapWithCondition(method = "runGame", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/client/twitch/TwitchStream;update()V"),
            @At(value = "INVOKE", target = "Lnet/minecraft/client/twitch/TwitchStream;submit()V")
    })
    private boolean sarcio$removeStreamCalls(TwitchStream instance) {
        return false;
    }

    @WrapWithCondition(method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/ProgressRenderer;progressStage(Ljava/lang/String;)V"))
    private boolean sarcio$skipWorldSwapProgress(ProgressRenderer instance, String message, ClientWorld world) {
        return world == null;
    }

    @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;updateShader(Lnet/minecraft/entity/Entity;)V"))
    private boolean sarcio$keepShaderOnPerspectiveChange(GameRenderer instance, Entity camera) {
        Minecraft minecraft = (Minecraft) (Object) this;
        return minecraft.getCamera() != minecraft.player;
    }
}

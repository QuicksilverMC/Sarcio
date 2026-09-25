package dev.rdh.sarcio.mixin.allocation_rate.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class, PlayerEntity.class})
abstract class EntityDisplayNameMixin {
	@Unique private long sarcio$displayNameTick = Long.MIN_VALUE;
	@Unique private Text sarcio$displayName;

	@Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true, require = 1)
	private void sarcio$displayNameCacheHit(CallbackInfoReturnable<Text> cir) {
		World world = ((Entity) (Object) this).world;
		if (world != null && this.sarcio$displayNameTick == world.getTime()) {
			cir.setReturnValue(this.sarcio$displayName);
		}
	}

	@Inject(method = "getDisplayName", at = @At("RETURN"), require = 1)
	private void sarcio$displayNameCacheStore(CallbackInfoReturnable<Text> cir) {
		World world = ((Entity) (Object) this).world;
		if (world != null) {
			this.sarcio$displayNameTick = world.getTime();
			this.sarcio$displayName = cir.getReturnValue();
		}
	}

	@SuppressWarnings("MixinAnnotationTarget")
	@WrapOperation(method = "getDisplayName", at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/entity/living/player/PlayerEntity;getHoverEvent()Lnet/minecraft/text/HoverEvent;"),
			@At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getHoverEvent()Lnet/minecraft/text/HoverEvent;")
	}, require = 1)
	private HoverEvent sarcio$hoverEventOnlyInSingleplayer(@Coerce Entity entity, Operation<HoverEvent> original) {
		return Minecraft.getInstance().isIntegratedServerRunning() ? original.call(entity) : null;
	}
}

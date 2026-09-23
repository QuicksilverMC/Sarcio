package dev.rdh.sarcio.mixin.allocation_rate.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Entity.class, EntityPlayer.class})
abstract class EntityDisplayNameMixin {
	@Unique private long sarcio$displayNameTick = Long.MIN_VALUE;
	@Unique private IChatComponent sarcio$displayName;

	@Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true, require = 1)
	private void sarcio$displayNameCacheHit(CallbackInfoReturnable<IChatComponent> cir) {
		World world = ((Entity) (Object) this).worldObj;
		if (world != null && this.sarcio$displayNameTick == world.getTotalWorldTime()) {
			cir.setReturnValue(this.sarcio$displayName);
		}
	}

	@Inject(method = "getDisplayName", at = @At("RETURN"), require = 1)
	private void sarcio$displayNameCacheStore(CallbackInfoReturnable<IChatComponent> cir) {
		World world = ((Entity) (Object) this).worldObj;
		if (world != null) {
			this.sarcio$displayNameTick = world.getTotalWorldTime();
			this.sarcio$displayName = cir.getReturnValue();
		}
	}

	@SuppressWarnings("MixinAnnotationTarget")
	@WrapOperation(method = "getDisplayName", at = {
			@At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getHoverEvent()Lnet/minecraft/event/HoverEvent;"),
			@At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getHoverEvent()Lnet/minecraft/event/HoverEvent;")
	}, require = 1)
	private HoverEvent sarcio$hoverEventOnlyInSingleplayer(@Coerce Entity entity, Operation<HoverEvent> original) {
		return Minecraft.getMinecraft().isIntegratedServerRunning() ? original.call(entity) : null;
	}
}

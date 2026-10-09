package dev.rdh.sarcio.mixin.mem.alloc.entity;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
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

@Mixin({Entity.class, PlayerEntity.class})
abstract class EntityDisplayNameMixin {
	@Unique private long sarcio$displayNameTick = Long.MIN_VALUE;
	@Unique private Text sarcio$displayName;

	@WrapMethod(method = "getDisplayName", require = 1)
	private Text sarcio$cacheDisplayName(Operation<Text> original) {
		World world = ((Entity) (Object) this).world;
		if (world == null) {
			return original.call();
		}

		long tick = world.getTime();
		if (this.sarcio$displayNameTick != tick) {
			this.sarcio$displayName = original.call();
			this.sarcio$displayNameTick = tick;
		}
		return this.sarcio$displayName;
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

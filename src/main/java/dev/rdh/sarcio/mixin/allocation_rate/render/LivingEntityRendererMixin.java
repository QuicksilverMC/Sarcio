package dev.rdh.sarcio.mixin.allocation_rate.render;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.scoreboard.team.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin {
	@Unique private static final Text sarcio$emptyName = new LiteralText("");
	@Unique private String sarcio$renderedName;

	@WrapOperation(
		method = "renderNameTag",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/LivingEntity;getDisplayName()Lnet/minecraft/text/Text;")
	)
	private Text prepareRenderedName(LivingEntity entity, Operation<Text> original) {
		String name = entity.getName();
		this.sarcio$renderedName = (entity instanceof PlayerEntity ? Team.getMemberDisplayName(entity.getScoreboardTeam(), name) : name) + "§r";
		return sarcio$emptyName;
	}

	@WrapOperation(
		method = "renderNameTag",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;getFormattedString()Ljava/lang/String;")
	)
	private String usePreparedName(Text name, Operation<String> original) {
		return name == sarcio$emptyName ? this.sarcio$renderedName : original.call(name);
	}

	@WrapOperation(
		method = "applyRotation",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/LivingEntity;getName()Ljava/lang/String;")
	)
	private String skipDefaultName(LivingEntity entity, Operation<String> original) {
		return entity instanceof PlayerEntity || entity.hasCustomName() ? original.call(entity) : null;
	}
}

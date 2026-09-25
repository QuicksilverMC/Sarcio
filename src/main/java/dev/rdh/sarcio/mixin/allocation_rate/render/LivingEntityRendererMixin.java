package dev.rdh.sarcio.mixin.allocation_rate.render;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.scoreboard.team.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntityRenderer.class)
abstract class LivingEntityRendererMixin {
	@Unique private static final Text sarcio$emptyName = new LiteralText("");
	@Unique private String sarcio$renderedName;

	@Redirect(
		method = "renderNameTag",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/LivingEntity;getDisplayName()Lnet/minecraft/text/Text;")
	)
	private Text prepareRenderedName(LivingEntity entity) {
		String name = entity.getName();
		this.sarcio$renderedName = entity instanceof PlayerEntity ? Team.getMemberDisplayName(entity.getScoreboardTeam(), name) : name;
		return sarcio$emptyName;
	}

	@Redirect(
		method = "renderNameTag",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;getFormattedString()Ljava/lang/String;")
	)
	private String usePreparedName(Text ignored) {
		return this.sarcio$renderedName;
	}

	@Redirect(
		method = "applyRotation",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/LivingEntity;getName()Ljava/lang/String;")
	)
	private String skipDefaultName(LivingEntity entity) {
		return entity instanceof PlayerEntity || entity.hasCustomName() ? entity.getName() : null;
	}
}

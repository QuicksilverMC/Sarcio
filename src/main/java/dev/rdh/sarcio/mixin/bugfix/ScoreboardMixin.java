package dev.rdh.sarcio.mixin.bugfix;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.team.Team;

@Mixin(Scoreboard.class)
public abstract class ScoreboardMixin {
	@Shadow public abstract Team getTeam(String name);

	@Inject(method = "removeTeam", at = @At("HEAD"), cancellable = true)
	private void sarcio$dontRemoveNull(Team team, CallbackInfo ci) {
		if (team == null) {
			ci.cancel();
		}
	}

	@Inject(method = "removeObjective", at = @At("HEAD"), cancellable = true)
	private void sarcio$dontRemoveNullObjective(ScoreboardObjective objective, CallbackInfo ci) {
		if (objective == null) {
			ci.cancel();
		}
	}

	@Inject(method = "addTeam", at = @At(value = "CONSTANT", args = "stringValue=A team with the name '"), cancellable = true)
	private void sarcio$reuseExistingTeam(String name, CallbackInfoReturnable<Team> cir) {
		cir.setReturnValue(this.getTeam(name));
	}

	@Inject(method = "removeMemberFromTeam(Ljava/lang/String;Lnet/minecraft/scoreboard/team/Team;)V", at = @At(value = "CONSTANT", args = "stringValue=Player is either on another team or not on any team. Cannot remove from team '"), cancellable = true)
	private void sarcio$dontThrowOnStaleTeam(String player, Team team, CallbackInfo ci) {
		ci.cancel();
	}
}

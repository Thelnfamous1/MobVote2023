package me.infamous.mob_vote_five.common.entity.ai;

import me.infamous.mob_vote_five.common.entity.Swimmer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SwimWithPlayerGoal<T extends Mob & Swimmer> extends Goal {
    private final T swimmer;
    private final double speedModifier;
    private final TargetingConditions playerTargeting;
    @Nullable
    private Player player;

    public SwimWithPlayerGoal(T swimmer, double pSpeedModifier, TargetingConditions playerTargeting) {
        this.swimmer = swimmer;
        this.speedModifier = pSpeedModifier;
        this.playerTargeting = playerTargeting;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.player = this.swimmer.level.getNearestPlayer(this.playerTargeting, this.swimmer);
        if (this.player == null) {
            return false;
        } else {
            return this.player.isSwimming() && this.swimmer.getTarget() != this.player;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.player != null && this.player.isSwimming() && this.swimmer.distanceToSqr(this.player) < 256.0D;
    }

    @Override
    public void start() {
        this.swimmer.startSwimmingWithPlayer(this.player);
    }

    @Override
    public void stop() {
        this.swimmer.stopSwimmingWithPlayer(this.player);
        this.player = null;
        this.swimmer.getNavigation().stop();
    }

    @Override
    public void tick() {
        this.swimmer.getLookControl().setLookAt(this.player, (float)(this.swimmer.getMaxHeadYRot() + 20), (float)this.swimmer.getMaxHeadXRot());
        if (this.swimmer.distanceToSqr(this.player) < 6.25D) {
            this.swimmer.getNavigation().stop();
        } else {
            this.swimmer.getNavigation().moveTo(this.player, this.speedModifier);
        }

        if (this.player.isSwimming() && this.player.level.random.nextInt(6) == 0) {
            this.swimmer.tickSwimmingWithPlayer(this.player);
        }
    }
}
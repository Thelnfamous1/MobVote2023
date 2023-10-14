package me.infamous.mob_vote_five.common.entity.ai;

import me.infamous.mob_vote_five.common.entity.Clumsy;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class StumbleGoal<T extends Mob & Clumsy> extends Goal {

    private final T clumsy;
    private final UniformInt fallInterval;
    private final int fallDuration;
    private int fallTicks;
    private long nextFallTimestamp;

    public StumbleGoal(T clumsy, UniformInt fallInterval, int fallDuration){
        this.clumsy = clumsy;
        this.fallInterval = fallInterval;
        this.fallDuration = fallDuration;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !this.clumsy.isFallingOver() && this.clumsy.canStumble() && this.clumsy.tickCount > this.nextFallTimestamp;
    }

    @Override
    public void start() {
        //MobVote2023.LOGGER.info("Starting StumbleGoal for {}", this.clumsy);
        this.clumsy.setFallingOver(true);
        this.fallTicks = this.fallDuration;
        int tryAgainIn = this.fallDuration + this.fallInterval.sample(this.clumsy.getRandom());
        this.nextFallTimestamp = this.clumsy.tickCount + tryAgainIn;
        this.clumsy.getNavigation().stop();
        //MobVote2023.LOGGER.info("{} will try to fall again in {} seconds", this.clumsy, tryAgainIn / 20.0D);
    }

    @Override
    public void tick() {
        super.tick();
        this.fallTicks--;
    }

    @Override
    public boolean canContinueToUse() {
        return this.fallTicks > 0 && this.clumsy.isAlive();
    }

    @Override
    public void stop() {
        this.clumsy.setFallingOver(false);
    }
}

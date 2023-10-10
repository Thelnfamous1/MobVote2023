package me.infamous.mob_vote_five.common.entity.ai;

import me.infamous.mob_vote_five.common.entity.Clumsy;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class StumbleGoal<T extends Mob & Clumsy> extends Goal {

    private final T clumsy;
    private final int oneInChance;
    private final int fallDuration;
    private int fallTicks;

    public StumbleGoal(T clumsy, int oneInChance, int fallDuration){
        this.clumsy = clumsy;
        this.oneInChance = oneInChance;
        this.fallDuration = fallDuration;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !this.clumsy.isFallingOver() && this.clumsy.canStumble() && this.clumsy.getRandom().nextInt(this.oneInChance) == 0;
    }

    @Override
    public void start() {
        this.clumsy.setFallingOver(true);
        this.fallTicks = this.fallDuration;
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

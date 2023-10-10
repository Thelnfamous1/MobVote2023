package me.infamous.mob_vote_five.common.entity.ai.hider;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RevealingGoal<T extends Mob & Hider> extends Goal {

    protected final T mob;
    private final int duration;
    private int emergingTicks;

    public RevealingGoal(T mob, int duration){
        this.mob = mob;
        this.duration = duration;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.isHidden() && this.mob.wantsToReveal();
    }

    @Override
    public void start() {
        this.mob.setRevealing();
        this.mob.playSound(this.mob.getRevealSound(), 5.0F, 1.0F);
        this.emergingTicks = this.duration;
    }

    @Override
    public void tick() {
        super.tick();
        this.emergingTicks--;
    }

    @Override
    public boolean canContinueToUse() {
        return this.emergingTicks > 0 && this.mob.isAlive();
    }

    @Override
    public void stop() {
        if(this.emergingTicks <= 0){
            this.mob.setRevealed();
        } else{
            this.mob.setHidden();
        }
    }
}

package me.infamous.mob_vote_five.common.entity.ai.hider;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HidingGoal<T extends Mob & Hider> extends Goal {

    protected final T mob;
    private final int duration;
    private int diggingTicks;

    public HidingGoal(T mob, int duration){
        this.mob = mob;
        this.duration = duration;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.isRevealed() && this.mob.wantsToHide();
    }

    @Override
    public void start() {
        this.diggingTicks = this.duration;
        this.mob.setHiding();
        this.mob.playSound(this.mob.getHideSound(), 5.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        this.diggingTicks--;
    }

    @Override
    public boolean canContinueToUse() {
        return this.diggingTicks > 0 && this.mob.isAlive();
    }

    @Override
    public void stop() {
        if(this.diggingTicks <= 0){
            this.mob.setHidden();
        } else{
            this.mob.setRevealed();
        }
    }
}

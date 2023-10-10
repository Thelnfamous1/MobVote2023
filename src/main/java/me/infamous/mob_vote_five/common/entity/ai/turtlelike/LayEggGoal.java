package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.LaysEggs;
import me.infamous.mob_vote_five.common.entity.HasHome;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.LevelReader;

public class LayEggGoal<T extends Animal & LaysEggs & HasHome> extends MoveToBlockGoal {
    private final T turtle;
    private final double closeEnough;

    public LayEggGoal(T pTurtle, double pSpeedModifier, int pSearchRange, double closeEnough) {
        super(pTurtle, pSpeedModifier, pSearchRange);
        this.turtle = pTurtle;
        this.closeEnough = closeEnough;
    }

    @Override
    public boolean canUse() {
        return this.turtle.hasEgg() && this.turtle.getHomePos().closerToCenterThan(this.turtle.position(), this.closeEnough) && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.getHomePos().closerToCenterThan(this.turtle.position(), this.closeEnough);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.turtle.isInWater() && this.isReachedTarget()) {
            if (this.turtle.getLayEggCounter() < 1) {
                this.turtle.setLayingEgg(true);
            } else if (this.turtle.getLayEggCounter() > this.adjustedTickDelay(200)) {
                this.turtle.layEgg((ServerLevel)this.turtle.level, this.blockPos.above());
                this.turtle.setHasEgg(false);
                this.turtle.setLayingEgg(false);
                this.turtle.setInLoveTime(600);
            }

            if (this.turtle.isLayingEgg()) {
                this.turtle.setLayEggCounter(this.turtle.getLayEggCounter() + 1);
            }
        }

    }


    @Override
    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
        return pLevel.isEmptyBlock(pPos.above()) && this.turtle.canLayEggsOn(pPos, pLevel);
    }
}
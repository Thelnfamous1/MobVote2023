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

    public LayEggGoal(T pTurtle, double pSpeedModifier) {
        super(pTurtle, pSpeedModifier, 16);
        this.turtle = pTurtle;
    }

    @Override
    public boolean canUse() {
        return this.turtle.hasEgg() && this.turtle.getHomePos().closerToCenterThan(this.turtle.position(), 9.0D) && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.getHomePos().closerToCenterThan(this.turtle.position(), 9.0D);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.turtle.isInWater() && this.isReachedTarget()) {
            if (this.turtle.getLayEggCounter() < 1) {
                this.turtle.setLayingEgg(true);
            } else if (this.turtle.getLayEggCounter() > this.adjustedTickDelay(200)) {
                this.turtle.layEgg((ServerLevel)this.turtle.level, this.blockPos);
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
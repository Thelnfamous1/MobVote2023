package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.LaysEggs;
import me.infamous.mob_vote_five.common.entity.HasHome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

public class GoToWaterGoal<T extends PathfinderMob & HasHome & LaysEggs> extends MoveToBlockGoal {
    private static final int GIVE_UP_TICKS = 1200;
    private final T turtle;

    public GoToWaterGoal(T pTurtle, double pSpeedModifier) {
        super(pTurtle, pTurtle.isBaby() ? 2.0D : pSpeedModifier, 24);
        this.turtle = pTurtle;
        this.verticalSearchStart = -1;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.turtle.isInWater() && this.tryTicks <= GIVE_UP_TICKS && this.isValidTarget(this.turtle.level, this.blockPos);
    }

    @Override
    public boolean canUse() {
        if (this.turtle.isBaby() && !this.turtle.isInWater()) {
            return super.canUse();
        } else {
            return !this.turtle.isGoingHome() && !this.turtle.isInWater() && !this.turtle.hasEgg() && super.canUse();
        }
    }

    @Override
    public boolean shouldRecalculatePath() {
         return this.tryTicks % 160 == 0;
      }

    @Override
    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos).is(Blocks.WATER);
    }
}
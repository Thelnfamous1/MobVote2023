package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.Swimmer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

public class SwimmerGoToWaterGoal<T extends PathfinderMob & Swimmer> extends MoveToBlockGoal {
    private static final int GIVE_UP_TICKS = 1200;
    private final T turtle;

    public SwimmerGoToWaterGoal(T pTurtle, double pSpeedModifier, int pSearchRange) {
        super(pTurtle, pSpeedModifier, pSearchRange);
        this.turtle = pTurtle;
        this.verticalSearchStart = -1;
    }

    @Override
    public boolean canContinueToUse() {
        return this.turtle.wantsToSwim() && !this.turtle.isInWater() && this.tryTicks <= GIVE_UP_TICKS && this.isValidTarget(this.turtle.level, this.blockPos);
    }

    @Override
    public boolean canUse() {
        return this.turtle.wantsToSwim() && !this.turtle.isInWater() && super.canUse();
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
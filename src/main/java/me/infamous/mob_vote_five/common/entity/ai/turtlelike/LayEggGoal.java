package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.LaysEggs;
import me.infamous.mob_vote_five.common.entity.HasHome;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;

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
        BlockPos blockpos = this.turtle.blockPosition();
        if (!this.turtle.isInWater() && this.isReachedTarget()) {
            if (this.turtle.getLayEggCounter() < 1) {
                this.turtle.setLayingEgg(true);
            } else if (this.turtle.getLayEggCounter() > this.adjustedTickDelay(200)) {
                Level level = this.turtle.level;
                level.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
                level.setBlock(this.blockPos.above(), Blocks.TURTLE_EGG.defaultBlockState().setValue(TurtleEggBlock.EGGS, this.turtle.getRandom().nextInt(4) + 1), 3);
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
        return pLevel.isEmptyBlock(pPos.above()) && TurtleEggBlock.isSand(pLevel, pPos);
    }
}
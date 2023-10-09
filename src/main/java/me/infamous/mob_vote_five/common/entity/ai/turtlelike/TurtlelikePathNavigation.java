package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.Traveller;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class TurtlelikePathNavigation<T extends Mob & Traveller> extends AmphibiousPathNavigation {
    private final T turtle;

    public TurtlelikePathNavigation(T pTurtle, Level pLevel) {
        super(pTurtle, pLevel);
        this.turtle = pTurtle;
    }

    @Override
    public boolean isStableDestination(BlockPos pPos) {
        if (this.turtle.isTravelling()) {
            return this.level.getBlockState(pPos).is(Blocks.WATER);
        }

        return !this.level.getBlockState(pPos.below()).isAir();
    }
}
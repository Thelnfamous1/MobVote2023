package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.LaysEggs;
import me.infamous.mob_vote_five.common.entity.HasHome;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class TurtlelikeRandomStrollGoal<T extends PathfinderMob & HasHome & LaysEggs> extends RandomStrollGoal {
    private final T turtle;

    public TurtlelikeRandomStrollGoal(T pTurtle, double pSpeedModifier, int pInterval) {
        super(pTurtle, pSpeedModifier, pInterval);
        this.turtle = pTurtle;
    }

    @Override
    public boolean canUse() {
        return !this.mob.isInWater() && !this.turtle.isGoingHome() && !this.turtle.hasEgg() && super.canUse();
    }
}
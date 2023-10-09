package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.Traveller;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class TravellerRandomStrollGoal<T extends PathfinderMob & Traveller> extends RandomStrollGoal {
    private final T turtle;

    public TravellerRandomStrollGoal(T pTurtle, double pSpeedModifier, int pInterval) {
        super(pTurtle, pSpeedModifier, pInterval);
        this.turtle = pTurtle;
    }

    @Override
    public boolean canUse() {
        return this.turtle.wantsToStroll() && super.canUse();
    }
}
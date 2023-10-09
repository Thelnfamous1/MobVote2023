package me.infamous.mob_vote_five.common.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;

public class MVWaterBoundPathNavigation extends WaterBoundPathNavigation {
    private final boolean allowBreaching;

    public MVWaterBoundPathNavigation(Mob pMob, Level pLevel, boolean allowBreaching) {
        super(pMob, pLevel);
        this.allowBreaching = allowBreaching;
    }

    @Override
    protected PathFinder createPathFinder(int pMaxVisitedNodes) {
        this.nodeEvaluator = new SwimNodeEvaluator(this.allowBreaching);
        return new PathFinder(this.nodeEvaluator, pMaxVisitedNodes);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.allowBreaching || this.isInLiquid();
    }
}

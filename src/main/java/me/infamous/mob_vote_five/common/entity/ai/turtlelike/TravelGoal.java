package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.Traveller;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class TravelGoal<T extends PathfinderMob & Traveller> extends Goal {
    private final T turtle;
    private final double speedModifier;
    private final int maxXZDist;
    private final int maxYDist;
    private boolean stuck;

    public TravelGoal(T pTurtle, double pSpeedModifier, int maxXZDist, int maxYDist) {
        this.turtle = pTurtle;
        this.speedModifier = pSpeedModifier;
        this.maxXZDist = maxXZDist;
        this.maxYDist = maxYDist;
    }

    @Override
    public boolean canUse() {
        return this.turtle.wantsToTravel();
    }

    @Override
    public void start() {
        RandomSource random = this.turtle.getRandom();
        int randomX = random.nextInt(this.maxXZDist + 1) - this.maxXZDist;
        int randomY = random.nextInt(this.maxYDist + 1) - this.maxYDist;
        int randomZ = random.nextInt(this.maxXZDist + 1) - this.maxXZDist;
        if ((double)randomY + this.turtle.getY() > (double)(this.turtle.level.getSeaLevel() - 1)) {
            randomY = 0;
        }

        BlockPos targetPos = new BlockPos((double)randomX + this.turtle.getX(), (double)randomY + this.turtle.getY(), (double)randomZ + this.turtle.getZ());
        this.turtle.setTravelPos(targetPos);
        this.turtle.setTravelling(true);
        this.stuck = false;
    }

    @Override
    public void tick() {
        if (this.turtle.getNavigation().isDone()) {
            Vec3 travelPosVec = Vec3.atBottomCenterOf(this.turtle.getTravelPos());
            Vec3 posTowardsTravelPos = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, travelPosVec, (float)Math.PI / 10F);
            if (posTowardsTravelPos == null) {
                posTowardsTravelPos = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, travelPosVec, (float)Math.PI / 2F);
            }

            if (posTowardsTravelPos != null) {
                int i = Mth.floor(posTowardsTravelPos.x);
                int j = Mth.floor(posTowardsTravelPos.z);
                int k = 34;
                if (!this.turtle.level.hasChunksAt(i - 34, j - 34, i + 34, j + 34)) {
                    posTowardsTravelPos = null;
                }
            }

            if (posTowardsTravelPos == null) {
                this.stuck = true;
                return;
            }
            this.turtle.getNavigation().moveTo(posTowardsTravelPos.x, posTowardsTravelPos.y, posTowardsTravelPos.z, this.speedModifier);
         }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.turtle.getNavigation().isDone() && !this.stuck && this.turtle.wantsToTravel();
    }

    @Override
    public void stop() {
        this.turtle.setTravelling(false);
        super.stop();
    }
}
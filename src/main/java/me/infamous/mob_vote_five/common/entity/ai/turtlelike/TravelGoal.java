package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.LaysEggs;
import me.infamous.mob_vote_five.common.entity.HasHome;
import me.infamous.mob_vote_five.common.entity.Traveller;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.phys.Vec3;

public class TravelGoal<T extends Animal & Traveller & HasHome & LaysEggs> extends Goal {
    private final T turtle;
    private final double speedModifier;
    private boolean stuck;

    public TravelGoal(T pTurtle, double pSpeedModifier) {
        this.turtle = pTurtle;
        this.speedModifier = pSpeedModifier;
    }

    @Override
    public boolean canUse() {
        return !this.turtle.isGoingHome() && !this.turtle.hasEgg() && this.turtle.isInWater();
    }

    @Override
    public void start() {
        int i = 512;
        int j = 4;
        RandomSource randomsource = this.turtle.getRandom();
        int k = randomsource.nextInt(1025) - 512;
        int l = randomsource.nextInt(9) - 4;
        int i1 = randomsource.nextInt(1025) - 512;
        if ((double)l + this.turtle.getY() > (double)(this.turtle.level.getSeaLevel() - 1)) {
            l = 0;
        }

        BlockPos blockpos = new BlockPos((double)k + this.turtle.getX(), (double)l + this.turtle.getY(), (double)i1 + this.turtle.getZ());
        this.turtle.setTravelPos(blockpos);
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
        return !this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.isGoingHome() && !this.turtle.isInLove() && !this.turtle.hasEgg();
    }

    @Override
    public void stop() {
        this.turtle.setTravelling(false);
        super.stop();
    }
}
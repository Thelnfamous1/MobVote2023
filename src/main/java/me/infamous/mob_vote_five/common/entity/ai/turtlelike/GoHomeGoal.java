package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.LaysEggs;
import me.infamous.mob_vote_five.common.entity.HasHome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class GoHomeGoal<T extends PathfinderMob & HasHome & LaysEggs> extends Goal {
      private final T turtle;
      private final double speedModifier;
      private boolean stuck;
      private int closeToHomeTryTicks;
      private static final int GIVE_UP_TICKS = 600;

    public GoHomeGoal(T pTurtle, double pSpeedModifier) {
         this.turtle = pTurtle;
         this.speedModifier = pSpeedModifier;
      }

      @Override
      public boolean canUse() {
         if (this.turtle.isBaby()) {
            return false;
         } else if (this.turtle.hasEgg()) {
            return true;
         } else if (this.turtle.getRandom().nextInt(reducedTickDelay(700)) != 0) {
            return false;
         } else {
            return !this.turtle.getHomePos().closerToCenterThan(this.turtle.position(), 64.0D);
         }
      }

    @Override
    public void start() {
         this.turtle.setGoingHome(true);
         this.stuck = false;
         this.closeToHomeTryTicks = 0;
      }

    @Override
    public void stop() {
         this.turtle.setGoingHome(false);
      }

    @Override
    public boolean canContinueToUse() {
         return !this.turtle.getHomePos().closerToCenterThan(this.turtle.position(), 7.0D) && !this.stuck && this.closeToHomeTryTicks <= this.adjustedTickDelay(GIVE_UP_TICKS);
      }

    @Override
    public void tick() {
         BlockPos homePos = this.turtle.getHomePos();
         boolean closeEnoughToHome = homePos.closerToCenterThan(this.turtle.position(), 16.0D);
         if (closeEnoughToHome) {
            ++this.closeToHomeTryTicks;
         }

         if (this.turtle.getNavigation().isDone()) {
            Vec3 homePosVec = Vec3.atBottomCenterOf(homePos);
            Vec3 posTowardsHome = DefaultRandomPos.getPosTowards(this.turtle, 16, 3, homePosVec, (float)Math.PI / 10F);
            if (posTowardsHome == null) {
               posTowardsHome = DefaultRandomPos.getPosTowards(this.turtle, 8, 7, homePosVec, (float)Math.PI / 2F);
            }

            if (posTowardsHome != null && !closeEnoughToHome && !this.turtle.level.getBlockState(new BlockPos(posTowardsHome)).is(Blocks.WATER)) {
               posTowardsHome = DefaultRandomPos.getPosTowards(this.turtle, 16, 5, homePosVec, (float)Math.PI / 2F);
            }

            if (posTowardsHome == null) {
               this.stuck = true;
               return;
            }

            this.turtle.getNavigation().moveTo(posTowardsHome.x, posTowardsHome.y, posTowardsHome.z, this.speedModifier);
         }

      }
   }
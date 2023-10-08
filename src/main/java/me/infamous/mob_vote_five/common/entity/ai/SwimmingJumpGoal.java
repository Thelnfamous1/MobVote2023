package me.infamous.mob_vote_five.common.entity.ai;

import me.infamous.mob_vote_five.common.entity.Swimmer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.JumpGoal;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

public class SwimmingJumpGoal<T extends Mob & Swimmer> extends JumpGoal {
   private static final int[] STEPS_TO_CHECK = new int[]{0, 1, 4, 5, 6, 7};
   private final T swimmer;
   private final int interval;
   private boolean breached;

   public SwimmingJumpGoal(T swimmer, int pInterval) {
      this.swimmer = swimmer;
      this.interval = reducedTickDelay(pInterval);
   }

   @Override
   public boolean canUse() {
      if (this.swimmer.getRandom().nextInt(this.interval) != 0) {
         return false;
      } else {
         Direction motionDirection = this.swimmer.getMotionDirection();
         int motionX = motionDirection.getStepX();
         int motionZ = motionDirection.getStepZ();
         BlockPos blockPos = this.swimmer.blockPosition();

         for(int step : STEPS_TO_CHECK) {
            if (!this.waterIsClear(blockPos, motionX, motionZ, step) || !this.surfaceIsClear(blockPos, motionX, motionZ, step)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean waterIsClear(BlockPos pPos, int pDx, int pDz, int pScale) {
      BlockPos blockpos = pPos.offset(pDx * pScale, 0, pDz * pScale);
      return this.swimmer.level.getFluidState(blockpos).is(FluidTags.WATER) && !this.swimmer.level.getBlockState(blockpos).getMaterial().blocksMotion();
   }

   private boolean surfaceIsClear(BlockPos pPos, int pDx, int pDz, int pScale) {
      return this.swimmer.level.getBlockState(pPos.offset(pDx * pScale, 1, pDz * pScale)).isAir() && this.swimmer.level.getBlockState(pPos.offset(pDx * pScale, 2, pDz * pScale)).isAir();
   }

   @Override
   public boolean canContinueToUse() {
      double yD = this.swimmer.getDeltaMovement().y;
      return (!(yD * yD < (double)0.03F) || this.swimmer.getXRot() == 0.0F || !(Math.abs(this.swimmer.getXRot()) < 10.0F) || !this.swimmer.isInWater()) && !this.swimmer.isOnGround();
   }

   @Override
   public boolean isInterruptable() {
      return false;
   }

   @Override
   public void start() {
      Direction motionDirection = this.swimmer.getMotionDirection();
      this.swimmer.startBreaching();
      this.swimmer.setDeltaMovement(this.swimmer.getDeltaMovement().add((double)motionDirection.getStepX() * 0.6D, 0.7D, (double)motionDirection.getStepZ() * 0.6D));
      this.swimmer.getNavigation().stop();
   }

   @Override
   public void stop() {
      this.swimmer.stopBreaching();
      this.swimmer.setXRot(0.0F);
   }

   @Override
   public void tick() {
      boolean wasBreached = this.breached;
      if (!wasBreached) {
         FluidState fluidState = this.swimmer.level.getFluidState(this.swimmer.blockPosition());
         this.breached = fluidState.is(FluidTags.WATER);
      }

      if (this.breached && !wasBreached) {
         this.swimmer.playSound(this.swimmer.getJumpSound(), 1.0F, 1.0F);
      }

      Vec3 deltaMovement = this.swimmer.getDeltaMovement();
      if (deltaMovement.y * deltaMovement.y < (double)0.03F && this.swimmer.getXRot() != 0.0F) {
         this.swimmer.setXRot(Mth.rotlerp(this.swimmer.getXRot(), 0.0F, 0.2F));
      } else if (deltaMovement.length() > (double)1.0E-5F) {
         double horizontalDistance = deltaMovement.horizontalDistance();
         double targetXRot = Math.atan2(-deltaMovement.y, horizontalDistance) * (double)(180F / (float)Math.PI);
         this.swimmer.setXRot((float)targetXRot);
      }

   }
}
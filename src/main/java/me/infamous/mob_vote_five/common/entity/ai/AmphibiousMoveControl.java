package me.infamous.mob_vote_five.common.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class AmphibiousMoveControl extends MoveControl {
   public static final double SINK_Y_OFFSET = 0.005D;
   private final int maxTurnX;
   private final int maxTurnY;
   private final float inWaterSpeedModifier;
   private final float outsideWaterSpeedModifier;
   private final boolean applyGravity;

   public AmphibiousMoveControl(Mob pMob, int pMaxTurnX, int pMaxTurnY, float pInWaterSpeedModifier, float pOutsideWaterSpeedModifier, boolean pApplyGravity) {
      super(pMob);
      this.maxTurnX = pMaxTurnX;
      this.maxTurnY = pMaxTurnY;
      this.inWaterSpeedModifier = pInWaterSpeedModifier;
      this.outsideWaterSpeedModifier = pOutsideWaterSpeedModifier;
      this.applyGravity = pApplyGravity;
   }

   public void tick() {
      if (this.mob.isInWater()) {
         if (this.applyGravity) this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0D, SINK_Y_OFFSET, 0.0D));
         if (this.operation == MoveControl.Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
            double xDist = this.wantedX - this.mob.getX();
            double yDist = this.wantedY - this.mob.getY();
            double zDist = this.wantedZ - this.mob.getZ();
            double distSqr = xDist * xDist + yDist * yDist + zDist * zDist;
            if (distSqr < (double)2.5000003E-7F) {
               this.mob.setZza(0.0F);
            } else {
               float targetYRot = (float)(Mth.atan2(zDist, xDist) * (double)(180F / (float)Math.PI)) - 90.0F;
               this.mob.setYRot(this.rotlerp(this.mob.getYRot(), targetYRot, (float)this.maxTurnY));
               this.mob.yBodyRot = this.mob.getYRot();
               this.mob.yHeadRot = this.mob.getYRot();
               float speed = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
               this.mob.setSpeed(speed * this.inWaterSpeedModifier);
               double horizontalDist = Math.sqrt(xDist * xDist + zDist * zDist);
               if (Math.abs(yDist) > (double)1.0E-5F || Math.abs(horizontalDist) > (double)1.0E-5F) {
                  float targetXRot = -((float)(Mth.atan2(yDist, horizontalDist) * (double)(180F / (float)Math.PI)));
                  targetXRot = Mth.clamp(Mth.wrapDegrees(targetXRot), (float)(-this.maxTurnX), (float)this.maxTurnX);
                  this.mob.setXRot(this.rotlerp(this.mob.getXRot(), targetXRot, 5.0F));
               }

               float relativeForward = Mth.cos(this.mob.getXRot() * ((float)Math.PI / 180F));
               float relativeVertical = Mth.sin(this.mob.getXRot() * ((float)Math.PI / 180F));
               this.mob.zza = relativeForward * speed;
               this.mob.yya = -relativeVertical * speed;
            }
         } else{
            this.mob.setSpeed(0.0F);
            this.mob.setXxa(0.0F);
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
         }
      }
       else {
         super.tick();
      }
   }
}
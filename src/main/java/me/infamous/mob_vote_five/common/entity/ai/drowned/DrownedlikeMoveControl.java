package me.infamous.mob_vote_five.common.entity.ai.drowned;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class DrownedlikeMoveControl<T extends Mob> extends MoveControl {
   private final T drowned;

   public DrownedlikeMoveControl(T pDrowned) {
      super(pDrowned);
      this.drowned = pDrowned;
   }

   @Override
   public void tick() {
      LivingEntity target = this.drowned.getTarget();
      if (/*this.drowned.wantsToSwim() && */this.drowned.isInWater()) {
         if (target != null && target.getY() > this.drowned.getY() /*|| this.drowned.searchingForLand*/) {
            this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
         }
         if (this.operation != MoveControl.Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
            this.drowned.setSpeed(0.0F);
            return;
         }
         double xDist = this.wantedX - this.drowned.getX();
         double yDist = this.wantedY - this.drowned.getY();
         double zDist = this.wantedZ - this.drowned.getZ();
         double dist = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
         yDist /= dist;
         float targetYRot = (float)(Mth.atan2(zDist, xDist) * (double)(180F / (float)Math.PI)) - 90.0F;
         this.drowned.setYRot(this.rotlerp(this.drowned.getYRot(), targetYRot, 90.0F));
         this.drowned.yBodyRot = this.drowned.getYRot();
         float speed = (float)(this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
         float lerpSpeed = Mth.lerp(0.125F, this.drowned.getSpeed(), speed);
         this.drowned.setSpeed(lerpSpeed);
         this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add((double)lerpSpeed * xDist * 0.005D, (double)lerpSpeed * yDist * 0.1D, (double)lerpSpeed * zDist * 0.005D));
      } else {
         if (!this.drowned.isOnGround()) {
            this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
         }
         super.tick();
      }
   }
}
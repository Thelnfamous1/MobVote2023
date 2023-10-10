package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.HasHome;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class TurtlelikeMoveControl<T extends Mob & HasHome> extends MoveControl {
   public static final double SINK_Y_OFFSET = 0.005D;
   private final T turtle;

   public TurtlelikeMoveControl(T pTurtle) {
      super(pTurtle);
      this.turtle = pTurtle;
   }

   private void updateSpeed() {
      if (this.turtle.isInWater()) {
         this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, SINK_Y_OFFSET, 0.0D));
         if (!this.turtle.getHomePos().closerToCenterThan(this.turtle.position(), 16.0D)) {
            this.turtle.setSpeed(Math.max(this.turtle.getSpeed() * 0.5F, 0.08F));
         }
         if (this.turtle.isBaby()) {
            this.turtle.setSpeed(Math.max(this.turtle.getSpeed() * 0.3333F, 0.06F));
         }
        } else if (this.turtle.isOnGround()) {
         this.turtle.setSpeed(Math.max(this.turtle.getSpeed() * 0.5F, 0.06F));
      }
   }

   @Override
   public void tick() {
      this.updateSpeed();
      if (this.operation == MoveControl.Operation.MOVE_TO && !this.turtle.getNavigation().isDone()) {
         double xDist = this.wantedX - this.turtle.getX();
         double yDist = this.wantedY - this.turtle.getY();
         double zDist = this.wantedZ - this.turtle.getZ();
         double dist = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
         yDist /= dist;
         float targetYRot = (float)(Mth.atan2(zDist, xDist) * (double)(180F / (float)Math.PI)) - 90.0F;
         this.turtle.setYRot(this.rotlerp(this.turtle.getYRot(), targetYRot, 90.0F));
         this.turtle.yBodyRot = this.turtle.getYRot();
         float targetSpeed = (float)(this.speedModifier * this.turtle.getAttributeValue(Attributes.MOVEMENT_SPEED));
         this.turtle.setSpeed(Mth.lerp(0.125F, this.turtle.getSpeed(), targetSpeed));
         this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, (double)this.turtle.getSpeed() * yDist * 0.1D, 0.0D));
      } else {
         this.turtle.setSpeed(0.0F);
      }
   }
}
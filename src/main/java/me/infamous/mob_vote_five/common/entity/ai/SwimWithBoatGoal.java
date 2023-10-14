package me.infamous.mob_vote_five.common.entity.ai;

import me.infamous.mob_vote_five.common.entity.Swimmer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class SwimWithBoatGoal<T extends PathfinderMob & Swimmer> extends Goal {
   private static final TargetingConditions TARGETING_CONDITIONS = TargetingConditions.forNonCombat().range(64.0D);
   private final float closeEnough;
   private final float tooFar;
   private final double speedModifier;
   private int timeToRecalcPath;
   private final T swimmer;
   @Nullable
   private Player following;
   private MoveGoal currentGoal;

   public SwimWithBoatGoal(T pMob, float closeEnough, float tooFar, double speedModifier) {
      this.swimmer = pMob;
      this.closeEnough = closeEnough;
      this.tooFar = tooFar;
      this.speedModifier = speedModifier;
   }

   @Override
   public boolean canUse() {
      if(!this.swimmer.wantsToSwimAway()){
         return false;
      }
      List<Player> nearbyPlayers = this.swimmer.level.getNearbyPlayers(TARGETING_CONDITIONS, this.swimmer, this.swimmer.getBoundingBox().inflate(8.0D));
      boolean foundBoat = false;

      for(Player player : nearbyPlayers) {
         Entity vehicle = player.getVehicle();
         if (vehicle instanceof Boat boat && boat.getControllingPassenger() == player && this.isDriving(player)) {
            foundBoat = true;
            break;
         }
      }

      return this.following != null && this.isDriving(this.following) || foundBoat;
   }

   private boolean isDriving(Player following) {
      //return Mth.abs(following.xxa) > 0.0F || Mth.abs(following.zza) > 0.0F;
      return true;
   }

   @Override
   public boolean isInterruptable() {
      return true;
   }

   @Override
   public boolean canContinueToUse() {
      return this.swimmer.wantsToSwimAway() && this.following != null && this.following.isPassenger() && this.isDriving(this.following);
   }

   @Override
   public void start() {
      for(Player player : this.swimmer.level.getNearbyPlayers(TARGETING_CONDITIONS, this.swimmer, this.swimmer.getBoundingBox().inflate(5.0D))) {
         if (player.getVehicle() instanceof Boat boat && boat.getControllingPassenger() == player) {
            this.following = player;
            this.swimmer.startSwimmingWithPlayer(player);
            break;
         }
      }

      this.timeToRecalcPath = 0;
      this.currentGoal = MoveGoal.GO_TO_BOAT;
   }

   @Override
   public void stop() {
      this.swimmer.stopSwimmingWithPlayer(this.following);
      this.following = null;
   }

   @Override
   public void tick() {
      boolean driving = this.isDriving(this.following);
      float speed = this.currentGoal == MoveGoal.GO_IN_BOAT_DIRECTION ? (driving ? 0.01F : 0.0F) : 0.015F;
      this.swimmer.moveRelative(speed, new Vec3(this.swimmer.xxa, this.swimmer.yya, this.swimmer.zza));
      this.swimmer.move(MoverType.SELF, this.swimmer.getDeltaMovement());
      if (--this.timeToRecalcPath <= 0) {
         this.timeToRecalcPath = this.adjustedTickDelay(10);
         if (this.currentGoal == MoveGoal.GO_TO_BOAT) {
            BlockPos moveToPos = this.following.blockPosition().relative(this.following.getDirection().getOpposite());
            this.swimmer.getNavigation().moveTo(moveToPos.getX(), moveToPos.getY(), moveToPos.getZ(), this.speedModifier);
            if (this.swimmer.distanceTo(this.following) < this.closeEnough) {
               this.timeToRecalcPath = 0;
               this.currentGoal = MoveGoal.GO_IN_BOAT_DIRECTION;
            }
         } else if (this.currentGoal == MoveGoal.GO_IN_BOAT_DIRECTION) {
            Direction followDirection = this.following.getMotionDirection();
            BlockPos moveToPos = this.following.blockPosition().relative(followDirection, 10);
            this.swimmer.getNavigation().moveTo(moveToPos.getX(), moveToPos.getY(), moveToPos.getZ(), this.speedModifier);
            if (this.swimmer.distanceTo(this.following) > this.tooFar) {
               this.timeToRecalcPath = 0;
               this.currentGoal = MoveGoal.GO_TO_BOAT;
            }
         }
      }
      this.swimmer.tickSwimmingWithPlayer(this.following);
   }

   enum MoveGoal {
      GO_TO_BOAT,
      GO_IN_BOAT_DIRECTION
   }
}
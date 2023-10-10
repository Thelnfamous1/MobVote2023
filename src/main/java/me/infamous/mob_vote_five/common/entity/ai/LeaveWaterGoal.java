package me.infamous.mob_vote_five.common.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class LeaveWaterGoal<T extends Mob> extends Goal {
    private final T mob;

    public LeaveWaterGoal(T mob){
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (this.mob.level.getFluidState(this.mob.blockPosition()).is(FluidTags.WATER) && this.mob.getMoveControl().hasWanted()){
            BlockPos wanted = new BlockPos(this.mob.getMoveControl().getWantedX(), this.mob.getMoveControl().getWantedX(), this.mob.getMoveControl().getWantedX());
            return this.mob.level.getFluidState(wanted).isEmpty();
        }
        return false;
    }

    @Override
    public void tick() {
        if(this.mob.horizontalCollision && this.mob.isInWater()){
            float yRotRadians = mob.getYRot() * Mth.DEG_TO_RAD;
            mob.setDeltaMovement(this.mob.getDeltaMovement().add(-Mth.sin(yRotRadians) * 0.2F, 0.1D, Mth.cos(yRotRadians) * 0.2F));
        }
    }
}

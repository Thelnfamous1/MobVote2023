package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class AquaticPanicGoal extends PanicGoal {

    public AquaticPanicGoal(PathfinderMob pTurtle, double pSpeedModifier) {
        super(pTurtle, pSpeedModifier);
    }

    @Override
    public boolean canUse() {
        if (!this.shouldPanic()) {
            return false;
        } else {
            BlockPos waterPos = this.lookForWater(this.mob.level, this.mob, 7);
            if (waterPos != null) {
                this.posX = waterPos.getX();
                this.posY = waterPos.getY();
                this.posZ = waterPos.getZ();
                return true;
            } else {
                return this.findRandomPosition();
            }
        }
    }
}
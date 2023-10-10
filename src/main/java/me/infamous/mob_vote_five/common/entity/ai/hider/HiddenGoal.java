package me.infamous.mob_vote_five.common.entity.ai.hider;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HiddenGoal<T extends Mob & Hider> extends Goal {

    protected final T mob;

    public HiddenGoal(T mob){
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.isHidden();
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isHidden();
    }
}

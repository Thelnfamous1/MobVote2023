package me.infamous.mob_vote_five.common.entity.ai;

import me.infamous.mob_vote_five.common.entity.Greeter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class WaveAtPlayerGoal<T extends Mob & Greeter> extends LookAtPlayerGoal {
    protected final T greeter;

    public WaveAtPlayerGoal(T pMob, Class<? extends LivingEntity> pLookAtType, float pLookDistance) {
        this(pMob, pLookAtType, pLookDistance, 0.02F);
    }

    public WaveAtPlayerGoal(T pMob, Class<? extends LivingEntity> pLookAtType, float pLookDistance, float pProbability) {
        this(pMob, pLookAtType, pLookDistance, pProbability, false);
    }

    public WaveAtPlayerGoal(T pMob, Class<? extends LivingEntity> pLookAtType, float pLookDistance, float pProbability, boolean pOnlyHorizontal) {
        super(pMob, pLookAtType, pLookDistance, pProbability, pOnlyHorizontal);
        this.greeter = pMob;
    }


    @Override
    public void start() {
        super.start();
        this.greeter.setGreeting(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.greeter.setGreeting(false);
    }
}

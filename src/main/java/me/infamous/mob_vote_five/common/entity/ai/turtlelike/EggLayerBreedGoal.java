package me.infamous.mob_vote_five.common.entity.ai.turtlelike;

import me.infamous.mob_vote_five.common.entity.LaysEggs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.GameRules;

public class EggLayerBreedGoal<T extends Animal & LaysEggs> extends BreedGoal {
    private final T turtle;

    public EggLayerBreedGoal(T pTurtle, double pSpeedModifier) {
        super(pTurtle, pSpeedModifier);
        this.turtle = pTurtle;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.turtle.hasEgg();
    }

    @Override
    protected void breed() {
        ServerPlayer loveCause = this.animal.getLoveCause();
        if (loveCause == null && this.partner.getLoveCause() != null) {
            loveCause = this.partner.getLoveCause();
        }

        if (loveCause != null) {
            loveCause.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, null);
        }

        this.turtle.setHasEgg(true);
        this.animal.resetLove();
        this.partner.resetLove();
        RandomSource random = this.animal.getRandom();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
        }
    }
}
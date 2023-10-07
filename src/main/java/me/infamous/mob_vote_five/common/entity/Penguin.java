package me.infamous.mob_vote_five.common.entity;

import me.infamous.mob_vote_five.common.datagen.MVTags;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class Penguin extends Animal {
    public Penguin(EntityType<? extends Penguin> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public static boolean checkPenguinSpawnRules(EntityType<Penguin> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getBlockState(pPos.below()).is(MVTags.PENGUINS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(pLevel, pPos);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(MVTags.PENGUIN_FOOD);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return MVEntityTypes.PENGUIN.get().create(pLevel);
    }
}

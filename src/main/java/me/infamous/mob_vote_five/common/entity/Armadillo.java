package me.infamous.mob_vote_five.common.entity;

import me.infamous.mob_vote_five.common.datagen.MVTags;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public class Armadillo extends Animal implements Roller{
    private static final EntityDataAccessor<Boolean> DATA_ROLLING = SynchedEntityData.defineId(Armadillo.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState revealAnimationState = new AnimationState();
    public final AnimationState hideAnimationState = new AnimationState();
    public final AnimationState hiddenAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState rollAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();

    public Armadillo(EntityType<? extends Armadillo> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public static boolean checkArmadilloSpawnRules(EntityType<Armadillo> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return Animal.checkAnimalSpawnRules(type, pLevel, pSpawnType, pPos, pRandom);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ROLLING, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_ROLLING.equals(pKey)) {
            if (this.isRolling()) {
                this.rollAnimationState.start(this.tickCount);
            } else {
                this.rollAnimationState.stop();
            }
        }

        super.onSyncedDataUpdated(pKey);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    public void tick() {
        if (this.level.isClientSide()) {
            if (this.isMoving()) {
                this.idleAnimationState.stop();
                this.walkAnimationState.startIfStopped(this.tickCount);
            } else {
                this.walkAnimationState.stop();
                this.idleAnimationState.startIfStopped(this.tickCount);
            }
        }

        super.tick();
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(MVTags.ARMADILLO_FOOD);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return MVEntityTypes.ARMADILLO.get().create(pLevel);
    }

    @Override
    public boolean isRolling() {
        return this.entityData.get(DATA_ROLLING);
    }

    @Override
    public void setRolling(boolean rolling) {
        this.entityData.set(DATA_ROLLING, rolling);
    }
}

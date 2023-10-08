package me.infamous.mob_vote_five.common.entity;

import me.infamous.mob_vote_five.common.datagen.MVTags;
import me.infamous.mob_vote_five.common.entity.ai.SwimWithPlayerGoal;
import me.infamous.mob_vote_five.common.entity.ai.SwimmingJumpGoal;
import me.infamous.mob_vote_five.common.entity.ai.turtlelike.*;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Penguin extends Animal implements Swimmer, HasHome, Traveller, LaysEggs {
    private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> TRAVEL_POS = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> GOING_HOME = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> TRAVELLING = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    protected static final TargetingConditions SWIM_WITH_PLAYER_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();
    private static final Ingredient FOOD_ITEMS = Ingredient.of(MVTags.PENGUIN_FOOD);
    public final AnimationState fallAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState leapAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    private int layEggCounter;

    public Penguin(EntityType<? extends Penguin> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DOOR_IRON_CLOSED, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DOOR_WOOD_CLOSED, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.DOOR_OPEN, -1.0F);
        this.moveControl = new TurtlelikeMoveControl<>(this);
        this.maxUpStep = 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public static boolean checkPenguinSpawnRules(EntityType<Penguin> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return onSpawnBlock(pPos, pLevel) && Animal.isBrightEnoughToSpawn(pLevel, pPos);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOME_POS, BlockPos.ZERO);
        this.entityData.define(HAS_EGG, false);
        this.entityData.define(TRAVEL_POS, BlockPos.ZERO);
        this.entityData.define(GOING_HOME, false);
        this.entityData.define(TRAVELLING, false);
        this.entityData.define(LAYING_EGG, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if(DATA_POSE.equals(pKey)){
            if(this.hasPose(Pose.LONG_JUMPING)){
                this.leapAnimationState.startIfStopped(this.tickCount);
            } else{
                this.leapAnimationState.stop();
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BreathAirGoal(this)); // dolphin
        //this.goalSelector.addGoal(0, new TryFindWaterGoal(this)); // dolphin
        //this.goalSelector.addGoal(1, new Dolphin.DolphinSwimToTreasureGoal(this)); // dolphin
        this.goalSelector.addGoal(1, new EggLayerBreedGoal<>(this, 1.0D)); // turtle
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0D)); // turtle
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, FOOD_ITEMS, false)); // turtle
        this.goalSelector.addGoal(2, new SwimWithPlayerGoal<>(this, 4.0D, SWIM_WITH_PLAYER_TARGETING)); // dolphin
        this.goalSelector.addGoal(3, new GoToWaterGoal<>(this, 1.0D)); // turtle
        this.goalSelector.addGoal(4, new GoHomeGoal<>(this, 1.0D)); // turtle
        //this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10)); // dolphin
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // dolphin
        //this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F)); // dolphin
        this.goalSelector.addGoal(5, new SwimmingJumpGoal<>(this, 10)); // dolphin
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2D, true)); // dolphin
        this.goalSelector.addGoal(7, new TravelGoal<>(this, 1.0D)); // turtle
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F)); // turtle
        //this.goalSelector.addGoal(8, new Dolphin.PlayWithItemsGoal()); // dolphin
        this.goalSelector.addGoal(8, new FollowBoatGoal(this)); // dolphin
        this.goalSelector.addGoal(9, new TurtlelikeRandomStrollGoal<>(this, 1.0D, 100)); // turtle
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 8.0F, 1.0D, 1.0D)); // dolphin

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Guardian.class)).setAlertOthers()); // dolphin
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.writeHomePosData(pCompound);
        this.writeHasEggData(pCompound);
        this.writeTravelPosData(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        this.readHomePosData(pCompound);
        super.readAdditionalSaveData(pCompound);
        this.readHasEggData(pCompound);
        this.readTravelPosData(pCompound);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setHomePos(this.blockPosition());
        this.setTravelPos(BlockPos.ZERO);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected int increaseAirSupply(int pCurrentAir) {
        return this.getMaxAirSupply();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
        if (!this.isGoingHome() && pLevel.getFluidState(pPos).is(FluidTags.WATER)) {
            return 10.0F;
        } else {
            return onSpawnBlock(pPos, pLevel) ? 10.0F : pLevel.getPathfindingCostFromLightLevels(pPos);
        }
    }

    public static boolean onSpawnBlock(BlockPos pPos, LevelReader pLevel) {
        return pLevel.getBlockState(pPos.below()).is(MVTags.PENGUINS_SPAWNABLE_ON);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
            BlockPos blockpos = this.blockPosition();
            if (onSpawnBlock(blockpos, this.level)) {
                this.level.levelEvent(2001, blockpos, Block.getId(this.level.getBlockState(blockpos.below())));
            }
        }
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.1F, pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null && (!this.isGoingHome() || !this.getHomePos().closerToCenterThan(this.position(), 20.0D))) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -TurtlelikeMoveControl.SINK_Y_OFFSET, 0.0D));
            }
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return !this.isInWater() && this.onGround && !this.isBaby() ? SoundEvents.PARROT_AMBIENT : super.getAmbientSound();
    }

    @Override
    protected void playSwimSound(float pVolume) {
        super.playSwimSound(pVolume * 1.5F);
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.DOLPHIN_SWIM;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PARROT_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.PARROT_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        SoundEvent stepSound = SoundEvents.PARROT_STEP;
        this.playSound(stepSound, 0.15F, 1.0F);
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove() && !this.hasEgg();
    }

    @Override
    protected float nextStep() {
        return this.moveDist + 0.15F;
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new TravellerPathNavigation<>(this, pLevel);
    }

    @Override
    public void tick() {
        if (this.level.isClientSide()) {
            boolean longJumping = this.hasPose(Pose.LONG_JUMPING);
            if (this.isMovingOnLand() && !longJumping) {
                this.idleAnimationState.stop();
                this.walkAnimationState.startIfStopped(this.tickCount);
            } else {
                this.walkAnimationState.stop();
                if(!longJumping) this.idleAnimationState.startIfStopped(this.tickCount);
            }

            if (this.isInWaterOrBubble() && !longJumping) {
                this.swimAnimationState.startIfStopped(this.tickCount);
            } else {
                this.swimAnimationState.stop();
            }
        }

        super.tick();
        if (this.isNoAi()) {
            this.setAirSupply(this.getMaxAirSupply());
        } else {
            if (this.level.isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03D) {
                Vec3 viewVector = this.getViewVector(0.0F);
                float xOffset = Mth.cos(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float zOffset = Mth.sin(this.getYRot() * ((float)Math.PI / 180F)) * 0.3F;
                float scale = 1.2F - this.random.nextFloat() * 0.7F;

                for(int i = 0; i < 2; ++i) {
                    this.level.addParticle(ParticleTypes.DOLPHIN, this.getX() - viewVector.x * (double)scale + (double)xOffset, this.getY() - viewVector.y, this.getZ() - viewVector.z * (double)scale + (double)zOffset, 0.0D, 0.0D, 0.0D);
                    this.level.addParticle(ParticleTypes.DOLPHIN, this.getX() - viewVector.x * (double)scale - (double)xOffset, this.getY() - viewVector.y, this.getZ() - viewVector.z * (double)scale - (double)zOffset, 0.0D, 0.0D, 0.0D);
                }
            }

        }
    }

    private boolean isMoving() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D;
    }

    private boolean isMovingOnLand() {
        return this.onGround && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D && !this.isInWaterOrBubble();
    }

    private boolean isMovingInWater() {
        return this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D && this.isInWaterOrBubble();
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

    @Override
    public void startSwimmingWithPlayer(Player player) {

    }

    @Override
    public void tickSwimmingWithPlayer(Player player) {

    }

    @Override
    public void stopSwimmingWithPlayer(@Nullable Player player) {

    }

    @Override
    public SoundEvent getJumpSound() {
        return SoundEvents.DOLPHIN_JUMP;
    }

    @Override
    public void startBreaching() {
        this.setPose(Pose.LONG_JUMPING);
    }

    @Override
    public void stopBreaching() {
        if(this.hasPose(Pose.LONG_JUMPING)) this.setPose(Pose.STANDING);
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean layingEgg) {
        this.layEggCounter = layingEgg ? 1 : 0;
        this.entityData.set(LAYING_EGG, layingEgg);
    }

    @Override
    public int getLayEggCounter() {
        return this.layEggCounter;
    }

    @Override
    public void setLayEggCounter(int layEggCounter) {
        this.layEggCounter = layEggCounter;
    }

    @Override
    public void onHatched(BlockPos pPos) {
        this.setHomePos(pPos);
    }

    @Override
    public BlockPos getHomePos() {
        return this.entityData.get(HOME_POS);
    }

    @Override
    public void setHomePos(BlockPos homePos) {
        this.entityData.set(HOME_POS, homePos);
    }

    @Override
    public boolean isGoingHome() {
        return this.entityData.get(GOING_HOME);
    }

    @Override
    public void setGoingHome(boolean goingHome) {
        this.entityData.set(GOING_HOME, goingHome);
    }

    @Override
    public BlockPos getTravelPos() {
        return this.entityData.get(TRAVEL_POS);
    }

    @Override
    public void setTravelPos(BlockPos homePos) {
        this.entityData.set(TRAVEL_POS, homePos);
    }

    @Override
    public boolean isTravelling() {
        return this.entityData.get(TRAVELLING);
    }

    @Override
    public void setTravelling(boolean travelling) {
        this.entityData.set(TRAVELLING, travelling);
    }
}

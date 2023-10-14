package me.infamous.mob_vote_five.common.entity;

import me.infamous.mob_vote_five.common.datagen.MVTags;
import me.infamous.mob_vote_five.common.entity.ai.BreachWaterGoal;
import me.infamous.mob_vote_five.common.entity.ai.LeaveWaterGoal;
import me.infamous.mob_vote_five.common.entity.ai.StumbleGoal;
import me.infamous.mob_vote_five.common.entity.ai.SwimWithBoatGoal;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Penguin extends Animal implements IAnimatable, Swimmer, HasHome, Traveller, LaysEggs, Clumsy {
    private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> TRAVEL_POS = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> GOING_HOME = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> TRAVELLING = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FALLING_OVER = SynchedEntityData.defineId(Penguin.class, EntityDataSerializers.BOOLEAN);
    protected static final TargetingConditions SWIM_WITH_PLAYER_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();
    private static final Ingredient FOOD_ITEMS = Ingredient.of(MVTags.PENGUIN_FOOD);
    protected static final AnimationBuilder FALL_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.penguin.falling", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    protected static final AnimationBuilder IDLE_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.penguin.idle", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder LEAP_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.penguin.jumping_into_the_land", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    protected static final AnimationBuilder PUSH_BOAT_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.penguin.push_boat", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder SWIM_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.penguin.swimming", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder WALK_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.penguin.walk", ILoopType.EDefaultLoopTypes.LOOP);
    private static final int FALL_DURATION = (int)Math.ceil(3.72 * 20);
    public static final float IN_WATER_SPEED_MODIFIER = 0.16F;
    public static final float OUTSIDE_WATER_SPEED_MODIFIER = 1.0F;
    public static final double RESIST_SINK_YD = 0.005;
    public static final float SWIMMING_FAST_SPEED_MODIFIER = 4.0F;
    private final AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);
    private int layEggCounter;
    private boolean isLandNavigator;
    private int rideTimeStamp;
    private WiggleMotion wiggleMotion = WiggleMotion.CENTER_TO_LEFT;

    public Penguin(EntityType<? extends Penguin> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.updateNavigation(false);
        this.maxUpStep = 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.15D).add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public static boolean checkPenguinSpawnRules(EntityType<Penguin> type, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return onSpawnBlock(pPos, pLevel) && Animal.isBrightEnoughToSpawn(pLevel, pPos);
    }

    private void updateNavigation(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this);
            this.lookControl = new LookControl(this);
            this.navigation = new GroundPathNavigation(this, this.level);
            this.isLandNavigator = true;
        } else {
            // dolphins have a move speed of 1.2 that gets multiplied by 0.02 when swimming for a final speed of 0.024
            // to replicate that speed without making the walking speed absurd, we use a move speed of 0.15 that gets multiplied by 0.16 for a final speed of 0.024
            this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, IN_WATER_SPEED_MODIFIER, OUTSIDE_WATER_SPEED_MODIFIER, true);
            this.lookControl = new SmoothSwimmingLookControl(this, 10);
            this.navigation = new TurtlelikePathNavigation<>(this, this.level);
            this.isLandNavigator = false;
        }
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
        this.entityData.define(FALLING_OVER, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new StumbleGoal<>(this, TimeUtil.rangeOfSeconds(25, 40), FALL_DURATION));
        this.goalSelector.addGoal(0, new BreathAirGoal(this)); // dolphin
        this.goalSelector.addGoal(0, new LeaveWaterGoal<>(this)); // custom
        //this.goalSelector.addGoal(0, new TryFindWaterGoal(this)); // dolphin
        //this.goalSelector.addGoal(1, new Dolphin.DolphinSwimToTreasureGoal(this)); // dolphin
        this.goalSelector.addGoal(0, new AquaticPanicGoal(this, 1.2D)); // turtle
        this.goalSelector.addGoal(1, new EggLayerBreedGoal<>(this, 1.0D)); // turtle
        this.goalSelector.addGoal(1, new LayEggGoal<>(this, 1.0D, 16, 9.0D)); // turtle, disabling for now
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1D, FOOD_ITEMS, false)); // turtle
        //this.goalSelector.addGoal(2, new SwimWithPlayerGoal<>(this, 4.0D, SWIM_WITH_PLAYER_TARGETING, 6.25D, 16)); // dolphin
        this.goalSelector.addGoal(3, new SwimmerGoToWaterGoal<>(this, 1.0D, 24)); // turtle
        this.goalSelector.addGoal(4, new GoHomeGoal<>(this, 1.0D, 7.0D)); // turtle
        //this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0D, 10)); // dolphin
        //this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // dolphin
        //this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F)); // dolphin
        this.goalSelector.addGoal(5, new BreachWaterGoal<>(this, 10)); // dolphin
        //this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 1.2D, true)); // dolphin
        this.goalSelector.addGoal(7, new TravelGoal<>(this, 1.0D, 512, 4)); // turtle
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F)); // turtle
        //this.goalSelector.addGoal(8, new Dolphin.PlayWithItemsGoal()); // dolphin
        this.goalSelector.addGoal(8, new SwimWithBoatGoal<>(this, 4, 12, SWIMMING_FAST_SPEED_MODIFIER)); // custom
        this.goalSelector.addGoal(9, new TravellerRandomStrollGoal<>(this, 1.0D, 100)); // turtle
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 8.0F, 1.0D, 1.0D)); // dolphin

        //this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, Guardian.class)).setAlertOthers()); // dolphin
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
        return isSpawnBlock(pPos.below(), pLevel);
    }

    public static boolean isSpawnBlock(BlockPos pPos, LevelReader pLevel) {
        return pLevel.getBlockState(pPos).is(MVTags.PENGUINS_SPAWNABLE_ON);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
            BlockPos blockPos = this.blockPosition();
            if (onSpawnBlock(blockPos, this.level)) {
                this.level.levelEvent(2001, blockPos, Block.getId(this.level.getBlockState(blockPos.below())));
            }
        }
        if(!this.level.isClientSide && (this.tickCount > this.rideTimeStamp + 1200 || !this.isInWater() && !this.isBreaching())){
            Entity firstPassenger = this.getFirstPassenger();
            if (firstPassenger == null) {
                return;
            }

            this.ejectPassengers();
        }
    }

    @Override
    protected boolean canRide(Entity pEntity) {
        return true;
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity firstPassenger = this.getFirstPassenger();
        if (firstPassenger instanceof LivingEntity controllingPassenger) {
            return controllingPassenger;
        }

        return null;
    }

    @Override
    public double getPassengersRidingOffset() {
        // Gobblefins are 2 blocks tall, player riding offset is -0.35, this puts the rider at the y position of the gobblefin
        return (double)this.getBbHeight() * 0.175D;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle();
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);

        if (!this.isBaby()) {
            if (this.isVehicle()) {
                return super.mobInteract(pPlayer, pHand);
            }
        }

        if (!itemInHand.isEmpty()) {
            if (this.isFood(itemInHand)) {
                return super.mobInteract(pPlayer, pHand);
            }
        }

        if(this.isBaby()){
            return super.mobInteract(pPlayer, pHand);
        } else if(this.isInWater() && this.canAddPassenger(pPlayer)){
            this.doPlayerRide(pPlayer);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    protected void doPlayerRide(Player pPlayer) {
        if (!this.level.isClientSide) {
            pPlayer.setYRot(this.getYRot());
            pPlayer.setXRot(this.getXRot());
            pPlayer.startRiding(this);
            this.rideTimeStamp = this.tickCount;
        }
    }

    // horse-like travel
    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isAlive()) {
            if (this.isVehicle()) {
                LivingEntity rider = this.getControllingPassenger();
                this.setYRot(rider.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(rider.getXRot());
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;

                float left = rider.xxa * 0.5F;
                float up = -Mth.sin(this.getXRot() * Mth.DEG_TO_RAD);
                float forward = rider.zza;
                /*
                if (forward <= 0.0F) {
                    forward *= 0.25F;
                }
                 */

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed(this.getMovementSpeed());
                    Vec3 inputVector = new Vec3(left, up, forward);
                    // need this to mimic what the move controller does, as it does not tick on the client
                    if(this.isInWater()){
                        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, RESIST_SINK_YD, 0.0D));
                    }
                    this.doTravel(inputVector, true);
                } else if (rider instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO); // server does not dictate any movement of the penguin if mounted by a player
                }
                this.calculateEntityAnimation(this, false);
            } else {
                this.flyingSpeed = 0.02F; // default
                this.doTravel(pTravelVector, false);
            }
        }

    }

    public float getMovementSpeed() {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (this.isInWater() ?
                IN_WATER_SPEED_MODIFIER : OUTSIDE_WATER_SPEED_MODIFIER);
    }

    public void doTravel(Vec3 pTravelVector, boolean playerControlled) {
        if ((playerControlled || this.isEffectiveAi()) && this.isInWater()) {
            this.moveRelative(playerControlled ? this.getSpeed() * SWIMMING_FAST_SPEED_MODIFIER : 0.1F, pTravelVector);
            if(playerControlled) {
                this.wiggle();
            }
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (playerControlled || (this.getTarget() == null && (!this.isGoingHome() || !this.getHomePos().closerToCenterThan(this.position(), 20.0D)))) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -RESIST_SINK_YD, 0.0D));
            }
        } else {
            super.travel(pTravelVector);
        }
    }

    private void wiggle() {
        if(this.tickCount % 20 == 0){
            this.wiggleMotion = this.wiggleMotion.getNext();
        }
        Vec3 wiggleVec = new Vec3(this.wiggleMotion.getImpulse(), 0, 0).normalize();
        this.moveRelative(this.getSpeed() * SWIMMING_FAST_SPEED_MODIFIER, wiggleVec);
        /*
        if(this.tickCount % 20 == 0){
            Vec3 randomPos = DefaultRandomPos.getPos(this, 5, 4); // taken from RunAroundLikeCrazyGoal
            if(randomPos == null) randomPos = this.position();

            // taken from RamTarget
            Vec3 wiggleDirection = (new Vec3((double)this.blockPosition().getX() - randomPos.x(), 0.0D, (double)this.blockPosition().getZ() - randomPos.z())).normalize();
            int moveSpeedAmp = this.hasEffect(MobEffects.MOVEMENT_SPEED) ? this.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() + 1 : 0;
            int moveSlowdownAmp = this.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) ? this.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() + 1 : 0;
            float moveAmp = 0.25F * (float)(moveSpeedAmp - moveSlowdownAmp);
            float baseKnockback = Mth.clamp(this.getSpeed() * 1.65F, 0.2F, 3.0F) + moveAmp;
            this.knockback(baseKnockback * 2.5F, wiggleDirection.x(), wiggleDirection.z());
        }
         */
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
    public void tick() {
        super.tick();

        boolean onLand = !this.isInWater();
        if (!onLand && this.isLandNavigator) {
            this.updateNavigation(false);
        }
        if (onLand && !this.isLandNavigator) {
            this.updateNavigation(true);
        }

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

    private boolean isBreaching() {
        return this.hasPose(Pose.LONG_JUMPING);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(MVTags.PENGUIN_FOOD);
    }

    @Nullable
    @Override
    public Penguin getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return MVEntityTypes.PENGUIN.get().create(pLevel);
    }

    @Override
    public void startSwimmingWithPlayer(Player player) {
        //player.addEffect(new MobEffectInstance(MVMobEffects.PENGUINS_GRACE.get(), 100), this);
    }

    @Override
    public void tickSwimmingWithPlayer(Player player) {
        /*
        if (player.isSwimming() && player.level.random.nextInt(6) == 0) {
            player.addEffect(new MobEffectInstance(MVMobEffects.PENGUINS_GRACE.get(), 100), this);
        }
         */
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
        if(this.isBreaching()) this.setPose(Pose.STANDING);
    }

    @Override
    public boolean wantsToFindWater() {
        if(this.isBaby()){
            return false;
        }
        return !this.isGoingHome() && !this.hasEgg();
    }

    @Override
    public boolean wantsToSwimAway() {
        return !this.isGoingHome() && !this.hasEgg() && !this.isInLove();
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
    public void layEgg(ServerLevel level, BlockPos layPos) {
        level.playSound(null, this.blockPosition(), SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);

        Penguin breedOffspring = this.getBreedOffspring(level, null);
        if (breedOffspring != null) {
            breedOffspring.onHatched(layPos);
            breedOffspring.setBaby(true);
            breedOffspring.moveTo(layPos, 0.0F, 0.0F);
            level.addFreshEntityWithPassengers(breedOffspring);
        }
        /*
        this.level.setBlock(layPos, Blocks.TURTLE_EGG.defaultBlockState().setValue(TurtleEggBlock.EGGS, this.random.nextInt(4) + 1), 3);
         */
    }

    @Override
    public boolean canLayEggsOn(BlockPos targetPos, LevelReader level) {
        return isSpawnBlock(targetPos, level);
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
    public boolean wantsToGoHome() {
        if (this.isBaby()) {
            return true;
        } else if (this.hasEgg()) {
            return true;
        } else if (this.random.nextInt(350) != 0) {
            return false;
        } else {
            return !this.getHomePos().closerToCenterThan(this.position(), 64.0D);
        }
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

    @Override
    public boolean wantsToTravel() {
        return !this.isGoingHome() && !this.hasEgg() && this.isInWater();
    }

    @Override
    public boolean canContinueTravelling() {
        return !this.isGoingHome() && !this.isInLove() && !this.hasEgg();
    }

    @Override
    public boolean wantsToStroll() {
        return !this.isInWater() && !this.isGoingHome() && !this.hasEgg();
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        if(this.isFallingOver()){
            event.getController().setAnimation(FALL_ANIMATION_STATE);
        } else if(this.isInWater() || this.isBreaching()){
            event.getController().setAnimation(SWIM_ANIMATION_STATE);
        } else if(event.isMoving()){
            event.getController().setAnimation(WALK_ANIMATION_STATE);
        } else{
            event.getController().setAnimation(IDLE_ANIMATION_STATE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public boolean canStumble() {
        return !this.getNavigation().isDone() && this.onGround && !this.isInWater() && !this.hasEgg();
    }

    @Override
    public boolean isFallingOver() {
        return this.entityData.get(FALLING_OVER);
    }

    @Override
    public void setFallingOver(boolean fallingOver) {
        this.entityData.set(FALLING_OVER, fallingOver);
    }
}

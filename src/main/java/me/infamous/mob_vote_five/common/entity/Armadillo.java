package me.infamous.mob_vote_five.common.entity;

import me.infamous.mob_vote_five.common.datagen.MVTags;
import me.infamous.mob_vote_five.common.entity.ai.hider.HiddenGoal;
import me.infamous.mob_vote_five.common.entity.ai.hider.Hider;
import me.infamous.mob_vote_five.common.entity.ai.hider.HidingGoal;
import me.infamous.mob_vote_five.common.entity.ai.hider.RevealingGoal;
import me.infamous.mob_vote_five.common.registry.MVDataSerializers;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
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

import java.util.UUID;

public class Armadillo extends Animal implements IAnimatable, Hider{
    private static final UUID COVERED_ARMOR_MODIFIER_UUID = UUID.fromString("2b0a0d7f-c097-4de0-ad8b-1079ede7e85c");
    private static final AttributeModifier COVERED_ARMOR_MODIFIER = new AttributeModifier(COVERED_ARMOR_MODIFIER_UUID, "Covered armor bonus", 20.0D, AttributeModifier.Operation.ADDITION);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(MVTags.ARMADILLO_FOOD);
    private static final EntityDataAccessor<Hider.HideState> DATA_HIDE_STATE = SynchedEntityData.defineId(Armadillo.class, MVDataSerializers.DIG_STATE.get());
    protected static final AnimationBuilder APPEAR_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.appear", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    protected static final AnimationBuilder HIDE_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.hide", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    protected static final AnimationBuilder HIDDEN_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.hide_idle", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder IDLE_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.idle", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ROLL_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.roll", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder WALK_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.walk", ILoopType.EDefaultLoopTypes.LOOP);
    private static final int REVEAL_DURATION = (int) Math.ceil(0.6D * 20);
    private static final int HIDE_DURATION = (int) Math.ceil(0.72D * 20);

    private final AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);

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
        this.entityData.define(DATA_HIDE_STATE, Hider.HideState.REVEALED);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RevealingGoal<>(this, REVEAL_DURATION));
        this.goalSelector.addGoal(0, new HidingGoal<>(this, HIDE_DURATION));
        this.goalSelector.addGoal(1, new HiddenGoal<>(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.2D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.SPAWN_EGG || (pReason == MobSpawnType.NATURAL && this.random.nextBoolean())) {
            this.setHidden();
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.isHidden() ? 1 : 0);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        if (pPacket.getData() == 1) {
            this.setHidden();
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.writeHideState(pCompound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readHideState(pCompound);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
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
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        if(this.isHidden()){
            if(event.isMoving()){
                event.getController().setAnimation(ROLL_ANIMATION_STATE);
            } else{
                event.getController().setAnimation(HIDDEN_ANIMATION_STATE);
            }
        } else if(this.isHiding()){
            event.getController().setAnimation(HIDE_ANIMATION_STATE);
        } else if(this.isRevealing()){
            event.getController().setAnimation(APPEAR_ANIMATION_STATE);
        } else{
             if(event.isMoving()){
                event.getController().setAnimation(WALK_ANIMATION_STATE);
            } else{
                event.getController().setAnimation(IDLE_ANIMATION_STATE);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public SoundEvent getRevealSound() {
        return SoundEvents.SHULKER_BOX_OPEN;
    }

    @Override
    public SoundEvent getHideSound() {
        return SoundEvents.SHULKER_BOX_CLOSE;
    }

    @Override
    public HideState getHideState() {
        return this.entityData.get(DATA_HIDE_STATE);
    }

    @Override
    public void setHideState(HideState hideState) {
        this.entityData.set(DATA_HIDE_STATE, hideState);
        if(!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(COVERED_ARMOR_MODIFIER);
            if (hideState == HideState.HIDDEN) {
                this.getAttribute(Attributes.ARMOR).addPermanentModifier(COVERED_ARMOR_MODIFIER);
            }
        }
    }

    @Override
    public void doPush(Entity pEntity) {
        super.doPush(pEntity);
        if(this.isHidden()){
            float yRot = pEntity.getYRot();
            this.setYRot(yRot);
            this.yRotO = this.getYRot();
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
        }
    }

    @Override
    public boolean wantsToHide() {
        return false;
    }

    @Override
    public boolean wantsToReveal() {
        // the boolean argument makes it so creative players are ignored for the search
        return this.level.getNearestPlayer(this.getX(), this.getY(), this.getZ(), 8, true) != null;
    }
}

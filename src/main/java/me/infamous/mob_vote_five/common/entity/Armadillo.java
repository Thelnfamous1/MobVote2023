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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Armadillo extends Animal implements IAnimatable, Roller{
    private static final EntityDataAccessor<Boolean> DATA_ROLLING = SynchedEntityData.defineId(Armadillo.class, EntityDataSerializers.BOOLEAN);
    protected static final AnimationBuilder APPEAR_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.appear", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    protected static final AnimationBuilder HIDE_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.hide", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    protected static final AnimationBuilder HIDDEN_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.hide_idle", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder IDLE_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.idle", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder ROLL_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.roll", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder WALK_ANIMATION_STATE = new AnimationBuilder().addAnimation("animation.armadillo.walk", ILoopType.EDefaultLoopTypes.LOOP);

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
        this.entityData.define(DATA_ROLLING, false);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
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


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        if(this.isRolling()){
            event.getController().setAnimation(ROLL_ANIMATION_STATE);
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
}

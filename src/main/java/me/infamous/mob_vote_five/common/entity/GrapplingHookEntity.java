package me.infamous.mob_vote_five.common.entity;

import me.infamous.mob_vote_five.common.duck.Grappler;
import me.infamous.mob_vote_five.common.item.GrapplingHookItem;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import me.infamous.mob_vote_five.common.registry.MVItems;
import me.infamous.mob_vote_five.mixin.ProjectileAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
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

import java.util.Optional;
import java.util.UUID;

public class GrapplingHookEntity extends AbstractArrow implements IAnimatable, IEntityAdditionalSpawnData {
    private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER_UUID = SynchedEntityData.defineId(GrapplingHookEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Integer> DATA_OWNERID_ID = SynchedEntityData.defineId(GrapplingHookEntity.class, EntityDataSerializers.INT);

    public static final boolean ENABLE_PICKUP = false;
    private static final String ITEM_TAG = "CrabClaw";
    private final AnimationFactory animationFactory = GeckoLibUtil.createFactory(this);

    protected static final AnimationBuilder OPEN_ANIM = new AnimationBuilder().addAnimation("animation.crab_claw.open", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    protected static final AnimationBuilder CLOSE_ANIM = new AnimationBuilder().addAnimation("animation.crab_claw.close", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME);
    private ItemStack crabClawItem = new ItemStack(MVItems.CRAB_CLAW.get());

    public GrapplingHookEntity(EntityType<? extends GrapplingHookEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public GrapplingHookEntity(Level pLevel, double pX, double pY, double pZ) {
        super(MVEntityTypes.GRAPPLING_HOOK.get(), pX, pY, pZ, pLevel);
    }

    public GrapplingHookEntity(Level pLevel, LivingEntity pShooter, ItemStack pStack) {
        super(MVEntityTypes.GRAPPLING_HOOK.get(), pShooter, pLevel);
        if (ENABLE_PICKUP) this.crabClawItem = pStack.copy();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNER_UUID, Optional.empty());
        this.entityData.define(DATA_OWNERID_ID, 0);
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (!this.level.isClientSide && (this.inGround || this.isNoPhysics()) && this.shakeTime <= 0) {
            if (this.tryPickup(pEntity)) {
                if(pEntity.getUseItem().getItem() instanceof GrapplingHookItem) pEntity.stopUsingItem();
                pEntity.take(this, 1);
                this.discard();
            }

        }

    }

    @Override
    protected boolean tryPickup(Player pPlayer) {
        return switch (this.pickup) {
            case ALLOWED -> this.getPickupItem().isEmpty() || pPlayer.getInventory().add(this.getPickupItem());
            case CREATIVE_ONLY -> pPlayer.getAbilities().instabuild;
            default -> false;
        };
    }

    @Override
    protected ItemStack getPickupItem() {
        return ENABLE_PICKUP ? this.crabClawItem.copy() : ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity hitEntity = pResult.getEntity();
        float damage = 8.0F;
        if (hitEntity instanceof LivingEntity livingentity) {
            damage += EnchantmentHelper.getDamageBonus(this.crabClawItem, livingentity.getMobType());
        }

        Entity owner = this.getOwner();
        DamageSource damageSource = DamageSource.thrown(this, owner == null ? this : owner);
        //this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.TRIDENT_HIT;
        if (hitEntity.hurt(damageSource, damage)) {
            if (hitEntity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (hitEntity instanceof LivingEntity victim) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(victim, owner);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)owner, victim);
                }

                this.doPostHurtEffects(victim);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        this.playSound(soundEvent, 1.0F, 1.0F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> tAnimationEvent) {
        if(this.inGround){
            tAnimationEvent.getController().setAnimation(CLOSE_ANIM);
        } else{
            tAnimationEvent.getController().setAnimation(OPEN_ANIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        /*
        Entity owner = this.getOwner();
        buffer.writeVarInt(owner == null ? 0 : owner.getId());
         */
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        /*
        Entity owner = this.level.getEntity(additionalData.readVarInt());
        if (owner != null) {
            this.setOwner(owner);
        }
         */
    }

    public Optional<UUID> getOwnerUUID(){
        return this.entityData.get(DATA_OWNER_UUID);
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID){
        this.entityData.set(DATA_OWNER_UUID, Optional.ofNullable(ownerUUID));
    }

    public int getOwnerId(){
        return this.entityData.get(DATA_OWNERID_ID);
    }

    public void setOwnerId(int ownerId){
        this.entityData.set(DATA_OWNERID_ID, ownerId);
    }

    @Override
    public void setOwner(@Nullable Entity pEntity) {
        super.setOwner(pEntity);
        this.setOwnerUUID(pEntity == null ? null : pEntity.getUUID());
        this.setOwnerId(pEntity == null ? 0 : pEntity.getId());
    }

    @Nullable
    @Override
    public Entity getOwner() {
        Entity cachedOwner = this.getCachedOwner();
        UUID ownerUUID = this.getOwnerUUID().orElse(null);
        if(ownerUUID == null) return null;

        int ownerId = this.getOwnerId();
        if (cachedOwner != null && !cachedOwner.isRemoved() && ownerId == cachedOwner.getId()) {
            return cachedOwner;
        } else {
            if (!this.level.isClientSide) {
                this.setOwner(((ServerLevel)this.level).getEntity(ownerUUID));
                Entity newCachedOwner = this.getCachedOwner();
                if(newCachedOwner instanceof ServerPlayer player){
                    ((Grappler)player).setGHook(this);
                }
                return newCachedOwner;
            } else if(ownerId != 0){
                this.setCachedOwner(this.level.getEntity(ownerId));
                return this.getCachedOwner();
            } else {
                return null;
            }
        }
    }

    private void setCachedOwner(@Nullable Entity cachedOwner) {
        ((ProjectileAccessor) this).mob_vote_five_setCachedOwner(cachedOwner);
    }

    @Nullable
    private Entity getCachedOwner() {
        return ((ProjectileAccessor) this).mob_vote_five_getCachedOwner();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setOwnerUUID(this.getUnsyncedOwnerUUID());
        if (ENABLE_PICKUP && pCompound.contains(ITEM_TAG, 10)) {
            this.crabClawItem = ItemStack.of(pCompound.getCompound(ITEM_TAG));
        }
    }

    @Nullable
    private UUID getUnsyncedOwnerUUID() {
        return ((ProjectileAccessor) this).mob_vote_five_getOwnerUUID();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (ENABLE_PICKUP) pCompound.put(ITEM_TAG, this.crabClawItem.save(new CompoundTag()));
    }
}

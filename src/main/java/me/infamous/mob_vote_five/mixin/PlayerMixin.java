package me.infamous.mob_vote_five.mixin;

import me.infamous.mob_vote_five.common.duck.Grappler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements Grappler {
    @Shadow public abstract void remove(RemovalReason pReason);

    @Unique
    private static final EntityDataAccessor<Optional<UUID>> DATA_GHOOK_UUID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UUID);
    @Unique
    private static final EntityDataAccessor<Integer> DATA_GHOOK_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
    @Unique
    @Nullable
    private Entity gHook;

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    private void handleDefineSynchedData(CallbackInfo ci){
        this.entityData.define(DATA_GHOOK_UUID, Optional.empty());
        this.entityData.define(DATA_GHOOK_ID, 0);
    }

    @Unique
    public int getGHookId() {
        return this.entityData.get(DATA_GHOOK_ID);
    }

    @Override
    public void setGHookId(int gHookId) {
        this.entityData.set(DATA_GHOOK_ID, gHookId);
    }

    @Override
    public void setGHook(@Nullable Entity gHook) {
        this.gHook = gHook;
        this.setGHookUUID(gHook == null ? null : gHook.getUUID());
        this.setGHookId(gHook == null ? 0 : gHook.getId());
    }

    @Unique
    private Optional<UUID> getGHookUUID(){
        return this.entityData.get(DATA_GHOOK_UUID);
    }

    @Unique
    private void setGHookUUID(@Nullable UUID ownerUUID){
        this.entityData.set(DATA_GHOOK_UUID, Optional.ofNullable(ownerUUID));
    }

    @Override
    public Entity getGrapplingHook() {
        UUID gHookUUID = this.getGHookUUID().orElse(null);
        if(gHookUUID == null) return null;

        int gHookID = this.getGHookId();
        if (this.gHook != null && !this.gHook.isRemoved() && gHookID == this.gHook.getId()) {
            return this.gHook;
        } else if (this.level instanceof ServerLevel) {
            this.setGHook(((ServerLevel)this.level).getEntity(gHookUUID));
            return this.gHook;
        } else if(gHookID != 0){
            this.gHook = this.level.getEntity(gHookID);
            return this.gHook;
        } else {
            return null;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void handleAddSaveData(CompoundTag pCompound, CallbackInfo ci){
        this.getGHookUUID().ifPresent(gHookUUID -> pCompound.putUUID("GrapplingHook", gHookUUID));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void handleReadSaveData(CompoundTag pCompound, CallbackInfo ci){
        if (pCompound.hasUUID("GrapplingHook")) {
            this.setGHookUUID(pCompound.getUUID("GrapplingHook"));
        }
    }
}

package me.infamous.mob_vote_five.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public interface HasHome {

    default void writeHomePosData(CompoundTag pCompound) {
        pCompound.putInt("HomePosX", this.getHomePos().getX());
        pCompound.putInt("HomePosY", this.getHomePos().getY());
        pCompound.putInt("HomePosZ", this.getHomePos().getZ());
    }

    default void readHomePosData(CompoundTag pCompound) {
        int i = pCompound.getInt("HomePosX");
        int j = pCompound.getInt("HomePosY");
        int k = pCompound.getInt("HomePosZ");
        this.setHomePos(new BlockPos(i, j, k));
    }

    BlockPos getHomePos();

    void setHomePos(BlockPos homePos);

    boolean isGoingHome();

    void setGoingHome(boolean goingHome);
}

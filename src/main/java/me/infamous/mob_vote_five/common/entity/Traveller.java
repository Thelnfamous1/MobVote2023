package me.infamous.mob_vote_five.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public interface Traveller {

    default void writeTravelPosData(CompoundTag pCompound) {
        pCompound.putInt("TravelPosX", this.getTravelPos().getX());
        pCompound.putInt("TravelPosY", this.getTravelPos().getY());
        pCompound.putInt("TravelPosZ", this.getTravelPos().getZ());
    }

    default void readTravelPosData(CompoundTag pCompound) {
        int l = pCompound.getInt("TravelPosX");
        int i1 = pCompound.getInt("TravelPosY");
        int j1 = pCompound.getInt("TravelPosZ");
        this.setTravelPos(new BlockPos(l, i1, j1));
    }

    BlockPos getTravelPos();

    void setTravelPos(BlockPos homePos);

    boolean isTravelling();

    void setTravelling(boolean travelling);
}

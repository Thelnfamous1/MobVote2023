package me.infamous.mob_vote_five.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;

public interface LaysEggs {

    default void writeHasEggData(CompoundTag pCompound) {
        pCompound.putBoolean("HasEgg", this.hasEgg());
    }

    default void readHasEggData(CompoundTag pCompound) {
        this.setHasEgg(pCompound.getBoolean("HasEgg"));
    }

    boolean hasEgg();

    void setHasEgg(boolean hasEgg);

    boolean isLayingEgg();

    void setLayingEgg(boolean layingEgg);

    int getLayEggCounter();

    void setLayEggCounter(int layEggCounter);

    void onHatched(BlockPos pPos);

    void layEgg(ServerLevel level, BlockPos layPos);

    boolean canLayEggsOn(BlockPos targetPos, LevelReader level);
}

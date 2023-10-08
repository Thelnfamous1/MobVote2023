package me.infamous.mob_vote_five.common.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

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
}

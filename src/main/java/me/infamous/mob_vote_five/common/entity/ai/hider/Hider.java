package me.infamous.mob_vote_five.common.entity.ai.hider;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvent;

public interface Hider {

    String HIDE_STATE = "HideState";

    default boolean isHiding(){
        return this.getHideState() == HideState.HIDING;
    }

    default boolean isRevealing(){
        return this.getHideState() == HideState.REVEALING;
    }

    default void setHiding(){
        this.setHideState(HideState.HIDING);
    }

    default void setRevealing(){
        this.setHideState(HideState.REVEALING);
    }

    default void readHideState(CompoundTag pCompound) {
        if (pCompound.contains(HIDE_STATE, Tag.TAG_ANY_NUMERIC)) {
            HideState hideState = HideState.byOrdinal(pCompound.getByte(HIDE_STATE));
            if(!hideState.isTransitional()) this.setHideState(hideState);
        }
    }

    default void writeHideState(CompoundTag pCompound) {
        if(!this.getHideState().isTransitional()) pCompound.putByte(HIDE_STATE, (byte) this.getHideState().ordinal());
    }

    SoundEvent getRevealSound();

    SoundEvent getHideSound();

    default void setRevealed(){
        this.setHideState(HideState.REVEALED);
    }

    default boolean isRevealed(){
        return this.getHideState() == HideState.REVEALED;
    }

    default void setHidden(){
        this.setHideState(HideState.HIDDEN);
    }

    default boolean isHidden(){
        return this.getHideState() == HideState.HIDDEN;
    }

    HideState getHideState();

    void setHideState(HideState hideState);

    boolean wantsToHide();

    boolean wantsToReveal();

    enum HideState {
        REVEALED(false),
        HIDING(true),
        HIDDEN(false),
        REVEALING(true);

        private final boolean transitional;

        HideState(boolean transitional) {
            this.transitional = transitional;
        }

        public static HideState byOrdinal(int ordinal){
            if (ordinal < 0 || ordinal > values().length) {
                ordinal = 0;
            }

            return values()[ordinal];
        }

        public boolean isTransitional() {
            return this.transitional;
        }
    }
}

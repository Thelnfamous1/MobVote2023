package me.infamous.mob_vote_five.common.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface Swimmer {

    void startSwimmingWithPlayer(Player player);

    void tickSwimmingWithPlayer(Player player);

    void stopSwimmingWithPlayer(@Nullable Player player);

    SoundEvent getJumpSound();

    void startBreaching();

    void stopBreaching();

    boolean wantsToFindWater();

    boolean canSwimWithPlayer(Player player);
}

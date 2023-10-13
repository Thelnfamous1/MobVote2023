package me.infamous.mob_vote_five.common.duck;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public interface Grappler {

    void setGHookId(int gHookId);

    void setGHook(@Nullable Entity gHook);

    @Nullable
    Entity getGrapplingHook();

}

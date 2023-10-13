package me.infamous.mob_vote_five.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(Projectile.class)
public interface ProjectileAccessor {

    @Nullable
    @Accessor("cachedOwner")
    Entity mob_vote_five_getCachedOwner();

    @Accessor("cachedOwner")
    void mob_vote_five_setCachedOwner(@Nullable Entity cachedOwner);

    @Nullable
    @Accessor("ownerUUID")
    UUID mob_vote_five_getOwnerUUID();

}

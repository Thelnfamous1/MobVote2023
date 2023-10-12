package me.infamous.mob_vote_five.mixin;

import me.infamous.mob_vote_five.common.registry.MVMobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;

@Mixin(Boat.class)
public abstract class BoatMixin {

    @Shadow @Nullable public abstract Entity getControllingPassenger();

    @ModifyVariable(method = "controlBoat", ordinal = 0, at = @At(value = "LOAD", ordinal = 3))
    private float modifyBoatInputSpeed(float original){
        if(this.getControllingPassenger() instanceof LivingEntity driver && driver.hasEffect(MVMobEffects.PENGUINS_GRACE.get())){
            return original * 2.0F;
        }
        return original;
    }
}

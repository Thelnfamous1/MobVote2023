package me.infamous.mob_vote_five.mixin;

import me.infamous.mob_vote_five.common.item.WolfArmorItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public class WolfMixin {

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void handleMobInteract(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir){
        InteractionResult armorInteract = WolfArmorItem.armorInteract((Wolf) (Object) this, pPlayer, pPlayer.getItemInHand(pHand));
        if(armorInteract.consumesAction()){
            cir.setReturnValue(armorInteract);
        }
    }
}

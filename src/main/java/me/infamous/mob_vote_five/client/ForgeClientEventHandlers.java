package me.infamous.mob_vote_five.client;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.client.keybinding.MVKeyMapping;
import me.infamous.mob_vote_five.common.entity.Armadillo;
import me.infamous.mob_vote_five.common.entity.Penguin;
import me.infamous.mob_vote_five.common.network.MVNetwork;
import me.infamous.mob_vote_five.common.network.ServerboundKickArmadilloPacket;
import me.infamous.mob_vote_five.common.network.ServerboundRetractGrapplingHookPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MobVote2023.MODID, value = Dist.CLIENT)
public class ForgeClientEventHandlers {
    /*
    private static boolean wasUsingItem = false;
    private static boolean stoppedUsingItem = false;
    private static boolean releasedKeyUse = false;
     */

    @SubscribeEvent
    static void onFOVModifer(ComputeFovModifierEvent event){
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if(localPlayer.getVehicle() instanceof Penguin penguin){
            float rideModifier = (penguin.getSpeed() * Penguin.SWIMMING_FAST_SPEED_MODIFIER / penguin.getMovementSpeed()) * 2.0F;
            if (penguin.getMovementSpeed() == 0.0F || Float.isNaN(rideModifier) || Float.isInfinite(rideModifier)) {
                rideModifier = 1.0F;
            }
            event.setNewFovModifier(event.getNewFovModifier() * rideModifier);
        }
    }

    @SubscribeEvent
    static void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.START && Minecraft.getInstance().player != null){
            if(MVKeyMapping.KEY_KICK_ARMADILLO.isInitialPress()) {
                HitResult hitResult = Minecraft.getInstance().hitResult;
                if (!Minecraft.getInstance().player.isSpectator()
                        && hitResult != null
                        && hitResult.getType() == HitResult.Type.ENTITY
                        && ((EntityHitResult)hitResult).getEntity() instanceof Armadillo armadillo
                        && !armadillo.isRevealed()) {
                    MVNetwork.SYNC_CHANNEL.sendToServer(new ServerboundKickArmadilloPacket(armadillo));
                }
            }
            if(MVKeyMapping.KEY_RETRACT_GRAPPLING_HOOK.isInitialPress()) {
                MVNetwork.SYNC_CHANNEL.sendToServer(new ServerboundRetractGrapplingHookPacket());
            }
        }
    }

    /*

    @SubscribeEvent
    static void onKeyInput(InputEvent.InteractionKeyMappingTriggered event){
        if(!event.isUseItem()) return;

        if(event.getKeyMapping() == Minecraft.getInstance().options.keyUse){
            if(Minecraft.getInstance().player.isHolding(is -> is.getItem() instanceof GrapplingHookItem)){
                if(stoppedUsingItem && !releasedKeyUse){
                    event.setCanceled(true);
                    event.setSwingHand(false);
                }
            }
        }
    }

    @SubscribeEvent
    static void onClientTick(TickEvent.ClientTickEvent event){
        if(event.phase == TickEvent.Phase.END) return;
        if(Minecraft.getInstance().player != null) handleUseKeyDown(Minecraft.getInstance().player);
    }

    private static void handleUseKeyDown(LocalPlayer player) {
        boolean isUsingItem = player.isUsingItem();
        if(!isUsingItem && wasUsingItem){
            wasUsingItem = false;
            stoppedUsingItem = true;
        } else if(!wasUsingItem){
            wasUsingItem = true;
            stoppedUsingItem = false;
        }

        boolean isDown = Minecraft.getInstance().options.keyUse.isDown();
        if(isDown && releasedKeyUse){
            releasedKeyUse = false;
        } else if(!releasedKeyUse){
            releasedKeyUse = true;
        }
    }

     */

}

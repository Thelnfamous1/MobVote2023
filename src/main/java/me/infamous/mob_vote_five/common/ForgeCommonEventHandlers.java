package me.infamous.mob_vote_five.common;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.Penguin;
import me.infamous.mob_vote_five.common.registry.MVMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = MobVote2023.MODID)
public class ForgeCommonEventHandlers {

    @SubscribeEvent
    static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(event.phase == TickEvent.Phase.END && !event.player.level.isClientSide && event.player.tickCount % 20 == 0 && event.player.getVehicle() instanceof Boat){
            List<Penguin> penguins = event.player.level.getEntitiesOfClass(Penguin.class, event.player.getBoundingBox().inflate(8));
            if(!penguins.isEmpty()){
                event.player.addEffect(new MobEffectInstance(MVMobEffects.PENGUINS_GRACE.get(), 100));
            }
        }
    }
}

package me.infamous.mob_vote_five.common;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.Armadillo;
import me.infamous.mob_vote_five.common.entity.Crab;
import me.infamous.mob_vote_five.common.entity.Penguin;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MobVote2023.MODID)
public class ModCommonEventHandlers {

    @SubscribeEvent
    static void onEntityAttributeCreation(EntityAttributeCreationEvent event){
        event.put(MVEntityTypes.ARMADILLO.get(), Armadillo.createAttributes().build());
        event.put(MVEntityTypes.CRAB.get(), Crab.createAttributes().build());
        event.put(MVEntityTypes.PENGUIN.get(), Penguin.createAttributes().build());
    }
}

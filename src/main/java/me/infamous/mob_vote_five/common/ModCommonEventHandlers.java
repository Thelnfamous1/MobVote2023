package me.infamous.mob_vote_five.common;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.entity.Armadillo;
import me.infamous.mob_vote_five.common.entity.Crab;
import me.infamous.mob_vote_five.common.entity.Penguin;
import me.infamous.mob_vote_five.common.network.MVNetwork;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MobVote2023.MODID)
public class ModCommonEventHandlers {

    @SubscribeEvent
    static void onRegisterSpawnPlacements(SpawnPlacementRegisterEvent event){
        event.register(MVEntityTypes.ARMADILLO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Armadillo::checkArmadilloSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(MVEntityTypes.CRAB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Crab::checkCrabSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(MVEntityTypes.PENGUIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Penguin::checkPenguinSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

    }

    @SubscribeEvent
    static void onEntityAttributeCreation(EntityAttributeCreationEvent event){
        event.put(MVEntityTypes.ARMADILLO.get(), Armadillo.createAttributes().build());
        event.put(MVEntityTypes.CRAB.get(), Crab.createAttributes().build());
        event.put(MVEntityTypes.PENGUIN.get(), Penguin.createAttributes().build());
    }

    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(MVNetwork::init);
    }
}

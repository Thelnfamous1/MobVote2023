package me.infamous.mob_vote_five.client;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.client.renderer.ArmadilloRenderer;
import me.infamous.mob_vote_five.client.renderer.CrabRenderer;
import me.infamous.mob_vote_five.client.renderer.PenguinRenderer;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MobVote2023.MODID, value = Dist.CLIENT)
public class ModClientEventHandlers {

    @SubscribeEvent
    static void onRenderRegistry(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(MVEntityTypes.ARMADILLO.get(), ArmadilloRenderer::new);
        event.registerEntityRenderer(MVEntityTypes.CRAB.get(), CrabRenderer::new);
        event.registerEntityRenderer(MVEntityTypes.PENGUIN.get(), PenguinRenderer::new);
    }

    /*
    @SubscribeEvent
    static void onRenderRegistry(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(MVModelLayers.ARMADILLO, ArmadilloModel::createBodyLayer);
        event.registerLayerDefinition(MVModelLayers.CRAB, CrabModel::createBodyLayer);
        event.registerLayerDefinition(MVModelLayers.PENGUIN, PenguinModel::createBodyLayer);
    }
     */
}

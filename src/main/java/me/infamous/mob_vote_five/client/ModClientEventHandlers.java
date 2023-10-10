package me.infamous.mob_vote_five.client;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.client.renderer.ArmadilloRenderer;
import me.infamous.mob_vote_five.client.renderer.CrabRenderer;
import me.infamous.mob_vote_five.client.renderer.PenguinRenderer;
import me.infamous.mob_vote_five.client.renderer.WolfArmorLayer;
import me.infamous.mob_vote_five.common.registry.MVEntityTypes;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.world.entity.EntityType;
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

    @SubscribeEvent
    static void onRenderRegistry(EntityRenderersEvent.AddLayers event){
        try{
            WolfRenderer renderer = event.getRenderer(EntityType.WOLF);
            renderer.addLayer(new WolfArmorLayer<>(renderer, event.getEntityModels()));
        } catch (ClassCastException e){
            MobVote2023.LOGGER.error("Could not add WolfArmorLayer to Wolf, its renderer is not an instance of WolfRenderer!");
        }
    }

    @SubscribeEvent
    static void onRenderRegistry(EntityRenderersEvent.RegisterLayerDefinitions event){
        /*
        event.registerLayerDefinition(MVModelLayers.ARMADILLO, ArmadilloModel::createBodyLayer);
        event.registerLayerDefinition(MVModelLayers.CRAB, CrabModel::createBodyLayer);
        event.registerLayerDefinition(MVModelLayers.PENGUIN, PenguinModel::createBodyLayer);
         */
        event.registerLayerDefinition(MVModelLayers.WOLF_ARMOR, () -> LayerDefinition.create(WolfArmorLayer.createBodyMesh(new CubeDeformation(0.25F)), 64, 32));
    }
}

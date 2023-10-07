package me.infamous.mob_vote_five.client.renderer.model;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import me.infamous.mob_vote_five.client.animation.CrabAnimation;
import me.infamous.mob_vote_five.common.entity.Crab;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class CrabModel<T extends Crab> extends HierarchicalModel<T> {
	private final ModelPart crab;
	private final ModelPart root;

	public CrabModel(ModelPart root) {
		this.crab = root.getChild("crab");
		this.root = root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition root = meshDefinition.getRoot();

		PartDefinition crab = root.addOrReplaceChild("crab", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -2.51F, -4.5F, 9.0F, 5.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 19.0F, 0.0F));

		PartDefinition mounth1 = crab.addOrReplaceChild("mounth1", CubeListBuilder.create().texOffs(0, 2).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -1.0F, -4.5F));

		PartDefinition mounth2 = crab.addOrReplaceChild("mounth2", CubeListBuilder.create().texOffs(0, 2).addBox(-0.5F, -0.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -1.0F, -4.5F));

		PartDefinition eye1 = crab.addOrReplaceChild("eye1", CubeListBuilder.create().texOffs(0, 5).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -2.0F, -4.02F));

		PartDefinition eye2 = crab.addOrReplaceChild("eye2", CubeListBuilder.create().texOffs(0, 5).mirror().addBox(-1.0F, -1.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.5F, -2.0F, -4.02F));

		PartDefinition great_pincer = crab.addOrReplaceChild("great_pincer", CubeListBuilder.create(), PartPose.offsetAndRotation(4.5F, 0.5F, -4.5F, 0.2618F, -0.6109F, -0.4363F));

		PartDefinition great_pincer_part1 = great_pincer.addOrReplaceChild("great_pincer_part1", CubeListBuilder.create().texOffs(0, 14).addBox(-6.0F, -4.0F, -2.0F, 7.0F, 5.0F, 4.0F, new CubeDeformation(0.05F)), PartPose.offset(1.5F, 1.5F, -2.0F));

		PartDefinition great_pincer_part2 = great_pincer.addOrReplaceChild("great_pincer_part2", CubeListBuilder.create().texOffs(0, 23).addBox(-6.0F, -1.0F, -2.0F, 7.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 1.5F, -2.0F));

		PartDefinition pincer = crab.addOrReplaceChild("pincer", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5F, 0.0F, -4.5F, 0.7854F, 0.6109F, 0.6109F));

		PartDefinition pincer_part1 = pincer.addOrReplaceChild("pincer_part1", CubeListBuilder.create().texOffs(19, 20).addBox(-0.5F, -2.5F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.05F)), PartPose.offset(-0.5F, 1.0F, -1.5F));

		PartDefinition pincer_part2 = pincer.addOrReplaceChild("pincer_part2", CubeListBuilder.create().texOffs(22, 14).addBox(-0.5F, -2.5F, -1.5F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 1.0F, -1.5F));

		PartDefinition leg1 = crab.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 1).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.75F, 2.5F, -3.25F, 0.0F, -0.6109F, 0.0F));

		PartDefinition sub_leg1 = leg1.addOrReplaceChild("sub_leg1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition leg2 = crab.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 1).mirror().addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, 2.5F, -3.25F, 0.0F, 0.6109F, 0.0F));

		PartDefinition sub_leg2 = leg2.addOrReplaceChild("sub_leg2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition leg3 = crab.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 1).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.75F, 2.5F, 0.0F));

		PartDefinition sub_leg3 = leg3.addOrReplaceChild("sub_leg3", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition leg4 = crab.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 1).mirror().addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.5F, 2.5F, 0.0F));

		PartDefinition sub_leg4 = leg4.addOrReplaceChild("sub_leg4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		PartDefinition leg5 = crab.addOrReplaceChild("leg5", CubeListBuilder.create().texOffs(0, 1).addBox(-3.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.75F, 2.5F, 3.25F, 0.0F, 0.6109F, 0.0F));

		PartDefinition sub_leg5 = leg5.addOrReplaceChild("sub_leg5", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.6981F));

		PartDefinition leg6 = crab.addOrReplaceChild("leg6", CubeListBuilder.create().texOffs(0, 1).mirror().addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.5F, 2.5F, 3.0F, 0.0F, -0.6109F, 0.0F));

		PartDefinition sub_leg6 = leg6.addOrReplaceChild("sub_leg6", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(0.0F, 0.0F, -0.5F, 4.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.greetAnimationState, CrabAnimation.CRAB_GREETING, ageInTicks);
		this.animate(entity.idleAnimationState, CrabAnimation.CRAB_IDLE, ageInTicks);
		this.animate(entity.walkAnimationState, CrabAnimation.CRAB_WALK, ageInTicks);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}
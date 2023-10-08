package me.infamous.mob_vote_five.client.renderer.model;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import me.infamous.mob_vote_five.client.animation.ArmadilloAnimation;
import me.infamous.mob_vote_five.common.entity.Armadillo;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class ArmadilloModel<T extends Armadillo> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart ball;

	public ArmadilloModel(ModelPart root) {
		this.root = root;
		this.body = root.getChild("body");
		this.ball = root.getChild("ball");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition root = meshDefinition.getRoot();

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 15.0F, 2.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-5.5F, -6.25F, -16.5F, 11.0F, 4.0F, 12.0F, new CubeDeformation(0.1F))
		.texOffs(34, 5).addBox(-5.5F, -10.25F, -16.5F, 11.0F, 4.0F, 12.0F, new CubeDeformation(0.2F))
		.texOffs(0, 0).addBox(-5.5F, -15.0F, -16.5F, 11.0F, 5.0F, 12.0F, new CubeDeformation(0.3F))
		.texOffs(0, 49).addBox(-5.0F, -14.75F, -16.5F, 10.0F, 16.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 17).addBox(-5.5F, -2.25F, -16.5F, 11.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.5F, 6.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(34, 33).addBox(-1.5F, 0.0F, -7.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -9.25F, 0.7854F, 0.0F, 0.0F));

		PartDefinition ear1 = head.addOrReplaceChild("ear1", CubeListBuilder.create().texOffs(0, 23).mirror().addBox(-1.0F, -4.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.5F, -1.5F, -0.4363F, 0.0F, 0.6109F));

		PartDefinition ear2 = head.addOrReplaceChild("ear2", CubeListBuilder.create().texOffs(0, 23).addBox(-1.0F, -4.0F, -0.5F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.5F, -1.5F, -0.4363F, 0.0F, -0.6109F));

		PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 6.0F, 5.25F));

		PartDefinition leg2 = body.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 6.0F, 5.25F));

		PartDefinition leg3 = body.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 17).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 6.0F, -6.5F));

		PartDefinition leg4 = body.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 17).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(3.0F, 6.0F, -6.5F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 7.25F, 0.7854F, 0.0F, 0.0F));

		PartDefinition ball = root.addOrReplaceChild("ball", CubeListBuilder.create().texOffs(54, 33).addBox(-5.5F, 1.25F, -0.5F, 11.0F, 5.0F, 7.0F, new CubeDeformation(0.25F))
		.texOffs(42, 65).addBox(-5.5F, -6.75F, -0.5F, 11.0F, 13.0F, 7.0F, new CubeDeformation(0.05F))
		.texOffs(42, 45).addBox(-5.5F, -6.75F, -6.5F, 11.0F, 13.0F, 7.0F, new CubeDeformation(0.4F))
		.texOffs(54, 21).addBox(-5.5F, 1.25F, -6.5F, 11.0F, 5.0F, 7.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 17.5F, 0.5F));

		return LayerDefinition.create(meshDefinition, 128, 128);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.revealAnimationState, ArmadilloAnimation.ARMADILLO_APPEAR, ageInTicks);
		this.animate(entity.hideAnimationState, ArmadilloAnimation.ARMADILLO_HIDE, ageInTicks);
		this.animate(entity.hiddenAnimationState, ArmadilloAnimation.ARMADILLO_HIDE_IDLE, ageInTicks);
		this.animate(entity.idleAnimationState, ArmadilloAnimation.ARMADILLO_IDLE, ageInTicks);
		this.animate(entity.rollAnimationState, ArmadilloAnimation.ARMADILLO_ROLL, ageInTicks);
		this.animate(entity.walkAnimationState, ArmadilloAnimation.ARMADILLO_WALK, ageInTicks);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}
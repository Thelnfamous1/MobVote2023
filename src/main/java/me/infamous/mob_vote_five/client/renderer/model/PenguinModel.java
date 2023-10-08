package me.infamous.mob_vote_five.client.renderer.model;// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import me.infamous.mob_vote_five.client.animation.PenguinAnimation;
import me.infamous.mob_vote_five.common.entity.Penguin;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class PenguinModel<T extends Penguin> extends HierarchicalModel<T> {
	private final ModelPart root;
	private final ModelPart penguin;

	public PenguinModel(ModelPart root) {
		this.root = root;
		this.penguin = root.getChild("penguin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition penguin = partdefinition.addOrReplaceChild("penguin", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -5.933F, -3.384F, 10.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 1.0F));

		PartDefinition tail = penguin.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(27, 0).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.567F, 3.616F, -0.6109F, 0.0F, 0.0F));

		PartDefinition fin_right = penguin.addOrReplaceChild("fin_right", CubeListBuilder.create().texOffs(23, 26).addBox(0.0F, 0.0F, -2.5F, 1.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -4.933F, 0.116F));

		PartDefinition fin_left = penguin.addOrReplaceChild("fin_left", CubeListBuilder.create().texOffs(23, 26).mirror().addBox(-1.0F, 0.0F, -2.5F, 1.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -4.933F, 0.116F));

		PartDefinition feet_right = penguin.addOrReplaceChild("feet_right", CubeListBuilder.create().texOffs(22, 19).mirror().addBox(-1.25F, 0.0F, -4.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.25F, 6.067F, 1.616F));

		PartDefinition feet_left = penguin.addOrReplaceChild("feet_left", CubeListBuilder.create().texOffs(22, 19).addBox(-1.75F, 0.0F, -4.0F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.25F, 6.067F, 1.616F));

		PartDefinition head = penguin.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -3.0F, -6.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 3).addBox(-1.0F, -3.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.933F, 0.116F));

		PartDefinition eye_right = head.addOrReplaceChild("eye_right", CubeListBuilder.create().texOffs(2, 24).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -3.5F, -3.02F));

		PartDefinition pupil_right = eye_right.addOrReplaceChild("pupil_right", CubeListBuilder.create().texOffs(4, 23).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 0.0F, -0.01F));

		PartDefinition eye_left = head.addOrReplaceChild("eye_left", CubeListBuilder.create().texOffs(2, 24).addBox(-1.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -3.5F, -3.02F));

		PartDefinition pupil_left = eye_left.addOrReplaceChild("pupil_left", CubeListBuilder.create().texOffs(4, 23).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.0F, -0.01F));

		PartDefinition eyebrown_left = head.addOrReplaceChild("eyebrown_left", CubeListBuilder.create().texOffs(0, 31).mirror().addBox(-2.5F, -2.5F, 0.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -4.5F, -3.5F));

		PartDefinition eyebrown_right = head.addOrReplaceChild("eyebrown_right", CubeListBuilder.create().texOffs(0, 31).addBox(-1.5F, -2.5F, 0.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -4.5F, -3.5F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.fallAnimationState, PenguinAnimation.PENGUIN_FALLING, ageInTicks);
		this.animate(entity.idleAnimationState, PenguinAnimation.PENGUIN_IDLE, ageInTicks);
		this.animate(entity.leapAnimationState, PenguinAnimation.PENGUIN_JUMPING_INTO_THE_LAND, ageInTicks);
		this.animate(entity.swimAnimationState, PenguinAnimation.PENGUIN_SWIMMING, ageInTicks);
		this.animate(entity.walkAnimationState, PenguinAnimation.PENGUIN_WALK, ageInTicks);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}
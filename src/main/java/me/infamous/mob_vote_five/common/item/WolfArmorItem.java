package me.infamous.mob_vote_five.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import me.infamous.mob_vote_five.MobVote2023;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.UUID;

public class WolfArmorItem extends Item {
    private static final UUID ARMOR_MODIFIER_UUID = UUID.fromString("74250699-44e0-4ee3-9761-8bab5d41efd2");
    private final int protection;
    private final ResourceLocation texture;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public WolfArmorItem(int pProtection, String pIdentifier, Item.Properties pProperties) {
        this(pProtection, new ResourceLocation(MobVote2023.MODID, "textures/entity/armor/wolf_armor" + pIdentifier + ".png"), pProperties);
    }
    public WolfArmorItem(int pProtection, ResourceLocation pIdentifier, Item.Properties pProperties) {
        super(pProperties);
        this.protection = pProtection;
        this.texture = pIdentifier;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ARMOR, new AttributeModifier(ARMOR_MODIFIER_UUID, "Wolf armor bonus", this.protection, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pSlot) {
        return pSlot == EquipmentSlot.CHEST ? this.defaultModifiers :  super.getDefaultAttributeModifiers(pSlot);
    }

    public static InteractionResult armorInteract(Wolf pTarget, Player pPlayer, ItemStack pStack) {
        if (pStack.getItem() instanceof WolfArmorItem && pTarget.isAlive()) {
            if (pTarget.isOwnedBy(pPlayer)) {
                if (!pPlayer.level.isClientSide) {
                    ItemStack oldArmor = getArmor(pTarget);
                    if(!oldArmor.isEmpty()){
                        pTarget.spawnAtLocation(oldArmor);
                    }
                    setArmor(pTarget, pStack.copy());
                    pTarget.level.gameEvent(pTarget, GameEvent.EQUIP, pTarget.position());
                    pStack.shrink(1);
                }

                return InteractionResult.sidedSuccess(pPlayer.level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getProtection() {
        return this.protection;
    }

    public static ItemStack getArmor(Wolf wolf) {
        return wolf.getItemBySlot(EquipmentSlot.CHEST);
    }

    private static void setArmor(Wolf wolf, ItemStack pStack) {
        wolf.setItemSlot(EquipmentSlot.CHEST, pStack);
        wolf.setDropChance(EquipmentSlot.CHEST, 0.0F);
    }

}
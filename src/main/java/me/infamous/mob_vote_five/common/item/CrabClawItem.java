package me.infamous.mob_vote_five.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import me.infamous.mob_vote_five.common.duck.Grappler;
import me.infamous.mob_vote_five.common.entity.GrapplingHookEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CrabClawItem extends Item {
    private static final UUID REACH_MODIFIER_UUID = UUID.fromString("54dba017-f1eb-407a-ad60-07a37f5e590e");
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public CrabClawItem(Properties pProperties, float blockReachBonus) {
        super(pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(REACH_MODIFIER_UUID, "Claw modifier", blockReachBonus, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {

            int useTime = this.getUseDuration(pStack) - pTimeLeft;
            if (useTime < 0) return;

            float powerForTime = getPowerForTime(useTime);
            if (!((double)powerForTime < 0.1D)) {
                if (!pLevel.isClientSide) {
                    pStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(pEntityLiving.getUsedItemHand()));
                    GrapplingHookEntity grapplingHook = new GrapplingHookEntity(pLevel, player, pStack);
                    grapplingHook.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, powerForTime * 3.0F, 1.0F);
                    if (player.getAbilities().instabuild) {
                        grapplingHook.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    if(pLevel.addFreshEntity(grapplingHook)){
                        ((Grappler)player).setGHook(grapplingHook);
                    }

                    pLevel.playSound(null, grapplingHook, SoundEvents.PISTON_EXTEND, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (GrapplingHookEntity.ENABLE_PICKUP && !player.getAbilities().instabuild) {
                        player.getInventory().removeItem(pStack);
                    }
                }

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    public static float getPowerForTime(int pCharge) {
        float power = (float)pCharge / 20.0F;
        power = (power * power + power * 2.0F) / 3.0F;
        if (power > 1.0F) {
            power = 1.0F;
        }

        return power;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pHand);
        Entity grapplingHook = ((Grappler) pPlayer).getGrapplingHook();
        if (grapplingHook != null) {
            grapple(grapplingHook, pLevel, pPlayer);
        } else {
            pPlayer.startUsingItem(pHand);
        }
        return InteractionResultHolder.consume(itemInHand);
    }

    private static void grapple(Entity grapplingHook, Level pLevel, Player player){
        /*
        float yRot = player.getYRot();
        float xRot = player.getXRot();
        float xStep = -Mth.sin(yRot * Mth.DEG_TO_RAD) * Mth.cos(xRot * Mth.DEG_TO_RAD);
        float yStep = -Mth.sin(xRot * Mth.DEG_TO_RAD);
        float zStep = Mth.cos(yRot * Mth.DEG_TO_RAD) * Mth.cos(xRot * Mth.DEG_TO_RAD);
         */
        float xStep = (float) (grapplingHook.getX() - player.getX());
        float yStep = (float) (grapplingHook.getY() - player.getY());
        float zStep = (float) (grapplingHook.getZ() - player.getZ());
        float distance = Mth.sqrt(xStep * xStep + yStep * yStep + zStep * zStep);
        int speedLevel = 1;
        float speed = 3.0F * ((1.0F + (float) speedLevel) / 4.0F);
        xStep *= speed / distance;
        yStep *= speed / distance;
        zStep *= speed / distance;
        player.push(xStep, yStep, zStep);
        if (player.isOnGround()) {
            player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999F, 0.0D));
        }

        pLevel.playSound(null, player, SoundEvents.PISTON_CONTRACT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pSlot) {
        return pSlot == EquipmentSlot.OFFHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pSlot);
    }

    @Override
    public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.OFFHAND;
    }
}

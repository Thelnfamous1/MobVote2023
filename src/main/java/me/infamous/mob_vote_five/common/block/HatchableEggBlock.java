package me.infamous.mob_vote_five.common.block;

import me.infamous.mob_vote_five.common.entity.LaysEggs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class HatchableEggBlock<T extends AgeableMob & LaysEggs> extends TurtleEggBlock {
    private final Supplier<EntityType<T>> typeSupplier;
    private final TagKey<Block> hatchBlocks;
    private final TagKey<EntityType<?>> ignoresFalling;
    private final TagKey<EntityType<?>> ignores;

    public HatchableEggBlock(Properties pProperties, Supplier<EntityType<T>> typeSupplier, TagKey<Block> hatchBlocks, TagKey<EntityType<?>> ignoresFalling, TagKey<EntityType<?>> ignores) {
        super(pProperties);
        this.typeSupplier = typeSupplier;
        this.hatchBlocks = hatchBlocks;
        this.ignoresFalling = ignoresFalling;
        this.ignores = ignores;
    }

    protected boolean onHatchBlock(BlockGetter pLevel, BlockPos pPos) {
        return isHatchBlock(pLevel, pPos.below());
    }

    protected boolean isHatchBlock(BlockGetter pReader, BlockPos pPos) {
        return pReader.getBlockState(pPos).is(this.hatchBlocks);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!pEntity.isSteppingCarefully()) {
            this.destroyEgg(pLevel, pState, pPos, pEntity, 100);
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float pFallDistance) {
        if (!pEntity.getType().is(this.ignoresFalling)) {
            this.destroyEgg(pLevel, pState, pPos, pEntity, 3);
        }

        super.fallOn(pLevel, pState, pPos, pEntity, pFallDistance);
    }

    protected void destroyEgg(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, int pChance) {
        if (this.canDestroyEgg(pLevel, pEntity)) {
            if (!pLevel.isClientSide && pLevel.random.nextInt(pChance) == 0 && pState.is(this)) {
                this.decreaseEggs(pLevel, pPos, pState);
            }

        }
    }

    protected boolean canDestroyEgg(Level pLevel, Entity pEntity) {
        if (pEntity.getType() != this.typeSupplier.get() && !pEntity.getType().is(this.ignores)) {
            if (!(pEntity instanceof LivingEntity)) {
                return false;
            } else {
                return pEntity instanceof Player || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(pLevel, pEntity);
            }
        } else {
            return false;
        }
    }

    protected void decreaseEggs(Level pLevel, BlockPos pPos, BlockState pState) {
        pLevel.playSound(null, pPos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + pLevel.random.nextFloat() * 0.2F);
        int numEggs = pState.getValue(EGGS);
        if (numEggs <= 1) {
            pLevel.destroyBlock(pPos, false);
        } else {
            pLevel.setBlock(pPos, pState.setValue(EGGS, numEggs - 1), 2);
            pLevel.gameEvent(GameEvent.BLOCK_DESTROY, pPos, GameEvent.Context.of(pState));
            pLevel.levelEvent(2001, pPos, Block.getId(pState));
        }

    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (this.shouldUpdateHatchLevel(pLevel) && this.onHatchBlock(pLevel, pPos)) {
            int hatchStep = pState.getValue(HATCH);
            if (hatchStep < 2) {
                pLevel.playSound(null, pPos, SoundEvents.TURTLE_EGG_CRACK, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
                pLevel.setBlock(pPos, pState.setValue(HATCH, hatchStep + 1), 2);
            } else {
                pLevel.playSound(null, pPos, SoundEvents.TURTLE_EGG_HATCH, SoundSource.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
                pLevel.removeBlock(pPos, false);

                for(int egg = 0; egg < pState.getValue(EGGS); ++egg) {
                    pLevel.levelEvent(2001, pPos, Block.getId(pState));
                    T baby = this.typeSupplier.get().create(pLevel);
                    baby.onHatched(pPos);
                    baby.setAge(AgeableMob.BABY_START_AGE);

                    baby.moveTo((double)pPos.getX() + 0.3D + (double)egg * 0.2D, pPos.getY(), (double)pPos.getZ() + 0.3D, 0.0F, 0.0F);
                    pLevel.addFreshEntity(baby);
                }
            }
        }

    }

    protected boolean shouldUpdateHatchLevel(Level pLevel) {
        float timeOfDay = pLevel.getTimeOfDay(1.0F);
        if ((double)timeOfDay < 0.69D && (double)timeOfDay > 0.65D) {
            return true;
        } else {
            return pLevel.random.nextInt(500) == 0;
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (this.onHatchBlock(pLevel, pPos) && !pLevel.isClientSide) {
            pLevel.levelEvent(2005, pPos, 0);
        }

    }
}

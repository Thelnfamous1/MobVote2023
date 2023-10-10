package me.infamous.mob_vote_five.common.registry;

import me.infamous.mob_vote_five.MobVote2023;
import me.infamous.mob_vote_five.common.item.CrabClawItem;
import me.infamous.mob_vote_five.common.item.WolfArmorItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MVItems {

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MobVote2023.MODID);

    public static final RegistryObject<Item> CRAB_SPAWN_EGG = registerSpawnEgg("turtle_spawn_egg", MVEntityTypes.CRAB, 15198183, 44975);
    public static final RegistryObject<Item> ARMADILLO_SPAWN_EGG = registerSpawnEgg("armadillo_spawn_egg", MVEntityTypes.ARMADILLO, 15198183, 44975);
    public static final RegistryObject<Item> PENGUIN_SPAWN_EGG = registerSpawnEgg("penguin_spawn_egg", MVEntityTypes.PENGUIN, 15198183, 44975);

    public static final RegistryObject<Item> ARMADILLO_SCUTE = ITEMS.register("armadillo_scute", () -> new Item((new Item.Properties()).tab(CreativeModeTab.TAB_MATERIALS)));

    public static final RegistryObject<Item> CRAB_CLAW = ITEMS.register("crab_claw", () -> new CrabClawItem((new Item.Properties().stacksTo(1)).tab(CreativeModeTab.TAB_TOOLS), 2.0F));

    public static final RegistryObject<Item> WOLF_ARMOR = ITEMS.register("wolf_armor", () -> new WolfArmorItem(3, "", (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));


    private static <T extends Mob> RegistryObject<Item> registerSpawnEgg(String name, RegistryObject<EntityType<T>> typeSupplier, int backgroundColor, int highlightColor) {
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(typeSupplier, backgroundColor, highlightColor, (new Item.Properties()).tab(CreativeModeTab.TAB_MISC)));
    }

    public static void register(IEventBus modEventBus){
        ITEMS.register(modEventBus);
    }
}

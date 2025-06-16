package me.fearlessdude.createbinary.item;

import me.fearlessdude.createbinary.CreateBinary;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CreateBinary.MOD_ID);

//    public static final RegistryObject<Item> CIRCUIT_BOARD = ITEMS.register("circuit_board",
//            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

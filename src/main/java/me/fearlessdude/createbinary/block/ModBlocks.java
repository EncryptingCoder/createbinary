package me.fearlessdude.createbinary.block;

import me.fearlessdude.createbinary.CreateBinary;
import me.fearlessdude.createbinary.block.custom.MyPeripheralBlock;
import me.fearlessdude.createbinary.block.custom.ComputerizedRedstoneLinkBlock;
import me.fearlessdude.createbinary.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, CreateBinary.MOD_ID);

    public static final RegistryObject<Block> MY_PERIPHERAL = registerBlock("my_peripheral",
            () -> new MyPeripheralBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).noLootTable()));
    public static final RegistryObject<Block> COMPUTERIZED_REDSTONE_LINK = registerBlock("computerized_redstone_link",
            () -> new ComputerizedRedstoneLinkBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)
                    .noOcclusion()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

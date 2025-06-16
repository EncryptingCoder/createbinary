package me.fearlessdude.createbinary.block.entity;

import me.fearlessdude.createbinary.CreateBinary;
import me.fearlessdude.createbinary.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreateBinary.MOD_ID);

    public static final RegistryObject<BlockEntityType<MyPeripheralTileEntity>> MY_PERIPHERAL_BE =
            BLOCK_ENTITIES.register("my_peripheral_be", () ->
                    BlockEntityType.Builder.of(MyPeripheralTileEntity::new,
                            ModBlocks.MY_PERIPHERAL.get()).build(null));

    public static final RegistryObject<BlockEntityType<ComputerizedRedstoneLinkBlockEntity>> COMPUTERIZED_REDSTONE_LINK_BE =
            BLOCK_ENTITIES.register("computerized_redstone_link_be", () ->
                    BlockEntityType.Builder.of(ComputerizedRedstoneLinkBlockEntity::new,
                            ModBlocks.COMPUTERIZED_REDSTONE_LINK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
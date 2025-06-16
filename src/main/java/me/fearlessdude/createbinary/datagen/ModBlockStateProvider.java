package me.fearlessdude.createbinary.datagen;

import me.fearlessdude.createbinary.CreateBinary;
import me.fearlessdude.createbinary.block.ModBlocks;
import me.fearlessdude.createbinary.block.custom.ComputerizedRedstoneLinkBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CreateBinary.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.MY_PERIPHERAL);

        registerComputerizedRedstoneLink(ModBlocks.COMPUTERIZED_REDSTONE_LINK.get());
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void registerComputerizedRedstoneLink(Block block) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            boolean receiving = state.getValue(ComputerizedRedstoneLinkBlock.RECEIVING);
            boolean transmitting = state.getValue(ComputerizedRedstoneLinkBlock.TRANSMITTING);

            String modelName = "block/computerized_redstone_link/";
//            if (receiver) {
//                modelName += "receiver";
//            } else {
//                modelName += "transmitter";
//            }

            boolean vertical = facing.getAxis().isVertical();
            if (!vertical) {
                modelName += "vertical_";
            }

            if (transmitting) {
                modelName += "transmitting";
            }
            if (receiving) {
                if (transmitting) { modelName += "_"; }
                modelName += "receiving";
            }
            if (!(transmitting || receiving)) {
                modelName += "unpowered";
            }

            ModelFile model = new ModelFile.UncheckedModelFile(modLoc(modelName));

            int x = 0;
            int y = 0;

            switch (facing) {
                case DOWN -> x = 180;
                case UP -> y = 180;
                case NORTH -> {
                    x = 270;
                    y = 180;
                }
                case SOUTH -> x = 270;
                case WEST -> {
                    x = 270;
                    y = 90;
                }
                case EAST -> {
                    x = 270;
                    y = 270;
                }
            }

            simpleBlockItem(block, model);

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(x)
                    .rotationY(y)
                    .build();
        });
    }

}

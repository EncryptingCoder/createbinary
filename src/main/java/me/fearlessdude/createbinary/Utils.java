package me.fearlessdude.createbinary;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.ForgeRegistries;

public class Utils {
    public static Item getByName(ResourceLocation loc) {
        var itemRegistryObject = ForgeRegistries.ITEMS.getHolder(loc);
        if (itemRegistryObject.isEmpty())
            return Items.AIR;
        return itemRegistryObject.get().get();
    }

    public static VoxelShape rotate(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, Shapes.empty() };

        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }

    public static VoxelShape rotateShapeToFacing(Direction facing, VoxelShape shape) {
        if (facing == Direction.UP) return shape; // Already in UP orientation

        VoxelShape[] buffer = new VoxelShape[]{ shape, Shapes.empty() };

        buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double newMinX = minX, newMinY = minY, newMinZ = minZ;
            double newMaxX = maxX, newMaxY = maxY, newMaxZ = maxZ;

            switch (facing) {
                case DOWN: // Flip upside down (around X axis)
                    newMinY = 1 - maxY;
                    newMaxY = 1 - minY;
                    break;

                case NORTH: // Rotate around X axis: plate on south wall, facing north
                    newMinY = minZ;
                    newMinZ = 1 - maxY;
                    newMaxY = maxZ;
                    newMaxZ = 1 - minY;
                    break;

                case SOUTH: // Rotate around X axis the other way
                    newMinY = 1 - maxZ;
                    newMinZ = minY;
                    newMaxY = 1 - minZ;
                    newMaxZ = maxY;
                    break;

                case EAST: // Rotate around Z axis: plate on east wall, facing west
                    newMinY = minZ;
                    newMinZ = 1 - maxY;
                    newMaxY = maxZ;
                    newMaxZ = 1 - minY;
                    break;

                case WEST: // Rotate around Z axis the other way
                    newMinY = minZ;
                    newMinZ = 1 - maxY;
                    newMaxY = maxZ;
                    newMaxZ = 1 - minY;
                    break;

                default:
                    break;
            }

            buffer[1] = Shapes.or(buffer[1], Shapes.create(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ));
        });

        if ((facing == Direction.WEST) | (facing == Direction.EAST)) {
            buffer[1] = rotate(Direction.NORTH, facing, buffer[1]);
        }

        return buffer[1];
    }
}

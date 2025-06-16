package me.fearlessdude.createbinary.block.entity;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.fearlessdude.createbinary.peripheral.MyPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MyPeripheralTileEntity extends BlockEntity {
    private final LazyOptional<IPeripheral> peripheral = LazyOptional.of(MyPeripheral::new);

    public MyPeripheralTileEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MY_PERIPHERAL_BE.get(), pos, state);
    }

//    @Nonnull
//    @Override
//    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
////        if (cap == ComputerCraftCapabilities.Peripheral.CAPABILITY) {
////            return peripheral.cast();
////        }
//        return super.getCapability(cap, side);
//    }
}

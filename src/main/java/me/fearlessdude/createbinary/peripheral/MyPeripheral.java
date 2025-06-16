package me.fearlessdude.createbinary.peripheral;

import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.fearlessdude.createbinary.block.entity.MyPeripheralTileEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class MyPeripheral implements IPeripheral {
    private final LazyOptional<IPeripheral> self;

    public MyPeripheral() {
        this.self = LazyOptional.of(() -> this);
    }

    public MyPeripheral(MyPeripheralTileEntity myPeripheralTileEntity) {
        this.self = LazyOptional.of(() -> this);
    }

    @Override
    public String getType() {
        return "my_peripheral";
    }

    @LuaFunction
    public final boolean sayHello() {
        return true;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other == this;
    }
}

package me.fearlessdude.createbinary.peripheral;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import me.fearlessdude.createbinary.CreateBinary;
import me.fearlessdude.createbinary.block.entity.ComputerizedRedstoneLinkBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.fearlessdude.createbinary.Utils.getByName;

public class ComputerizedRedstoneLinkPeripheral implements IPeripheral {
    public ComputerizedRedstoneLinkBlockEntity parent;

    public ComputerizedRedstoneLinkPeripheral(ComputerizedRedstoneLinkBlockEntity be) {
        this.parent = be;
    }

    @Override
    public String getType() {
        return new ResourceLocation(CreateBinary.MOD_ID, "computerized_redstone_link").toString();
    }

    @LuaFunction
    public final String[] getHandles() {
        List<String> handles = new ArrayList<>();
        for (UUID uuid : parent.pairs.keySet()) {
            handles.add(uuid.toString());
        }

        return handles.toArray(new String[0]);
    }

    @LuaFunction
    public final String openHandle(IArguments arguments) throws LuaException {
        String _item1 = arguments.getString(0);
        String _item2 = arguments.getString(1);
        Item item1 = getByName(new ResourceLocation(_item1));
        Item item2 = getByName(new ResourceLocation(_item2));
        if (item1 == null)
            throw new LuaException(_item1 + " is not a valid item id!");
        if (item2 == null)
            throw new LuaException(_item2 + " is not a valid item id!");

        ComputerizedRedstoneLinkBlockEntity.LinkPair pair =
                new ComputerizedRedstoneLinkBlockEntity.LinkPair(parent, new Item[]{item1, item2});

        pair.setFrequency(pair.items);

        return parent.add(pair).toString();
    }

    @LuaFunction(mainThread = true)
    public final void setSignal(IArguments arguments) throws LuaException {
        String uuid = arguments.getString(0);
        int value = arguments.getInt(1);
        System.out.println("sx: " + value);
        System.out.println(parent.pairs.toString());
        parent.pairs.get(UUID.fromString(uuid)).sendSignal = value;
    }


    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return false;
    }
}

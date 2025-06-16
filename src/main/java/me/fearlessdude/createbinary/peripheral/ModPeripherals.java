package me.fearlessdude.createbinary.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import me.fearlessdude.createbinary.CreateBinary;
import me.fearlessdude.createbinary.block.custom.MyPeripheralBlock;
import me.fearlessdude.createbinary.block.entity.ComputerizedRedstoneLinkBlockEntity;
import me.fearlessdude.createbinary.block.entity.MyPeripheralTileEntity;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;


@Mod.EventBusSubscriber(modid = CreateBinary.MOD_ID)
public class ModPeripherals {
    @SubscribeEvent
    public static void attachPeripherals(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof MyPeripheralTileEntity myPerf) {
            PeripheralProvider.attach(event, myPerf, MyPeripheral::new);
        }
        if (event.getObject() instanceof ComputerizedRedstoneLinkBlockEntity be) {
            PeripheralProvider.attach(event, be, ComputerizedRedstoneLinkPeripheral::new);
        }
    }

    public static final Capability<IPeripheral> CAPABILITY_PERIPHERAL = CapabilityManager.get(new CapabilityToken<>() {});
    private static final ResourceLocation PERIPHERAL = new ResourceLocation(CreateBinary.MOD_ID, "peripheral");

    // A {@link ICapabilityProvider} that lazily creates an {@link IPeripheral} when required.
    private static final class PeripheralProvider<O extends BlockEntity> implements ICapabilityProvider {
        private final O blockEntity;
        private final Function<O, IPeripheral> factory;
        private @Nullable LazyOptional<IPeripheral> peripheral;

        private PeripheralProvider(O blockEntity, Function<O, IPeripheral> factory) {
            this.blockEntity = blockEntity;
            this.factory = factory;
        }

        private static <O extends BlockEntity> void attach(AttachCapabilitiesEvent<BlockEntity> event, O blockEntity, Function<O, IPeripheral> factory) {
            var provider = new PeripheralProvider<>(blockEntity, factory);
            event.addCapability(PERIPHERAL, provider);
            event.addListener(provider::invalidate);
        }

        private void invalidate() {
            if (peripheral != null) peripheral.invalidate();
            peripheral = null;
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
            if (capability != CAPABILITY_PERIPHERAL) return LazyOptional.empty();
            if (blockEntity.isRemoved()) return LazyOptional.empty();

            var peripheral = this.peripheral;
            return (peripheral == null ? (this.peripheral = LazyOptional.of(() -> factory.apply(blockEntity))) : peripheral).cast();
        }
    }
}

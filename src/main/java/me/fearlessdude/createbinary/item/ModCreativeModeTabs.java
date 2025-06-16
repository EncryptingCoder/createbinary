package me.fearlessdude.createbinary.item;

import me.fearlessdude.createbinary.CreateBinary;
import me.fearlessdude.createbinary.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateBinary.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MOD_TAB = CREATIVE_MODE_TABS.register("mod_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.DRAGON_EGG))
                    .title(Component.translatable("creativetab.mod_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
//                        output.accept(ModItems.CIRCUIT_BOARD.get());

//                        output.accept(ModBlocks.TANK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

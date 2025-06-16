package me.fearlessdude.createbinary.datagen;

import me.fearlessdude.createbinary.CreateBinary;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CreateBinary.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new me.fearlessdude.createbinary.datagen.ModRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), me.fearlessdude.createbinary.datagen.ModLootTableProvider.create(packOutput));

        generator.addProvider(event.includeClient(), new me.fearlessdude.createbinary.datagen.ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new me.fearlessdude.createbinary.datagen.ModItemModelProvider(packOutput, existingFileHelper));

        me.fearlessdude.createbinary.datagen.ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new me.fearlessdude.createbinary.datagen.ModBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new me.fearlessdude.createbinary.datagen.ModItemTagGenerator(packOutput, lookupProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
    }
}

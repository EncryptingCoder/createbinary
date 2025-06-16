package me.fearlessdude.createbinary.datagen.loot;

import me.fearlessdude.createbinary.block.ModBlocks;
import me.fearlessdude.createbinary.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
//        this.dropSelf(ModBlocks.SAPPHIRE_BLOCK.get());
        this.dropSelf(ModBlocks.COMPUTERIZED_REDSTONE_LINK.get());

//        this.add(ModBlocks.SAPPHIRE_ORE.get(),
//                block -> createCopperLikeOreDrops(ModBlocks.SAPPHIRE_ORE.get(), ModItems.RAW_SAPPHIRE.get()));

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}

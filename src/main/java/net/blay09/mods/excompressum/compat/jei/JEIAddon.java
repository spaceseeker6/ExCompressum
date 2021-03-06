package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.ExCompressumReloadEvent;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerRegistryEntry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.blay09.mods.excompressum.api.woodencrucible.WoodenCrucibleRegistryEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.List;

@JEIPlugin
public class JEIAddon implements IModPlugin {

	public static IJeiHelpers jeiHelpers;

	@Override
	public void register(IModRegistry registry) {
		jeiHelpers = registry.getJeiHelpers();

		List<HeavySieveRecipe> heavySieveRecipes = Lists.newArrayList();
		if(ExRegistro.doMeshesSplitLootTables()) {
			Collection<HeavySieveRegistryEntry> entries = HeavySieveRegistry.INSTANCE.getEntries().values();
			for (SieveMeshRegistryEntry sieveMesh : SieveMeshRegistry.getEntries().values()) {
				for(HeavySieveRegistryEntry entry : entries) {
					if(!entry.getRewardsForMesh(sieveMesh).isEmpty()) {
						heavySieveRecipes.add(new HeavySieveRecipe(entry, sieveMesh));
					}
				}
			}
		} else {
			for(HeavySieveRegistryEntry entry : HeavySieveRegistry.INSTANCE.getEntries().values()) {
				heavySieveRecipes.add(new HeavySieveRecipe(entry));
			}
		}
		registry.addRecipes(heavySieveRecipes, HeavySieveRecipeCategory.UID);

		List<CompressedHammerRecipe> compressedHammerRecipes = Lists.newArrayList();
		for(CompressedHammerRegistryEntry entry : CompressedHammerRegistry.INSTANCE.getEntries().values()) {
			compressedHammerRecipes.add(new CompressedHammerRecipe(entry));
		}
		registry.addRecipes(compressedHammerRecipes, CompressedHammerRecipeCategory.UID);

		List<WoodenCrucibleRecipe> woodenCrucibleRecipes = Lists.newArrayList();
		ArrayListMultimap<String, WoodenCrucibleRegistryEntry> fluidOutputMap = ArrayListMultimap.create();
		for(WoodenCrucibleRegistryEntry entry : WoodenCrucibleRegistry.INSTANCE.getEntries().values()) {
			fluidOutputMap.put(entry.getFluid().getName(), entry);
		}
		for(String fluidName : fluidOutputMap.keySet()) {
			woodenCrucibleRecipes.add(new WoodenCrucibleRecipe(FluidRegistry.getFluid(fluidName), fluidOutputMap.get(fluidName)));
		}
		registry.addRecipes(woodenCrucibleRecipes, WoodenCrucibleRecipeCategory.UID);

		registry.addRecipes(Lists.newArrayList(new ChickenStickRecipe()), ChickenStickRecipeCategory.UID);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.heavySieve), HeavySieveRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.woodenCrucible), WoodenCrucibleRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerDiamond), CompressedHammerRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerGold), CompressedHammerRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerIron), CompressedHammerRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerStone), CompressedHammerRecipeCategory.UID);
		registry.addRecipeCatalyst(new ItemStack(ModItems.compressedHammerWood), CompressedHammerRecipeCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(
				new HeavySieveRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new CompressedHammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new WoodenCrucibleRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ChickenStickRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
	}

}

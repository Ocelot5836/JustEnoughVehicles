package com.ocelot.vehicle.jei.plugin;

import com.mrcrayfish.vehicle.client.gui.GuiFluidExtractor;
import com.mrcrayfish.vehicle.client.gui.GuiFluidMixer;
import com.mrcrayfish.vehicle.common.container.ContainerFluidExtractor;
import com.mrcrayfish.vehicle.common.container.ContainerFluidMixer;
import com.mrcrayfish.vehicle.init.ModBlocks;
import com.mrcrayfish.vehicle.init.ModItems;
import com.mrcrayfish.vehicle.item.ItemPart;
import com.ocelot.vehicle.jei.VehicleModJei;
import com.ocelot.vehicle.jei.plugin.fluidextractor.FluidExtractorCategory;
import com.ocelot.vehicle.jei.plugin.fluidextractor.FluidExtractorRecipeMaker;
import com.ocelot.vehicle.jei.plugin.fluidmixer.FluidMixerCategory;
import com.ocelot.vehicle.jei.plugin.fluidmixer.FluidMixerRecipeMaker;
import com.ocelot.vehicle.jei.plugin.workstation.VehicleCategory;
import com.ocelot.vehicle.jei.plugin.workstation.VehicleRecipeMaker;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@JEIPlugin
public class VehicleModJeiPlugin implements IModPlugin {

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
		subtypeRegistry.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.VEHICLE_CRATE), stack -> {
			if (stack.getSubCompound("BlockEntityTag") != null) {
				NBTTagCompound blockEntityTag = stack.getSubCompound("BlockEntityTag");
				return blockEntityTag != null ? blockEntityTag.getString("vehicle") : ISubtypeRegistry.ISubtypeInterpreter.NONE;
			}
			return ISubtypeRegistry.ISubtypeInterpreter.NONE;
		});
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		registry.addRecipeCategories(
				new FluidExtractorCategory(guiHelper),
				new FluidMixerCategory(guiHelper),
				new VehicleCategory(guiHelper)
		);
	}

	@Override
	public void register(IModRegistry registry) {
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();

		registry.addRecipes(FluidExtractorRecipeMaker.getRecipes(jeiHelpers), VehicleModJei.FLUID_EXTRACTOR_UID);
		registry.addRecipes(FluidMixerRecipeMaker.getRecipes(jeiHelpers), VehicleModJei.FLUID_MIXER_UID);
		registry.addRecipes(VehicleRecipeMaker.getRecipes(jeiHelpers), VehicleModJei.VEHICLE_UID);

		registry.addRecipeClickArea(GuiFluidExtractor.class, 93, 35, 24, 17, VehicleModJei.FLUID_EXTRACTOR_UID, VanillaRecipeCategoryUid.FUEL);
		registry.addRecipeClickArea(GuiFluidMixer.class, 51, 26, 47, 46, VehicleModJei.FLUID_MIXER_UID, VanillaRecipeCategoryUid.FUEL);

		registry.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_EXTRACTOR), VehicleModJei.FLUID_EXTRACTOR_UID, VanillaRecipeCategoryUid.FUEL);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_MIXER), VehicleModJei.FLUID_MIXER_UID, VanillaRecipeCategoryUid.FUEL);
		registry.addRecipeCatalyst(new ItemStack(ModBlocks.WORKSTATION), VehicleModJei.VEHICLE_UID);

		IRecipeTransferRegistry recipeTransferRegistry = registry.getRecipeTransferRegistry();
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFluidExtractor.class, VehicleModJei.FLUID_EXTRACTOR_UID, 1, 1, 2, 36);
		recipeTransferRegistry.addRecipeTransferHandler(ContainerFluidMixer.class, VehicleModJei.FLUID_MIXER_UID, 1, 1, 2, 36);

		IIngredientBlacklist ingredientBlacklist = jeiHelpers.getIngredientBlacklist();
		// Blacklist the models for vehicles
		ingredientBlacklist.addIngredientToBlacklist(new ItemStack(ModItems.MODELS));
	}
}

package com.ocelot.vehicle.jei.plugin;

import com.mrcrayfish.vehicle.client.screen.FluidExtractorScreen;
import com.mrcrayfish.vehicle.client.screen.FluidMixerScreen;
import com.mrcrayfish.vehicle.init.ModBlocks;
import com.mrcrayfish.vehicle.inventory.container.FluidExtractorContainer;
import com.mrcrayfish.vehicle.inventory.container.FluidMixerContainer;
import com.ocelot.vehicle.jei.VehicleModJei;
import com.ocelot.vehicle.jei.plugin.fluidextractor.FluidExtractorCategory;
import com.ocelot.vehicle.jei.plugin.fluidmixer.FluidMixerCategory;
import com.ocelot.vehicle.jei.plugin.workstation.VehicleCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

/**
 * @author Ocelot
 */
@JeiPlugin
public class VehicleModJeiPlugin implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(VehicleModJei.MOD_ID, "vehicle");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry)
    {
        subtypeRegistry.registerSubtypeInterpreter(ModBlocks.VEHICLE_CRATE.get().asItem(), stack ->
        {
            if (stack.getChildTag("BlockEntityTag") != null)
            {
                CompoundNBT blockEntityTag = stack.getChildTag("BlockEntityTag");
                return blockEntityTag != null ? blockEntityTag.getString("Vehicle") : ISubtypeInterpreter.NONE;
            }
            return ISubtypeInterpreter.NONE;
        });
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new FluidExtractorCategory(guiHelper),
                new FluidMixerCategory(guiHelper),
                new VehicleCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        VehicleRecipeValidator.Results recipes = VehicleRecipeValidator.getValidRecipes();
        registration.addRecipes(recipes.getFluidExtractorRecipes(), VehicleModJei.FLUID_EXTRACTOR_UID);
        registration.addRecipes(recipes.getFluidMixerRecipes(), VehicleModJei.FLUID_MIXER_UID);
        registration.addRecipes(recipes.getVehicleRecipes(), VehicleModJei.VEHICLE_UID);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration)
    {
        registration.addRecipeClickArea(FluidExtractorScreen.class, 93, 35, 24, 17, VehicleModJei.FLUID_EXTRACTOR_UID, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeClickArea(FluidMixerScreen.class, 51, 26, 47, 46, VehicleModJei.FLUID_MIXER_UID, VanillaRecipeCategoryUid.FUEL);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_EXTRACTOR.get()), VehicleModJei.FLUID_EXTRACTOR_UID, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FLUID_MIXER.get()), VehicleModJei.FLUID_MIXER_UID, VanillaRecipeCategoryUid.FUEL);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WORKSTATION.get()), VehicleModJei.VEHICLE_UID);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration)
    {
        registration.addRecipeTransferHandler(FluidExtractorContainer.class, VehicleModJei.FLUID_EXTRACTOR_UID, 1, 1, 2, 36);
        registration.addRecipeTransferHandler(FluidMixerContainer.class, VehicleModJei.FLUID_MIXER_UID, 1, 1, 2, 36);
    }
}

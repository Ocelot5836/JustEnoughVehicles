package com.ocelot.vehicle.jei.plugin.fluidextractor;

import com.mrcrayfish.vehicle.crafting.FluidExtract;
import com.mrcrayfish.vehicle.crafting.FluidExtractorRecipes;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ocelot
 */
public final class FluidExtractorRecipeMaker
{
    private FluidExtractorRecipeMaker()
    {
    }

    public static List<FluidExtractorRecipeWrapper> getRecipes(IJeiHelpers jeiHelpers)
    {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        FluidExtractorRecipes fluidExtractorRecipes = FluidExtractorRecipes.getInstance();
        Map<ItemStack, FluidExtract> extractingMap = fluidExtractorRecipes.getExtractingMap();

        List<FluidExtractorRecipeWrapper> recipes = new ArrayList<>();
        extractingMap.forEach((input, output) ->
        {
            List<ItemStack> itemInputs = stackHelper.getSubtypes(input.copy());
            recipes.add(new FluidExtractorRecipeWrapper(jeiHelpers.getGuiHelper(), itemInputs, output.createStack()));
        });
        return recipes;
    }
}

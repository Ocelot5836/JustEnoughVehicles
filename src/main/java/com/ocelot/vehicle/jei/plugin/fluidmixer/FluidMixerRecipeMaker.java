package com.ocelot.vehicle.jei.plugin.fluidmixer;

import com.mrcrayfish.vehicle.crafting.FluidExtract;
import com.mrcrayfish.vehicle.crafting.FluidMixerRecipe;
import com.mrcrayfish.vehicle.crafting.FluidMixerRecipes;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ocelot
 */
public final class FluidMixerRecipeMaker
{
    private FluidMixerRecipeMaker()
    {
    }

    public static List<FluidMixerRecipeWrapper> getRecipes(IJeiHelpers jeiHelpers)
    {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        FluidMixerRecipes fluidMixerRecipes = FluidMixerRecipes.getInstance();
        Map<FluidMixerRecipe, FluidExtract> mixingMap = fluidMixerRecipes.getMixingMap();

        List<FluidMixerRecipeWrapper> recipes = new ArrayList<>();
        mixingMap.forEach((input, output) ->
        {
            List<ItemStack> itemInputs = stackHelper.getSubtypes(input.getIngredient());
            Fluid fluidInput1 = input.getFluids()[0];
            Fluid fluidInput2 = input.getFluids()[1];
            recipes.add(new FluidMixerRecipeWrapper(jeiHelpers.getGuiHelper(), itemInputs, new FluidStack(fluidInput1, input.getFluidAmount(fluidInput1)), new FluidStack(fluidInput2, input.getFluidAmount(fluidInput2)), output.createStack()));
        });
        return recipes;
    }
}

package com.ocelot.vehicle.jei.plugin.fluidmixer

import com.mrcrayfish.vehicle.crafting.FluidMixerRecipes
import mezz.jei.api.IJeiHelpers
import net.minecraftforge.fluids.FluidStack
import java.util.*

object FluidMixerRecipeMaker {

    fun getRecipes(helpers: IJeiHelpers): List<FluidMixerRecipeWrapper> {
        val stackHelper = helpers.stackHelper
        val fluidMixerRecipes = FluidMixerRecipes.getInstance()
        val mixingMap = fluidMixerRecipes.mixingMap

        val recipes = ArrayList<FluidMixerRecipeWrapper>()

        for (entry in mixingMap.entries) {
            val mixingRecipe = entry.key
            val mixingResult = entry.value
            val itemInputs = stackHelper.getSubtypes(mixingRecipe.ingredient)
            val recipe = FluidMixerRecipeWrapper(itemInputs, FluidStack(mixingRecipe.fluids[0], mixingRecipe.amounts[0]), FluidStack(mixingRecipe.fluids[1], mixingRecipe.amounts[1]), mixingResult.createStack())
            recipes.add(recipe)
        }

        return recipes
    }
}
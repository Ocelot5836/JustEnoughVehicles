package com.ocelot.vehicle.jei.plugin.fluidextractor

import com.mrcrayfish.vehicle.crafting.FluidExtractorRecipes
import mezz.jei.api.IJeiHelpers
import java.util.*

object FluidExtractorRecipeMaker {

    fun getRecipes(helpers: IJeiHelpers): List<FluidExtractorRecipeWrapper> {
        val stackHelper = helpers.stackHelper
        val fluidExtractorRecipes = FluidExtractorRecipes.getInstance()
        val extractingMap = fluidExtractorRecipes.extractingMap

        val recipes = ArrayList<FluidExtractorRecipeWrapper>()

        for (entry in extractingMap.entries) {
            val mixingInput = entry.key
            val mixingResult = entry.value
            val itemInputs = stackHelper.getSubtypes(mixingInput)
            val recipe = FluidExtractorRecipeWrapper(helpers.guiHelper, itemInputs, mixingResult.createStack())
            recipes.add(recipe)
        }

        return recipes
    }
}
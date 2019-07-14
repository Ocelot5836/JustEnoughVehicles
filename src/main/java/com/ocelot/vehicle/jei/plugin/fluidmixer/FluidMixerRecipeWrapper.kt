package com.ocelot.vehicle.jei.plugin.fluidmixer

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.FluidStack

class FluidMixerRecipeWrapper(private val itemInput: List<ItemStack>, private val fluidInput1: FluidStack, private val fluidInput2: FluidStack, private val fluidOutput: FluidStack) : IRecipeWrapper {

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, listOf(itemInput))
        ingredients.setInputs(VanillaTypes.FLUID, listOf(fluidInput1, fluidInput2))
        ingredients.setOutput(VanillaTypes.FLUID, fluidOutput)
    }
}
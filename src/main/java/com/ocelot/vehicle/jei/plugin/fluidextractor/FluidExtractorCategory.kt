package com.ocelot.vehicle.jei.plugin.fluidextractor

import com.mrcrayfish.vehicle.init.ModBlocks
import com.mrcrayfish.vehicle.tileentity.TileEntityFluidExtractor
import com.ocelot.vehicle.jei.VehicleModJei
import com.ocelot.vehicle.jei.plugin.fluidmixer.FluidMixerCategory
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IDrawableAnimated
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack

class FluidExtractorCategory(guiHelper: IGuiHelper) : IRecipeCategory<FluidExtractorRecipeWrapper> {

    private val background = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 0, 66, 112, 61)
    private val icon = guiHelper.createDrawableIngredient(ItemStack(ModBlocks.FLUID_EXTRACTOR))
    private val staticFlame = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 0, 14, 14)
    private val animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true)
    private val outputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 59)
    private val localizedName = I18n.format(UNLOCALIZED_TITLE)

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun drawExtras(minecraft: Minecraft) = animatedFlame.draw(minecraft, 32, 40)

    override fun getTitle(): String = localizedName

    override fun getModName(): String = VehicleModJei.VEHICLE_NAME

    override fun getUid(): String = VehicleModJei.FLUID_EXTRACTOR_UID

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: FluidExtractorRecipeWrapper, ingredients: IIngredients) {
        val stacks = recipeLayout.itemStacks
        val fluids = recipeLayout.fluidStacks

        stacks.init(inputSlot, true, 31, 19)

        fluids.init(outputTank, false, 95, 1, 16, 59, TileEntityFluidExtractor.TANK_CAPACITY, false, outputOverlay)

        stacks.set(ingredients)
        fluids.set(ingredients)
    }

    companion object {
        const val UNLOCALIZED_TITLE = "cvmjei.category.fluid_extracting.title"

        const val inputSlot = 0
        const val outputTank = 0
    }
}
package com.ocelot.vehicle.jei.plugin.fluidmixer

import com.mrcrayfish.vehicle.init.ModBlocks
import com.ocelot.vehicle.jei.VehicleModJei
import com.ocelot.vehicle.jei.plugin.workstation.VehicleCategory
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IDrawableAnimated
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack

class FluidMixerCategory(guiHelper: IGuiHelper) : IRecipeCategory<FluidMixerRecipeWrapper> {

    private val background = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 0, 0, 160, 66)
    private val icon = guiHelper.createDrawableIngredient(ItemStack(ModBlocks.FLUID_MIXER))
    private val staticFlame = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 0, 14, 14)
    private val animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true)
    private val inputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 29)
    private val outputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 59)
    private val localizedName = I18n.format(VehicleModJei.FLUID_MIXER_UNLOCALIZED_TITLE)

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun drawExtras(minecraft: Minecraft) = animatedFlame.draw(minecraft, 1, 15)

    override fun getTitle(): String = localizedName

    override fun getModName(): String = VehicleModJei.VEHICLE_NAME

    override fun getUid(): String = VehicleModJei.FLUID_MIXER_UID

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: FluidMixerRecipeWrapper, ingredients: IIngredients) {
        val stacks = recipeLayout.itemStacks
        val fluids = recipeLayout.fluidStacks

        stacks.init(inputSlot, true, 94, 24)

        fluids.init(fluidInput1, true, 25, 1, 16, 29, 5000, false, inputOverlay)
        fluids.init(fluidInput2, true, 25, 36, 16, 29, 5000, false, inputOverlay)
        fluids.init(fluidOutput, false, 143, 4, 16, 59, 10000, false, outputOverlay)

        stacks.set(ingredients)
        fluids.set(ingredients)
    }

    companion object {
        const val inputSlot = 0
        const val fluidInput1 = 0
        const val fluidInput2 = 1
        const val fluidOutput = 2
    }
}
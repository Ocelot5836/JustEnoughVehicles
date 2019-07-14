package com.ocelot.vehicle.jei.plugin.fluidmixer

import com.mrcrayfish.vehicle.init.ModBlocks
import com.mrcrayfish.vehicle.tileentity.TileEntityFluidMixer
import com.ocelot.vehicle.jei.VehicleModJei
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IDrawableAnimated
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack

class FluidMixerCategory(private val guiHelper: IGuiHelper) : IRecipeCategory<FluidMixerRecipeWrapper> {

    private val background = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 0, 0, 160, 66)
    private val icon = guiHelper.createDrawableIngredient(ItemStack(ModBlocks.FLUID_MIXER))
    private val staticFlame = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 0, 14, 14)
    private val animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true)
    private val inputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 29)
    private val outputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 59)

    private var extractionProgress = 0
    private var startTime = System.currentTimeMillis()

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun drawExtras(minecraft: Minecraft) {
        animatedFlame.draw(minecraft, 1, 15)

        val msPassed = ((System.currentTimeMillis() - startTime) % TileEntityFluidMixer.FLUID_MAX_PROGRESS * MS_PER_TICK)
        val value = msPassed.toDouble() / (TileEntityFluidMixer.FLUID_MAX_PROGRESS * MS_PER_TICK).toDouble()

        // TODO add creation animation

        if (extractionProgress > TileEntityFluidMixer.FLUID_MAX_PROGRESS) {
            extractionProgress = 0
        }
    }

    override fun getTitle(): String = ModBlocks.FLUID_MIXER.localizedName

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

        const val PROGRESS_SPEED = 1
        const val MS_PER_TICK = 1000 / 20 * PROGRESS_SPEED
    }
}
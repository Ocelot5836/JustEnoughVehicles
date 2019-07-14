package com.ocelot.vehicle.jei.plugin.fluidextractor

import com.mrcrayfish.vehicle.tileentity.TileEntityFluidExtractor
import com.mrcrayfish.vehicle.util.FluidUtils
import com.mrcrayfish.vehicle.util.RenderUtil
import com.ocelot.vehicle.jei.VehicleModJei
import com.ocelot.vehicle.jei.plugin.util.ProgressTimer
import mezz.jei.api.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack

class FluidExtractorRecipeWrapper(guiHelper: IGuiHelper, private val itemInput: List<ItemStack>, private val fluidOutput: FluidStack) : IRecipeWrapper {

    private val progressOverlayStatic = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 0, 24, 17)
    private val progressOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 200, 0, 24, 17)
    private val progress = ProgressTimer(TileEntityFluidExtractor.FLUID_MAX_PROGRESS)

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, listOf(itemInput))
        ingredients.setOutput(VanillaTypes.FLUID, fluidOutput)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        GlStateManager.disableDepth()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)

        RenderUtil.drawGradientRectHorizontal(61, 22, 60 + progressOverlay.width, 21 + progressOverlay.height, -1, (0xff shl 24) or FluidUtils.getAverageFluidColor(fluidOutput.fluid), 0.0)
        progressOverlayStatic.draw(minecraft, 61, 22)
        progressOverlay.draw(minecraft, 61, 22, 0, 0, (22 * progress.percentage + 1).toInt(), 0)

        GlStateManager.disableBlend()
        GlStateManager.enableDepth()
    }
}
package com.ocelot.vehicle.jei.plugin.fluidmixer

import com.mrcrayfish.vehicle.tileentity.TileEntityFluidMixer
import com.mrcrayfish.vehicle.util.FluidUtils
import com.mrcrayfish.vehicle.util.RenderUtil
import com.ocelot.vehicle.jei.VehicleModJei
import com.ocelot.vehicle.jei.plugin.util.ProgressTimer
import mezz.jei.api.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraft.util.math.MathHelper
import net.minecraftforge.fluids.FluidStack


class FluidMixerRecipeWrapper(guiHelper: IGuiHelper, private val itemInput: List<ItemStack>, private val fluidInput1: FluidStack, private val fluidInput2: FluidStack, private val fluidOutput: FluidStack) : IRecipeWrapper {

    private val progressBackgroundMask = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 17, 76, 26)
    private val progressMask = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 65, 20, 76, 26)

    private val progress = ProgressTimer(TileEntityFluidMixer.FLUID_MAX_PROGRESS)
    private val fluidInput1Color = FluidUtils.getAverageFluidColor(fluidInput1.fluid)
    private val fluidInput2Color = FluidUtils.getAverageFluidColor(fluidInput2.fluid)
    private val fluidOutputColor = FluidUtils.getAverageFluidColor(fluidOutput.fluid)
    private val startColor = averageColor(fluidInput1Color, fluidInput2Color)

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, listOf(itemInput))
        ingredients.setInputs(VanillaTypes.FLUID, listOf(fluidInput1, fluidInput2))
        ingredients.setOutput(VanillaTypes.FLUID, fluidOutput)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        var percentagePrevious = 0.0
        val percentageBarHorizontal = MathHelper.clamp((progress.percentage - percentagePrevious) / (BAR_HORIZONTAL.toDouble() / SUM_LENGTH.toDouble()), 0.0, 1.0)
        percentagePrevious += BAR_HORIZONTAL.toDouble() / SUM_LENGTH.toDouble()

        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)

        Gui.drawRect(43, 11, 43 + (percentageBarHorizontal * BAR_HORIZONTAL).toInt(), 11 + BAR_VERTICAL, (FLUID_ALPHA shl 24) or fluidInput1Color)
        Gui.drawRect(43, 47, 43 + (percentageBarHorizontal * BAR_HORIZONTAL).toInt(), 47 + BAR_VERTICAL, (FLUID_ALPHA shl 24) or fluidInput2Color)

        if (progress.percentage >= percentagePrevious) {
            val percentageJunction = MathHelper.clamp((progress.percentage - percentagePrevious) / (JUNCTION_SIZE.toDouble() / SUM_LENGTH.toDouble()), 0.0, 1.0)
            percentagePrevious += JUNCTION_SIZE.toDouble() / SUM_LENGTH.toDouble()

            val alpha = (0x82.toDouble() * percentageJunction).toInt()
            val fluidInput1JunctionColor = (alpha shl 24) or fluidInput1Color
            val fluidInput2JunctionColor = (alpha shl 24) or fluidInput2Color

            Gui.drawRect(55, 10, 55 + JUNCTION_SIZE, 10 + JUNCTION_SIZE, fluidInput1JunctionColor)
            Gui.drawRect(55, 46, 55 + JUNCTION_SIZE, 46 + JUNCTION_SIZE, fluidInput2JunctionColor)
        }

        if (progress.percentage >= percentagePrevious) {
            val percentageBarVertical = MathHelper.clamp((progress.percentage - percentagePrevious) / (BAR_VERTICAL.toDouble() / SUM_LENGTH.toDouble()), 0.0, 1.0)
            percentagePrevious += BAR_VERTICAL.toDouble() / SUM_LENGTH.toDouble()

            Gui.drawRect(56, 20, 56 + BAR_VERTICAL, 20 + (percentageBarVertical * BAR_VERTICAL).toInt(), (FLUID_ALPHA shl 24) or fluidInput1Color)
            Gui.drawRect(56, 38 + BAR_VERTICAL - (percentageBarVertical * BAR_VERTICAL).toInt(), 56 + BAR_VERTICAL, 38 + BAR_VERTICAL, (FLUID_ALPHA shl 24) or fluidInput2Color)
        }

        if (progress.percentage >= percentagePrevious) {
            val percentageJunction = MathHelper.clamp((progress.percentage - percentagePrevious) / (JUNCTION_SIZE.toDouble() / SUM_LENGTH.toDouble()), 0.0, 1.0)
            percentagePrevious += JUNCTION_SIZE.toDouble() / SUM_LENGTH.toDouble()

            val alpha = (0x82.toDouble() * percentageJunction).toInt()
            val color = (alpha shl 24) or startColor

            Gui.drawRect(55, 28, 55 + JUNCTION_SIZE, 28 + JUNCTION_SIZE, color)
        }

        if (progress.percentage >= percentagePrevious) {
            val percentageMix = MathHelper.clamp((progress.percentage - percentagePrevious) / (MIX_HORIZONTAL.toDouble() / SUM_LENGTH.toDouble()), 0.0, 1.0)
            percentagePrevious += MIX_HORIZONTAL.toDouble() / SUM_LENGTH.toDouble()

            GlStateManager.disableDepth()
            RenderUtil.drawGradientRectHorizontal(65, 20, 65 + MIX_HORIZONTAL, 46, (FLUID_ALPHA shl 24) or startColor, (FLUID_ALPHA shl 24) or fluidOutputColor, 0.0)
            GlStateManager.color(1f, 1f, 1f, 1f)
            progressBackgroundMask.draw(minecraft, 65, 20)
            progressMask.draw(minecraft, 65, 20, 0, 0, (percentageMix * MIX_HORIZONTAL).toInt(), 0)
            GlStateManager.enableDepth()
        }

        GlStateManager.disableBlend()
    }

    private fun averageColor(color1: Int, color2: Int): Int {
        val a1 = color1 shr 24 and 0xff
        val r1 = color1 shr 16 and 0xff
        val g1 = color1 shr 8 and 0xff
        val b1 = color1 and 0xff
        val a2 = color2 shr 24 and 0xff
        val r2 = color2 shr 16 and 0xff
        val g2 = color2 shr 8 and 0xff
        val b2 = color2 and 0xff
        return ((a1 + a2) / 2 shl 24) or ((r1 + r2) / 2 shl 16) or ((g1 + g2) / 2 shl 8) or ((b1 + b2) / 2)
    }

    companion object {
        const val BAR_HORIZONTAL = 12
        const val BAR_VERTICAL = 8
        const val MIX_HORIZONTAL = 76
        const val JUNCTION_SIZE = 10
        const val SUM_LENGTH = BAR_HORIZONTAL + BAR_VERTICAL + JUNCTION_SIZE * 2 + MIX_HORIZONTAL
        const val FLUID_ALPHA = 0x82
    }
}
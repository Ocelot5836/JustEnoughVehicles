package com.ocelot.vehicle.jei.plugin.workstation

import com.mrcrayfish.vehicle.init.ModBlocks
import com.ocelot.vehicle.jei.VehicleModJei
import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack

class VehicleCategory(guiHelper: IGuiHelper) : IRecipeCategory<VehicleRecipeWrapper> {

    private val background = guiHelper.createBlankDrawable(144, 113)
    private val shadow = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 112, 74, 90, 10)
    private val icon = guiHelper.createDrawableIngredient(ItemStack(ModBlocks.WORKSTATION))
    private val slot = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 43, 18, 18)
    private val localizedName = I18n.format(VehicleModJei.VEHICLE_UNLOCALIZED_TITLE)

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun getTitle(): String = localizedName

    override fun getModName(): String = VehicleModJei.VEHICLE_NAME

    override fun getUid(): String = VehicleModJei.VEHICLE_UID

    override fun drawExtras(minecraft: Minecraft) {
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA)
        shadow.draw(minecraft, 27, 45)
        GlStateManager.disableBlend()

        slot.draw(minecraft, 0, 64)
        slot.draw(minecraft, 20, 64)
        slot.draw(minecraft, 40, 64)
        slot.draw(minecraft, 126, 64)
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: VehicleRecipeWrapper, ingredients: IIngredients) {
        val stacks = recipeLayout.itemStacks

        for (i in 0 until ingredients.getInputs(VanillaTypes.ITEM).size) {
            stacks.init(i + ingredientsStartSlot, true, (i % 8) * 18, 95 + (i / 8) * 18)
        }
        stacks.init(engineSlot, true, 20, 64)
        stacks.init(wheelsSlot, true, 40, 64)
        stacks.init(outputSlot, false, 126, 64)

        stacks.set(ingredients)
    }

    companion object {
        const val engineSlot = 0
        const val wheelsSlot = 1
        const val outputSlot = 2
        const val ingredientsStartSlot = 3
    }
}
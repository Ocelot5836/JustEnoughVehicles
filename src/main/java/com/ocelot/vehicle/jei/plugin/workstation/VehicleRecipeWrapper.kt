package com.ocelot.vehicle.jei.plugin.workstation

import com.mrcrayfish.vehicle.client.gui.GuiWorkstation
import com.mrcrayfish.vehicle.entity.EngineType
import com.mrcrayfish.vehicle.entity.EntityPoweredVehicle
import com.mrcrayfish.vehicle.entity.EntityVehicle
import com.ocelot.vehicle.jei.VehicleModJei
import mezz.jei.api.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.ingredients.VanillaTypes
import mezz.jei.api.recipe.IRecipeWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import java.util.function.Function

class VehicleRecipeWrapper(guiHelper: IGuiHelper, private val itemInputs: List<List<ItemStack>>, private val vehicleOutput: VehicleIngredient) : IRecipeWrapper {

    private val slot = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 43, 18, 18)
    private val slotIconColor = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 194, 43, 16, 16)
    private val slotIconNone = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 210, 43, 16, 16)

    override fun getIngredients(ingredients: IIngredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, itemInputs)
        ingredients.setOutput(VanillaTypes.ITEM, vehicleOutput.crate)
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
        val vehicle = vehicleOutput.vehicle

        if (vehicle.canBeColored()) {
            slotIconColor.draw(minecraft, 1, 65)
        } else {
            slotIconNone.draw(minecraft, 1, 65)
        }
        if (vehicle !is EntityPoweredVehicle || vehicle.engineType == EngineType.NONE) {
            slotIconNone.draw(minecraft, 21, 65)
        }
        if (vehicle !is EntityPoweredVehicle || !vehicle.canChangeWheels()) {
            slotIconNone.draw(minecraft, 41, 65)
        }

        for (i in 0 until MATERIAL_SLOTS) {
            slot.draw(minecraft, i * 18, 95)
        }

        minecraft.fontRenderer.drawString(I18n.format("cvmjei.category.vehicle.materials"), 0, 85, 4210752)

        GlStateManager.enableDepth()
        GlStateManager.color(1f, 1f, 1f, 1f)
        GlStateManager.translate(72f, 50f, 100f)
        GlStateManager.rotate(-5f, 1f, 0f, 0f)
        GlStateManager.rotate(Minecraft.getMinecraft().player.ticksExisted + minecraft.renderPartialTicks, 0f, 1f, 0f)
        GlStateManager.scale(SCALE, -SCALE, SCALE)

        val position = GuiWorkstation.DISPLAY_PROPERTIES[vehicleOutput.vehicle.javaClass]
        if (position != null) {
            //Apply vehicle rotations, translations, and scale
            GlStateManager.scale(position.scale, position.scale, position.scale)
            GlStateManager.rotate(position.rotX.toFloat(), 1f, 0f, 0f)
            GlStateManager.rotate(position.rotY.toFloat(), 0f, 1f, 0f)
            GlStateManager.rotate(position.rotZ.toFloat(), 0f, 0f, 1f)
            GlStateManager.translate(position.x, position.y, position.z)
        }
        minecraft.renderManager.getEntityClassRenderObject<EntityVehicle>(vehicleOutput.vehicle.javaClass).doRender(vehicle, 0.0, 0.0, 0.0, 0f, 0f)
        GlStateManager.disableDepth()
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): MutableList<String> {
        val tooltip = ArrayList<String>()

        addSlotTooltip(tooltip, 0, 64, mouseX, mouseY, SlotType.COLOR)
        addSlotTooltip(tooltip, 20, 64, mouseX, mouseY, SlotType.ENGINE)
        addSlotTooltip(tooltip, 40, 64, mouseX, mouseY, SlotType.WHEELS)

        return tooltip
    }

    private fun addSlotTooltip(tooltip: MutableList<String>, slotX: Int, slotY: Int, mouseX: Int, mouseY: Int, type: SlotType) {
        if (mouseX in slotX until slotX + 18 && mouseY in slotY until slotY + 18) {
            if (!type.applicable.apply(vehicleOutput.vehicle)) {
                tooltip.add("${TextFormatting.WHITE}${I18n.format("vehicle.tooltip.${type.registryName}")}")
                tooltip.add("${TextFormatting.GRAY}${I18n.format("vehicle.tooltip.not_applicable")}")
            } else if (type == SlotType.COLOR) {
                tooltip.add("${TextFormatting.AQUA}${I18n.format("vehicle.tooltip.optional")}")
                tooltip.add("${TextFormatting.GRAY}${I18n.format("vehicle.tooltip.${type.registryName}")}")
            }
        }
    }

    companion object {
        const val SCALE = 20f
        const val MATERIAL_SLOTS = 8
    }

    enum class SlotType(val registryName: String, val applicable: Function<EntityVehicle, Boolean>) {
        COLOR("paint_color", Function { it.canBeColored() }),
        ENGINE("engine", Function { it is EntityPoweredVehicle && it.engineType != EngineType.NONE }),
        WHEELS("wheels", Function { it is EntityPoweredVehicle && it.canChangeWheels() })
    }
}
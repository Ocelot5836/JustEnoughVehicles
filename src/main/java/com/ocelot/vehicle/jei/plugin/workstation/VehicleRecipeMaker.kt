package com.ocelot.vehicle.jei.plugin.workstation

import com.mrcrayfish.vehicle.crafting.VehicleRecipes
import com.mrcrayfish.vehicle.entity.EngineType
import com.mrcrayfish.vehicle.entity.EntityPoweredVehicle
import com.mrcrayfish.vehicle.entity.EntityVehicle
import com.mrcrayfish.vehicle.init.ModItems
import com.mrcrayfish.vehicle.item.ItemEngine
import com.ocelot.vehicle.jei.VehicleModJei
import mezz.jei.api.IJeiHelpers
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.world.EnumDifficulty
import net.minecraft.world.GameType
import net.minecraft.world.WorldSettings
import net.minecraft.world.WorldType
import net.minecraftforge.fml.common.registry.EntityRegistry
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.oredict.OreDictionary
import java.util.*

object VehicleRecipeMaker {

    fun getRecipes(helpers: IJeiHelpers): List<VehicleRecipeWrapper> {
        val stackHelper = helpers.stackHelper
        val vehicleRecipes = VehicleRecipes.RECIPES

        val recipes = ArrayList<VehicleRecipeWrapper>()
        //TODO find a way to remove this fake world
        val fakeWorld = WorldClient(null, WorldSettings(0, GameType.NOT_SET, false, false, WorldType.DEFAULT), 0, EnumDifficulty.PEACEFUL, null)

        for (entry in vehicleRecipes.entries) {
            val vehicleClass = entry.key
            val vehicleRecipe = entry.value
            val vehicleEntry = EntityRegistry.getEntry(vehicleClass)
            if (vehicleEntry == null) {
                VehicleModJei.LOGGER.warn("Vehicle class $vehicleClass is not a registered vehicle entity. Skipping!")
                continue
            }

            val vehicleEntity = vehicleEntry.newInstance(fakeWorld)
            if (vehicleEntity !is EntityVehicle) {
                VehicleModJei.LOGGER.warn("Vehicle $vehicleEntity does not extend EntityVehicle. Skipping!")
                continue
            }

            val itemInputs = ArrayList<List<ItemStack>>()

            if(vehicleEntity is EntityPoweredVehicle){
                vehicleEntity.setEngine(true)
                vehicleEntity.setWheels(true)
            }

            if (vehicleEntity is EntityPoweredVehicle && vehicleEntity.engineType != EngineType.NONE) {
                ForgeRegistries.ITEMS.forEach { item ->
                    if (item is ItemEngine && item.engineType == vehicleEntity.engineType) {
                        itemInputs.add(stackHelper.getSubtypes(ItemStack(item, 1, OreDictionary.WILDCARD_VALUE)))
                    }
                }
            } else {
                itemInputs.add(listOf(ItemStack.EMPTY))
            }

            if (vehicleEntity is EntityPoweredVehicle && vehicleEntity.canChangeWheels()) {
                itemInputs.add(stackHelper.getSubtypes(ItemStack(ModItems.WHEEL, 1,OreDictionary.WILDCARD_VALUE)))
            } else {
                itemInputs.add(listOf(ItemStack.EMPTY))
            }

            vehicleRecipe.materials.forEach { itemInputs.add(stackHelper.getSubtypes(it.copy())) }

            val recipe = VehicleRecipeWrapper(helpers.guiHelper, itemInputs, VehicleIngredient(vehicleEntity, vehicleEntry))
            recipes.add(recipe)
        }

        return recipes
    }
}
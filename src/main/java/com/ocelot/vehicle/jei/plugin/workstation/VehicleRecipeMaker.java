package com.ocelot.vehicle.jei.plugin.workstation;

import com.mrcrayfish.vehicle.crafting.VehicleRecipes;
import com.mrcrayfish.vehicle.entity.EngineType;
import com.mrcrayfish.vehicle.entity.EntityPoweredVehicle;
import com.mrcrayfish.vehicle.entity.EntityVehicle;
import com.mrcrayfish.vehicle.init.ModItems;
import com.mrcrayfish.vehicle.item.ItemEngine;
import com.ocelot.vehicle.jei.VehicleModJei;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ocelot
 */
public final class VehicleRecipeMaker
{
    private VehicleRecipeMaker()
    {
    }

    public static List<VehicleRecipeWrapper> getRecipes(IJeiHelpers jeiHelpers)
    {
        IStackHelper stackHelper = jeiHelpers.getStackHelper();
        Map<Class<? extends EntityVehicle>, VehicleRecipes.VehicleRecipe> vehicleRecipes = VehicleRecipes.RECIPES;

        List<VehicleRecipeWrapper> recipes = new ArrayList<>();
        World fakeWorld = new WorldClient(null, new WorldSettings(0, GameType.NOT_SET, false, false, WorldType.DEFAULT), 0, EnumDifficulty.PEACEFUL, null);

        vehicleRecipes.forEach((vehicleClass, vehicleRecipe) ->
        {
            EntityEntry vehicleEntry = EntityRegistry.getEntry(vehicleClass);
            if (vehicleEntry == null)
            {
                VehicleModJei.LOGGER.warn("Vehicle class {} is not a registered vehicle entity. Skipping!", vehicleClass);
                return;
            }

            Entity vehicleEntity = vehicleEntry.newInstance(fakeWorld);
            if (!(vehicleEntity instanceof EntityVehicle))
            {
                VehicleModJei.LOGGER.warn("Vehicle {} does not extend EntityVehicle. Skipping!", vehicleEntity);
                return;
            }

            List<List<ItemStack>> itemInputs = new ArrayList<>();
            VehicleIngredient vehicleOutput = new VehicleIngredient((EntityVehicle) vehicleEntity, vehicleEntry);

            if (((EntityVehicle) vehicleEntity).canBeColored())
            {
                itemInputs.add(stackHelper.getSubtypes(new ItemStack(Items.DYE, 1, OreDictionary.WILDCARD_VALUE)));
            }
            else
            {
                itemInputs.add(Collections.emptyList());
            }

            if (vehicleEntity instanceof EntityPoweredVehicle)
            {
                EntityPoweredVehicle poweredVehicle = (EntityPoweredVehicle) vehicleEntity;
                poweredVehicle.setEngine(true);
                poweredVehicle.setWheels(true);

                if (poweredVehicle.getEngineType() != EngineType.NONE)
                {
                    ForgeRegistries.ITEMS.forEach(item ->
                    {
                        if (item instanceof ItemEngine && ((ItemEngine) item).getEngineType() == poweredVehicle.getEngineType())
                        {
                            itemInputs.add(stackHelper.getSubtypes(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE)));
                        }
                    });
                }
                else
                {
                    itemInputs.add(Collections.singletonList(ItemStack.EMPTY));
                }

                if (poweredVehicle.canChangeWheels())
                {
                    itemInputs.add(stackHelper.getSubtypes(new ItemStack(ModItems.WHEEL, 1, OreDictionary.WILDCARD_VALUE)));
                }
                else
                {
                    itemInputs.add(Collections.singletonList(ItemStack.EMPTY));
                }
            }
            else
            {
                itemInputs.add(Collections.singletonList(ItemStack.EMPTY));
                itemInputs.add(Collections.singletonList(ItemStack.EMPTY));
            }

            vehicleRecipe.getMaterials().forEach(material -> itemInputs.add(stackHelper.getSubtypes(material.copy())));

            recipes.add(new VehicleRecipeWrapper(jeiHelpers.getGuiHelper(), itemInputs, vehicleOutput));
        });

        return recipes;
    }
}

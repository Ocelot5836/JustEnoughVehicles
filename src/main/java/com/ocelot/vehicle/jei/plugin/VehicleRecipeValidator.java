package com.ocelot.vehicle.jei.plugin;

import com.mrcrayfish.vehicle.crafting.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Ocelot
 */
public final class VehicleRecipeValidator
{
    private static final Logger LOGGER = LogManager.getLogger();

    private VehicleRecipeValidator()
    {
    }

    public static VehicleRecipeValidator.Results getValidRecipes()
    {
        Results results = new Results();
        ClientWorld world = Objects.requireNonNull(Minecraft.getInstance().world);
        RecipeManager recipeManager = world.getRecipeManager();

        for (IRecipe<?> recipe : getRecipes(recipeManager, RecipeType.FLUID_EXTRACTOR))
        {
            if (recipe instanceof FluidExtractorRecipe)
            {
                FluidExtractorRecipe extractorRecipe = (FluidExtractorRecipe) recipe;
                if (extractorRecipe.getIngredient().isEmpty())
                {
                    LOGGER.error("Recipe has no input. {}", recipe.getId());
                    continue;
                }
                if (extractorRecipe.getResult().createStack().isEmpty())
                {
                    LOGGER.error("Recipe has no output. {}", recipe.getId());
                    continue;
                }

                results.fluidExtractorRecipes.add(extractorRecipe);
            }
        }

        for (IRecipe<?> recipe : getRecipes(recipeManager, RecipeType.FLUID_MIXER))
        {
            if (recipe instanceof FluidMixerRecipe)
            {
                FluidMixerRecipe mixerRecipe = (FluidMixerRecipe) recipe;

                if (mixerRecipe.getInputs().length != 2)
                {
                    LOGGER.error("Recipe has too many inputs. {}", recipe.getId());
                    continue;
                }
                boolean singleInput = false;
                for (FluidEntry entry : mixerRecipe.getInputs())
                {
                    if (!entry.createStack().isEmpty())
                    {
                        singleInput = true;
                        break;
                    }
                }
                if (!singleInput)
                {
                    LOGGER.error("Recipe has no inputs. {}", recipe.getId());
                    continue;
                }
                if (mixerRecipe.getResult().createStack().isEmpty())
                {
                    LOGGER.error("Recipe has no output. {}", recipe.getId());
                    continue;
                }

                results.fluidMixerRecipes.add(mixerRecipe);
            }
        }

        for (IRecipe<?> recipe : getRecipes(recipeManager, RecipeType.CRAFTING))
        {
            if (recipe instanceof VehicleRecipe)
            {
                VehicleRecipe vehicleRecipe = (VehicleRecipe) recipe;

                if (vehicleRecipe.getMaterials().isEmpty())
                {
                    LOGGER.error("Recipe has no inputs. {}", recipe.getId());
                    continue;
                }
                if (vehicleRecipe.getVehicle() == null)
                {
                    LOGGER.error("Recipe has no output. {}", recipe.getId());
                    continue;
                }

                results.vehicleRecipes.add(vehicleRecipe);
            }
        }

        return results;
    }

    private static Collection<IRecipe<?>> getRecipes(RecipeManager recipeManager, IRecipeType<?> recipeType)
    {
        return recipeManager.getRecipes().stream().filter(recipe -> recipe.getType() == recipeType).collect(Collectors.toSet());
    }

    public static class Results
    {
        private final List<FluidExtractorRecipe> fluidExtractorRecipes = new ArrayList<>();
        private final List<FluidMixerRecipe> fluidMixerRecipes = new ArrayList<>();
        private final List<VehicleRecipe> vehicleRecipes = new ArrayList<>();

        public Results()
        {
        }

        public List<FluidExtractorRecipe> getFluidExtractorRecipes()
        {
            return this.fluidExtractorRecipes;
        }

        public List<FluidMixerRecipe> getFluidMixerRecipes()
        {
            return this.fluidMixerRecipes;
        }

        public List<VehicleRecipe> getVehicleRecipes()
        {
            return this.vehicleRecipes;
        }
    }
}

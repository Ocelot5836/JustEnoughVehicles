package com.ocelot.vehicle.jei.plugin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.vehicle.crafting.FluidExtractorRecipe;
import com.mrcrayfish.vehicle.init.ModBlocks;
import com.mrcrayfish.vehicle.util.FluidUtils;
import com.mrcrayfish.vehicle.util.RenderUtil;
import com.ocelot.vehicle.jei.VehicleModJei;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author Ocelot
 */
public class FluidExtractorCategory implements IRecipeCategory<FluidExtractorRecipe>
{
    private final IDrawableStatic progressOverlayStatic;
    private final IDrawableStatic progressOverlay;
    private final ITickTimer progress;

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableAnimated animatedFlame;
    private final IDrawableStatic outputOverlay;
    private final String localizedName;

    public FluidExtractorCategory(IGuiHelper guiHelper)
    {
        this.progressOverlayStatic = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 0, 24, 17);
        this.progressOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 200, 0, 24, 17);
        this.progress = guiHelper.createTickTimer(600, 600, false);

        IDrawableStatic staticFlame = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 0, 14, 14);
        this.background = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 0, 66, 112, 61);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.FLUID_EXTRACTOR.get()));
        this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.outputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 59);
        this.localizedName = I18n.format(VehicleModJei.FLUID_EXTRACTOR_UNLOCALIZED_TITLE);
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return background;
    }

    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setIngredients(FluidExtractorRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInput(VanillaTypes.ITEM, recipe.getIngredient());
        ingredients.setOutput(VanillaTypes.FLUID, recipe.getResult().createStack());
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return localizedName;
    }

    @Nonnull
    @Override
    public ResourceLocation getUid()
    {
        return VehicleModJei.FLUID_EXTRACTOR_UID;
    }

    @Override
    public Class<? extends FluidExtractorRecipe> getRecipeClass()
    {
        return FluidExtractorRecipe.class;
    }

    @Override
    public void draw(FluidExtractorRecipe recipe, double mouseX, double mouseY)
    {
        this.animatedFlame.draw(32, 40);

        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderUtil.drawGradientRectHorizontal(61, 22, 60 + progressOverlay.getWidth(), 21 + progressOverlay.getHeight(), -1, (0xff << 24) | FluidUtils.getAverageFluidColor(recipe.getResult().getFluid()));
        this.progressOverlayStatic.draw(61, 22);
        this.progressOverlay.draw(61, 22, 0, 0, (int) (22 * ((double) this.progress.getValue() / (double) this.progress.getMaxValue()) + 1), 0);

        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @Nonnull FluidExtractorRecipe recipe, @Nonnull IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();

        stacks.init(0, true, 31, 19);

        fluids.init(0, false, 95, 1, 16, 59, 5000, false, outputOverlay);

        stacks.set(ingredients);
        fluids.set(ingredients);
    }
}

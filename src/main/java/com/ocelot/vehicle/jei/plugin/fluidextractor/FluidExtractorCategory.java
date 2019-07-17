package com.ocelot.vehicle.jei.plugin.fluidextractor;

import com.mrcrayfish.vehicle.init.ModBlocks;
import com.mrcrayfish.vehicle.tileentity.TileEntityFluidExtractor;
import com.ocelot.vehicle.jei.VehicleModJei;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidExtractorCategory implements IRecipeCategory<FluidExtractorRecipeWrapper> {

	private static final int INPUT_SLOT = 0;
	private static final int OUTPUT_TANK = 0;

	private final IDrawableStatic background;
	private final IDrawable icon;
	private final IDrawableAnimated animatedFlame;
	private final IDrawableStatic outputOverlay;
	private final String localizedName;

	public FluidExtractorCategory(IGuiHelper guiHelper) {
		IDrawableStatic staticFlame = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 0, 14, 14);

		this.background = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 0, 66, 112, 61);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.FLUID_EXTRACTOR));
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
		this.outputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 59);
		this.localizedName = I18n.format(VehicleModJei.FLUID_EXTRACTOR_UNLOCALIZED_TITLE);
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Nullable
	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return localizedName;
	}

	@Nonnull
	@Override
	public String getModName() {
		return VehicleModJei.VEHICLE_NAME;
	}

	@Nonnull
	@Override
	public String getUid() {
		return VehicleModJei.FLUID_EXTRACTOR_UID;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		this.animatedFlame.draw(minecraft, 32, 40);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull FluidExtractorRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();

		stacks.init(INPUT_SLOT, true, 31, 19);

		fluids.init(OUTPUT_TANK, false, 95, 1, 16, 59, TileEntityFluidExtractor.TANK_CAPACITY, false, outputOverlay);

		stacks.set(ingredients);
		fluids.set(ingredients);
	}
}

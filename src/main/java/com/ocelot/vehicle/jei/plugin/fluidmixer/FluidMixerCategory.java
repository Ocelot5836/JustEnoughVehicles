package com.ocelot.vehicle.jei.plugin.fluidmixer;

import com.mrcrayfish.vehicle.init.ModBlocks;
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

public class FluidMixerCategory implements IRecipeCategory<FluidMixerRecipeWrapper> {

	private static final int INPUT_SLOT = 0;
	private static final int INPUT_TANK_1 = 0;
	private static final int INPUT_TANK_2 = 1;
	private static final int OUTPUT_TANK = 2;

	private final IDrawableStatic background;
	private final IDrawable icon;
	private final IDrawableAnimated animatedFlame;
	private final IDrawableStatic inputOverlay;
	private final IDrawableStatic outputOverlay;
	private final String localizedName;

	public FluidMixerCategory(IGuiHelper guiHelper) {
		IDrawableStatic staticFlame = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 0, 14, 14);

		this.background = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 0, 0, 160, 66);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.FLUID_MIXER));
		this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
		this.inputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 29);
		this.outputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 59);
		this.localizedName = I18n.format(VehicleModJei.FLUID_MIXER_UNLOCALIZED_TITLE);
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
		return VehicleModJei.FLUID_MIXER_UID;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		this.animatedFlame.draw(minecraft, 1, 15);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull FluidMixerRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();

		stacks.init(INPUT_SLOT, true, 94, 24);

		fluids.init(INPUT_TANK_1, true, 25, 1, 16, 29, 5000, false, inputOverlay);
		fluids.init(INPUT_TANK_2, true, 25, 36, 16, 29, 5000, false, inputOverlay);
		fluids.init(OUTPUT_TANK, false, 143, 4, 16, 59, 10000, false, outputOverlay);

		stacks.set(ingredients);
		fluids.set(ingredients);
	}
}

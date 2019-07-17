package com.ocelot.vehicle.jei.plugin.workstation;

import com.mrcrayfish.vehicle.init.ModBlocks;
import com.ocelot.vehicle.jei.VehicleModJei;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VehicleCategory implements IRecipeCategory<VehicleRecipeWrapper> {

	private static final int ENGINE_SLOT = 0;
	private static final int WHEELS_SLOT = 1;
	private static final int OUTPUT_SLOT = 2;
	private static final int INGREDIENTS_SLOT_START = 3;

	private final IDrawableStatic background;
	private final IDrawable icon;
	private final IDrawableStatic shadow;
	private final IDrawableStatic slot;
	private final String localizedName;

	public VehicleCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createBlankDrawable(144, 113);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.WORKSTATION));
		this.shadow = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 112, 74, 90, 10);
		this.slot = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 43, 18, 18);
		this.localizedName = I18n.format(VehicleModJei.VEHICLE_UNLOCALIZED_TITLE);
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
		return VehicleModJei.VEHICLE_UID;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		shadow.draw(minecraft, 27, 45);
		GlStateManager.disableBlend();

		slot.draw(minecraft, 0, 64);
		slot.draw(minecraft, 20, 64);
		slot.draw(minecraft, 40, 64);
		slot.draw(minecraft, 126, 64);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, @Nonnull VehicleRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

		for (int i = 0; i < ingredients.getInputs(VanillaTypes.ITEM).size(); i++) {
			stacks.init(i + INGREDIENTS_SLOT_START, true, (i % 8) * 18, 95 + (i / 8) * 18);
		}
		stacks.init(ENGINE_SLOT, true, 20, 64);
		stacks.init(WHEELS_SLOT, true, 40, 64);
		stacks.init(OUTPUT_SLOT, false, 126, 64);

		stacks.set(ingredients);
	}
}

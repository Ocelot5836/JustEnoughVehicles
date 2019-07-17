package com.ocelot.vehicle.jei.plugin.fluidextractor;

import com.mrcrayfish.vehicle.tileentity.TileEntityFluidExtractor;
import com.mrcrayfish.vehicle.util.FluidUtils;
import com.mrcrayfish.vehicle.util.RenderUtil;
import com.ocelot.vehicle.jei.VehicleModJei;
import com.ocelot.vehicle.jei.plugin.util.ProgressTimer;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class FluidExtractorRecipeWrapper implements IRecipeWrapper {

	private final IDrawableStatic progressOverlayStatic;
	private final IDrawableStatic progressOverlay;
	private final ProgressTimer progress;

	private final List<ItemStack> itemInput;
	private final FluidStack fluidOutput;

	FluidExtractorRecipeWrapper(IGuiHelper guiHelper, List<ItemStack> itemInput, FluidStack fluidOutput) {
		this.progressOverlayStatic = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 0, 24, 17);
		this.progressOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 200, 0, 24, 17);
		this.progress = new ProgressTimer(TileEntityFluidExtractor.FLUID_MAX_PROGRESS);

		this.itemInput = itemInput;
		this.fluidOutput = fluidOutput;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(this.itemInput));
		ingredients.setOutput(VanillaTypes.FLUID, this.fluidOutput);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		GlStateManager.disableDepth();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		RenderUtil.drawGradientRectHorizontal(61, 22, 60 + progressOverlay.getWidth(), 21 + progressOverlay.getHeight(), -1, (0xff << 24) | FluidUtils.getAverageFluidColor(fluidOutput.getFluid()), 0.0);
		progressOverlayStatic.draw(minecraft, 61, 22);
		progressOverlay.draw(minecraft, 61, 22, 0, 0, (int) (22 * progress.getPercentage() + 1), 0);

		GlStateManager.disableBlend();
		GlStateManager.enableDepth();
	}
}

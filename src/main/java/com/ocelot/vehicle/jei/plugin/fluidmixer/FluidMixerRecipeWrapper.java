package com.ocelot.vehicle.jei.plugin.fluidmixer;

import com.mrcrayfish.vehicle.tileentity.TileEntityFluidMixer;
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
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FluidMixerRecipeWrapper implements IRecipeWrapper {

	public static final int BAR_HORIZONTAL = 12;
	public static final int BAR_VERTICAL = 8;
	public static final int MIX_HORIZONTAL = 76;
	public static final int JUNCTION_SIZE = 10;
	public static final int SUM_LENGTH = BAR_HORIZONTAL + BAR_VERTICAL + JUNCTION_SIZE * 2 + MIX_HORIZONTAL;
	public static final int FLUID_ALPHA = 0x82;

	private final IDrawableStatic progressBackgroundMask;
	private final IDrawableStatic progressMask;

	private final ProgressTimer progress;
	private final int fluidInput1Color;
	private final int fluidInput2Color;
	private final int fluidOutputColor;
	private final int startColor;

	private final List<ItemStack> itemInput;
	private final FluidStack fluidInput1;
	private final FluidStack fluidInput2;
	private final FluidStack fluidOutput;

	FluidMixerRecipeWrapper(IGuiHelper guiHelper, List<ItemStack> itemInput, FluidStack fluidInput1, FluidStack fluidInput2, FluidStack fluidOutput) {
		this.progressBackgroundMask = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 17, 76, 26);
		this.progressMask = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 65, 20, 76, 26);

		this.progress = new ProgressTimer(TileEntityFluidMixer.FLUID_MAX_PROGRESS);
		this.fluidInput1Color = FluidUtils.getAverageFluidColor(fluidInput1.getFluid());
		this.fluidInput2Color = FluidUtils.getAverageFluidColor(fluidInput2.getFluid());
		this.fluidOutputColor = FluidUtils.getAverageFluidColor(fluidOutput.getFluid());
		this.startColor = averageColor(this.fluidInput1Color, this.fluidInput2Color);

		this.itemInput = itemInput;
		this.fluidInput1 = fluidInput1;
		this.fluidInput2 = fluidInput2;
		this.fluidOutput = fluidOutput;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(this.itemInput));
		ingredients.setInputs(VanillaTypes.FLUID, Arrays.asList(this.fluidInput1, this.fluidInput2));
		ingredients.setOutput(VanillaTypes.FLUID, this.fluidOutput);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		double percentagePrevious = 0;
		double percentageBarHorizontal = MathHelper.clamp((this.progress.getPercentage() - percentagePrevious) / ((double) BAR_HORIZONTAL / (double) SUM_LENGTH), 0, 1);
		percentagePrevious += (double) BAR_HORIZONTAL / (double) SUM_LENGTH;

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		Gui.drawRect(43, 11, 43 + (int) (percentageBarHorizontal * BAR_HORIZONTAL), 11 + BAR_VERTICAL, FLUID_ALPHA << 24 | this.fluidInput1Color);
		Gui.drawRect(43, 47, 43 + (int) (percentageBarHorizontal * BAR_HORIZONTAL), 47 + BAR_VERTICAL, FLUID_ALPHA << 24 | this.fluidInput2Color);

		if (this.progress.getPercentage() >= percentagePrevious) {
			double percentageJunction = MathHelper.clamp((this.progress.getPercentage() - percentagePrevious) / ((double) JUNCTION_SIZE / (double) SUM_LENGTH), 0, 1);
			percentagePrevious += (double) JUNCTION_SIZE / (double) SUM_LENGTH;

			int alpha = (int) ((double) 0x82 * percentageJunction);
			int fluidInput1JunctionColor = alpha << 24 | this.fluidInput1Color;
			int fluidInput2JunctionColor = alpha << 24 | this.fluidInput2Color;

			Gui.drawRect(55, 10, 55 + JUNCTION_SIZE, 10 + JUNCTION_SIZE, fluidInput1JunctionColor);
			Gui.drawRect(55, 46, 55 + JUNCTION_SIZE, 46 + JUNCTION_SIZE, fluidInput2JunctionColor);
		}

		if (this.progress.getPercentage() >= percentagePrevious) {
			double percentageBarVertical = MathHelper.clamp((this.progress.getPercentage() - percentagePrevious) / ((double) BAR_VERTICAL / (double) SUM_LENGTH), 0, 1);
			percentagePrevious += (double) BAR_VERTICAL / (double) SUM_LENGTH;

			Gui.drawRect(56, 20, 56 + BAR_VERTICAL, 20 + (int) (percentageBarVertical * BAR_VERTICAL), FLUID_ALPHA << 24 | this.fluidInput1Color);
			Gui.drawRect(56, 38 + BAR_VERTICAL - (int) (percentageBarVertical * BAR_VERTICAL), 56 + BAR_VERTICAL, 38 + BAR_VERTICAL, FLUID_ALPHA << 24 | this.fluidInput2Color);
		}

		if (this.progress.getPercentage() >= percentagePrevious) {
			double percentageJunction = MathHelper.clamp((this.progress.getPercentage() - percentagePrevious) / ((double) JUNCTION_SIZE / (double) SUM_LENGTH), 0, 1);
			percentagePrevious += (double) JUNCTION_SIZE / (double) SUM_LENGTH;

			int alpha = (int) ((double) 0x82 * percentageJunction);
			int color = (alpha << 24) | this.startColor;

			Gui.drawRect(55, 28, 55 + JUNCTION_SIZE, 28 + JUNCTION_SIZE, color);
		}

		if (this.progress.getPercentage() >= percentagePrevious) {
			double percentageMix = MathHelper.clamp((this.progress.getPercentage() - percentagePrevious) / ((double) MIX_HORIZONTAL / (double) SUM_LENGTH), 0, 1);

			GlStateManager.disableDepth();
			RenderUtil.drawGradientRectHorizontal(65, 20, 65 + MIX_HORIZONTAL, 46, FLUID_ALPHA << 24 | this.startColor, FLUID_ALPHA << 24 | this.fluidOutputColor, 0);
			GlStateManager.color(1f, 1f, 1f, 1f);
			this.progressBackgroundMask.draw(minecraft, 65, 20);
			this.progressMask.draw(minecraft, 65, 20, 0, 0, (int) (percentageMix * MIX_HORIZONTAL), 0);
			GlStateManager.enableDepth();
		}

		GlStateManager.disableBlend();
	}

	private static int averageColor(int color1, int color2) {
		int a1 = (color1 >> 24) & 0xff;
		int r1 = (color1 >> 16) & 0xff;
		int g1 = (color1 >> 8) & 0xff;
		int b1 = color1 & 0xff;
		int a2 = (color2 >> 24) & 0xff;
		int r2 = (color2 >> 16) & 0xff;
		int g2 = (color2 >> 8) & 0xff;
		int b2 = color2 & 0xff;
		return ((a1 + a2) / 2) << 24 | ((r1 + r2) / 2) << 16 | ((g1 + g2) / 2) << 8 | (b1 + b2) / 2;
	}
}

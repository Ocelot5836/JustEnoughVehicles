package com.ocelot.vehicle.jei.plugin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.vehicle.crafting.FluidMixerRecipe;
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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Ocelot
 */
public class FluidMixerCategory implements IRecipeCategory<FluidMixerRecipe>
{
    public static final int BAR_HORIZONTAL = 12;
    public static final int BAR_VERTICAL = 8;
    public static final int MIX_HORIZONTAL = 76;
    public static final int JUNCTION_SIZE = 10;
    public static final int SUM_LENGTH = BAR_HORIZONTAL + BAR_VERTICAL + JUNCTION_SIZE * 2 + MIX_HORIZONTAL;
    public static final int FLUID_ALPHA = 0x82;

    private final IDrawableStatic progressBackgroundMask;
    private final IDrawableStatic progressMask;
    private final ITickTimer progress;

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableAnimated animatedFlame;
    private final IDrawableStatic inputOverlay;
    private final IDrawableStatic outputOverlay;
    private final String localizedName;

    public FluidMixerCategory(IGuiHelper guiHelper)
    {
        this.progressBackgroundMask = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 17, 76, 26);
        this.progressMask = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 65, 20, 76, 26);
        this.progress = guiHelper.createTickTimer(100, 100, false);

        IDrawableStatic staticFlame = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 0, 14, 14);
        this.background = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 0, 0, 160, 66);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.FLUID_MIXER.get()));
        this.animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.inputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 29);
        this.outputOverlay = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 160, 14, 16, 59);
        this.localizedName = I18n.format(VehicleModJei.FLUID_MIXER_UNLOCALIZED_TITLE);
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
    public void setIngredients(FluidMixerRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInput(VanillaTypes.ITEM, recipe.getIngredient());
        ingredients.setInputs(VanillaTypes.FLUID, Arrays.asList(recipe.getInputs()[0].createStack(), recipe.getInputs()[1].createStack()));
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
        return VehicleModJei.FLUID_MIXER_UID;
    }

    @Override
    public Class<? extends FluidMixerRecipe> getRecipeClass()
    {
        return FluidMixerRecipe.class;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void draw(FluidMixerRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY)
    {
        this.animatedFlame.draw(matrixStack, 1, 15);
        int fluidInput1Color = FluidUtils.getAverageFluidColor(recipe.getInputs()[0].getFluid());
        int fluidInput2Color = FluidUtils.getAverageFluidColor(recipe.getInputs()[1].getFluid());
        int fluidOutputColor = FluidUtils.getAverageFluidColor(recipe.getResult().getFluid());
        int startColor = averageColor(fluidInput1Color, fluidInput2Color);

        double progressPercentage = (double) this.progress.getValue() / (double) this.progress.getMaxValue();
        double percentagePrevious = 0;
        double percentageBarHorizontal = MathHelper.clamp((progressPercentage - percentagePrevious) / ((double) BAR_HORIZONTAL / (double) SUM_LENGTH), 0, 1);
        percentagePrevious += (double) BAR_HORIZONTAL / (double) SUM_LENGTH;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        Screen.fill(matrixStack, 43, 11, 43 + (int) (percentageBarHorizontal * BAR_HORIZONTAL), 11 + BAR_VERTICAL, FLUID_ALPHA << 24 | fluidInput1Color);
        Screen.fill(matrixStack, 43, 47, 43 + (int) (percentageBarHorizontal * BAR_HORIZONTAL), 47 + BAR_VERTICAL, FLUID_ALPHA << 24 | fluidInput2Color);

        if (progressPercentage >= percentagePrevious)
        {
            double percentageJunction = MathHelper.clamp((progressPercentage - percentagePrevious) / ((double) JUNCTION_SIZE / (double) SUM_LENGTH), 0, 1);
            percentagePrevious += (double) JUNCTION_SIZE / (double) SUM_LENGTH;

            int alpha = (int) ((double) 0x82 * percentageJunction);
            int fluidInput1JunctionColor = alpha << 24 | fluidInput1Color;
            int fluidInput2JunctionColor = alpha << 24 | fluidInput2Color;

            Screen.fill(matrixStack, 55, 10, 55 + JUNCTION_SIZE, 10 + JUNCTION_SIZE, fluidInput1JunctionColor);
            Screen.fill(matrixStack, 55, 46, 55 + JUNCTION_SIZE, 46 + JUNCTION_SIZE, fluidInput2JunctionColor);
        }

        if (progressPercentage >= percentagePrevious)
        {
            double percentageBarVertical = MathHelper.clamp((progressPercentage - percentagePrevious) / ((double) BAR_VERTICAL / (double) SUM_LENGTH), 0, 1);
            percentagePrevious += (double) BAR_VERTICAL / (double) SUM_LENGTH;

            Screen.fill(matrixStack, 56, 20, 56 + BAR_VERTICAL, 20 + (int) (percentageBarVertical * BAR_VERTICAL), FLUID_ALPHA << 24 | fluidInput1Color);
            Screen.fill(matrixStack, 56, 38 + BAR_VERTICAL - (int) (percentageBarVertical * BAR_VERTICAL), 56 + BAR_VERTICAL, 38 + BAR_VERTICAL, FLUID_ALPHA << 24 | fluidInput2Color);
        }

        if (progressPercentage >= percentagePrevious)
        {
            double percentageJunction = MathHelper.clamp((progressPercentage - percentagePrevious) / ((double) JUNCTION_SIZE / (double) SUM_LENGTH), 0, 1);
            percentagePrevious += (double) JUNCTION_SIZE / (double) SUM_LENGTH;

            int alpha = (int) ((double) 0x82 * percentageJunction);
            int color = (alpha << 24) | startColor;

            Screen.fill(matrixStack, 55, 28, 55 + JUNCTION_SIZE, 28 + JUNCTION_SIZE, color);
        }

        if (progressPercentage >= percentagePrevious)
        {
            double percentageMix = MathHelper.clamp((progressPercentage - percentagePrevious) / ((double) MIX_HORIZONTAL / (double) SUM_LENGTH), 0, 1);

            RenderSystem.disableDepthTest();
            RenderSystem.pushMatrix();
            RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
            RenderUtil.drawGradientRectHorizontal(65, 20, 65 + MIX_HORIZONTAL, 46, FLUID_ALPHA << 24 | startColor, FLUID_ALPHA << 24 | fluidOutputColor);
            RenderSystem.popMatrix();
            this.progressBackgroundMask.draw(matrixStack, 65, 20);
            this.progressMask.draw(matrixStack, 65, 20, 0, 0, (int) (percentageMix * MIX_HORIZONTAL), 0);
            RenderSystem.enableDepthTest();
        }

        RenderSystem.disableBlend();
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @Nonnull FluidMixerRecipe recipeWrapper, @Nonnull IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluids = recipeLayout.getFluidStacks();

        stacks.init(0, true, 94, 24);
        fluids.init(0, true, 25, 1, 16, 29, 5000, false, inputOverlay);
        fluids.init(1, true, 25, 36, 16, 29, 5000, false, inputOverlay);
        fluids.init(2, false, 143, 4, 16, 59, 10000, false, outputOverlay);

        stacks.set(ingredients);
        fluids.set(ingredients);
    }

    private static int averageColor(int color1, int color2)
    {
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

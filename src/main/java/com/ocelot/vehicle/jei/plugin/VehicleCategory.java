package com.ocelot.vehicle.jei.plugin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mrcrayfish.vehicle.block.BlockVehicleCrate;
import com.mrcrayfish.vehicle.client.render.Axis;
import com.mrcrayfish.vehicle.common.entity.PartPosition;
import com.mrcrayfish.vehicle.crafting.VehicleRecipe;
import com.mrcrayfish.vehicle.entity.*;
import com.mrcrayfish.vehicle.init.ModBlocks;
import com.mrcrayfish.vehicle.item.EngineItem;
import com.mrcrayfish.vehicle.item.WheelItem;
import com.ocelot.vehicle.jei.VehicleModJei;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Ocelot
 */
public class VehicleCategory implements IRecipeCategory<VehicleRecipe>
{
    private static final int MATERIAL_SLOTS = 8;
    private static final float SCALE = 20;
    private static final Item[] DYES = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof DyeItem).toArray(Item[]::new);
    private static final Item[] ENGINES = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof EngineItem).toArray(Item[]::new);
    private static final Item[] WHEELS = ForgeRegistries.ITEMS.getValues().stream().filter(item -> item instanceof WheelItem).toArray(Item[]::new);

    private static final Map<EntityType<?>, Entity> VEHICLES = new WeakHashMap<>();
    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawableStatic shadow;
    private final IDrawableStatic slot;
    private final IDrawableStatic slotIconNone;
    private final String localizedName;

    public VehicleCategory(IGuiHelper guiHelper)
    {
        this.background = guiHelper.createBlankDrawable(144, 113);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.WORKSTATION.get()));
        this.shadow = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 112, 74, 90, 10);
        this.slot = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 43, 18, 18);
        this.slotIconNone = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 194, 43, 16, 16);
        this.localizedName = I18n.format(VehicleModJei.VEHICLE_UNLOCALIZED_TITLE);
    }

    @Nullable
    private static Entity getEntity(VehicleRecipe recipe)
    {
        return VEHICLES.computeIfAbsent(recipe.getVehicle(), key -> key.create(Objects.requireNonNull(Minecraft.getInstance().world)));
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
    public void setIngredients(VehicleRecipe recipe, IIngredients ingredients)
    {
        Entity entity = getEntity(recipe);

        List<List<ItemStack>> itemInputs = new ArrayList<>();

        if (entity instanceof VehicleEntity)
        {
            VehicleEntity vehicle = (VehicleEntity) entity;
            if (vehicle.canBeColored())
            {
                itemInputs.add(Arrays.stream(DYES).map(ItemStack::new).collect(Collectors.toList()));
            }
            else
            {
                itemInputs.add(Collections.emptyList());
            }
        }
        else
        {
            itemInputs.add(Collections.emptyList());
        }

        if (entity instanceof PoweredVehicleEntity)
        {
            PoweredVehicleEntity poweredVehicle = (PoweredVehicleEntity) entity;
            poweredVehicle.setEngine(true);
            poweredVehicle.setWheels(true);
            poweredVehicle.setEngineTier(EngineTier.WOOD);

            if (poweredVehicle.getEngineType() != EngineType.NONE)
            {
                itemInputs.add(Arrays.stream(ENGINES).filter(item -> ((EngineItem) item).getEngineType() == poweredVehicle.getEngineType()).map(ItemStack::new).collect(Collectors.toList()));
            }
            else
            {
                itemInputs.add(Collections.emptyList());
            }

            if (poweredVehicle.canChangeWheels())
            {
                itemInputs.add(Arrays.stream(WHEELS).map(ItemStack::new).collect(Collectors.toList()));
            }
            else
            {
                itemInputs.add(Collections.emptyList());
            }
        }
        else
        {
            itemInputs.add(Collections.emptyList());
            itemInputs.add(Collections.emptyList());
        }

        recipe.getMaterials().forEach(material -> itemInputs.add(Collections.singletonList(material.copy())));

        ingredients.setInputLists(VanillaTypes.ITEM, itemInputs);
        ingredients.setOutput(VanillaTypes.ITEM, BlockVehicleCrate.create(Objects.requireNonNull(recipe.getVehicle().getRegistryName()), 0, null, null, 0));
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
        return VehicleModJei.VEHICLE_UID;
    }

    @Override
    public Class<? extends VehicleRecipe> getRecipeClass()
    {
        return VehicleRecipe.class;
    }

    @Override
    public void draw(VehicleRecipe recipe, double mouseX, double mouseY)
    {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.shadow.draw(27, 45);
        RenderSystem.disableBlend();

        this.slot.draw(0, 64);
        this.slot.draw(20, 64);
        this.slot.draw(40, 64);
        this.slot.draw(126, 64);
        Entity vehicle = getEntity(recipe);

        if (!SlotType.COLOR.isApplicable(vehicle))
            this.slotIconNone.draw(1, 65);
        if (!SlotType.ENGINE.isApplicable(vehicle))
            this.slotIconNone.draw(21, 65);
        if (!SlotType.WHEELS.isApplicable(vehicle))
            this.slotIconNone.draw(41, 65);
        for (int i = 0; i < MATERIAL_SLOTS; i++)
            this.slot.draw(i * 18, 95);

        Minecraft.getInstance().fontRenderer.drawString(I18n.format(VehicleModJei.MOD_ID + ".category.vehicle.materials"), 0, 85, 4210752);

        if (vehicle == null)
            return;

        RenderSystem.pushMatrix();
        RenderSystem.translatef(72, 50, 1050.0F);
        RenderSystem.scalef(-1.0F, -1.0F, -1.0F);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0.0D, 0.0D, 1000.0D);
        matrixStack.scale(SCALE, SCALE, SCALE);
        Quaternion quaternion = Vector3f.XN.rotationDegrees(5.0F);
        Quaternion quaternion1 = Vector3f.YN.rotationDegrees(Objects.requireNonNull(Minecraft.getInstance().player).ticksExisted + Animation.getPartialTickTime());
        quaternion.multiply(quaternion1);
        matrixStack.rotate(quaternion);
        VehicleProperties properties = VehicleProperties.getProperties(vehicle.getType());
        PartPosition position = PartPosition.DEFAULT;
        if (properties != null)
        {
            position = properties.getDisplayPosition();
        }

        matrixStack.scale((float) position.getScale(), (float) position.getScale(), (float) position.getScale());
        matrixStack.rotate(Axis.POSITIVE_X.rotationDegrees((float) position.getRotX()));
        matrixStack.rotate(Axis.POSITIVE_Y.rotationDegrees((float) position.getRotY()));
        matrixStack.rotate(Axis.POSITIVE_Z.rotationDegrees((float) position.getRotZ()));
        matrixStack.translate(position.getX(), position.getY(), position.getZ());
        EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
        renderManager.setRenderShadow(false);
        renderManager.setCameraOrientation(quaternion);
        IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        renderManager.renderEntityStatic(vehicle, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixStack, renderTypeBuffer, 15728880);
        renderTypeBuffer.finish();
        renderManager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }

    @Nonnull
    @Override
    public List<String> getTooltipStrings(VehicleRecipe recipe, double mouseX, double mouseY)
    {
        List<String> tooltip = new ArrayList<>();

        for (int i = 0; i < SlotType.values().length; i++)
            addSlotTooltip(tooltip, i * 20, recipe, mouseX, mouseY, SlotType.values()[i]);

        return tooltip;
    }

    private void addSlotTooltip(List<String> tooltip, int slotX, VehicleRecipe recipe, double mouseX, double mouseY, SlotType type)
    {
        if (type.isApplicable(getEntity(recipe)))
            return;

        if (mouseX >= slotX && mouseX < slotX + 18 && mouseY >= 64 && mouseY < 82)
        {
            tooltip.add(TextFormatting.WHITE + I18n.format("vehicle.tooltip." + type.registryName));
            tooltip.add(TextFormatting.GRAY + I18n.format("vehicle.tooltip.not_applicable"));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @Nonnull VehicleRecipe recipeWrapper, @Nonnull IIngredients ingredients)
    {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        for (int i = 0; i < SlotType.values().length; i++)
            stacks.init(i, true, i * 20, 64);
        stacks.init(3, false, 126, 64);
        for (int i = 0; i < ingredients.getInputs(VanillaTypes.ITEM).size(); i++)
            stacks.init(4 + i, true, (i % 8) * 18, 95 + (i / 8) * 18);

        stacks.set(ingredients);
    }

    private enum SlotType
    {
        COLOR("paint_color", VehicleEntity::canBeColored),
        ENGINE("engine", vehicle -> vehicle instanceof PoweredVehicleEntity && ((PoweredVehicleEntity) vehicle).getEngineType() != EngineType.NONE),
        WHEELS("wheels", vehicle -> vehicle instanceof PoweredVehicleEntity && ((PoweredVehicleEntity) vehicle).canChangeWheels());

        private final String registryName;
        private final Predicate<VehicleEntity> applicable;

        SlotType(String registryName, Predicate<VehicleEntity> applicable)
        {
            this.registryName = registryName;
            this.applicable = applicable;
        }

        private boolean isApplicable(@Nullable Entity entity)
        {
            return entity instanceof VehicleEntity && applicable.test((VehicleEntity) entity);
        }
    }
}

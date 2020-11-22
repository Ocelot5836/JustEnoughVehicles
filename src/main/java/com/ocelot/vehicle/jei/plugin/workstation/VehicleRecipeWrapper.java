//package com.ocelot.vehicle.jei.plugin.workstation;
//
//import com.mrcrayfish.vehicle.common.entity.PartPosition;
//import com.mrcrayfish.vehicle.entity.EngineType;
//import com.mrcrayfish.vehicle.entity.EntityPoweredVehicle;
//import com.mrcrayfish.vehicle.entity.EntityVehicle;
//import com.mrcrayfish.vehicle.entity.VehicleProperties;
//import com.ocelot.vehicle.jei.VehicleModJei;
//import mezz.jei.api.IGuiHelper;
//import mezz.jei.api.gui.IDrawableStatic;
//import mezz.jei.api.ingredients.IIngredients;
//import mezz.jei.api.ingredients.VanillaTypes;
//import mezz.jei.api.recipe.IRecipeWrapper;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.text.TextFormatting;
//
//import javax.annotation.Nonnull;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Function;
//
//public class VehicleRecipeWrapper implements IRecipeWrapper
//{
//
//    public static final float SCALE = 20;
//    public static final int MATERIAL_SLOTS = 8;
//
//    private final IDrawableStatic slot;
//    private final IDrawableStatic slotIconColor;
//    private final IDrawableStatic slotIconNone;
//
//    private final List<List<ItemStack>> itemInputs;
//    private final VehicleIngredient vehicleOutput;
//
//    VehicleRecipeWrapper(IGuiHelper guiHelper, List<List<ItemStack>> itemInputs, VehicleIngredient vehicleOutput)
//    {
//        this.slot = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 176, 43, 18, 18);
//        this.slotIconColor = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 194, 43, 16, 16);
//        this.slotIconNone = guiHelper.createDrawable(VehicleModJei.RECIPE_GUI_VEHICLE, 210, 43, 16, 16);
//
//        this.itemInputs = itemInputs;
//        this.vehicleOutput = vehicleOutput;
//    }
//
//    @Override
//    public void getIngredients(IIngredients ingredients)
//    {
//        ingredients.setInputLists(VanillaTypes.ITEM, this.itemInputs);
//        ingredients.setOutput(VanillaTypes.ITEM, this.vehicleOutput.crate);
//    }
//
//    @Override
//    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
//    {
//        EntityVehicle vehicle = vehicleOutput.vehicle;
//
//        if (SlotType.COLOR.isApplicable(vehicle))
//        {
//            slotIconColor.draw(minecraft, 1, 65);
//        }
//        else
//        {
//            slotIconNone.draw(minecraft, 1, 65);
//        }
//        if (!SlotType.ENGINE.isApplicable(vehicle))
//        {
//            slotIconNone.draw(minecraft, 21, 65);
//        }
//        if (!SlotType.WHEELS.isApplicable(vehicle))
//        {
//            slotIconNone.draw(minecraft, 41, 65);
//        }
//
//        for (int i = 0; i < MATERIAL_SLOTS; i++)
//        {
//            slot.draw(minecraft, i * 18, 95);
//        }
//
//        minecraft.fontRenderer.drawString(I18n.format(VehicleModJei.MOD_ID + ".category.vehicle.materials"), 0, 85, 4210752);
//
//        GlStateManager.enableDepth();
//        GlStateManager.color(1, 1, 1, 1);
//        GlStateManager.translate(72, 50, 100);
//        GlStateManager.rotate(-5, 1, 0, 0);
//        GlStateManager.rotate(Minecraft.getMinecraft().player.ticksExisted + minecraft.getRenderPartialTicks(), 0f, 1f, 0f);
//        GlStateManager.scale(SCALE, -SCALE, SCALE);
//
//        VehicleProperties properties = VehicleProperties.getProperties(vehicleOutput.vehicle.getClass());
//        if (properties != null)
//        {
//            PartPosition position = properties.getDisplayPosition();
//            //Apply vehicle rotations, translations, and scale
//            GlStateManager.scale(position.getScale(), position.getScale(), position.getScale());
//            GlStateManager.rotate((float) position.getRotX(), 1, 0, 0);
//            GlStateManager.rotate((float) position.getRotY(), 0, 1, 0);
//            GlStateManager.rotate((float) position.getRotZ(), 0, 0, 1);
//            GlStateManager.translate(position.getX(), position.getY(), position.getZ());
//        }
//        minecraft.getRenderManager().getEntityClassRenderObject(vehicleOutput.vehicle.getClass()).doRender(vehicle, 0, 0, 0, 0, 0);
//        GlStateManager.disableDepth();
//    }
//
//    @Nonnull
//    @Override
//    public List<String> getTooltipStrings(int mouseX, int mouseY)
//    {
//        List<String> tooltip = new ArrayList<>();
//
//        addSlotTooltip(tooltip, 0, 64, mouseX, mouseY, SlotType.COLOR);
//        addSlotTooltip(tooltip, 20, 64, mouseX, mouseY, SlotType.ENGINE);
//        addSlotTooltip(tooltip, 40, 64, mouseX, mouseY, SlotType.WHEELS);
//
//        return tooltip;
//    }
//
//    private void addSlotTooltip(List<String> tooltip, int slotX, int slotY, int mouseX, int mouseY, SlotType type)
//    {
//        if (mouseX >= slotX && mouseX < slotX + 18 && mouseY >= slotY && mouseY < slotY + 18)
//        {
//            if (!type.isApplicable(vehicleOutput.vehicle))
//            {
//                tooltip.add(TextFormatting.WHITE + I18n.format("vehicle.tooltip." + type.registryName));
//                tooltip.add(TextFormatting.GRAY + I18n.format("vehicle.tooltip.not_applicable"));
//            }
//            else if (type == SlotType.COLOR)
//            {
//                tooltip.add(TextFormatting.AQUA + I18n.format("vehicle.tooltip.optional"));
//                tooltip.add(TextFormatting.GRAY + I18n.format("vehicle.tooltip." + type.registryName));
//            }
//        }
//    }
//
//    private enum SlotType
//    {
//        COLOR("paint_color", EntityVehicle::canBeColored),
//        ENGINE("engine", vehicle -> vehicle instanceof EntityPoweredVehicle && ((EntityPoweredVehicle) vehicle).getEngineType() != EngineType.NONE),
//        WHEELS("wheels", vehicle -> vehicle instanceof EntityPoweredVehicle && ((EntityPoweredVehicle) vehicle).canChangeWheels());
//
//        private final String registryName;
//        private final Function<EntityVehicle, Boolean> applicable;
//
//        SlotType(String registryName, Function<EntityVehicle, Boolean> applicable)
//        {
//            this.registryName = registryName;
//            this.applicable = applicable;
//        }
//
//        public boolean isApplicable(EntityVehicle vehicle)
//        {
//            return applicable.apply(vehicle);
//        }
//
//        public String getRegistryName()
//        {
//            return registryName;
//        }
//    }
//}

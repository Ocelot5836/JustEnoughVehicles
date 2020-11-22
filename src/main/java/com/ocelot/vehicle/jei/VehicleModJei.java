package com.ocelot.vehicle.jei;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Ocelot
 */
@Mod(VehicleModJei.MOD_ID)
public class VehicleModJei
{
    public static final String MOD_ID = "jev";

    public static final ResourceLocation FLUID_EXTRACTOR_UID = new ResourceLocation(MOD_ID, "fluid_extractor");
    public static final ResourceLocation FLUID_MIXER_UID = new ResourceLocation(MOD_ID, "fluid_mixer");
    public static final ResourceLocation VEHICLE_UID = new ResourceLocation(MOD_ID, "vehicle");

    public static final String FLUID_EXTRACTOR_UNLOCALIZED_TITLE = MOD_ID + ".category.fluid_extracting.title";
    public static final String FLUID_MIXER_UNLOCALIZED_TITLE = MOD_ID + ".category.fluid_mixing.title";
    public static final String VEHICLE_UNLOCALIZED_TITLE = MOD_ID + ".category.vehicle.title";

    public static final ResourceLocation RECIPE_GUI_VEHICLE = new ResourceLocation(MOD_ID, "textures/gui/gui_vehicle.png");
}

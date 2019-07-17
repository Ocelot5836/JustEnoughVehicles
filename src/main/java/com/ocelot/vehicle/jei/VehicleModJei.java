package com.ocelot.vehicle.jei;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = VehicleModJei.MOD_ID, name = VehicleModJei.NAME, version = VehicleModJei.VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", dependencies = "required-after:vehicle@[0.41.0,);required-after:jei")
public class VehicleModJei {
	public static final String MOD_ID = "cvmjei";
	public static final String VEHICLE_NAME = "MrCrayfish's Vehicle Mod";
	public static final String NAME = "$VEHICLE_NAME JEI";
	public static final String VERSION = "1.0";

	public static final String FLUID_EXTRACTOR_UID = "$MOD_ID:fluid_extractor";
	public static final String FLUID_MIXER_UID = "$MOD_ID:fluid_mixer";
	public static final String VEHICLE_UID = "$MOD_ID:vehicle";

	public static final String FLUID_EXTRACTOR_UNLOCALIZED_TITLE = "cvmjei.category.fluid_extracting.title";
	public static final String FLUID_MIXER_UNLOCALIZED_TITLE = "cvmjei.category.fluid_mixing.title";
	public static final String VEHICLE_UNLOCALIZED_TITLE = "cvmjei.category.vehicle.title";

	public static final ResourceLocation RECIPE_GUI_VEHICLE = new ResourceLocation(MOD_ID, "textures/gui/gui_vehicle.png");
	public static final Logger LOGGER = LogManager.getFormatterLogger(MOD_ID);
}

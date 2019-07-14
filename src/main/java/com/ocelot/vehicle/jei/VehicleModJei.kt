package com.ocelot.vehicle.jei

import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Mod

@Mod(modid = VehicleModJei.MOD_ID, name = VehicleModJei.NAME, version = VehicleModJei.VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", dependencies = "required-after:vehicle@[0.41.0,);required-after:jei")
class VehicleModJei {
    companion object {
        const val MOD_ID = "cvmjei"
        const val VEHICLE_NAME = "MrCrayfish's Vehicle Mod"
        const val NAME = "$VEHICLE_NAME JEI"
        const val VERSION = "1.0"

        const val FLUID_EXTRACTOR_UID = "$MOD_ID:fluid_extractor"
        const val FLUID_MIXER_UID = "$MOD_ID:fluid_mixer"
        val RECIPE_GUI_VEHICLE = ResourceLocation(MOD_ID, "textures/gui/gui_vehicle.png")
    }
}
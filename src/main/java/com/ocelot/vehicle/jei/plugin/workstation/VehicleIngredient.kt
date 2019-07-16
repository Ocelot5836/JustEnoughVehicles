package com.ocelot.vehicle.jei.plugin.workstation

import com.mrcrayfish.vehicle.block.BlockVehicleCrate
import com.mrcrayfish.vehicle.entity.EngineTier
import com.mrcrayfish.vehicle.entity.EntityVehicle
import com.mrcrayfish.vehicle.entity.WheelType
import net.minecraftforge.fml.common.registry.EntityEntry

class VehicleIngredient(val vehicle: EntityVehicle, val vehicleEntry: EntityEntry) {

    private val registryName = vehicleEntry.registryName!!
    val crate = BlockVehicleCrate.create(registryName, 0, EngineTier.WOOD, WheelType.STANDARD, 0)!!

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VehicleIngredient

        return vehicleEntry == other.vehicleEntry
    }

    override fun hashCode(): Int {
        return vehicleEntry.hashCode()
    }

    override fun toString(): String {
        return "VehicleIngredient(vehicle=$registryName)"
    }
}

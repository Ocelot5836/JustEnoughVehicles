package com.ocelot.vehicle.jei.plugin.workstation;

import com.mrcrayfish.vehicle.block.BlockVehicleCrate;
import com.mrcrayfish.vehicle.entity.EngineTier;
import com.mrcrayfish.vehicle.entity.EntityVehicle;
import com.mrcrayfish.vehicle.entity.WheelType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.EntityEntry;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * @author Ocelot
 */
public class VehicleIngredient
{
    public final EntityVehicle vehicle;
    private final EntityEntry vehicleEntry;
    public final ItemStack crate;

    VehicleIngredient(EntityVehicle vehicle, EntityEntry vehicleEntry)
    {
        Validate.notNull(vehicleEntry.getRegistryName(), "Registry name of Vehicle {} cannot be null!", vehicle.getClass());
        this.vehicle = vehicle;
        this.vehicleEntry = vehicleEntry;
        this.crate = BlockVehicleCrate.create(vehicleEntry.getRegistryName(), 0, EngineTier.WOOD, WheelType.STANDARD, 0);
    }

    @Override
    public boolean equals(Object other)
    {
        if (this == other) return true;
        if (!(other instanceof VehicleIngredient)) return false;
        return this.vehicleEntry.equals(((VehicleIngredient) other).vehicleEntry);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.vehicleEntry);
    }

    @Override
    public String toString()
    {
        return "VehicleIngredient{vehicleEntry=" + this.vehicleEntry.getRegistryName() + "}";
    }
}

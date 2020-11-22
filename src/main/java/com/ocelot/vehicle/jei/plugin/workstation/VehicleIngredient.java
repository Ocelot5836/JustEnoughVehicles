//package com.ocelot.vehicle.jei.plugin.workstation;
//
//import com.mrcrayfish.vehicle.block.BlockVehicleCrate;
//import com.mrcrayfish.vehicle.entity.VehicleEntity;
//import net.minecraft.entity.EntityType;
//import net.minecraft.item.ItemStack;
//import org.apache.commons.lang3.Validate;
//
//import java.util.Objects;
//
//public class VehicleIngredient
//{
//    private final EntityType<? extends VehicleEntity> vehicle;
//    public final ItemStack crate;
//
//    public VehicleIngredient(EntityType<? extends VehicleEntity> vehicle)
//    {
//        Validate.notNull(vehicle.getRegistryName(), "Registry name of Vehicle {} cannot be null!", vehicle);
//        this.vehicle = vehicle;
//        this.crate = BlockVehicleCrate.create(vehicle.getRegistryName(), 0, null, null, 0);
//    }
//
//    @Override
//    public boolean equals(Object other)
//    {
//        if (this == other) return true;
//        if (!(other instanceof VehicleIngredient)) return false;
//        return this.vehicle.equals(((VehicleIngredient) other).vehicle);
//    }
//
//    @Override
//    public int hashCode()
//    {
//        return Objects.hash(this.vehicle);
//    }
//
//    @Override
//    public String toString()
//    {
//        return "VehicleIngredient(vehicle=" + this.vehicle.getRegistryName() + ")";
//    }
//}

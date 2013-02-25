package mffs.api;

import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract interface IPowerLinkItem
{
    public abstract int getPercentageCapacity(ItemStack paramItemStack, TileEntityMFFS paramTileEntityMachines, World paramWorld);

    public abstract int getAvailablePower(ItemStack paramItemStack, TileEntityMFFS paramTileEntityMachines, World paramWorld);

    public abstract int getMaximumPower(ItemStack paramItemStack, TileEntityMFFS paramTileEntityMachines, World paramWorld);

    public abstract boolean consumePower(ItemStack paramItemStack, int paramInt, boolean paramBoolean, TileEntityMFFS paramTileEntityMachines, World paramWorld);

    public abstract boolean insertPower(ItemStack paramItemStack, int paramInt, boolean paramBoolean, TileEntityMFFS paramTileEntityMachines, World paramWorld);

    public abstract int getPowersourceID(ItemStack paramItemStack, TileEntityMFFS paramTileEntityMachines, World paramWorld);

    public abstract int getfreeStorageAmount(ItemStack paramItemStack, TileEntityMFFS paramTileEntityMachines, World paramWorld);

    public abstract boolean isPowersourceItem();
}
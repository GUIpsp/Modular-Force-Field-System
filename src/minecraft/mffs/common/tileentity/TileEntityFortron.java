package mffs.common.tileentity;

import ic2.api.energy.event.EnergyTileUnloadEvent;

import java.util.LinkedList;
import java.util.List;

import mffs.api.IFortronStorage;
import mffs.common.Fortron;
import mffs.common.FrequencyGrid;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

import com.google.common.io.ByteArrayDataInput;

/**
 * A TileEntity that is powered by fortron.
 * 
 * @author Calclavia
 * 
 */
public abstract class TileEntityFortron extends TileEntityMFFSInventory implements ITankContainer, IFortronStorage
{
	public LiquidTank fortronTank;

	@Override
	public void invalidate()
	{
		// TODO:FIXTHIS
		FrequencyGrid.getWorldMap(this.worldObj).getExtractor().remove(Integer.valueOf(getDeviceID()));
		super.invalidate();
	}

	/**
	 * Packet Methods
	 */
	@Override
	public List getPacketUpdate()
	{
		List objects = new LinkedList();
		objects.addAll(super.getPacketUpdate());
		objects.add(Fortron.getAmount(this.fortronTank.getLiquid()));
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);
		this.fortronTank.setLiquid(Fortron.getFortron(dataStream.readInt()));
	}

	/**
	 * NBT Methods
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.fortronTank.setLiquid(LiquidStack.loadLiquidStackFromNBT(nbt.getCompoundTag("fortron")));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if (this.fortronTank.getLiquid() != null)
		{
			NBTTagCompound fortronCompound = new NBTTagCompound();
			this.fortronTank.getLiquid().writeToNBT(fortronCompound);
			nbt.setTag("fortron", fortronCompound);
		}

	}

	/**
	 * Liquid Functions.
	 */
	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill)
	{
		if (resource.isLiquidEqual(Fortron.LIQUID_FORTRON))
		{
			return this.fortronTank.fill(resource, doFill);
		}

		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill)
	{
		return this.fill(ForgeDirection.getOrientation(tankIndex), resource, doFill);
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return this.fortronTank.drain(maxDrain, doDrain);
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain)
	{
		return this.drain(ForgeDirection.getOrientation(tankIndex), maxDrain, doDrain);
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction)
	{
		return new ILiquidTank[] { this.fortronTank };
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type)
	{
		if (type.isLiquidEqual(Fortron.LIQUID_FORTRON))
		{
			return this.fortronTank;
		}

		return null;
	}

	@Override
	public void setFortron(int joules)
	{
		this.fortronTank.setLiquid(Fortron.getFortron(joules));
	}

	@Override
	public int getFortron()
	{
		return Fortron.getAmount(this.fortronTank);
	}

	@Override
	public int getCapacity()
	{
		return this.fortronTank.getCapacity();
	}

	@Override
	public int consumeFortron(int joules, boolean doUse)
	{
		return Fortron.getAmount(this.fortronTank.drain(joules, doUse));
	}
}

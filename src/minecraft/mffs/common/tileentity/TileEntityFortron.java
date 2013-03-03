package mffs.common.tileentity;

import java.util.LinkedList;
import java.util.List;

import mffs.api.FortronGrid;
import mffs.api.IFortronFrequency;
import mffs.common.Fortron;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;

import com.google.common.io.ByteArrayDataInput;

/**
 * A TileEntity that is powered by fortron.
 * 
 * @author Calclavia
 * 
 */
public abstract class TileEntityFortron extends TileEntityMFFSInventory implements ITankContainer, IFortronFrequency
{
	protected LiquidTank fortronTank = new LiquidTank(Fortron.LIQUID_FORTRON.copy(), LiquidContainerRegistry.BUCKET_VOLUME, this);
	private int frequency;

	@Override
	public void initiate()
	{
		FortronGrid.INSTANCE.register(this);
		super.initiate();
	}

	@Override
	public void invalidate()
	{
		FortronGrid.INSTANCE.unregister(this);
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
		objects.add(this.getFrequency());
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		if (packetID == 1)
		{
			super.onReceivePacket(packetID, dataStream);
			this.fortronTank.setLiquid(Fortron.getFortron(dataStream.readInt()));
			this.setFrequency(dataStream.readInt());
		}
		else if (packetID == 2)
		{
			this.setFrequency(dataStream.readInt());
		}
	}

	/**
	 * NBT Methods
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		setFrequency(nbt.getInteger("frequency"));
		this.fortronTank.setLiquid(LiquidStack.loadLiquidStackFromNBT(nbt.getCompoundTag("fortron")));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("frequency", getFrequency());

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
	public void setFortronEnergy(int joules)
	{
		this.fortronTank.setLiquid(Fortron.getFortron(joules));
	}

	@Override
	public int getFortronEnergy()
	{
		return Fortron.getAmount(this.fortronTank);
	}

	@Override
	public int getFortronCapacity()
	{
		return this.fortronTank.getCapacity();
	}

	@Override
	public int consumeFortron(int joules, boolean doUse)
	{
		return Fortron.getAmount(this.fortronTank.drain(joules, doUse));
	}

	@Override
	public int getFrequency()
	{
		return this.frequency;
	}

	@Override
	public void setFrequency(int frequency)
	{
		this.frequency = frequency;
	}
}

package mffs.jiqi.t;

import icbm.api.IBlockFrequency;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

public abstract class TShengBuo extends TileEntityMFFSInventory implements IBlockFrequency
{
	private int frequency;

	@Override
	public List getPacketUpdate()
	{
		List objects = new LinkedList();
		objects.addAll(super.getPacketUpdate());
		objects.add(this.getFrequency());
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);

		if (packetID == 1)
		{
			this.setFrequency(dataStream.readInt());
		}
		else if (packetID == 2)
		{
			this.setFrequency(dataStream.readInt());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.setFrequency(nbt.getInteger("frequency"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("frequency", this.getFrequency());
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

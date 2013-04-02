package mffs.jiqi.t;

import icbm.api.IBlockFrequency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mffs.LiGuanLi;
import mffs.api.ISecurityCenter;
import mffs.api.card.ILink;
import mffs.api.fortron.IFortronFrequency;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

import com.google.common.io.ByteArrayDataInput;

public abstract class TShengBuo extends TZhuang implements IBlockFrequency
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
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream) throws IOException
	{
		super.onReceivePacket(packetID, dataStream);

		if (packetID == TPacketType.DESCRIPTION.ordinal())
		{
			this.setFrequency(dataStream.readInt());
		}
		else if (packetID == TPacketType.FREQUENCY.ordinal())
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

	/**
	 * Gets the first linked security station, based on the card slots and frequency.
	 * 
	 * @return
	 */
	public ISecurityCenter getSecurityCenter()
	{
		/**
		 * Try to find in the cards first.
		 */
		if (this.getSecurityCenters().size() > 0)
		{
			return this.getSecurityCenters().get(0);
		}

		return null;
	}

	public List<ISecurityCenter> getSecurityCenters()
	{
		List<ISecurityCenter> securityCenters = new ArrayList<ISecurityCenter>();

		/**
		 * Try to find in the cards first.
		 */
		for (ItemStack itemStack : this.getCards())
		{
			if (itemStack != null && itemStack.getItem() instanceof ILink)
			{
				Vector3 linkPos = ((ILink) itemStack.getItem()).getLink(itemStack);

				TileEntity tileEntity = linkPos.getTileEntity(this.worldObj);

				if (linkPos != null && tileEntity instanceof ISecurityCenter)
				{
					if (!securityCenters.contains(tileEntity))
					{
						securityCenters.add((ISecurityCenter) tileEntity);
					}
				}
			}
		}

		for (IFortronFrequency tileEntity : LiGuanLi.instance().get(this.getFrequency()))
		{
			if (tileEntity instanceof ISecurityCenter)
			{
				if (!securityCenters.contains(tileEntity))
				{
					securityCenters.add((ISecurityCenter) tileEntity);
				}
			}
		}

		return securityCenters;
	}
}

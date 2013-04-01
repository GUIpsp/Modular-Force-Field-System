package mffs.jiqi.t;

import mffs.ZhuYao;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;

import com.google.common.io.ByteArrayDataInput;

public class TLiQiang extends TileEntityAdvanced implements IPacketReceiver
{
	private Vector3 zhuYao = null;

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		if (this.getZhuYao() != null)
		{
			return PacketManager.getPacket(ZhuYao.CHANNEL, this, this.zhuYao.intX(), this.zhuYao.intY(), this.zhuYao.intZ());
		}

		return null;
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.setZhuYao(new Vector3(dataStream.readInt(), dataStream.readInt(), dataStream.readInt()));
			this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setZhuYao(Vector3 position)
	{
		this.zhuYao = position;
	}

	public TFangYingJi getZhuYao()
	{
		if (this.getZhuYaoSafe() != null)
		{
			return getZhuYaoSafe();
		}

		if (!this.worldObj.isRemote)
		{
			this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, 0);
		}

		return null;
	}

	public TFangYingJi getZhuYaoSafe()
	{
		if (this.zhuYao != null)
		{
			if (this.zhuYao.getTileEntity(this.worldObj) instanceof TFangYingJi)
			{
				return (TFangYingJi) this.zhuYao.getTileEntity(this.worldObj);
			}
		}
		
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.zhuYao = Vector3.readFromNBT(nbt.getCompoundTag("zhuYao"));

	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		if (this.getZhuYao() != null)
		{
			nbt.setCompoundTag("zhuYao", this.zhuYao.writeToNBT(new NBTTagCompound()));
		}
	}
}
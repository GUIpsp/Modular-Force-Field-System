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
	private int bHaoMa, bMD = 0;
	private Vector3 zhuYao = null;

	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ZhuYao.CHANNEL, this, this.bHaoMa, this.bMD);
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.setFangGe(dataStream.readInt(), dataStream.readInt());
			this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void setFangGe(int blockID, int metadata)
	{
		if (this.bHaoMa != blockID || this.bMD != metadata)
		{
			this.bHaoMa = Math.max(blockID, 0);
			this.bMD = Math.max(metadata, 0);

			if (!this.worldObj.isRemote)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket());
			}
		}
	}

	public int getHaoMa()
	{
		return this.bHaoMa;
	}

	public int getMD()
	{
		return this.bMD;
	}

	public void setZhuYao(TFangYingJi fangYingJi)
	{
		this.zhuYao = new Vector3(fangYingJi);
	}

	public TFangYingJi getZhuYao()
	{
		if (this.zhuYao != null)
		{
			if (this.zhuYao.getTileEntity(this.worldObj) instanceof TFangYingJi)
			{
				return (TFangYingJi) this.zhuYao.getTileEntity(this.worldObj);
			}
		}

		this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, 0);
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		this.bHaoMa = nbt.getInteger("bHaoMa");
		this.bMD = nbt.getInteger("bMD");
		this.zhuYao = Vector3.readFromNBT(nbt.getCompoundTag("zhuYao"));

	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		nbt.setInteger("bHaoMa", this.bHaoMa);
		nbt.setInteger("bMD", this.bMD);

		if (this.getZhuYao() != null)
		{
			nbt.setCompoundTag("zhuYao", this.zhuYao.writeToNBT(new NBTTagCompound()));
		}
	}
}
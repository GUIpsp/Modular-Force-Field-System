package mffs.common.tileentity;

import ic2.api.IWrenchable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mffs.api.IStatusToggle;
import mffs.common.MFFSConfiguration;
import mffs.common.ZhuYao;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityDisableable;

import com.google.common.io.ByteArrayDataInput;

public abstract class TileEntityMFFS extends TileEntityDisableable implements IPacketReceiver, IWrenchable, IStatusToggle, IRotatable
{
	/**
	 * Is the machine active and working?
	 */
	private boolean isActive = false;

	/**
	 * The switch mode determines the mode in which the machine is switched to. Used for determining
	 * if the machine is accepting "X" type of events for turning on.
	 */
	protected short switchMode = 0;

	/**
	 * Is the machine being switched on or is it off?
	 */
	protected boolean switchValue = false;

	protected int maxSwitchMode = 3;

	protected Random random = new Random();
	protected Ticket chunkTicket;

	public int getPercentageCapacity()
	{
		return 0;
	}

	public boolean hasPowerSource()
	{
		return false;
	}

	public TAnQuan getLinkedSecurityStation()
	{
		return null;
	}

	public List getPacketUpdate()
	{
		List objects = new ArrayList();
		objects.add(1);
		objects.add(this.isActive);
		objects.add(this.switchMode);
		objects.add(this.switchValue);
		return objects;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ZhuYao.CHANNEL, this, this.getPacketUpdate().toArray());
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.onReceivePacket(dataStream.readInt(), dataStream);
		}
		catch (Exception e)
		{
			ZhuYao.LOGGER.severe(MessageFormat.format("Packet receiving failed: {0}", this.getClass().getSimpleName()));
			e.printStackTrace();
		}
	}

	/**
	 * Inherit this function to receive packets. Make sure this function is supered.
	 */
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		if (packetID == 1)
		{
			this.isActive = dataStream.readBoolean();
			this.switchMode = dataStream.readShort();
			this.switchValue = dataStream.readBoolean();
		}
	}

	@Override
	public void initiate()
	{
		super.initiate();

		if (MFFSConfiguration.chunckLoader)
		{
			this.registerChunkLoading();
		}

		PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
	}

	public void toogleSwitchMode()
	{
		if (getStatusMode() >= this.maxSwitchMode)
		{
			this.switchMode = 0;
		}
		else
		{
			this.switchMode = ((short) (this.switchMode + 1));
		}
	}

	public boolean isPoweredByRedstone()
	{
		return this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public short getStatusMode()
	{
		return this.switchMode;
	}

	@Override
	public boolean getStatusValue()
	{
		return this.switchValue;
	}

	@Override
	public boolean canToggle()
	{
		if (getStatusMode() == 2)
		{
			return true;
		}
		return false;
	}

	@Override
	public void onToggle()
	{
		this.switchValue = !this.switchValue;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		this.isActive = nbttagcompound.getBoolean("isActive");
		this.switchValue = nbttagcompound.getBoolean("switchValue");
		this.switchMode = nbttagcompound.getShort("switchMode");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setShort("switchMode", this.switchMode);
		nbttagcompound.setBoolean("isActive", this.isActive);
		nbttagcompound.setBoolean("switchValue", this.switchValue);
	}

	public boolean isActive()
	{
		return this.isActive;
	}

	public void setActive(boolean flag)
	{
		this.isActive = flag;
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return true;
	}

	@Override
	public short getFacing()
	{
		return (short) this.getDirection(this.worldObj, this.xCoord, this.yCoord, this.zCoord).ordinal();
	}

	@Override
	public void setFacing(short facing)
	{
		this.setDirection(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.getOrientation(facing));
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return false;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1.0F;
	}

	public void forceChunkLoading(ForgeChunkManager.Ticket ticket)
	{
		if (this.chunkTicket == null)
		{
			this.chunkTicket = ticket;
		}

		ChunkCoordIntPair Chunk = new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4);
		ForgeChunkManager.forceChunk(ticket, Chunk);
	}

	protected void registerChunkLoading()
	{
		if (this.chunkTicket == null)
		{
			this.chunkTicket = ForgeChunkManager.requestTicket(ZhuYao.instance, this.worldObj, ForgeChunkManager.Type.NORMAL);
		}
		if (this.chunkTicket == null)
		{
			ZhuYao.LOGGER.fine("No free Chunkloaders available");
			return;
		}

		this.chunkTicket.getModData().setInteger("xCoord", this.xCoord);
		this.chunkTicket.getModData().setInteger("yCoord", this.yCoord);
		this.chunkTicket.getModData().setInteger("zCoord", this.zCoord);
		ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));

		forceChunkLoading(this.chunkTicket);
	}

	@Override
	public void invalidate()
	{
		ForgeChunkManager.releaseTicket(this.chunkTicket);
		super.invalidate();
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return null;
	}

	/**
	 * Direction Methods
	 */
	@Override
	public ForgeDirection getDirection(IBlockAccess world, int x, int y, int z)
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata());
	}

	@Override
	public void setDirection(World world, int x, int y, int z, ForgeDirection facingDirection)
	{
		this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal(), 3);
	}
}
package mffs.common.tileentity;

import ic2.api.IWrenchable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.io.ByteArrayDataInput;

import mffs.api.IModularProjector;
import mffs.api.ISwitchable;
import mffs.api.PointXYZ;
import mffs.common.FrequencyGrid;
import mffs.common.MFFSConfiguration;
import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.card.ItemCardDataLink;
import mffs.common.card.ItemCardPersonalID;
import mffs.common.card.ItemCardPowerLink;
import mffs.common.card.ItemCardSecurityLink;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.network.IPacketReceiver;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public abstract class TileEntityMFFS extends TileEntityAdvanced implements IPacketReceiver, IWrenchable, ISwitchable, IRotatable
{
	/**
	 * Is the machine active and working?
	 */
	private boolean isActive = false;
	protected int deviceID = 0;

	/**
	 * The switch mode determines the mode in which the machine is switched to. Used for determining
	 * if the machine is accepting "X" type of events for turning on.
	 */
	protected short switchMode = 0;

	/**
	 * Is the machine being switched on or is it off?
	 */
	protected boolean switchValue = false;
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

	public abstract TileEntitySecurityStation getLinkedSecurityStation();

	/*
	 * @Override public void onNetworkHandlerEvent(int key, String value) { switch (key) { case 0:
	 * toogleSwitchMode(); break; } }
	 * 
	 * 
	 * 
	 * 
	 * @Override public void onNetworkHandlerUpdate(String field) {
	 * this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord); }
	 */

	public List getPacketUpdate()
	{
		List objects = new ArrayList();
		objects.add(1);
		objects.add(this.isActive);
		objects.add(this.deviceID);
		objects.add(this.switchMode);
		objects.add(this.switchValue);

		return objects;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketManager.getPacket(ModularForceFieldSystem.CHANNEL, this, this.getPacketUpdate().toArray());
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
			ModularForceFieldSystem.LOGGER.severe("Packet receiving failed: " + this.getClass().getSimpleName());
			e.printStackTrace();
		}
	}

	/**
	 * Inherit this function to receive packets. Make sure this function is supered.
	 */
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{

	}

	@Override
	public void initiate()
	{
		super.initiate();

		this.deviceID = FrequencyGrid.getWorldMap(this.worldObj).refreshID(this, this.deviceID);

		if (MFFSConfiguration.chunckLoader)
		{
			registerChunkLoading();
		}

		// NetworkHandlerClient.requestInitialData(this, true);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.worldObj.isRemote && this.deviceID == 0)
		{
			if (this.ticks % 300 == 0)
			{
				// NetworkHandlerClient.requestInitialData(this, true);
			}
		}
	}

	public short getMaxSwitchMode()
	{
		return 3;
	}

	public short getMinSwitchMode()
	{
		return 0;
	}

	public void toogleSwitchMode()
	{
		if (getSwitchMode() >= getMaxSwitchMode())
		{
			this.switchMode = getMinSwitchMode();
		}
		else
		{
			this.switchMode = ((short) (this.switchMode + 1));
		}

		// NetworkHandlerServer.updateTileEntityField(this, "switchMode");
	}

	public boolean isPoweredByRedstone()
	{
		return ((this.worldObj.isBlockGettingPowered(this.xCoord, this.yCoord, this.zCoord)) || (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)));
	}

	public short getSwitchMode()
	{
		return this.switchMode;
	}

	public boolean getSwitchValue()
	{
		return this.switchValue;
	}

	@Override
	public boolean canSwitch()
	{
		if (getSwitchMode() == 2)
		{
			return true;
		}
		return false;
	}

	@Override
	public void onSwitch()
	{
		this.switchValue = !this.switchValue;
		// NetworkHandlerServer.updateTileEntityField(this, "switchValue");
	}

	public int getDeviceID()
	{
		return this.deviceID;
	}

	public void setDeviceID(int i)
	{
		this.deviceID = i;
	}

	public PointXYZ getMachinePoint()
	{
		return new PointXYZ(this.xCoord, this.yCoord, this.zCoord, this.worldObj);
	}

	public abstract Container getContainer(InventoryPlayer paramInventoryPlayer);

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		this.isActive = nbttagcompound.getBoolean("isActive");
		this.switchValue = nbttagcompound.getBoolean("switchValue");
		this.deviceID = nbttagcompound.getInteger("deviceID");
		this.switchMode = nbttagcompound.getShort("switchMode");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setShort("switchMode", this.switchMode);
		nbttagcompound.setBoolean("isActive", this.isActive);
		nbttagcompound.setBoolean("switchValue", this.switchValue);
		nbttagcompound.setInteger("deviceID", this.deviceID);
	}

	public boolean wrenchCanManipulate(EntityPlayer entityPlayer, int side)
	{
		if (!SecurityHelper.isAccessGranted(this, entityPlayer, this.worldObj, SecurityRight.EB))
		{
			return false;
		}
		return true;
	}

	public boolean isActive()
	{
		return this.isActive;
	}

	public void setActive(boolean flag)
	{
		this.isActive = flag;
		// NetworkHandlerServer.updateTileEntityField(this, "isActive");
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		if (side == getFacing())
		{
			return false;
		}
		if ((this instanceof TileEntitySecStorage))
		{
			return false;
		}
		if ((this instanceof TileEntitySecurityStation))
		{
			return false;
		}
		if (this.isActive)
		{
			return false;
		}

		return wrenchCanManipulate(entityPlayer, side);
	}

	@Override
	public short getFacing()
	{
		return (short) this.getDirection().ordinal();
	}

	@Override
	public void setFacing(short facing)
	{
		this.setDirection(ForgeDirection.getOrientation(facing));
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		if (this.isActive)
		{
			return false;
		}
		return wrenchCanManipulate(entityPlayer, 0);
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
			this.chunkTicket = ForgeChunkManager.requestTicket(ModularForceFieldSystem.instance, this.worldObj, ForgeChunkManager.Type.NORMAL);
		}
		if (this.chunkTicket == null)
		{
			ModularForceFieldSystem.LOGGER.fine("No free Chunkloaders available");
			return;
		}

		this.chunkTicket.getModData().setInteger("MachineX", this.xCoord);
		this.chunkTicket.getModData().setInteger("MachineY", this.yCoord);
		this.chunkTicket.getModData().setInteger("MachineZ", this.zCoord);
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
		return new ItemStack(net.minecraft.block.Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)]);
	}

	/**
	 * Direction Methods
	 */
	@Override
	public ForgeDirection getDirection()
	{
		return ForgeDirection.getOrientation(this.getBlockMetadata());
	}

	@Override
	public void setDirection(ForgeDirection facingDirection)
	{
		this.worldObj.setBlockMetadata(this.xCoord, this.yCoord, this.zCoord, facingDirection.ordinal());
	}
}
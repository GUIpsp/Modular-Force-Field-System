package mffs.common.tileentity;

import ic2.api.IWrenchable;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
import mffs.network.INetworkHandlerEventListener;
import mffs.network.INetworkHandlerListener;
import mffs.network.client.NetworkHandlerClient;
import mffs.network.server.NetworkHandlerServer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ISidedInventory;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.implement.IRotatable;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public abstract class TileEntityMFFS extends TileEntityAdvanced implements INetworkHandlerListener, INetworkHandlerEventListener, ISidedInventory, IWrenchable, ISwitchable, IRotatable
{
	private boolean isActive = false;
	protected int deviceID = 0;
	protected short switchMode = 0;
	protected boolean switchValue = false;
	protected Random random = new Random();
	protected Ticket chunkTicket;

	@Override
	public String getInvName()
	{
		return TranslationHelper.getLocal(this.getBlockType().getBlockName() + ".name");
	}

	public int getPercentageCapacity()
	{
		return 0;
	}

	public boolean hasPowerSource()
	{
		return false;
	}

	public abstract TileEntitySecurityStation getLinkedSecurityStation();

	@Override
	public void onNetworkHandlerEvent(int key, String value)
	{
		switch (key)
		{
			case 0:
				toogleSwitchMode();
				break;
		}
	}

	@Override
	public List getFieldsForUpdate()
	{
		List NetworkedFields = new LinkedList();
		NetworkedFields.clear();

		NetworkedFields.add("Active");
		NetworkedFields.add("Side");
		NetworkedFields.add("DeviceID");
		NetworkedFields.add("DeviceName");
		NetworkedFields.add("switchMode");
		NetworkedFields.add("SwitchValue");

		return NetworkedFields;
	}

	@Override
	public void onNetworkHandlerUpdate(String field)
	{
		this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
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

		NetworkHandlerClient.requestInitialData(this, true);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (this.worldObj.isRemote && this.deviceID == 0)
		{
			if (this.ticks % 300 == 0)
			{
				NetworkHandlerClient.requestInitialData(this, true);
			}
		}
	}

	public short getMaxswitchMode()
	{
		return 0;
	}

	public short getMinswitchMode()
	{
		return 0;
	}

	public void toogleSwitchMode()
	{
		if (getSwitchMode() == getMaxswitchMode())
		{
			this.switchMode = getMinswitchMode();
		}
		else
		{
			this.switchMode = ((short) (this.switchMode + 1));
		}

		NetworkHandlerServer.updateTileEntityField(this, "switchMode");
	}

	public boolean isRedstoneSignal()
	{
		if ((this.worldObj.isBlockGettingPowered(this.xCoord, this.yCoord, this.zCoord)) || (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)))
		{
			return true;
		}
		return false;
	}

	public short getSwitchMode()
	{
		if (this.switchMode < getMinswitchMode())
		{
			this.switchMode = getMinswitchMode();
		}
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
		this.switchValue = (!this.switchValue);
		NetworkHandlerServer.updateTileEntityField(this, "SwitchValue");
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

	public abstract void dropPlugins();

	public void dropPlugins(int slot, IInventory inventory)
	{
		if (this.worldObj.isRemote)
		{
			setInventorySlotContents(slot, null);
			return;
		}

		if (inventory.getStackInSlot(slot) != null)
		{
			if (((inventory.getStackInSlot(slot).getItem() instanceof ItemCardSecurityLink)) || ((inventory.getStackInSlot(slot).getItem() instanceof ItemCardPowerLink)) || ((inventory.getStackInSlot(slot).getItem() instanceof ItemCardPersonalID)) || ((inventory.getStackInSlot(slot).getItem() instanceof ItemCardDataLink)))
			{
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, new ItemStack(ModularForceFieldSystem.itemCardEmpty, 1)));
			}
			else
			{
				this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord, this.yCoord, this.zCoord, inventory.getStackInSlot(slot)));
			}

			inventory.setInventorySlotContents(slot, null);
			onInventoryChanged();
		}
	}

	public abstract Container getContainer(InventoryPlayer paramInventoryPlayer);

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		this.isActive = nbttagcompound.getBoolean("active");
		this.switchValue = nbttagcompound.getBoolean("SwitchValue");
		this.deviceID = nbttagcompound.getInteger("DeviceID");
		this.switchMode = nbttagcompound.getShort("switchMode");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setShort("switchMode", this.switchMode);
		nbttagcompound.setBoolean("active", this.isActive);
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
		NetworkHandlerServer.updateTileEntityField(this, "Active");
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
			System.out.println("[ModularForceFieldSystem] No free Chunkloaders available");
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

	public abstract boolean isItemValid(ItemStack paramItemStack, int paramInt);

	public abstract int getSlotStackLimit(int paramInt);

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		if (this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this)
		{
			return false;
		}
		return entityplayer.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(net.minecraft.block.Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)]);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		return null;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	public int countItemsInSlot(IModularProjector.Slots slt)
	{
		if (getStackInSlot(slt.slot) != null)
		{
			return getStackInSlot(slt.slot).stackSize;
		}
		return 0;
	}

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
package mffs.common.tileentity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mffs.common.MFFSConfiguration;
import mffs.common.SecurityRight;
import mffs.common.ZhuYao;
import mffs.common.card.ItemCardFrequency;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.upgrade.ItemModuleScale;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import universalelectricity.prefab.network.PacketManager;

import com.google.common.io.ByteArrayDataInput;

public class TFangYu extends TileEntityFortron
{
	public enum ActionMode
	{
		WARN, CONFISCATE, ASSASINATE, ANTI_HOSTILE, ANTI_FRIENDLY, ANTIBIOTIC;

		public ActionMode toggle()
		{
			int newOrdinal = this.ordinal() + 1;

			if (newOrdinal >= this.values().length)
			{
				newOrdinal = 0;
			}
			return this.values()[newOrdinal];
		}

	}

	private static final int FORTRON_CONSUMPTION = 2;

	private static final int BAN_LIST_START = 4;
	private static final int INVENTORY_START = 15;

	/**
	 * True if the current confiscation mode is for "banning selected items".
	 */
	private boolean isBanMode = true;
	public ActionMode actionMode = ActionMode.WARN;

	public TFangYu()
	{
		this.fortronTank.setCapacity(20 * LiquidContainerRegistry.BUCKET_VOLUME);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.isPoweredByRedstone())
			{
				if (!this.isActive())
				{
					this.setActive(true);
				}
			}
			else
			{
				if (this.isActive())
				{
					this.setActive(false);
				}
			}

			if (this.isActive() || (this.getStackInSlot(0) != null && this.getStackInSlot(0).itemID == ZhuYao.itemCardInfinite.itemID))
			{
				if (this.ticks % 10 == 0)
				{
					if (this.requestFortron(FORTRON_CONSUMPTION, true) > 0)
					{
						this.scan();
					}
				}
			}

			if (this.playersUsing > 0)
			{
				PacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 12);
			}
		}
	}

	@Override
	public List getPacketUpdate()
	{
		List objects = new LinkedList();
		objects.addAll(super.getPacketUpdate());
		objects.add(this.isBanMode);
		objects.add(this.actionMode.ordinal());
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream)
	{
		super.onReceivePacket(packetID, dataStream);

		if (packetID == 1)
		{
			this.isBanMode = dataStream.readBoolean();
			this.actionMode = ActionMode.values()[dataStream.readInt()];
		}
		else if (packetID == 3)
		{
			this.actionMode = this.actionMode.toggle();
		}
		else if (packetID == 4)
		{
			this.isBanMode = !this.isBanMode;
		}
	}

	public boolean isBanMode()
	{
		return this.isBanMode;
	}

	public int getActionRange()
	{
		if ((getStackInSlot(3) != null) && (getStackInSlot(3).getItem() == ZhuYao.itemModuleScale))
		{
			return getStackInSlot(3).stackSize;
		}

		return 0;
	}

	public int getWarnRange()
	{
		if ((getStackInSlot(2) != null) && (getStackInSlot(2).getItem() == ZhuYao.itemModuleScale))
		{
			return getActionRange() + (getStackInSlot(2).stackSize + 3);
		}

		return getActionRange() + 3;
	}

	public boolean hasSecurityCard()
	{
		if ((getStackInSlot(1) != null) && (getStackInSlot(1).getItem() == ZhuYao.itemCardSecurityLink))
		{
			return true;
		}

		return false;
	}

	public void scan()
	{
		try
		{
			TAnQuan securityStation = this.getLinkedSecurityStation();

			int xmininfo = this.xCoord - getWarnRange();
			int xmaxinfo = this.xCoord + getWarnRange() + 1;
			int ymininfo = this.yCoord - getWarnRange();
			int ymaxinfo = this.yCoord + getWarnRange() + 1;
			int zmininfo = this.zCoord - getWarnRange();
			int zmaxinfo = this.zCoord + getWarnRange() + 1;

			int xminaction = this.xCoord - getActionRange();
			int xmaxaction = this.xCoord + getActionRange() + 1;
			int yminaction = this.yCoord - getActionRange();
			int ymaxaction = this.yCoord + getActionRange() + 1;
			int zminaction = this.zCoord - getActionRange();
			int zmaxaction = this.zCoord + getActionRange() + 1;

			List<EntityLiving> infoLivinglist = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xmininfo, ymininfo, zmininfo, xmaxinfo, ymaxinfo, zmaxinfo));
			List<EntityLiving> actionLivinglist = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xminaction, yminaction, zminaction, xmaxaction, ymaxaction, zmaxaction));

			Set<EntityLiving> warnList = new HashSet<EntityLiving>();
			Set<EntityLiving> actionList = new HashSet<EntityLiving>();

			for (EntityLiving entityLiving : actionLivinglist)
			{
				double distance = Vector3.distance(new Vector3(this), new Vector3(entityLiving));
				if (distance <= getActionRange())
				{
					if (entityLiving instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer) entityLiving;
						actionList.add(entityLiving);
					}
				}
			}

			for (EntityLiving entityLiving : infoLivinglist)
			{
				if (entityLiving instanceof EntityPlayer && !actionList.contains(entityLiving))
				{
					EntityPlayer player = (EntityPlayer) entityLiving;
					double distance = Vector3.distance(new Vector3(this), new Vector3(entityLiving));

					if (distance <= getWarnRange())
					{
						if (!warnList.contains(player))
						{
							warnList.add(player);

							boolean isGranted = false;

							if (securityStation != null && securityStation.isAccessGranted(player.username, SecurityRight.SR))
							{
								isGranted = true;
								// TODO: CHECK MFFS NOTIFICATION SETTING < MODE 3
							}

							if (!isGranted)
							{
								player.addChatMessage("[" + this.getInvName() + "] Warning! You are in scanning range!");
								player.attackEntityFrom(ZhuYao.areaDefense, 1);
							}
						}
					}
				}
			}

			if (this.worldObj.rand.nextInt(5) == 0)
			{
				Iterator<EntityLiving> it = actionList.iterator();

				while (it.hasNext())
				{
					doDefense(it.next());
				}
			}

		}
		catch (Exception e)
		{
			ZhuYao.LOGGER.severe("Defense Station has an error!");
			e.printStackTrace();
		}
	}

	public void doDefense(EntityLiving entityLiving)
	{
		boolean hasPermission = false;

		/**
		 * Check for security permission to see if this player should be ignored.
		 */
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;

			TAnQuan securityStation = getLinkedSecurityStation();

			if (securityStation != null && securityStation.isAccessGranted(player.username, SecurityRight.SR))
			{
				hasPermission = true;
			}
		}

		/**
		 * Based on the action mode of the defense station, apply an defensive action to the
		 * specified living entity.
		 */
		switch (this.actionMode)
		{
			case WARN:
			{
				if (!hasPermission && entityLiving instanceof EntityPlayer)
				{
					((EntityPlayer) entityLiving).addChatMessage("[" + this.getInvName() + "] Leave this zone immediately. You have no right to enter.");
				}
				break;
			}
			case CONFISCATE:
			{
				Set<ItemStack> controlledStacks = new HashSet<ItemStack>();

				for (int i = BAN_LIST_START; i < INVENTORY_START; i++)
				{
					if (getStackInSlot(i) != null)
					{
						controlledStacks.add(this.getStackInSlot(i));
					}
				}

				int confiscationCount = 0;
				IInventory inventory = null;

				if (!hasPermission && entityLiving instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) entityLiving;
					inventory = player.inventory;
				}
				else if (entityLiving instanceof IInventory)
				{
					inventory = (IInventory) entityLiving;
				}

				if (inventory != null)
				{
					for (int i = 0; i < inventory.getSizeInventory(); i++)
					{
						// The ItemStack currently being checked.
						ItemStack checkStack = inventory.getStackInSlot(i);

						if (checkStack != null)
						{
							boolean stacksMatch = false;

							for (ItemStack itemStack : controlledStacks)
							{
								if (itemStack != null)
								{
									if (itemStack.isItemEqual(checkStack))
									{
										stacksMatch = true;
										break;
									}
								}
							}

							if ((isBanMode() && stacksMatch) || (!isBanMode() && !stacksMatch))
							{
								mergeIntoInventory(this, inventory.getStackInSlot(i), true);
								inventory.setInventorySlotContents(i, null);
								confiscationCount++;
								this.requestFortron(MFFSConfiguration.defenceStationSearchForceEnergy, false);
							}
						}
					}

					if (confiscationCount > 0 && entityLiving instanceof EntityPlayer)
					{
						((EntityPlayer) entityLiving).addChatMessage("[" + this.getInvName() + "] " + confiscationCount + " of your items has been confiscated.");
					}
				}

				break;
			}
			case ASSASINATE:
			{
				if (!hasPermission && entityLiving instanceof EntityPlayer)
				{
					EntityPlayer player = (EntityPlayer) entityLiving;
					player.addChatMessage("[" + this.getInvName() + "] Fairwell.");

					for (int i = 0; i < player.inventory.getSizeInventory(); i++)
					{
						this.mergeIntoInventory(this, player.inventory.getStackInSlot(i), true);
						player.inventory.setInventorySlotContents(i, null);
					}

					player.attackEntityFrom(ZhuYao.areaDefense, Integer.MAX_VALUE);
					this.requestFortron(MFFSConfiguration.defenceStationKillForceEnergy, false);
				}

				break;
			}
			case ANTI_HOSTILE:
			{
				if (entityLiving instanceof IMob && !(entityLiving instanceof INpc))
				{
					entityLiving.attackEntityFrom(ZhuYao.areaDefense, Integer.MAX_VALUE);
				}
				break;
			}
			case ANTI_FRIENDLY:
			{
				if (!(entityLiving instanceof IMob && !(entityLiving instanceof INpc)))
				{
					entityLiving.attackEntityFrom(ZhuYao.areaDefense, Integer.MAX_VALUE);
				}
				break;
			}
			case ANTIBIOTIC:
			{
				if (!(entityLiving instanceof EntityPlayer))
				{
					entityLiving.attackEntityFrom(ZhuYao.areaDefense, Integer.MAX_VALUE);
				}
				break;
			}

		}

		// TODO: CONSUME ENERGY
	}

	public boolean mergeIntoInventory(IInventory inventory, ItemStack itemStack, boolean loop)
	{
		if (itemStack != null)
		{
			for (int i = INVENTORY_START; i < inventory.getSizeInventory(); i++)
			{
				ItemStack checkStack = inventory.getStackInSlot(i);

				if (checkStack == null)
				{
					inventory.setInventorySlotContents(i, itemStack);
					return true;
				}
				else if (checkStack.isItemEqual(itemStack))
				{
					int freeSpace = checkStack.getMaxStackSize() - checkStack.stackSize;

					checkStack.stackSize += Math.min(itemStack.stackSize, freeSpace);
					itemStack.stackSize -= freeSpace;

					if (itemStack.stackSize <= 0)
					{
						itemStack = null;
						return true;
					}
				}
			}

			if (loop)
			{
				for (int i = 0; i > 6; i++)
				{
					ForgeDirection direction = ForgeDirection.getOrientation(i);
					TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), direction);

					if (tileEntity instanceof IInventory)
					{
						if (this.mergeIntoInventory((IInventory) tileEntity, itemStack, false))
						{
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	@Override
	public int getSizeInventory()
	{
		return 38;
	}

	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		switch (slotID)
		{
			case 0:
				return itemStack.getItem() instanceof ItemCardFrequency;
			case 1:
				return itemStack.getItem() instanceof ItemCardSecurityLink;
			case 2:
				return itemStack.getItem() instanceof ItemModuleScale;
			case 3:
				return itemStack.getItem() instanceof ItemModuleScale;
		}

		if (slotID >= BAN_LIST_START)
		{
			return true;
		}

		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.isBanMode = nbt.getBoolean("isBanMode");
		this.actionMode = ActionMode.values()[nbt.getInteger("actionMode")];
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("isBanMode", this.isBanMode);
		nbt.setInteger("actionMode", this.actionMode.ordinal());
	}

}
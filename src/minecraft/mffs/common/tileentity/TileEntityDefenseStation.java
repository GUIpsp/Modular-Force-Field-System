package mffs.common.tileentity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mffs.api.IPowerLinkItem;
import mffs.common.FrequencyGridOld;
import mffs.common.InventoryHelper;
import mffs.common.MFFSConfiguration;
import mffs.common.SecurityRight;
import mffs.common.ZhuYao;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.container.ContainerAreaDefenseStation;
import mffs.common.upgrade.ItemModuleScale;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import universalelectricity.core.vector.Vector3;

public class TileEntityDefenseStation extends TileEntityFortron
{
	public enum ActionMode
	{
		WARN, CONFISCATE, ASSASINATE, ANTI_HOSTILE, ANTI_FRIENDLY, ANTIBIOTIC
	}

	private static final int FORTRON_CONSUMPTION = 0;

	/**
	 * True if the current confiscation mode is for "banning selected items".
	 */
	private boolean isBanMode = true;
	public ActionMode actionMode = ActionMode.WARN;
	private int scanMode = 0;
	private int distance = 0;

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

			// TODO: FIX CONSUP
			if (this.isActive() || (this.getStackInSlot(0) != null && this.getStackInSlot(0).itemID == ZhuYao.itemCardInfinite.itemID))
			{
				if (this.ticks % 10 == 0)
				{
					if (this.requestFortron(FORTRON_CONSUMPTION, true) >= 0)
					{
						this.scan();
					}
				}
			}
		}
	}

	public int getScanmode()
	{
		return this.scanMode;
	}

	public void setScanmode(int scanmode)
	{
		this.scanMode = scanmode;
	}

	public boolean isBanMode()
	{
		return this.isBanMode;
	}

	public int getActionDistance()
	{
		if ((getStackInSlot(3) != null) && (getStackInSlot(3).getItem() == ZhuYao.itemModuleScale))
		{
			return getStackInSlot(3).stackSize;
		}

		return 0;
	}

	public int getInfoDistance()
	{
		if ((getStackInSlot(2) != null) && (getStackInSlot(2).getItem() == ZhuYao.itemModuleScale))
		{
			return getActionDistance() + (getStackInSlot(2).stackSize + 3);
		}

		return getActionDistance() + 3;
	}

	public boolean hasSecurityCard()
	{
		if ((getStackInSlot(1) != null) && (getStackInSlot(1).getItem() == ZhuYao.itemCardSecurityLink))
		{
			return true;
		}

		return false;
	}

	@Override
	public void invalidate()
	{
		FrequencyGridOld.getWorldMap(this.worldObj).getDefStation().remove(Integer.valueOf(getDeviceID()));
		super.invalidate();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.isBanMode = nbt.getBoolean("isBanMode");
		this.actionMode = ActionMode.values()[nbt.getInteger("actionMode")];
		this.scanMode = nbt.getInteger("scanmode");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("isBanMode", this.isBanMode);
		nbt.setInteger("actionMode", this.actionMode.ordinal());
		nbt.setInteger("scanmode", this.scanMode);
	}

	public void scan()
	{
		try
		{
			TileEntitySecurityStation securityStation = this.getLinkedSecurityStation();

			int xmininfo = this.xCoord - getInfoDistance();
			int xmaxinfo = this.xCoord + getInfoDistance() + 1;
			int ymininfo = this.yCoord - getInfoDistance();
			int ymaxinfo = this.yCoord + getInfoDistance() + 1;
			int zmininfo = this.zCoord - getInfoDistance();
			int zmaxinfo = this.zCoord + getInfoDistance() + 1;

			int xminaction = this.xCoord - getActionDistance();
			int xmaxaction = this.xCoord + getActionDistance() + 1;
			int yminaction = this.yCoord - getActionDistance();
			int ymaxaction = this.yCoord + getActionDistance() + 1;
			int zminaction = this.zCoord - getActionDistance();
			int zmaxaction = this.zCoord + getActionDistance() + 1;

			List<EntityLiving> infoLivinglist = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xmininfo, ymininfo, zmininfo, xmaxinfo, ymaxinfo, zmaxinfo));
			List<EntityLiving> actionLivinglist = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xminaction, yminaction, zminaction, xmaxaction, ymaxaction, zmaxaction));

			Set<EntityLiving> warnlist = new HashSet<EntityLiving>();
			Set<EntityLiving> actionList = new HashSet<EntityLiving>();

			for (EntityLiving entityLiving : infoLivinglist)
			{
				if ((entityLiving instanceof EntityPlayer))
				{
					EntityPlayer player = (EntityPlayer) entityLiving;
					double distance = Vector3.distance(new Vector3(this), new Vector3(entityLiving));

					if (distance <= getInfoDistance() || getScanmode() != 1)
					{
						if (!warnlist.contains(player))
						{
							warnlist.add(player);

							boolean isGranted = false;

							if (securityStation != null && securityStation.isAccessGranted(player.username, SecurityRight.SR))
							{
								isGranted = true;
								// TODO: CHECK MFFS NOTIFICATION SETTING < MOE 3
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

			for (EntityLiving entityLiving : actionLivinglist)
			{
				double distance = Vector3.distance(new Vector3(this), new Vector3(entityLiving));
				if (distance <= getActionDistance() || getScanmode() != 1)
				{
					if (entityLiving instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer) entityLiving;
						actionList.add(entityLiving);

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

			TileEntitySecurityStation securityStation = getLinkedSecurityStation();

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
					((EntityPlayer) entityLiving).addChatMessage("!!! [Area Defence]  Get out immediately you have no right to be here!!!");
				}
				break;
			}
			case CONFISCATE:
			{
				Set<ItemStack> controlledStacks = new HashSet<ItemStack>();

				for (int i = 5; i < 15; i++)
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

						boolean stacksMatch = false;

						for (ItemStack itemStack : controlledStacks)
						{
							if (itemStack.isItemEqual(checkStack))
							{
								stacksMatch = true;
								break;
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
					player.addChatMessage("!!! [Area Defence] Fairwell.");

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

	public boolean mergeIntoInventory(IInventory inventory, ItemStack itemstacks, boolean loop)
	{
		int count = 0;

		if ((inventory instanceof TileEntitySecStorage))
		{
			count = 1;
		}
		if ((inventory instanceof TileEntityDefenseStation))
		{
			count = 15;
		}
		for (int a = count; a <= inventory.getSizeInventory() - 1; a++)
		{
			if (inventory.getStackInSlot(a) == null)
			{
				inventory.setInventorySlotContents(a, itemstacks);
				return true;
			}
			if ((inventory.getStackInSlot(a).getItem() == itemstacks.getItem()) && (inventory.getStackInSlot(a).getItemDamage() == itemstacks.getItemDamage()) && (inventory.getStackInSlot(a).stackSize < inventory.getStackInSlot(a).getMaxStackSize()))
			{
				int free = inventory.getStackInSlot(a).getMaxStackSize() - inventory.getStackInSlot(a).stackSize;

				if (free > itemstacks.stackSize)
				{
					inventory.getStackInSlot(a).stackSize += itemstacks.stackSize;
					return true;
				}
				inventory.getStackInSlot(a).stackSize = inventory.getStackInSlot(a).getMaxStackSize();
				itemstacks.stackSize -= free;
			}

		}

		if (loop)
		{
			addremoteInventory(itemstacks);
		}
		return false;
	}

	public void addremoteInventory(ItemStack itemstacks)
	{
		IInventory inv = InventoryHelper.findAttachedInventory(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		if (inv != null)
		{
			mergeIntoInventory(inv, itemstacks, false);
		}
	}

	@Override
	public int getSizeInventory()
	{
		return 36;
	}

	@Override
	public Container getContainer(InventoryPlayer inventoryplayer)
	{
		return new ContainerAreaDefenseStation(inventoryplayer.player, this);
	}

	@Override
	public boolean isItemValid(int slotID, ItemStack itemStack)
	{
		switch (slotID)
		{
			case 0:
				if ((itemStack.getItem() instanceof IPowerLinkItem))
				{
					return true;
				}
				break;
			case 1:
				if ((itemStack.getItem() instanceof ItemCardSecurityLink))
				{
					return true;
				}
				break;
			case 2:
			case 3:
				if ((itemStack.getItem() instanceof ItemModuleScale))
				{
					return true;
				}
				break;
		}
		if ((slotID >= 5) && (slotID <= 14))
		{
			return true;
		}

		return false;
	}
}
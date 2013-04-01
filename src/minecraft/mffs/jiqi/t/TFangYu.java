package mffs.jiqi.t;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mffs.LiGuanLi;
import mffs.ZhuYao;
import mffs.api.IDefenseStation;
import mffs.api.ISecurityCenter;
import mffs.api.SecurityPermission;
import mffs.api.fortron.IFortronFrequency;
import mffs.api.modules.IDefenseStationModule;
import mffs.api.modules.IModule;
import mffs.it.ka.ItKa;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

import com.google.common.io.ByteArrayDataInput;

public class TFangYu extends TModuleAcceptor implements IDefenseStation
{
	public enum ActionMode
	{
		WARN, CONFISCATE, ASSASINATE, ANTI_HOSTILE, ANTI_FRIENDLY, ANTIBIOTIC;

		public ActionMode toggle()
		{
			int newOrdinal = this.ordinal() + 1;

			if (newOrdinal >= ActionMode.values().length)
			{
				newOrdinal = 0;
			}
			return ActionMode.values()[newOrdinal];
		}

	}

	/**
	 * True if the current confiscation mode is for "banning selected items".
	 */
	private boolean isBanMode = true;

	public TFangYu()
	{
		this.fortronTank.setCapacity(20 * LiquidContainerRegistry.BUCKET_VOLUME);
		this.startModuleIndex = 2;
		this.endModuleIndex = 8;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.isActive() || (this.getStackInSlot(0) != null && this.getStackInSlot(0).itemID == ZhuYao.itKaWuXian.itemID))
			{
				if (this.ticks % 10 == 0)
				{
					if (this.requestFortron(this.getFortronCost() * 10, false) > 0)
					{
						this.requestFortron(this.getFortronCost() * 10, true);
						this.scan();
					}
				}
			}
		}
	}

	@Override
	public List getPacketUpdate()
	{
		List objects = new LinkedList();
		objects.addAll(super.getPacketUpdate());
		objects.add(this.isBanMode);
		return objects;
	}

	@Override
	public void onReceivePacket(int packetID, ByteArrayDataInput dataStream) throws IOException
	{
		super.onReceivePacket(packetID, dataStream);

		if (packetID == TPacketType.DESCRIPTION.ordinal())
		{
			this.isBanMode = dataStream.readBoolean();
		}
		else if (packetID == TPacketType.TOGGLE_MODE.ordinal())
		{
			this.isBanMode = !this.isBanMode;
		}
	}

	public boolean isBanMode()
	{
		return this.isBanMode;
	}

	@Override
	public int getActionRange()
	{
		return this.getModuleCount(ZhuYao.itMDaXiao);
	}

	@Override
	public int getWarningRange()
	{
		return this.getModuleCount(ZhuYao.itMJuLi) + this.getActionRange() + 3;
	}

	public void scan()
	{
		try
		{
			ISecurityCenter securityStation = this.getSecurityCenter();

			int xmininfo = this.xCoord - getWarningRange();
			int xmaxinfo = this.xCoord + getWarningRange() + 1;
			int ymininfo = this.yCoord - getWarningRange();
			int ymaxinfo = this.yCoord + getWarningRange() + 1;
			int zmininfo = this.zCoord - getWarningRange();
			int zmaxinfo = this.zCoord + getWarningRange() + 1;

			int xminaction = this.xCoord - getActionRange();
			int xmaxaction = this.xCoord + getActionRange() + 1;
			int yminaction = this.yCoord - getActionRange();
			int ymaxaction = this.yCoord + getActionRange() + 1;
			int zminaction = this.zCoord - getActionRange();
			int zmaxaction = this.zCoord + getActionRange() + 1;

			List<EntityLiving> infoLivinglist = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xmininfo, ymininfo, zmininfo, xmaxinfo, ymaxinfo, zmaxinfo));
			List<EntityLiving> actionLivinglist = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xminaction, yminaction, zminaction, xmaxaction, ymaxaction, zmaxaction));

			for (EntityLiving entityLiving : infoLivinglist)
			{
				if (entityLiving instanceof EntityPlayer && !actionLivinglist.contains(entityLiving))
				{
					EntityPlayer player = (EntityPlayer) entityLiving;
					double distance = Vector3.distance(new Vector3(this), new Vector3(entityLiving));

					if (distance <= this.getWarningRange())
					{
						boolean isGranted = false;

						if (securityStation != null && securityStation.isAccessGranted(player.username, SecurityPermission.BYPASS_DEFENSE_STATION))
						{
							isGranted = true;
						}

						if (!isGranted && this.worldObj.rand.nextInt(3) == 0)
						{
							player.addChatMessage("[" + this.getInvName() + "] Warning! You are in scanning range!");
							player.attackEntityFrom(ZhuYao.areaDefense, 1);
						}

					}
				}
			}

			if (this.worldObj.rand.nextInt(3) == 0)
			{
				for (EntityLiving entityLiving : actionLivinglist)
				{
					double distance = Vector3.distance(new Vector3(this), new Vector3(entityLiving));

					if (distance <= this.getActionRange())
					{
						this.doDefense(entityLiving);
					}
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

			ISecurityCenter securityStation = this.getSecurityCenter();

			if (securityStation != null && securityStation.isAccessGranted(player.username, SecurityPermission.BYPASS_DEFENSE_STATION))
			{
				return;
			}
		}

		for (ItemStack itemStack : this.getModuleStacks())
		{
			if (itemStack.getItem() instanceof IDefenseStationModule)
			{
				IDefenseStationModule module = (IDefenseStationModule) itemStack.getItem();

				if (module.onDefend(this, entityLiving) || entityLiving.isDead)
				{
					break;
				}
			}
		}
	}

	@Override
	public boolean mergeIntoInventory(ItemStack itemStack)
	{
		for (int dir = 0; dir < 5; dir++)
		{
			ForgeDirection direction = ForgeDirection.getOrientation(dir);
			TileEntity tileEntity = VectorHelper.getTileEntityFromSide(this.worldObj, new Vector3(this), direction);

			if (tileEntity instanceof IInventory)
			{
				IInventory inventory = (IInventory) tileEntity;

				for (int i = 0; i < inventory.getSizeInventory(); i++)
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
			}
		}

		this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.xCoord + 0.5, this.yCoord + 1, this.zCoord + 0.5, itemStack));

		return false;
	}

	@Override
	public int getSizeInventory()
	{
		return 2 + 8 + 9 * 2;
	}

	@Override
	public int getFortronCost()
	{
		float cost = 2;

		for (ItemStack itemStack : this.getModuleStacks())
		{
			if (itemStack != null)
			{
				cost += itemStack.stackSize * ((IModule) itemStack.getItem()).getFortronCost(this.getActionRange());
			}
		}

		return Math.round(cost);
	}

	@Override
	public Set<ItemStack> getFilteredItems()
	{
		Set<ItemStack> stacks = new HashSet<ItemStack>();

		for (int i = this.endModuleIndex; i < this.getSizeInventory() - 1; i++)
		{
			if (this.getStackInSlot(i) != null)
			{
				stacks.add(this.getStackInSlot(i));
			}
		}
		return stacks;
	}

	@Override
	public boolean getFilterMode()
	{
		return this.isBanMode;
	}

	@Override
	public boolean isStackValidForSlot(int slotID, ItemStack itemStack)
	{
		if (slotID == 0 || slotID == 1)
		{
			return itemStack.getItem() instanceof ItKa;
		}

		if (slotID >= this.endModuleIndex)
		{
			return true;
		}

		return itemStack.getItem() instanceof IModule;
	}

	@Override
	public Set<ItemStack> getCards()
	{
		Set<ItemStack> cards = new HashSet<ItemStack>();
		cards.add(super.getCard());
		cards.add(this.getStackInSlot(1));
		return cards;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.isBanMode = nbt.getBoolean("isBanMode");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("isBanMode", this.isBanMode);
	}

	public static IDefenseStation getNearestDefenseStation(World world, Vector3 position)
	{
		for (IFortronFrequency frequencyTile : LiGuanLi.INSTANCE.get())
		{
			if (((TileEntity) frequencyTile).worldObj == world && frequencyTile instanceof IDefenseStation)
			{
				IDefenseStation defenseStation = (IDefenseStation) frequencyTile;

				if (position.distanceTo(new Vector3((TileEntity) defenseStation)) <= defenseStation.getActionRange())
				{
					return defenseStation;
				}
			}
		}

		return null;
	}
}
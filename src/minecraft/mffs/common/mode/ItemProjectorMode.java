package mffs.common.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mffs.api.IProjector;
import mffs.api.IProjectorMode;
import mffs.common.ForceFieldType;
import mffs.common.Functions;
import mffs.common.ProjectorTypes;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class ItemProjectorMode extends ItemMFFS implements IProjectorMode
{
	private static List instances = new ArrayList();

	public static List get_instances()
	{
		return instances;
	}

	public ItemProjectorMode(int i, String name)
	{
		super(i, name);
		this.setMaxStackSize(8);
		this.instances.add(this);
		this.setNoRepair();
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if ((!world.isRemote) && ((tileEntity instanceof IProjector)))
		{
			if (!SecurityHelper.isAccessGranted(tileEntity, entityplayer, world, SecurityRight.EB))
			{
				return false;
			}
			if (((IProjector) tileEntity).getStackInSlot(1) == null)
			{
				((IProjector) tileEntity).setInventorySlotContents(1, itemstack.splitStack(1));
				Functions.ChattoPlayer(entityplayer, "[Projector] Success: <Projector Module " + ProjectorTypes.typeFromItem(((IProjector) tileEntity).getMode()).displayName + "> installed");
				//((TileEntityProjector) tileEntity).checkslots();
				return true;
			}

			Functions.ChattoPlayer(entityplayer, "[Projector] Fail: Slot is not empty");
			return false;
		}

		return false;
	}
	
	@Override
	public ForceFieldType getForceFieldType()
	{
		return ForceFieldType.Default;
	}
}
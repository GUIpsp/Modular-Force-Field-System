package mffs.common.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mffs.common.ForceFieldTyps;
import mffs.common.Functions;
import mffs.common.IModularProjector;
import mffs.common.ProjectorTypes;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.item.ItemMFFS;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class ItemModuleBase extends ItemMFFS {

	private static List instances = new ArrayList();

	public static List get_instances() {
		return instances;
	}

	public ItemModuleBase(int i, String name) {
		super(i, name);
		this.setMaxStackSize(8);
		this.instances.add(this);
		this.setNoRepair();
	}

	public abstract boolean supportsDistance();

	public abstract boolean supportsStrength();

	public abstract boolean supportsMatrix();

	public abstract void calculateField(IModularProjector modularProjector,
			Set paramSet);

	@Override
	public boolean onItemUseFirst(ItemStack itemstack,
			EntityPlayer entityplayer, World world, int i, int j, int k,
			int side, float hitX, float hitY, float hitZ) {
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);

		if ((!world.isRemote) && ((tileEntity instanceof IModularProjector))) {
			if (!SecurityHelper.isAccessGranted(tileEntity, entityplayer,
					world, SecurityRight.EB)) {
				return false;
			}
			if (((IModularProjector) tileEntity).getStackInSlot(1) == null) {
				((IModularProjector) tileEntity).setInventorySlotContents(1,
						itemstack.splitStack(1));
				Functions
						.ChattoPlayer(
								entityplayer,
								"[Projector] Success: <Projector Module "
										+ ProjectorTypes
												.typeFromItem(((IModularProjector) tileEntity)
														.getStackInSlot(1)
														.getItem()).displayName
										+ "> installed");
				((TileEntityProjector) tileEntity).checkslots();
				return true;
			}

			Functions.ChattoPlayer(entityplayer,
					"[Projector] Fail: Slot is not empty");
			return false;
		}

		return false;
	}

	public ForceFieldTyps getForceFieldTyps() {
		return ForceFieldTyps.Default;
	}

	public abstract boolean supportsOption(Item paramItem);
}
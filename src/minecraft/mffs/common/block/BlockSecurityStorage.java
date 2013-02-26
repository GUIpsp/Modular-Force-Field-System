package mffs.common.block;

import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.multitool.ItemMultitool;
import mffs.common.tileentity.TileEntitySecStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSecurityStorage extends BlockMFFS {

	public BlockSecurityStorage(int i) {
		super(i, "securityStorage");
		this.blockIndexInTexture = 7 * 16;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySecStorage();
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9) {
		if (world.isRemote) {
			return true;
		}

		if ((entityplayer.getCurrentEquippedItem() != null)
				&& ((entityplayer.getCurrentEquippedItem().getItem() instanceof ItemMultitool))) {
			return false;
		}

		TileEntitySecStorage tileentity = (TileEntitySecStorage) world
				.getBlockTileEntity(i, j, k);
		if (tileentity != null) {
			if (SecurityHelper.isAccessGranted(tileentity, entityplayer, world,
					SecurityRight.OSS)) {
				if (!world.isRemote) {
					entityplayer.openGui(ModularForceFieldSystem.instance, 0,
							world, i, j, k);
				}
				return true;
			}
			return true;
		}

		return true;
	}
}
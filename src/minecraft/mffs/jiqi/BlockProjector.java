package mffs.jiqi;

import mffs.jiqi.t.TFangYingJi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockProjector extends BJiQi
{
	public BlockProjector(int id)
	{
		super(id, "projector");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TFangYingJi();
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
	{
		TFangYingJi tileentity = (TFangYingJi) world.getBlockTileEntity(i, j, k);

		if (tileentity.isDisabled())
		{
			return false;
		}

		return super.onBlockActivated(world, i, j, k, entityplayer, par6, par7, par8, par9);
	}
}
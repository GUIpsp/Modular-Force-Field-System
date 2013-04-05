package mffs.machine;

import mffs.machine.tile.TileProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockProjector extends BlockMachine
{
	public BlockProjector(int id)
	{
		super(id, "projector");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileProjector();
	}

	@Override
	public boolean onMachineActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
	{
		TileProjector tileentity = (TileProjector) world.getBlockTileEntity(i, j, k);

		if (tileentity.isDisabled())
		{
			return false;
		}

		return super.onMachineActivated(world, i, j, k, entityplayer, par6, par7, par8, par9);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public int getLightValue(IBlockAccess iBlockAccess, int x, int y, int z)
	{
		TileProjector tileEntity = (TileProjector) iBlockAccess.getBlockTileEntity(x, y, z);

		if (tileEntity.getMode() != null)
		{
			return 10;
		}

		return super.getLightValue(iBlockAccess, x, y, z);
	}
}
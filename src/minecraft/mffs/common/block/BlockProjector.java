package mffs.common.block;

import java.util.Random;

import mffs.common.ModularForceFieldSystem;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockProjector extends BlockMFFS
{
	public BlockProjector(int i)
	{
		super(i, "forceFieldProjector");
		this.setTextureFile(ModularForceFieldSystem.TEXTURE_DIRECTORY + "projector.png");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityProjector();
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
	{
		TileEntityProjector tileentity = (TileEntityProjector) world.getBlockTileEntity(i, j, k);

		if (tileentity.isBurnout())
		{
			return false;
		}

		return super.onBlockActivated(world, i, j, k, entityplayer, par6, par7, par8, par9);
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k, Random random)
	{
		TileEntityProjector tileentity = (TileEntityProjector) world.getBlockTileEntity(i, j, k);

		if (tileentity.isBurnout())
		{
			double d = i + Math.random();
			double d1 = j + Math.random();
			double d2 = k + Math.random();

			world.spawnParticle("smoke", d, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
}
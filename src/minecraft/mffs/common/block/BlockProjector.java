package mffs.common.block;

import mffs.common.ModularForceFieldSystem;
import mffs.common.ProjectorTypes;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockProjector extends BlockMFFSMachine
{
	public BlockProjector(int id)
	{
		super(id, "forceFieldProjector");
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

		if (tileentity.isDisabled())
		{
			return false;
		}

		return super.onBlockActivated(world, i, j, k, entityplayer, par6, par7, par8, par9);
	}

	@Override
	public int getBlockTexture(IBlockAccess iBlockAccess, int x, int y, int z, int side)
	{
		TileEntity t = iBlockAccess.getBlockTileEntity(x, y, z);

		if (t instanceof TileEntityProjector)
		{
			TileEntityProjector tileEntity = (TileEntityProjector) t;

			if (tileEntity.getMode() != null)
			{
				return (ProjectorTypes.typeFromItem(tileEntity.getMode()).ordinal() + 1) * 16 + super.getBlockTexture(iBlockAccess, x, y, z, side);
			}

		}

		return super.getBlockTexture(iBlockAccess, x, y, z, side);
	}
}
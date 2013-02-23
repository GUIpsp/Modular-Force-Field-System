package mffs.common.block;

import mffs.common.ModularForceFieldSystem;
import mffs.common.tileentity.TileEntityConverter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockConverter extends BlockMFFS
{
	public BlockConverter(int i)
	{
		super(i, "mffsConverter");
		this.blockIndexInTexture = 4 * 16;

	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityConverter();
	}
}
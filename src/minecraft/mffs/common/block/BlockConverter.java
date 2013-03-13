package mffs.common.block;

import mffs.common.tileentity.TileEntityConverter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockConverter extends BlockMFFSMachine
{

	public BlockConverter(int i)
	{
		super(i, "mffsConverter");

	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityConverter();
	}
}
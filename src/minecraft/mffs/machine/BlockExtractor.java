package mffs.machine;

import mffs.machine.tile.TileExtractor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockExtractor extends BlockMachine
{

	public BlockExtractor(int i)
	{
		super(i, "forcilliumExtractor");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileExtractor();
	}
}
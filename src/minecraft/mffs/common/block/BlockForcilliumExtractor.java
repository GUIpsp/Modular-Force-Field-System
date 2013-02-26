package mffs.common.block;

import mffs.common.tileentity.TileEntityExtractor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockForcilliumExtractor extends BlockMFFSMachine
{

	public BlockForcilliumExtractor(int i)
	{
		super(i, "forcilliumExtractor");
		this.blockIndexInTexture = 6 * 16;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityExtractor();
	}
}
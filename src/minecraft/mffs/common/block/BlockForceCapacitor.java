package mffs.common.block;

import mffs.common.tileentity.TileEntityCapacitor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockForceCapacitor extends BlockMFFSMachine
{

	public BlockForceCapacitor(int i)
	{
		super(i, "forceCapacitor");
		this.blockIndexInTexture = 2 * 16;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityCapacitor();
	}
}
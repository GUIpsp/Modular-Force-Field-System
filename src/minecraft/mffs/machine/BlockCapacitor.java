package mffs.machine;

import mffs.machine.tile.TileCapacitor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCapacitor extends BlockMachine
{
	public BlockCapacitor(int i)
	{
		super(i, "fortronCapacitor");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileCapacitor();
	}
}
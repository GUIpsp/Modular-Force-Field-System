package mffs.common.block;

import mffs.common.tileentity.TileEntityControlSystem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockControlSystem extends BlockMFFSMachine
{
	public BlockControlSystem(int i)
	{
		super(i, "controlSystem");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityControlSystem();
	}
}
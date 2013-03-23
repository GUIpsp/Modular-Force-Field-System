package mffs.common.block;

import mffs.common.tileentity.TKongZhi;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockControlSystem extends BJiQi
{
	public BlockControlSystem(int i)
	{
		super(i, "controlSystem");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TKongZhi();
	}
}
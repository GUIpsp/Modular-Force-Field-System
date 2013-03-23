package mffs.common.block;

import mffs.common.tileentity.TDianRong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFortronCapacitor extends BJiQi
{
	public BlockFortronCapacitor(int i)
	{
		super(i, "fortronCapacitor");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TDianRong();
	}
}
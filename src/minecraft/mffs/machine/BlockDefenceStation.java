package mffs.machine;

import mffs.machine.tile.TileSecurityStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDefenceStation extends BlockMachine
{
	public BlockDefenceStation(int i)
	{
		super(i, "securityCenter");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileSecurityStation();
	}
}
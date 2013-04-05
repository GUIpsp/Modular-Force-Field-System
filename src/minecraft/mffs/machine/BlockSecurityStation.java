package mffs.machine;

import mffs.machine.tile.TileDefenceStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSecurityStation extends BlockMachine
{
	public BlockSecurityStation(int i)
	{
		super(i, "defenseStation");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileDefenceStation();
	}
}
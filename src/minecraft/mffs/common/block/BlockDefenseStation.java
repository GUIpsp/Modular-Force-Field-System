package mffs.common.block;

import mffs.common.tileentity.TileEntityDefenseStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDefenseStation extends BlockMFFSMachine
{

	public BlockDefenseStation(int i)
	{
		super(i, "defenseStation");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityDefenseStation();
	}
}
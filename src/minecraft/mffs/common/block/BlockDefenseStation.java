package mffs.common.block;

import mffs.common.tileentity.TileEntityDefenseStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDefenseStation extends BlockMFFS
{

	public BlockDefenseStation(int i)
	{
		super(i, "defenseStation");
		this.blockIndexInTexture = 5 * 16;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntityDefenseStation();
	}
}
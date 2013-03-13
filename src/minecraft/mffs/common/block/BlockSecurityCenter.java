package mffs.common.block;

import mffs.common.tileentity.TileEntitySecurityStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSecurityCenter extends BlockMFFSMachine
{
	public BlockSecurityCenter(int i, int texture)
	{
		super(i, "securityCenter");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntitySecurityStation();
	}
}
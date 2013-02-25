package mffs.common.block;

import mffs.common.tileentity.TileEntitySecurityStation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSecurityStation extends BlockMFFS
{
	public BlockSecurityStation(int i, int texture)
	{
		super(i, "securityStation");
		this.blockIndexInTexture = texture;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEntitySecurityStation();
	}
}
package mffs.jiqi;

import mffs.jiqi.t.TAnQuan;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BFangYu extends BJiQi
{
	public BFangYu(int i)
	{
		super(i, "securityCenter");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TAnQuan();
	}
}
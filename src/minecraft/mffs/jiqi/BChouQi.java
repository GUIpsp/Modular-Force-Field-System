package mffs.jiqi;

import mffs.jiqi.t.TChouQi;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BChouQi extends BJiQi
{

	public BChouQi(int i)
	{
		super(i, "forcilliumExtractor");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TChouQi();
	}
}
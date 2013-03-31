package mffs.jiqi;

import mffs.jiqi.t.TFangYu;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BAnQuan extends BJiQi
{
	public BAnQuan(int i)
	{
		super(i, "defenseStation");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TFangYu();
	}
}
package mffs.jiqi;

import mffs.jiqi.t.TFangYu;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDefenseStation extends BJiQi
{
	public BlockDefenseStation(int i)
	{
		super(i, "defenseStation");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TFangYu();
	}
}
package mffs.jiqi;

import mffs.jiqi.t.TChouQi;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockForcilliumExtractor extends BJiQi
{

	public BlockForcilliumExtractor(int i)
	{
		super(i, "forcilliumExtractor");
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TChouQi();
	}
}
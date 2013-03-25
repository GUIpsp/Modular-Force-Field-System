package mffs.it.muo.fangyingji;

import mffs.jiqi.t.TFangYingJi;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public interface IInteriorCheck
{
	public void checkInteriorBlock(Vector3 position, World world, TFangYingJi projector);
}
package mffs.common.module;

import mffs.common.tileentity.TFangYingJi;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public interface IInteriorCheck
{
	public void checkInteriorBlock(Vector3 position, World world, TFangYingJi projector);
}
package chb.mods.mffs.common.options;

import net.minecraft.world.World;
import chb.mods.mffs.api.PointXYZ;
import chb.mods.mffs.common.tileentity.TileEntityProjector;



public interface IInteriorCheck {
	
	public void checkInteriorBlock(PointXYZ png ,World world,TileEntityProjector Projector);
	
}

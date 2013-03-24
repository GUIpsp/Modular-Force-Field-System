package mffs.common.module.fangyingji;

import java.util.Map;

import mffs.common.module.ItM;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleJammer extends ItM
{
	public ItemModuleJammer(int i)
	{
		super(i, "moduleJammer");
	}

	public boolean checkJammerinfluence(Vector3 point, World world, TFangYingJi projector)
	{
		Map<Integer, TFangYingJi> InnerMap = null;
		/*
		 * InnerMap = FrequencyGridOld.getWorldMap(world).getJammer();
		 * 
		 * for (TFangYingJi tileentity : InnerMap.values()) { boolean logicswitch = false;
		 * 
		 * logicswitch = true;// tileentity.getPowerSourceID() != Projector.getPowerSourceID();
		 * 
		 * if ((logicswitch) && (tileentity.isActive())) { for (Vector3 position :
		 * tileentity.getInteriorPoints()) { if ((position.intX() == point.intX()) &&
		 * (position.intY() == point.intY()) && (position.intZ() == point.intZ())) {
		 * projector.onDisable(20 * 10); return true; } } } }
		 */

		return false;
	}
}
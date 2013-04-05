package mffs.item.module.projector;

import java.util.Map;

import mffs.item.module.ItemModule;
import mffs.machine.tile.TileProjector;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleJammer extends ItemModule
{
	public ItemModuleJammer(int i)
	{
		super(i, "moduleJammer");
	}

	public boolean checkJammerinfluence(Vector3 point, World world, TileProjector projector)
	{
		Map<Integer, TileProjector> InnerMap = null;
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
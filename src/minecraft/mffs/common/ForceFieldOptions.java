package mffs.common;

import java.util.Iterator;
import java.util.Map;

import mffs.common.tileentity.TFangYingJi;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public final class ForceFieldOptions
{

	public static boolean BlockProtected(World world, int x, int y, int z, EntityPlayer entityplayer)
	{
		Map ProjectorinrangeMap = FrequencyGridOld.getWorldMap(world).getProjector();
		for (Iterator i$ = ProjectorinrangeMap.values().iterator(); i$.hasNext();)
		{
			TFangYingJi tileentity = (TFangYingJi) i$.next();

			int dx = tileentity.xCoord - x;
			int dy = tileentity.yCoord - y;
			int dz = tileentity.zCoord - z;

			int dist = (int) Math.round(Math.sqrt(dx * dx + dy * dy + dz * dz));

			if ((dist <= 64) && (tileentity.isActive()) && (tileentity.getMode() != null) )
			{
				Map<Integer, TFangYingJi> InnerMap = null;
				InnerMap = FrequencyGridOld.getWorldMap(world).getProjector();

				for (TFangYingJi tileentity2 : InnerMap.values())
				{
					boolean logicswitch = tileentity2.equals(tileentity);

					if ((logicswitch) && (tileentity2.isActive()))
					{
						if (entityplayer != null)
						{
							if (!SecurityHelper.isAccessGranted(tileentity, entityplayer, world, SecurityRight.RPB, true))
							{
								return true;
							}
						}
						else
						{
							return true;
						}
					}
				}
			}
		}
		TFangYingJi tileentity;
		return false;
	}
}
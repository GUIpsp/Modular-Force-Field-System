package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.common.ZhuYao;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;

public class ItemModeTube extends ItemProjectorMode
{
	public ItemModeTube(int i)
	{
		super(i, "modeTube");
	}

	@Override
	public void doCalculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior, ForgeDirection direction, Vector3 translation, Vector3 posScale, Vector3 negScale)
	{
		for (int x = -negScale.intX(); x <= posScale.intX(); x++)
		{
			for (int z = -negScale.intZ(); z <= posScale.intZ(); z++)
			{
				for (int y = -negScale.intY(); y <= posScale.intY(); y++)
				{
					if (!(direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) && (y == -negScale.intY() || y == posScale.intY()))
					{
						blockDef.add(Vector3.add(translation, new Vector3(x, y, z)));
					}

					if (!(direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH) && (z == -negScale.intZ() || z == posScale.intZ()))
					{
						blockDef.add(Vector3.add(translation, new Vector3(x, y, z)));
					}
					
					if (!(direction == ForgeDirection.WEST || direction == ForgeDirection.EAST) && (x == -negScale.intX() || x == posScale.intX()))
					{
						blockDef.add(Vector3.add(translation, new Vector3(x, y, z)));
					}
				}
			}
		}
	}
}
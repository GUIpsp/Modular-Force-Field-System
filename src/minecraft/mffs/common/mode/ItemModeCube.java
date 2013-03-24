package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.client.model.ModelCube;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemModeCube extends ItemProjectorMode
{
	public ItemModeCube(int i)
	{
		super(i, "modeCube");
	}

	@Override
	public void doCalculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior, ForgeDirection direction, Vector3 center, Vector3 posScale, Vector3 negScale)
	{
		for (int x = -negScale.intX(); x <= posScale.intX(); x++)
		{
			for (int z = -negScale.intZ(); z <= posScale.intZ(); z++)
			{
				for (int y = -negScale.intY(); y <= posScale.intY(); y++)
				{
					if (y == -negScale.intY() || y == posScale.intY() || x == -negScale.intX() || x == posScale.intX() || z == -negScale.intZ() || z == posScale.intZ())
					{
						blockDef.add(Vector3.add(center, new Vector3(x, y, z)));
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(IProjector projector, double x, double y, double z, float f, long ticks)
	{
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		ModelCube.INSTNACE.render();
	}
}
package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.client.model.ModelCube;
import mffs.common.ZhuYao;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemModeSphere extends ItemProjectorMode
{
	public ItemModeSphere(int i)
	{
		super(i, "modeSphere");
	}

	@Override
	public void doCalculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior, ForgeDirection direction, Vector3 center, Vector3 posScale, Vector3 negScale)
	{
		int radius = projector.getModuleCount(ZhuYao.itemModuleScale, 14, 15) + 4;

		int yDown = radius;

		if (projector.getModuleCount(ZhuYao.itemModuleManipulator) > 0)
		{
			yDown = 0;
		}

		for (float x = -negScale.intX(); x <= posScale.intX(); x += 0.5f)
		{
			for (float z = -negScale.intZ(); z <= posScale.intZ(); z += 0.5f)
			{
				for (float y = -negScale.intY(); y <= posScale.intY(); y += 0.5f)
				{
					Vector3 checkPosition = new Vector3(x, y, z);
					double distance = Vector3.distance(new Vector3(), checkPosition);

					if (distance <= radius && distance > radius - 1)
					{
						blockDef.add(Vector3.add(center, checkPosition));
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(IProjector projector, double x1, double y1, double z1, float f, long ticks)
	{
		float scale = 0.06f;
		GL11.glScalef(scale, scale, scale);

		int radius = 6;

		int steps = (int) Math.ceil(Math.PI / Math.atan(1.0D / radius));

		for (int phi_n = 0; phi_n < 2 * steps; phi_n++)
		{
			for (int theta_n = 0; theta_n < steps; theta_n++)
			{
				double phi = Math.PI * 2 / steps * phi_n;
				double theta = Math.PI / steps * theta_n;

				Vector3 vector = new Vector3(Math.sin(theta) * Math.cos(phi), Math.cos(theta), Math.sin(theta) * Math.sin(phi));
				vector.multiply(radius);
				GL11.glTranslated(vector.x, vector.y, vector.z);
				ModelCube.INSTNACE.render();
				GL11.glTranslated(-vector.x, -vector.y, -vector.z);
			}
		}
	}
}
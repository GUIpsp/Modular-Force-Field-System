package mffs.item.mode;

import java.util.Set;

import mffs.ModularForceFieldSystem;
import mffs.api.IProjector;
import mffs.muoxing.ModelCube;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemProjectorModeSphere extends ItemProjectorMode
{
	public ItemProjectorModeSphere(int i)
	{
		super(i, "modeSphere");
	}

	@Override
	public void doCalculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior, ForgeDirection direction, Vector3 center, Vector3 posScale, Vector3 negScale)
	{
		int radius = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale);

		int steps = (int) Math.ceil(Math.PI / Math.atan(1.0D / radius / 2));

		// for (int i = 0; i <= radius; i++)
		{
			int i = radius;

			for (int phi_n = 0; phi_n < 2 * steps; phi_n++)
			{
				for (int theta_n = 0; theta_n < steps; theta_n++)
				{
					double phi = Math.PI * 2 / steps * phi_n;
					double theta = Math.PI / steps * theta_n;

					Vector3 point = new Vector3(Math.sin(theta) * Math.cos(phi), Math.cos(theta), Math.sin(theta) * Math.sin(phi)).multiply(i);

					if (i == radius)
					{
						blockDef.add(Vector3.add(center, point));
					}
					else
					{
						//blockInterior.add(Vector3.add(center, point));
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(IProjector projector, double x1, double y1, double z1, float f, long ticks)
	{
		float scale = 0.15f;
		GL11.glScalef(scale, scale, scale);

		float radius = 1.5f;
		int steps = (int) Math.ceil(Math.PI / Math.atan(1.0D / radius / 2));

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
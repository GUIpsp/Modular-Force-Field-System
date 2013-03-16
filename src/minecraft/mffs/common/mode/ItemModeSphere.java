package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.client.model.ModelCube;
import mffs.common.ZhuYao;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemModeSphere extends ItemProjectorMode
{
	public ItemModeSphere(int i)
	{
		super(i, "modeSphere");
	}

	@Override
	public void calculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior)
	{
		ForgeDirection direction = projector.getDirection(((TileEntity) projector).worldObj, ((TileEntity) projector).xCoord, ((TileEntity) projector).yCoord, ((TileEntity) projector).zCoord);

		int zTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		Vector3 translation = new Vector3(xTranslationPos - xTranslationNeg, yTranslationPos - yTranslationNeg, zTranslationPos - zTranslationNeg);

		int radius = projector.getModuleCount(ZhuYao.itemModuleScale, 14, 15) + 4;

		int yDown = radius;

		if (projector.getModuleCount(ZhuYao.itemModuleManipulator) > 0)
		{
			yDown = 0;
		}

		for (int x = -radius; x <= radius; x++)
		{
			for (int z = -radius; z <= radius; z++)
			{
				for (int y = -yDown; y <= radius; y++)
				{
					Vector3 checkPosition = new Vector3(x, y, z);
					double distance = Vector3.distance(new Vector3(), checkPosition);

					if (distance <= radius && distance > radius - 1)
					{
						blockDef.add(Vector3.add(translation, checkPosition));
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(IProjector projector, double x1, double y1, double z1, float f, long ticks)
	{
		GL11.glTranslatef(0, (float) Math.sin(Math.toRadians(ticks * 3)) / 6, 0);
		GL11.glRotatef(ticks * 4, 0, 1, 0);

		float scale = 0.25f;
		GL11.glScalef(scale, scale, scale);
		GL11.glRotatef(36f + ticks * 4, 0, 1, 1);
		GL11.glColor4f(1, 1, 1, 0.8f);

		int radius = 8;

		int steps = (int) Math.ceil(Math.PI / Math.atan(1.0D / radius));

		for (int phi_n = 0; phi_n < 2 * steps; phi_n++)
		{
			for (int theta_n = 0; theta_n < steps; theta_n++)
			{
				double phi = Math.PI * 2 / steps * phi_n;
				double theta = Math.PI / steps * theta_n;

				Vector3 vector = new Vector3(Math.sin(theta) * Math.cos(phi), Math.cos(theta), Math.sin(theta) * Math.sin(phi));
				GL11.glTranslated(vector.x, vector.y, vector.z);
				ModelCube.INSTNACE.render();
				GL11.glTranslated(-vector.x, -vector.y, -vector.z);
			}
		}
	}
}
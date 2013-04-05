package mffs.item.mode;

import java.util.Set;

import mffs.ModularForceFieldSystem;
import mffs.api.IProjector;
import mffs.muoxing.MPlane;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemProjectorModeTube extends ItemProjectorMode
{
	public ItemProjectorModeTube(int i)
	{
		super(i, "modeTube");
	}

	@Override
	public void doCalculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior, ForgeDirection direction, Vector3 translation, Vector3 posScale, Vector3 negScale)
	{
		boolean requireInterior = projector.getModuleCount(ModularForceFieldSystem.itemModuleFusion) > 0;

		for (float x = -negScale.intX(); x <= posScale.intX(); x += 0.5f)
		{
			for (float z = -negScale.intZ(); z <= posScale.intZ(); z += 0.5f)
			{
				for (float y = -negScale.intY(); y <= posScale.intY(); y += 0.5f)
				{
					if (!(direction == ForgeDirection.UP || direction == ForgeDirection.DOWN) && (y == -negScale.intY() || y == posScale.intY()))
					{
						blockDef.add(Vector3.add(translation, new Vector3(x, y, z)));
						continue;
					}

					if (!(direction == ForgeDirection.NORTH || direction == ForgeDirection.SOUTH) && (z == -negScale.intZ() || z == posScale.intZ()))
					{
						blockDef.add(Vector3.add(translation, new Vector3(x, y, z)));
						continue;
					}

					if (!(direction == ForgeDirection.WEST || direction == ForgeDirection.EAST) && (x == -negScale.intX() || x == posScale.intX()))
					{
						blockDef.add(Vector3.add(translation, new Vector3(x, y, z)));
						continue;
					}

					if (requireInterior)
						blockInterior.add(Vector3.add(translation, new Vector3(x, y, z)));
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(IProjector projector, double x, double y, double z, float f, long ticks)
	{
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		GL11.glTranslatef(-0.5f, 0, 0);
		MPlane.INSTNACE.render();
		GL11.glTranslatef(1f, 0, 0);
		MPlane.INSTNACE.render();
		GL11.glTranslatef(-0.5f, 0f, 0);
		GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(0.5f, 0f, 0f);
		MPlane.INSTNACE.render();
		GL11.glTranslatef(-1f, 0f, 0f);
		MPlane.INSTNACE.render();
	}
}
package mffs.client.render;

import mffs.common.ZhuYao;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RenderProjector extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float f)
	{
		if (t instanceof TileEntityProjector)
		{
			TileEntityProjector tileEntity = (TileEntityProjector) t;
			this.bindTextureByName(ZhuYao.MODEL_DIRECTORY + "force_cube.png");
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5f, (float) y + 1.4f, (float) z + 0.5f);

			if (tileEntity.getMode() != null)
			{
				/**
				 * Enable Blending
				 */
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				/**
				 * Disable Lighting
				 */
				RenderHelper.disableStandardItemLighting();
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

				tileEntity.getMode().render(tileEntity, x, y, z, f, tileEntity.getTicks());

				/**
				 * Disable Blending
				 */
				GL11.glShadeModel(GL11.GL_FLAT);
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
				GL11.glDisable(GL11.GL_BLEND);
			}

			GL11.glPopMatrix();
		}
	}
}
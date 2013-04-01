package mffs.quanran;

import java.util.Random;

import mffs.ZhuYao;
import mffs.jiqi.t.TFangYingJi;
import mffs.quanran.muoxing.MFangYingJi;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RFangYingJi extends TileEntitySpecialRenderer
{
	public static final MFangYingJi MODEL = new MFangYingJi();

	@Override
	public void renderTileEntityAt(TileEntity t, double x, double y, double z, float f)
	{
		if (t instanceof TFangYingJi)
		{
			TFangYingJi tileEntity = (TFangYingJi) t;

			/**
			 * Render Model
			 */
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
			this.bindTextureByName(ZhuYao.MODEL_DIRECTORY + tileEntity.getBlockType().getUnlocalizedName2() + ".png");
			GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
			MODEL.render(0.0625F);
			GL11.glPopMatrix();

			if (tileEntity.getMode() != null)
			{
				/**
				 * Render Projection
				 */
				Tessellator tessellator = Tessellator.instance;

				RenderHelper.disableStandardItemLighting();
				GL11.glPushMatrix();
				GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_CULL_FACE);
				GL11.glDepthMask(false);
				GL11.glPushMatrix();

				tessellator.startDrawing(6);
				float height = 2;
				float width = 2;
				tessellator.setColorRGBA(72, 198, 255, 255);
				tessellator.addVertex(0.0D, 0.0D, 0.0D);
				tessellator.setColorRGBA_I(0, 0);
				tessellator.addVertex(-0.866D * width, height, -0.5F * width);
				tessellator.addVertex(0.866D * width, height, -0.5F * width);
				tessellator.addVertex(0.0D, height, 1.0F * width);
				tessellator.addVertex(-0.866D * width, height, -0.5F * width);
				tessellator.draw();

				GL11.glPopMatrix();
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glShadeModel(GL11.GL_FLAT);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				RenderHelper.enableStandardItemLighting();
				GL11.glPopMatrix();

				/**
				 * Render Hologram
				 */
				GL11.glPushMatrix();
				GL11.glTranslated(x + 0.5, y + 1.4, z + 0.5);
				this.bindTextureByName(ZhuYao.MODEL_DIRECTORY + "force_cube.png");

				// Enable Blending
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				// Disable Lighting/Glow On
				RenderHelper.disableStandardItemLighting();
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

				GL11.glPushMatrix();
				GL11.glColor4f(1, 1, 1, (float) Math.sin((double) tileEntity.getTicks() / 10) / 2 + 1);
				GL11.glTranslatef(0, (float) Math.sin(Math.toRadians(tileEntity.getTicks() * 3)) / 6, 0);
				GL11.glRotatef(tileEntity.getTicks() * 4, 0, 1, 0);
				GL11.glRotatef(36f + tileEntity.getTicks() * 4, 0, 1, 1);
				tileEntity.getMode().render(tileEntity, x, y, z, f, tileEntity.getTicks());
				GL11.glPopMatrix();

				// Enable Lighting/Glow Off
				RenderHelper.enableStandardItemLighting();

				// Disable Blending
				GL11.glShadeModel(GL11.GL_FLAT);
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
				GL11.glDisable(GL11.GL_BLEND);

				GL11.glPopMatrix();
			}
		}
	}
}
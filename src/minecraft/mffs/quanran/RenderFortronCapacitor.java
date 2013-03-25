package mffs.quanran;

import mffs.jiqi.t.TDianRong;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

public class RenderFortronCapacitor extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f)
	{
		if ((tileEntity instanceof TDianRong))
		{
			TDianRong topview = (TDianRong) tileEntity;
			GL11.glPushMatrix();
			GL11.glPolygonOffset(-10.0F, -10.0F);
			GL11.glEnable(32823);
			int side = topview.getDirection(tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord).ordinal();
			float dx = 0.0625F;
			float dz = 0.0625F;
			float displayWidth = 0.875F;
			float displayHeight = 0.875F;
			GL11.glTranslatef((float) x, (float) y, (float) z);

			// TODO: NEST THIS IN A CLASS AND ROTATE THE RENDER BASED ON WHERE THE PLAYER IS LOOKING
			// AT THE TILE ENTITY FROM.

			switch (side)
			{
				case 1:
				{
					break;
				}
				case 0:
					GL11.glTranslatef(1.0F, 1.0F, 0.0F);
					GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

					break;
				case 3:
					GL11.glTranslatef(0.0F, 1.0F, 0.0F);
					GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);

					break;
				case 2:
					GL11.glTranslatef(1.0F, 1.0F, 1.0F);
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);

					break;
				case 5:
					GL11.glTranslatef(0.0F, 1.0F, 1.0F);
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);

					break;
				case 4:
					GL11.glTranslatef(1.0F, 1.0F, 0.0F);
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			}

			GL11.glTranslatef(dx + displayWidth / 2.0F, 1.0F, dz + displayHeight / 2.0F);
			GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			FontRenderer fontRenderer = getFontRenderer();
			int maxWidth = 1;
			String header = "Fortron Capacitor";
			maxWidth = Math.max(fontRenderer.getStringWidth(header), maxWidth);
			maxWidth += 4;
			int lineHeight = fontRenderer.FONT_HEIGHT + 2;
			int requiredHeight = lineHeight * 1;
			float scaleX = displayWidth / maxWidth;
			float scaleY = displayHeight / requiredHeight;
			float scale = Math.min(scaleX, scaleY);
			GL11.glScalef(scale, -scale, scale);
			GL11.glDepthMask(false);

			int realHeight = (int) Math.floor(displayHeight / scale);
			int realWidth = (int) Math.floor(displayWidth / scale);
			int offsetY;
			int offsetX;

			if (scaleX < scaleY)
			{
				offsetX = 2;
				offsetY = (realHeight - requiredHeight) / 2;
			}
			else
			{
				offsetX = (realWidth - maxWidth) / 2 + 2;
				offsetY = 0;
			}
			GL11.glDisable(2896);
			fontRenderer.drawString(header, offsetX - realWidth / 2, 1 + offsetY - realHeight / 2 + -2 * lineHeight, 1);
			fontRenderer.drawString("Capacity: ", offsetX - realWidth / 2, 1 + offsetY - realHeight / 2 + 0 * lineHeight, 1);
			fontRenderer.drawString(String.valueOf(topview.getFortronEnergy()).concat(" % "), offsetX + realWidth / 2 - offsetX - fontRenderer.getStringWidth(String.valueOf(topview.getFortronEnergy()).concat(" % ")), offsetY - realHeight / 2 - 0 * lineHeight, 1);
			fontRenderer.drawString("Range: ", offsetX - realWidth / 2, 1 + offsetY - realHeight / 2 + 1 * lineHeight, 1);
			fontRenderer.drawString(String.valueOf(topview.getTransmissionRange()), offsetX + realWidth / 2 - offsetX - fontRenderer.getStringWidth(String.valueOf(topview.getTransmissionRange())), offsetY - realHeight / 2 + 1 * lineHeight, 1);
			fontRenderer.drawString("Linked Device: ", offsetX - realWidth / 2, 1 + offsetY - realHeight / 2 + 2 * lineHeight, 1);
			// fontRenderer.drawString(String.valueOf(topview.getLinkedProjector()), offsetX +
			// realWidth / 2 - offsetX -
			// fontRenderer.getStringWidth(String.valueOf(topview.getLinkedProjector())), offsetY -
			// realHeight / 2 + 2 * lineHeight, 1);
			GL11.glEnable(2896);
			GL11.glDepthMask(true);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glDisable(32823);
			GL11.glPopMatrix();
		}
	}
}
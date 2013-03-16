package mffs.client.render;

import mffs.common.ZhuYao;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.model.ModelRenderer;
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
			GL11.glPushMatrix();
			GL11.glTranslatef((float) x + 0.5f, (float) y + 1.4f, (float) z + 0.5f);

			if (tileEntity.getMode() != null)
			{
				this.bindTextureByName(ZhuYao.BLOCK_DIRECTORY + "fortron.png");
				tileEntity.getMode().render(tileEntity, x, y, z, f, tileEntity.getTicks());
			}

			GL11.glPopMatrix();
		}
	}
}
package mffs.client.gui;

import net.minecraft.client.Minecraft;
import mffs.common.tileentity.TileEntityFortronCapacitor;

public class GuiButtonTransferMode extends GuiButtonMFFS
{
	private TileEntityFortronCapacitor tileEntity;

	public GuiButtonTransferMode(int id, int x, int y, TileEntityFortronCapacitor tileEntity)
	{
		super(id, x, y);
		this.tileEntity = tileEntity;
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y)
	{
		super.drawButton(minecraft, x, y);
		this.offset.y = 18 * this.tileEntity.getTransferMode().ordinal();
	}
}

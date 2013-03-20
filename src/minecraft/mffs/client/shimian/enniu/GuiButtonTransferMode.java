package mffs.client.shimian.enniu;

import mffs.common.tileentity.TDianRong;
import net.minecraft.client.Minecraft;

public class GuiButtonTransferMode extends GuiButtonMFFS
{
	private TDianRong tileEntity;

	public GuiButtonTransferMode(int id, int x, int y, TDianRong tileEntity)
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

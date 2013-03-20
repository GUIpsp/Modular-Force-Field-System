package mffs.client.shimian.enniu;

import net.minecraft.client.Minecraft;
import mffs.common.tileentity.TDianRong;

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

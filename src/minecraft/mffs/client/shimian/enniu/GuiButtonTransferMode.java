package mffs.client.shimian.enniu;

import mffs.client.shimian.GuiMFFS;
import mffs.common.tileentity.TDianRong;
import net.minecraft.client.Minecraft;
import universalelectricity.core.vector.Vector2;

public class GuiButtonTransferMode extends GuiButtonMFFS
{
	private TDianRong tileEntity;

	public GuiButtonTransferMode(int id, int x, int y, GuiMFFS mainGui, TDianRong tileEntity)
	{
		super(id, x, y, new Vector2(), mainGui);
		this.tileEntity = tileEntity;
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y)
	{
		String transferName = this.tileEntity.getTransferMode().name().toLowerCase();
		char first = Character.toUpperCase(transferName.charAt(0));
		transferName = first + transferName.substring(1);
		this.displayString = "transferMode" + transferName;
		this.offset.y = 18 * this.tileEntity.getTransferMode().ordinal();

		super.drawButton(minecraft, x, y);
	}
}

package mffs.client.gui;

import mffs.common.SecurityRight;
import mffs.common.ZhuYao;
import mffs.common.container.ContainerSecurityStation;
import mffs.common.tileentity.TAnQuan;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.vector.Vector2;

public class GuiSecurityStation extends GuiMFFS
{
	private TAnQuan tileEntity;
	private SecurityRight hoverSR;
	private boolean editMode = false;

	public GuiSecurityStation(EntityPlayer player, TAnQuan tileEntity)
	{
		super(new ContainerSecurityStation(player, tileEntity), tileEntity);
		this.tileEntity = tileEntity;
		this.buttonList.clear();
	}

	@Override
	public void initGui()
	{
		this.textFieldPos = new Vector2(109, 92);
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 4), 6, 4210752);

		this.drawTextWithTooltip("rights", "%1", 85, 22, x, y, 0);

		if (this.tileEntity.getStackInSlot(0) != null)
		{
			this.fontRenderer.drawString("Bypass Force Field", 40, 32, 0);
			this.fontRenderer.drawString("Excempt from Defence", 40, 42, 0);
			this.fontRenderer.drawString("Access Security Blocks", 40, 52, 0);
			this.fontRenderer.drawString("Access Fortron Blocks", 40, 62, 0);
			this.fontRenderer.drawString("Access Secure Storage", 40, 72, 0);
		}

		this.textFieldFrequency.drawTextBox();

		this.drawTextWithTooltip("master", 28, 90 + (this.fontRenderer.FONT_HEIGHT / 2), x, y);

		super.drawGuiContainerForegroundLayer(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);

		this.drawSlot(7, 30);

		this.drawSlot(7, 90);

		// Internal Inventory
		for (int var4 = 0; var4 < 9; var4++)
		{
			this.drawSlot(8 + var4 * 18 - 1, 110);
		}

		this.drawPatch(ZhuYao.GUI_DIRECTORY + "sub_patch.png", this.containerWidth + 35, this.containerHeight + 20, 129, 63);
	}
}
package mffs.client.gui;

import mffs.client.GraphicButton;
import mffs.common.container.ContainerCapacitor;
import mffs.common.tileentity.TileEntityCapacitor;
import mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;

public class GuiCapacitor extends GuiMFFS
{
	private TileEntityCapacitor tileEntity;

	public GuiCapacitor(EntityPlayer player, TileEntityCapacitor tileentity)
	{
		super(new ContainerCapacitor(player, tileentity));
		this.tileEntity = tileentity;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.textFieldFrequency = new GuiTextField(this.fontRenderer, 30, 76, 60, 12);
		this.textFieldFrequency.setMaxStringLength(4);
		this.textFieldFrequency.setText("0");

		this.controlList.clear();
		this.controlList.add(new GraphicButton(0, this.width / 2 + 65, this.height / 2 - 100, this.tileEntity, 0));
		this.controlList.add(new GraphicButton(1, this.width / 2 + 5, this.height / 2 - 35, this.tileEntity, 1));

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);

		/**
		 * Upgrade Slots
		 */
		this.drawSlot(153, 46);
		this.drawSlot(153, 66);
		this.drawSlot(153, 86);

		/**
		 * Frequency Card Slot
		 */
		this.drawSlot(8, 73);

		this.drawEnergy(8, 115, 0);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), this.ySize / 2 - this.tileEntity.getInvName().length() * 5, 6, 4210752);

		this.fontRenderer.drawString("Linked Device: " + this.tileEntity.getLinketProjector(), 8, 30, 4210752);
		this.fontRenderer.drawString("Range: " + this.tileEntity.getTransmitRange(), 8, 45, 4210752);
		this.fontRenderer.drawString("Frequency:", 8, 60, 4210752);
		this.textFieldFrequency.drawTextBox();
		this.fontRenderer.drawString("Force Power: ", 8, 95, 4210752);
		this.fontRenderer.drawString(ElectricInfo.getDisplay(this.tileEntity.getStorageAvailablePower(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplay(this.tileEntity.getMaximumPower(), ElectricUnit.JOULES), 8, 105, 4210752);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		super.actionPerformed(guibutton);
		NetworkHandlerClient.fireTileEntityEvent(this.tileEntity, guibutton.id, "");
	}
}
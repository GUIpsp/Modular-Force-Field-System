package mffs.client.gui;

import mffs.client.GraphicButton;
import mffs.common.container.ContainerCapacitor;
import mffs.common.tileentity.TileEntityFortronCapacitor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.vector.Vector2;

public class GuiCapacitor extends GuiMFFS
{

	private TileEntityFortronCapacitor tileEntity;

	public GuiCapacitor(EntityPlayer player, TileEntityFortronCapacitor tileentity)
	{
		super(new ContainerCapacitor(player, tileentity));
		this.tileEntity = tileentity;
	}

	@Override
	public void initGui()
	{
		this.textFieldPos = new Vector2(30, 76);

		super.initGui();

		this.controlList.clear();
		this.controlList.add(new GraphicButton(0, this.width / 2 + 65, this.height / 2 - 100, this.tileEntity, 0));
		this.controlList.add(new GraphicButton(1, this.width / 2 + 5, this.height / 2 - 35, this.tileEntity, 1));

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 4), 6, 4210752);

		GL11.glPushMatrix();
		GL11.glRotatef(-90, 0, 0, 1);
		this.drawTextWithTooltip("upgrade", -95, 140, x, y);
		GL11.glPopMatrix();

		// this.drawTextWithTooltip("linkedDevice", "%1: " + this.tileEntity.getLinkedProjector(),
		// 8, 30, x, y);
		this.drawTextWithTooltip("range", "%1: " + this.tileEntity.getTransmitRange(), 8, 45, x, y);
		this.drawTextWithTooltip("frequency", "%1:", 8, 60, x, y);
		this.textFieldFrequency.drawTextBox();
		this.drawTextWithTooltip("fortron", "%1:", 8, 95, x, y);
		this.fontRenderer.drawString(ElectricInfo.getDisplayShort(this.tileEntity.getFortronEnergy(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplayShort(this.tileEntity.getFortronCapacity(), ElectricUnit.JOULES), 8, 105, 4210752);
		super.drawGuiContainerForegroundLayer(x, y);
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

		this.drawForce(8, 115, Math.min((float) this.tileEntity.getFortronEnergy() / (float) this.tileEntity.getFortronCapacity(), 1));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		super.actionPerformed(guibutton);
		// NetworkHandlerClient.fireTileEntityEvent(this.tileEntity, guibutton.id, "");
	}
}
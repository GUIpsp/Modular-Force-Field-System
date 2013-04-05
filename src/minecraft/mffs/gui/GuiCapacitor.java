package mffs.gui;

import mffs.ModularForceFieldSystem;
import mffs.container.ContainerCapacitor;
import mffs.gui.button.GuiButtonPressTransferMode;
import mffs.machine.tile.TileCapacitor;
import mffs.machine.tile.TileMFFS.TPacketType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.vector.Vector2;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiCapacitor extends GuiMFFS
{
	private TileCapacitor tileEntity;

	public GuiCapacitor(EntityPlayer player, TileCapacitor tileentity)
	{
		super(new ContainerCapacitor(player, tileentity), tileentity);
		this.tileEntity = tileentity;
	}

	@Override
	public void initGui()
	{
		this.textFieldPos = new Vector2(50, 76);
		super.initGui();
		this.buttonList.add(new GuiButtonPressTransferMode(1, this.width / 2 + 15, this.height / 2 - 37, this, this.tileEntity));

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), this.xSize / 2 - this.fontRenderer.getStringWidth(this.tileEntity.getInvName()) / 2, 6, 4210752);

		GL11.glPushMatrix();
		GL11.glRotatef(-90, 0, 0, 1);
		this.drawTextWithTooltip("upgrade", -95, 140, x, y);
		GL11.glPopMatrix();

		this.drawTextWithTooltip("linkedDevice", "%1: " + this.tileEntity.getLinkedDevices().size(), 8, 26, x, y);
		this.drawTextWithTooltip("transmissionRate", "%1: " + ElectricityDisplay.getDisplayShort(this.tileEntity.getTransmissionRate(), ElectricUnit.JOULES), 8, 38, x, y);
		this.drawTextWithTooltip("range", "%1: " + this.tileEntity.getTransmissionRange(), 8, 50, x, y);
		this.drawTextWithTooltip("frequency", "%1:", 8, 62, x, y);
		this.textFieldFrequency.drawTextBox();
		this.drawTextWithTooltip("fortron", "%1:", 8, 95, x, y);
		this.fontRenderer.drawString(ElectricityDisplay.getDisplayShort(this.tileEntity.getFortronEnergy(), ElectricUnit.JOULES) + "/" + ElectricityDisplay.getDisplayShort(this.tileEntity.getFortronCapacity(), ElectricUnit.JOULES), 8, 105, 4210752);
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
		 * Frequency Card Slots
		 */
		this.drawSlot(8, 73);
		this.drawSlot(26, 73);

		this.drawForce(8, 115, Math.min((float) this.tileEntity.getFortronEnergy() / (float) this.tileEntity.getFortronCapacity(), 1));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		super.actionPerformed(guibutton);

		if (guibutton.id == 1)
		{
			PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ModularForceFieldSystem.CHANNEL, this.tileEntity, TPacketType.TOGGLE_MODE.ordinal()));
		}
	}
}
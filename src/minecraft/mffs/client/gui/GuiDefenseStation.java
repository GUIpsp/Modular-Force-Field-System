package mffs.client.gui;

import mffs.common.ZhuYao;
import mffs.common.container.ContainerDefenseStation;
import mffs.common.tileentity.TileEntityDefenseStation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector2;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiDefenseStation extends GuiMFFS
{
	private TileEntityDefenseStation tileEntity;
	private boolean editMode = false;

	public GuiDefenseStation(EntityPlayer player, TileEntityDefenseStation tileEntity)
	{
		super(new ContainerDefenseStation(player, tileEntity), tileEntity);
		this.tileEntity = tileEntity;
		this.xSize = 256;
		this.ySize = 216;
	}

	@Override
	public void initGui()
	{
		this.textFieldPos = new Vector2(33, 29);
		super.initGui();

		this.buttonList.add(new GuiButtonMFFS(0, this.width / 2 + 107, this.height / 2 - 104));
		this.buttonList.add(new GuiButtonMFFS(1, this.width / 2 + 10, this.height / 2 - 58));
		this.buttonList.add(new GuiButtonMFFS(2, this.width / 2 + 47, this.height / 2 - 38));
	}

	@Override
	protected void actionPerformed(GuiButton guiButton)
	{
		super.actionPerformed(guiButton);
		
		switch (guiButton.id)
		{
			case 1:
				PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ZhuYao.CHANNEL, this.tileEntity, 3));
				break;
			case 2:
				PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ZhuYao.CHANNEL, this.tileEntity, 4));
				break;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), 7, 9, 4210752);

		switch (this.tileEntity.actionMode)
		{
			case WARN:
				this.fontRenderer.drawString("Warn", 110, 55, 4210752);
				this.fontRenderer.drawString(" send Info", 95, 85, 4210752);
				this.fontRenderer.drawString(" to player ", 95, 95, 4210752);
				this.fontRenderer.drawString(" without (SR)", 95, 105, 4210752);
				this.fontRenderer.drawString(" Stay Right", 95, 115, 4210752);
				break;
			case ASSASINATE:
				this.fontRenderer.drawString("Assasination", 110, 55, 4210752);

				this.fontRenderer.drawString(" kill player", 95, 85, 4210752);
				this.fontRenderer.drawString(" without (SR)", 95, 95, 4210752);
				this.fontRenderer.drawString(" gathers his", 95, 105, 4210752);
				this.fontRenderer.drawString(" equipment", 95, 115, 4210752);
				break;
			case CONFISCATE:
				this.fontRenderer.drawString("Confiscate", 95, 55, 4210752);

				this.fontRenderer.drawString("scans player", 95, 85, 4210752);
				this.fontRenderer.drawString("without (AAI)", 95, 95, 4210752);
				this.fontRenderer.drawString("and remove", 95, 105, 4210752);
				this.fontRenderer.drawString("banned items", 95, 115, 4210752);
				break;
			case ANTIBIOTIC:
				this.fontRenderer.drawString("NPC kill", 110, 55, 4210752);
				this.fontRenderer.drawString("kill any NPC", 95, 85, 4210752);
				this.fontRenderer.drawString("friendly or", 95, 95, 4210752);
				this.fontRenderer.drawString("hostile", 95, 105, 4210752);
				break;
			case ANTI_HOSTILE:
				this.fontRenderer.drawString("NPC kill", 110, 55, 4210752);

				this.fontRenderer.drawString("kill only", 95, 85, 4210752);
				this.fontRenderer.drawString("hostile NPCs", 95, 95, 4210752);
				break;
			case ANTI_FRIENDLY:
				this.fontRenderer.drawString("NPC kill", 110, 55, 4210752);

				this.fontRenderer.drawString("kill only", 95, 85, 4210752);
				this.fontRenderer.drawString("friendly NPCs", 95, 95, 4210752);
		}

		this.fontRenderer.drawString("Action Desc:", 95, 73, 139);

		this.fontRenderer.drawString("items", 205, 68, 2263842);

		if (!this.tileEntity.isBanMode())
		{
			this.fontRenderer.drawString("allowed", 200, 82, 2263842);
		}
		else
		{
			this.fontRenderer.drawString("banned", 200, 82, 16711680);
		}

		this.fontRenderer.drawString("Warning", 35, 55, 139);
		this.fontRenderer.drawString("Range: " + this.tileEntity.getWarnRange(), 12, 73, 4210752);

		this.fontRenderer.drawString("Action", 35, 91, 15612731);
		this.fontRenderer.drawString("Range: " + this.tileEntity.getActionRange(), 12, 111, 4210752);

		this.textFieldFrequency.drawTextBox();
		super.drawGuiContainerForegroundLayer(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		int xSize = 256;
		int ySize = 216;
		this.mc.renderEngine.func_98187_b(ZhuYao.GUI_DIRECTORY + "GuiDefStation.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, xSize, ySize);
	}

}
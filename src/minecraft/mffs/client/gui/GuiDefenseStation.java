package mffs.client.gui;

import mffs.common.ModularForceFieldSystem;
import mffs.common.container.ContainerAreaDefenseStation;
import mffs.common.tileentity.TileEntityDefenseStation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class GuiDefenseStation extends GuiMFFS
{
	private TileEntityDefenseStation DefenceStation;
	private boolean editMode = false;

	public GuiDefenseStation(EntityPlayer player, TileEntityDefenseStation tileEntity)
	{
		super(new ContainerAreaDefenseStation(player, tileEntity));
		this.DefenceStation = tileEntity;
		this.xSize = 256;
		this.ySize = 216;
	}

	@Override
	protected void mouseClicked(int i, int j, int k)
	{
		super.mouseClicked(i, j, k);

		int xMin = (this.width - this.xSize) / 2;
		int yMin = (this.height - this.ySize) / 2;

		int x = i - xMin;
		int y = j - yMin;

		if (this.editMode)
		{
			this.editMode = false;
		}
		else if ((x >= 115) && (y >= 5) && (x <= 233) && (y <= 19))
		{
			// NetworkHandlerClient.fireTileEntityEvent(this.DefenceStation, 10, "null");
			this.editMode = true;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		int xSize = 256;
		int ySize = 216;
		int textur = this.mc.renderEngine.getTexture(ModularForceFieldSystem.TEXTURE_DIRECTORY + "GuiDefStation.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textur);
		int w = (this.width - xSize) / 2;
		int k = (this.height - ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, xSize, ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		// NetworkHandlerClient.fireTileEntityEvent(this.DefenceStation, guibutton.id, "");
	}

	@Override
	public void initGui()
	{/*
	 * this.controlList.add(new GuiButtonMFFS(0, this.width / 2 + 107, this.height / 2 - 104,
	 * this.DefenceStation, 0)); this.controlList.add(new GuiButtonMFFS(100, this.width / 2 + 47,
	 * this.height / 2 - 38, this.DefenceStation, 1)); this.controlList.add(new GuiButtonMFFS(101,
	 * this.width / 2 - 36, this.height / 2 - 58, this.DefenceStation, 2)); this.controlList.add(new
	 * GuiButtonMFFS(102, this.width / 2 + 20, this.height / 2 - 58, this.DefenceStation, 3));
	 */
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("MFFS Defence Station", 7, 9, 4210752);
		// this.fontRenderer.drawString(this.DefenceStation.getDeviceName(), 120, 9, 4210752);
		// TODO: REMOVED NAME
		switch (this.DefenceStation.getActionmode())
		{
			case 0:
				this.fontRenderer.drawString("inform", 110, 55, 4210752);

				this.fontRenderer.drawString(" send Info", 95, 85, 4210752);
				this.fontRenderer.drawString(" to player ", 95, 95, 4210752);
				this.fontRenderer.drawString(" without (SR)", 95, 105, 4210752);
				this.fontRenderer.drawString(" Stay Right", 95, 115, 4210752);
				break;
			case 1:
				this.fontRenderer.drawString("kill", 110, 55, 4210752);

				this.fontRenderer.drawString(" kill player", 95, 85, 4210752);
				this.fontRenderer.drawString(" without (SR)", 95, 95, 4210752);
				this.fontRenderer.drawString(" gathers his", 95, 105, 4210752);
				this.fontRenderer.drawString(" equipment", 95, 115, 4210752);
				break;
			case 2:
				this.fontRenderer.drawString("search", 110, 55, 4210752);

				this.fontRenderer.drawString("scans player", 95, 85, 4210752);
				this.fontRenderer.drawString("without (AAI)", 95, 95, 4210752);
				this.fontRenderer.drawString("and remove", 95, 105, 4210752);
				this.fontRenderer.drawString("banned items", 95, 115, 4210752);
				break;
			case 3:
				this.fontRenderer.drawString("NPC kill", 110, 55, 4210752);
				this.fontRenderer.drawString("kill any NPC", 95, 85, 4210752);
				this.fontRenderer.drawString("friendly or", 95, 95, 4210752);
				this.fontRenderer.drawString("hostile", 95, 105, 4210752);
				break;
			case 4:
				this.fontRenderer.drawString("NPC kill", 110, 55, 4210752);

				this.fontRenderer.drawString("kill only", 95, 85, 4210752);
				this.fontRenderer.drawString("hostile NPCs", 95, 95, 4210752);
				break;
			case 5:
				this.fontRenderer.drawString("NPC kill", 110, 55, 4210752);

				this.fontRenderer.drawString("kill only", 95, 85, 4210752);
				this.fontRenderer.drawString("friendly NPCs", 95, 95, 4210752);
		}

		this.fontRenderer.drawString("Action desc:", 95, 73, 139);

		this.fontRenderer.drawString("items", 205, 68, 2263842);

		if (this.DefenceStation.getcontratyp() == 0)
		{
			this.fontRenderer.drawString("allowed", 200, 82, 2263842);
		}

		if (this.DefenceStation.getcontratyp() == 1)
		{
			this.fontRenderer.drawString("banned", 200, 82, 16711680);
		}

		// if (this.DefenceStation.getPowerSourceID() != 0)
		{
			this.fontRenderer.drawString("FE: " + this.DefenceStation.getCapacity() + " %", 35, 31, 4210752);
		}
		// else
		{
			this.fontRenderer.drawString("No Link/OOR", 35, 31, 16711680);
		}

		if (this.DefenceStation.hasSecurityCard())
		{
			this.fontRenderer.drawString("linked", 120, 31, 2263842);
		}

		this.fontRenderer.drawString("warning", 35, 55, 139);
		this.fontRenderer.drawString("perimeter: " + this.DefenceStation.getInfoDistance(), 12, 73, 4210752);

		this.fontRenderer.drawString("action", 35, 91, 15612731);
		this.fontRenderer.drawString("perimeter: " + this.DefenceStation.getActionDistance(), 12, 111, 4210752);

		this.fontRenderer.drawString("inventory ", 180, 195, 4210752);
	}
}
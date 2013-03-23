package mffs.client.shimian.enniu;

import mffs.client.shimian.GuiMFFS;
import mffs.common.ZhuYao;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector2;
import universalelectricity.prefab.TranslationHelper;

public class GuiButtonMFFS extends GuiButton
{
	protected Vector2 offset = new Vector2();

	/**
	 * Stuck determines if the button is hard pressed done, or disabled looking.
	 */
	public boolean stuck = false;
	private int type;
	private GuiMFFS mainGui;
	private String name;

	public GuiButtonMFFS(int id, int x, int y, Vector2 offset, GuiMFFS mainGui, String name)
	{
		super(id, x, y, 18, 18, name);
		this.offset = offset;
		this.mainGui = mainGui;
	}

	public GuiButtonMFFS(int id, int x, int y, Vector2 offset)
	{
		this(id, x, y, offset, null, "");
	}

	public GuiButtonMFFS(int id, int x, int y)
	{
		this(id, x, y, new Vector2());
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y)
	{
		if (this.drawButton)
		{
			Minecraft.getMinecraft().renderEngine.bindTexture(ZhuYao.GUI_BUTTON);

			if (this.stuck)
			{
				GL11.glColor4f(0.6f, 0.6f, 0.6f, 1);
			}
			else if (this.isPointInRegion(this.xPosition, this.yPosition, this.width, this.height, x, y))
			{
				GL11.glColor4f(0.85f, 0.85f, 0.85f, 1);
			}
			else
			{
				GL11.glColor4f(1, 1, 1, 1);
			}

			this.drawTexturedModalRect(this.xPosition, this.yPosition, this.offset.intX(), this.offset.intY(), this.width, this.height);
			this.mouseDragged(minecraft, x, y);
			/*
			 * 
			 * if (this.tileEntity instanceof TileEntityMFFS && this.type == 0) {
			 * drawTexturedModalRect(this.xPosition, this.yPosition, 80 + ((TileEntityMFFS)
			 * this.tileEntity).getStatusMode() * 16, 112, this.width, this.height); }
			 * 
			 * if ((this.tileEntity instanceof TileEntityConverter)) { if (this.type == 1) {
			 * drawTexturedModalRect(this.xPosition, this.yPosition, 80 + ((TileEntityConverter)
			 * this.tileEntity).getIC_Output() * 16, 128, this.width, this.height); } if (this.type
			 * == 2) { drawTexturedModalRect(this.xPosition, this.yPosition, 80 +
			 * ((TileEntityConverter) this.tileEntity).getUE_Output() * 16, 128, this.width,
			 * this.height); }
			 * 
			 * }
			 * 
			 * if ((this.tileEntity instanceof TileEntityControlSystem)) { if
			 * (((TileEntityControlSystem) this.tileEntity).getStackInSlot(1) != null) { if
			 * (this.type == 1) { if (((TileEntityControlSystem) this.tileEntity).getRemoteActive())
			 * { drawTexturedModalRect(this.xPosition, this.yPosition, 176, 80, this.width,
			 * this.height); }
			 * 
			 * if (!((TileEntityControlSystem) this.tileEntity).getRemoteActive()) {
			 * drawTexturedModalRect(this.xPosition, this.yPosition, 192, 80, this.width,
			 * this.height); } } if ((this.type == 2) && (((TileEntityControlSystem)
			 * this.tileEntity).getRemoteSwitchModi() > 0)) { drawTexturedModalRect(this.xPosition,
			 * this.yPosition, 80 + ((TileEntityControlSystem)
			 * this.tileEntity).getRemoteSwitchModi() * 16, 112, this.width, this.height); }
			 * 
			 * if ((this.type == 3) && (((TileEntityControlSystem)
			 * this.tileEntity).getRemoteSwitchModi() == 3)) { if (((TileEntityControlSystem)
			 * this.tileEntity).getRemoteSwitchValue()) { drawTexturedModalRect(this.xPosition,
			 * this.yPosition, 208, 80, this.width, this.height); } else {
			 * drawTexturedModalRect(this.xPosition, this.yPosition, 224, 80, this.width,
			 * this.height); } } }
			 * 
			 * }
			 * 
			 * if ((this.tileEntity instanceof TileEntityDefenseStation)) { if (this.type == 1) {
			 * drawTexturedModalRect(this.xPosition, this.yPosition, 176 +
			 * ((TileEntityDefenseStation) this.tileEntity).getcontratyp() * 16, 80, this.width,
			 * this.height); }
			 * 
			 * if (this.type == 2) { drawTexturedModalRect(this.xPosition, this.yPosition, 64 +
			 * ((TileEntityDefenseStation) this.tileEntity).getActionmode() * 16, 96, this.width,
			 * this.height); }
			 * 
			 * if (this.type == 3) { drawTexturedModalRect(this.xPosition, this.yPosition, 160 +
			 * ((TileEntityDefenseStation) this.tileEntity).getScanmode() * 16, 96, this.width,
			 * this.height); } } if ((this.tileEntity instanceof TileEntityFortronCapacitor)) { if
			 * (this.type == 1) { drawTexturedModalRect(this.xPosition, this.yPosition, 96 +
			 * ((TileEntityFortronCapacitor) this.tileEntity).getPowerLinkMode() * 16, 80,
			 * this.width, this.height); } } if ((this.tileEntity instanceof TileEntityProjector)) {
			 * if (this.type == 1) { drawTexturedModalRect(this.xPosition, this.yPosition, 0 +
			 * ((TileEntityProjector) this.tileEntity).getAccessType() * 16, 80, this.width,
			 * this.height); } }
			 */
		}
	}

	@Override
	protected void mouseDragged(Minecraft minecraft, int x, int y)
	{
		if (this.mainGui != null && this.displayString != null && this.displayString.length() > 0)
		{
			if (this.isPointInRegion(this.xPosition, this.yPosition, this.width, this.height, x, y))
			{
				this.mainGui.tooltip = TranslationHelper.getLocal("gui." + this.displayString + ".tooltip");
			}
		}
	}

	protected boolean isPointInRegion(int x, int y, int width, int height, int checkX, int checkY)
	{
		int var7 = 0;
		int var8 = 0;
		checkX -= var7;
		checkY -= var8;
		return checkX >= x - 1 && checkX < x + width + 1 && checkY >= y - 1 && checkY < y + height + 1;
	}
}
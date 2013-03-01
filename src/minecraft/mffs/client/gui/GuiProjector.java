package mffs.client.gui;

import mffs.client.GraphicButton;
import mffs.common.ModularForceFieldSystem;
import mffs.common.ProjectorTypes;
import mffs.common.container.ContainerProjector;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.vector.Vector2;

public class GuiProjector extends GuiMFFS
{
	private TileEntityProjector tileEntity;
	private boolean editMode = false;

	public GuiProjector(EntityPlayer player, TileEntityProjector tileEntity)
	{
		super(new ContainerProjector(player, tileEntity));
		this.tileEntity = tileEntity;
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
		else if ((x >= 10) && (y >= 5) && (x <= 141) && (y <= 19))
		{
			// NetworkHandlerClient.fireTileEntityEvent(this.projector, 10, "null");
			this.editMode = true;
		}
	}

	@Override
	public void initGui()
	{
		this.textFieldPos = new Vector2(30, 76);
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 4), 6, 4210752);
		//this.drawTextWithTooltip("upgrade", 120, 30, x, y);
		//this.drawTextWithTooltip("frequency", "%1:", 8, 60, x, y);
		//this.textFieldFrequency.drawTextBox();
		this.fontRenderer.drawString(ElectricInfo.getDisplayShort(this.tileEntity.getFortronEnergy(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplayShort(this.tileEntity.getFortronCapacity(), ElectricUnit.JOULES), 8, 105, 4210752);
		this.drawTextWithTooltip("fortron", "%1:", 8, 95, x, y);

		super.drawGuiContainerForegroundLayer(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);

		/**
		 * Upgrade Slots
		 */
		for (int drawX = 0; drawX < 4; drawX++)
		{
			for (int drawY = 0; drawY < 4; drawY++)
			{
				this.drawSlot(drawX * 18 + 60, drawY * 18 + 20);
			}
		}

		/**
		 * Frequency Card Slot
		 */
		//this.drawSlot(8, 73);

		this.drawForce(8, 115, Math.min((float) this.tileEntity.getFortronEnergy() / (float) this.tileEntity.getFortronCapacity(), 1));

		/*
		 * int textur = this.mc.renderEngine.getTexture(ModularForceFieldSystem.TEXTURE_DIRECTORY +
		 * "GuiProjector.png");
		 * 
		 * GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); this.mc.renderEngine.bindTexture(textur);
		 * 
		 * int w = (this.width - this.xSize) / 2; int k = (this.height - this.ySize) / 2;
		 * 
		 * drawTexturedModalRect(w, k, 0, 0, this.xSize, this.ySize); int i1 = 79 *
		 * this.tileEntity.getFortronCapacity() / 100; drawTexturedModalRect(w + 8, k + 91, 176, 0,
		 * i1 + 1, 79);
		 * 
		 * if (this.tileEntity.hasValidTypeMod()) { if
		 * (ProjectorTypes.typeFromItem(this.tileEntity.getType()).ProTyp != 7) {
		 * drawTexturedModalRect(w + 119, k + 63, 177, 143, 16, 16); }
		 * 
		 * if ((ProjectorTypes.typeFromItem(this.tileEntity.getType()).ProTyp != 4) &&
		 * (ProjectorTypes.typeFromItem(this.tileEntity.getType()).ProTyp != 2)) {
		 * drawTexturedModalRect(w + 155, k + 63, 177, 143, 16, 16); }
		 * 
		 * if ((ProjectorTypes.typeFromItem(this.tileEntity.getType()).ProTyp == 1) ||
		 * (ProjectorTypes.typeFromItem(this.tileEntity.getType()).ProTyp == 2) ||
		 * (ProjectorTypes.typeFromItem(this.tileEntity.getType()).ProTyp == 6) ||
		 * (ProjectorTypes.typeFromItem(this.tileEntity.getType()).ProTyp == 7) ||
		 * (ProjectorTypes.typeFromItem(this.tileEntity.getType()).ProTyp == 8)) {
		 * drawTexturedModalRect(w + 137, k + 28, 177, 143, 16, 16);
		 * 
		 * drawTexturedModalRect(w + 137, k + 62, 177, 143, 16, 16);
		 * 
		 * drawTexturedModalRect(w + 154, k + 45, 177, 143, 16, 16);
		 * 
		 * drawTexturedModalRect(w + 120, k + 45, 177, 143, 16, 16); }
		 * 
		 * if (this.tileEntity.hasOption(ModularForceFieldSystem.itemOptionCamouflage, true)) {
		 * drawTexturedModalRect(w + 137, k + 45, 177, 143, 16, 16); } }
		 */
	}

}
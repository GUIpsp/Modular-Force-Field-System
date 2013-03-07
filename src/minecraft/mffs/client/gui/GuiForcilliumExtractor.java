package mffs.client.gui;

import mffs.client.GuiButtonMFFS;
import mffs.common.container.ContainerForcilliumExtractor;
import mffs.common.tileentity.TileEntityForcilliumExtractor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.vector.Vector2;

public class GuiForcilliumExtractor extends GuiMFFS
{
	private TileEntityForcilliumExtractor tileEntity;
	private boolean editMode = false;

	public GuiForcilliumExtractor(EntityPlayer player, TileEntityForcilliumExtractor tileentity)
	{
		super(new ContainerForcilliumExtractor(player, tileentity), tileentity);
		this.tileEntity = tileentity;
	}

	@Override
	public void initGui()
	{
		this.textFieldPos = new Vector2(30, 43);
		super.initGui();
		this.controlList.clear();
		this.controlList.add(new GuiButtonMFFS(0, this.width / 2 + 65, this.height / 2 - 100, this.tileEntity, 0));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 3.5), 6, 4210752);

		this.drawTextWithTooltip("frequency", "%1:", 8, 30, x, y);
		this.textFieldFrequency.drawTextBox();

		GL11.glPushMatrix();
		GL11.glRotatef(-90, 0, 0, 1);
		this.drawTextWithTooltip("upgrade", -95, 140, x, y);
		GL11.glPopMatrix();

		this.drawTextWithTooltip("progress", "%1: " + (int) (100 - ((float) this.tileEntity.processTime / (float) this.tileEntity.REQUIRED_TIME) * 100) + "%", 8, 70, x, y);
		this.drawTextWithTooltip("fortron", "%1: " + ElectricInfo.getDisplayShort(this.tileEntity.getFortronEnergy(), ElectricUnit.JOULES), 8, 105, x, y);

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
		this.drawSlot(8, 40);

		/**
		 * Focillium Input
		 */
		this.drawSlot(8, 82);
		this.drawBar(30, 84, (float) this.tileEntity.processTime / (float) this.tileEntity.REQUIRED_TIME);

		/**
		 * Force Power Bar
		 */
		this.drawForce(8, 115, (float) this.tileEntity.getFortronEnergy() / (float) this.tileEntity.getFortronCapacity());
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		super.actionPerformed(guibutton);
		// NetworkHandlerClient.fireTileEntityEvent(this.tileEntity, guibutton.id, "");
	}
}
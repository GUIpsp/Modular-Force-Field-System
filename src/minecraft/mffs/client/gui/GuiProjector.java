package mffs.client.gui;

import mffs.common.container.ContainerProjector;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.vector.Vector2;

public class GuiProjector extends GuiMFFS
{
	private TileEntityProjector tileEntity;
	private boolean editMode = false;

	public GuiProjector(EntityPlayer player, TileEntityProjector tileEntity)
	{
		super(new ContainerProjector(player, tileEntity), tileEntity);
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui()
	{
		this.textFieldPos = new Vector2(100, 91);
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 3.5), 6, 4210752);
		this.drawTextWithTooltip("matrix", 32, 25, x, y);
		this.drawTextWithTooltip("frequency", "%1:", 8, 92, x, y);
		this.textFieldFrequency.drawTextBox();

		this.drawTextWithTooltip("fortron", "%1: " + ElectricInfo.getDisplayShort(this.tileEntity.getFortronEnergy(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplayShort(this.tileEntity.getFortronCapacity(), ElectricUnit.JOULES), 8, 110, x, y);
		super.drawGuiContainerForegroundLayer(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);

		/**
		 * Frequency Card Slot
		 */
		this.drawSlot(75, 88);

		/**
		 * Matrix Slots
		 */

		// Mode
		this.drawSlot(18 + 90, 18 + 25);

		// Up
		this.drawSlot(18 + 80, 20, SlotType.ARR_UP);
		this.drawSlot(18 + 100, 20, SlotType.ARR_UP);

		// Left
		this.drawSlot(18 + 60, 35, SlotType.ARR_LEFT);
		this.drawSlot(18 + 60, 55, SlotType.ARR_LEFT);

		// Right
		this.drawSlot(138, 35, SlotType.ARR_RIGHT);
		this.drawSlot(138, 55, SlotType.ARR_RIGHT);

		// Down
		this.drawSlot(18 + 80, 66, SlotType.ARR_DOWN);
		this.drawSlot(18 + 100, 66, SlotType.ARR_DOWN);

		/**
		 * Up and Down
		 */
		this.drawSlot(55, 40, SlotType.ARR_UP);
		this.drawSlot(55, 60, SlotType.ARR_DOWN);
		this.drawSlot(37, 40, SlotType.ARR_UP);
		this.drawSlot(37, 60, SlotType.ARR_DOWN);

		/**
		 * Upgrades
		 */
		this.drawSlot(17, 40);
		this.drawSlot(17, 60);

		/**
		 * Fortron Bar
		 */
		this.drawForce(8, 120, Math.min((float) this.tileEntity.getFortronEnergy() / (float) this.tileEntity.getFortronCapacity(), 1));
	}

}
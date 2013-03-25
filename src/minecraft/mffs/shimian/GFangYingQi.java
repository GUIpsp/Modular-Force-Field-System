package mffs.shimian;

import mffs.jiqi.t.TFangYingJi;
import mffs.rongqi.CFangYingJi;
import net.minecraft.entity.player.EntityPlayer;
import universalelectricity.core.electricity.ElectricityDisplay;
import universalelectricity.core.electricity.ElectricityDisplay.ElectricUnit;
import universalelectricity.core.vector.Vector2;

public class GFangYingQi extends GuiMFFS
{
	private TFangYingJi tileEntity;

	public GFangYingQi(EntityPlayer player, TFangYingJi tileEntity)
	{
		super(new CFangYingJi(player, tileEntity), tileEntity);
		this.tileEntity = tileEntity;
	}

	@Override
	public void initGui()
	{
		this.textFieldPos = new Vector2(48, 91);
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), this.xSize / 2 - this.fontRenderer.getStringWidth(this.tileEntity.getInvName()) / 2, 6, 4210752);
		this.drawTextWithTooltip("matrix", 32, 20, x, y);
		this.drawTextWithTooltip("frequency", "%1:", 8, 76, x, y);
		this.textFieldFrequency.drawTextBox();

		this.drawTextWithTooltip("fortron", "%1: " + ElectricityDisplay.getDisplayShort(this.tileEntity.getFortronEnergy(), ElectricUnit.JOULES) + "/" + ElectricityDisplay.getDisplayShort(this.tileEntity.getFortronCapacity(), ElectricUnit.JOULES), 8, 110, x, y);
		this.fontRenderer.drawString("\u00a74-" + ElectricityDisplay.getDisplayShort(this.tileEntity.getFortronCost(), ElectricUnit.JOULES), 120, 121, 4210752);
		super.drawGuiContainerForegroundLayer(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);

		// Frequency Card Slot
		this.drawSlot(9, 88);
		this.drawSlot(9 + 18, 88);

		/**
		 * Matrix Slots
		 */

		// Mode
		this.drawSlot(117, 44, SlotType.NONE, 1f, 0.4f, 0.4f);

		// Directional Modules
		for (int xSlot = 0; xSlot < 4; xSlot++)
		{
			for (int ySlot = 0; ySlot < 4; ySlot++)
			{
				if (!(xSlot == 1 && ySlot == 1) && !(xSlot == 2 && ySlot == 2) && !(xSlot == 1 && ySlot == 2) && !(xSlot == 2 && ySlot == 1))
				{
					SlotType type = SlotType.NONE;

					if (xSlot == 0 && ySlot == 0)
					{
						type = SlotType.ARR_UP_LEFT;
					}
					else if (xSlot == 0 && ySlot == 3)
					{
						type = SlotType.ARR_DOWN_LEFT;
					}
					else if (xSlot == 3 && ySlot == 0)
					{
						type = SlotType.ARR_UP_RIGHT;
					}
					else if (xSlot == 3 && ySlot == 3)
					{
						type = SlotType.ARR_DOWN_RIGHT;
					}
					else if (ySlot == 0)
					{
						type = SlotType.ARR_UP;
					}
					else if (ySlot == 3)
					{
						type = SlotType.ARR_DOWN;
					}
					else if (xSlot == 0)
					{
						type = SlotType.ARR_LEFT;
					}
					else if (xSlot == 3)
					{
						type = SlotType.ARR_RIGHT;
					}

					this.drawSlot(90 + 18 * xSlot, 17 + 18 * ySlot, type);
				}
			}
		}

		// Upgrades
		for (int xSlot = 0; xSlot < 3; xSlot++)
		{
			for (int ySlot = 0; ySlot < 2; ySlot++)
			{
				this.drawSlot(18 + 18 * xSlot, 35 + 18 * ySlot);
			}
		}

		// Fortron Bar
		this.drawForce(8, 120, Math.min((float) this.tileEntity.getFortronEnergy() / (float) this.tileEntity.getFortronCapacity(), 1));
	}
}
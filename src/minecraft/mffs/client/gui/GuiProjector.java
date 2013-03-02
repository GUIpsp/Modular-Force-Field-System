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
	public void initGui()
	{
		this.textFieldPos = new Vector2(100, 91);
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 3.5), 6, 4210752);
		this.drawTextWithTooltip("matrix", 75, 20, x, y);
		this.drawTextWithTooltip("frequency", "%1:", 8, 92, x, y);
		this.textFieldFrequency.drawTextBox();
		
		this.drawTextWithTooltip("fortron", "%1: "+ElectricInfo.getDisplayShort(this.tileEntity.getFortronEnergy(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplayShort(this.tileEntity.getFortronCapacity(), ElectricUnit.JOULES), 8, 110, x, y);
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
		
		//Mode
		this.drawSlot(1 * 18 + 80, 1 * 18 + 30);

		//Up
		this.drawSlot(1 * 18 + 80, 0 * 18 + 30,  SlotType.ARR_UP);
		this.drawSlot(1 * 18 + 80, 0 * 18 + 30,  SlotType.ARR_UP);

		/*
		for (int drawX = 0; drawX < 3; drawX++)
		{
			for (int drawY = 0; drawY < 3; drawY++)
			{
				SlotType type = SlotType.NONE;

				if (drawX == 2 && drawY == 1)
				{
					type = SlotType.ARR_RIGHT;
				}
				else if (drawX == 0 && drawY == 1)
				{
					type = SlotType.ARR_LEFT;
				}
				else if (drawX == 1 && drawY == 0)
				{
					type = SlotType.ARR_UP;
				}
				else if (drawX == 1 && drawY == 2)
				{
					type = SlotType.ARR_DOWN;
				}

				this.drawSlot(drawX * 18 + 80, drawY * 18 + 30, type);
			}
		}*/

		/**
		 * Up and Down
		 */
		this.drawSlot(55, 40, SlotType.ARR_UP);
		this.drawSlot(55, 60, SlotType.ARR_DOWN);

		/**
		 * Fortron Bar
		 */
		this.drawForce(8, 120, Math.min((float) this.tileEntity.getFortronEnergy() / (float) this.tileEntity.getFortronCapacity(), 1));
	}

}
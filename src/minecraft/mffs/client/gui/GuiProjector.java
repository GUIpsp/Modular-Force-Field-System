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
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 3.5), 6, 4210752);
		this.drawTextWithTooltip("matrix", 120, 20, x, y);
		this.drawTextWithTooltip("frequency", "%1:", 8, 60, x, y);
		this.textFieldFrequency.drawTextBox();
		this.fontRenderer.drawString(ElectricInfo.getDisplayShort(this.tileEntity.getFortronEnergy(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplayShort(this.tileEntity.getFortronCapacity(), ElectricUnit.JOULES), 8, 105, 4210752);
		this.drawTextWithTooltip("fortron", "%1:", 8, 95, x, y);

		super.drawGuiContainerForegroundLayer(x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);

		/**
		 * Matrix Slots
		 */
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

				this.drawSlot(drawX * 18 + 115, drawY * 18 + 30, type);
			}
		}

		this.drawSlot(90, 30, SlotType.ARR_UP);
		this.drawSlot(90, 50, SlotType.ARR_DOWN);

		/**
		 * Frequency Card Slot
		 */
		this.drawSlot(8, 73);
		this.drawForce(8, 115, Math.min((float) this.tileEntity.getFortronEnergy() / (float) this.tileEntity.getFortronCapacity(), 1));
	}

}
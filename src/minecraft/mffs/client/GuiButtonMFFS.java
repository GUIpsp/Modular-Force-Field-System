package mffs.client;

import mffs.client.gui.GuiMFFS;
import mffs.common.ModularForceFieldSystem;
import mffs.common.tileentity.TileEntityControlSystem;
import mffs.common.tileentity.TileEntityConverter;
import mffs.common.tileentity.TileEntityDefenseStation;
import mffs.common.tileentity.TileEntityFortronCapacitor;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

public class GuiButtonMFFS extends GuiButton
{
	private GuiMFFS mainGui;
	private int type;

	public GuiButtonMFFS(int par1, int par2, int par3, GuiMFFS mainGui, int type)
	{
		super(par1, par2, par3, 16, 16, "");
		this.mainGui = mainGui;
		this.type = type;
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y)
	{
		if (this.drawButton)
		{
			GL11.glBindTexture(3553, minecraft.renderEngine.getTexture(ModularForceFieldSystem.ITEM_TEXTURE_FILE));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			if (this.tileEntity instanceof TileEntityMFFS && this.type == 0)
			{
				drawTexturedModalRect(this.xPosition, this.yPosition, 80 + ((TileEntityMFFS) this.tileEntity).getStatusMode() * 16, 112, this.width, this.height);
			}

			if ((this.tileEntity instanceof TileEntityConverter))
			{
				if (this.type == 1)
				{
					drawTexturedModalRect(this.xPosition, this.yPosition, 80 + ((TileEntityConverter) this.tileEntity).getIC_Output() * 16, 128, this.width, this.height);
				}
				if (this.type == 2)
				{
					drawTexturedModalRect(this.xPosition, this.yPosition, 80 + ((TileEntityConverter) this.tileEntity).getUE_Output() * 16, 128, this.width, this.height);
				}

			}

			if ((this.tileEntity instanceof TileEntityControlSystem))
			{
				if (((TileEntityControlSystem) this.tileEntity).getStackInSlot(1) != null)
				{
					if (this.type == 1)
					{
						if (((TileEntityControlSystem) this.tileEntity).getRemoteActive())
						{
							drawTexturedModalRect(this.xPosition, this.yPosition, 176, 80, this.width, this.height);
						}

						if (!((TileEntityControlSystem) this.tileEntity).getRemoteActive())
						{
							drawTexturedModalRect(this.xPosition, this.yPosition, 192, 80, this.width, this.height);
						}
					}
					if ((this.type == 2) && (((TileEntityControlSystem) this.tileEntity).getRemoteSwitchModi() > 0))
					{
						drawTexturedModalRect(this.xPosition, this.yPosition, 80 + ((TileEntityControlSystem) this.tileEntity).getRemoteSwitchModi() * 16, 112, this.width, this.height);
					}

					if ((this.type == 3) && (((TileEntityControlSystem) this.tileEntity).getRemoteSwitchModi() == 3))
					{
						if (((TileEntityControlSystem) this.tileEntity).getRemoteSwitchValue())
						{
							drawTexturedModalRect(this.xPosition, this.yPosition, 208, 80, this.width, this.height);
						}
						else
						{
							drawTexturedModalRect(this.xPosition, this.yPosition, 224, 80, this.width, this.height);
						}
					}
				}

			}

			if ((this.tileEntity instanceof TileEntityDefenseStation))
			{
				if (this.type == 1)
				{
					drawTexturedModalRect(this.xPosition, this.yPosition, 176 + ((TileEntityDefenseStation) this.tileEntity).getcontratyp() * 16, 80, this.width, this.height);
				}

				if (this.type == 2)
				{
					drawTexturedModalRect(this.xPosition, this.yPosition, 64 + ((TileEntityDefenseStation) this.tileEntity).getActionmode() * 16, 96, this.width, this.height);
				}

				if (this.type == 3)
				{
					drawTexturedModalRect(this.xPosition, this.yPosition, 160 + ((TileEntityDefenseStation) this.tileEntity).getScanmode() * 16, 96, this.width, this.height);
				}
			}
			if ((this.tileEntity instanceof TileEntityFortronCapacitor))
			{
				if (this.type == 1)
				{
					drawTexturedModalRect(this.xPosition, this.yPosition, 96 + ((TileEntityFortronCapacitor) this.tileEntity).getPowerLinkMode() * 16, 80, this.width, this.height);
				}
			}
			if ((this.tileEntity instanceof TileEntityProjector))
			{
				if (this.type == 1)
				{
					drawTexturedModalRect(this.xPosition, this.yPosition, 0 + ((TileEntityProjector) this.tileEntity).getAccessType() * 16, 80, this.width, this.height);
				}
			}
		}
	}

	@Override
	protected void mouseDragged(Minecraft minecraft, int x, int y)
	{
		System.out.println("DRAGGED!");
	}
}
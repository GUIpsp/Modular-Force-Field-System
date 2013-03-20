package mffs.client.shimian;

import mffs.client.shimian.enniu.GuiButtonMFFS;
import mffs.common.ZhuYao;
import mffs.common.container.ContainerControlSystem;
import mffs.common.tileentity.TKongZhi;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GKongZhi extends GuiMFFS
{
	private TKongZhi ControlSystem;
	private boolean editMode = false;
	private EntityPlayer player;

	public GKongZhi(EntityPlayer player, TKongZhi tileEntity)
	{
		super(new ContainerControlSystem(player, tileEntity), tileEntity);
		this.ControlSystem = tileEntity;
		this.xSize = 256;
		this.ySize = 216;
		this.player = player;
	}

	@Override
	protected void keyTyped(char c, int i)
	{
		if ((i != 1) && (this.editMode))
		{
			if (c == '\r')
			{
				this.editMode = false;
				return;
			}

			if (i == 14)
			{
				// NetworkHandlerClient.fireTileEntityEvent(this.ControlSystem, 12, "");
			}
			if ((i != 54) && (i != 42) && (i != 58) && (i != 14))
			{
				// NetworkHandlerClient.fireTileEntityEvent(this.ControlSystem, 11,
				// String.valueOf(c));
			}
		}
		else
		{
			super.keyTyped(c, i);
		}
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
		else if ((x >= 115) && (y >= 5) && (x <= 234) && (y <= 19))
		{
			// NetworkHandlerClient.fireTileEntityEvent(this.ControlSystem, 10, "null");
			this.editMode = true;
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		this.mc.renderEngine.func_98187_b(ZhuYao.TEXTURE_DIRECTORY + "GuiControlSystem.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int w = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if (guibutton.id == 103)
		{
			// NetworkHandlerClient.fireTileEntityEvent(this.ControlSystem, guibutton.id,
			// this.player.username);
		}
		else
		{
			// NetworkHandlerClient.fireTileEntityEvent(this.ControlSystem, guibutton.id, "");
		}
	}

	@Override
	public void initGui()
	{
		this.buttonList.add(new GuiButtonMFFS(100, this.width / 2 - 115, this.height / 2 - 45));
		this.buttonList.add(new GuiButtonMFFS(101, this.width / 2 - 115, this.height / 2 - 25));
		this.buttonList.add(new GuiButtonMFFS(102, this.width / 2 - 115, this.height / 2 - 5));
		this.buttonList.add(new GuiButton(103, this.width / 2 + -65, this.height / 2 - 8, 100, 20, "Open Remote Gui"));
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("MFFS Control System", 8, 9, 4210752);
		// this.fontRenderer.drawString(this.ControlSystem.getDeviceName(), 123, 9, 4210752);
		// TODO: REMOVED NAME
		this.fontRenderer.drawString("DataLink", 190, 54, 4210752);
		this.fontRenderer.drawString("Reader", 190, 65, 4210752);

		this.fontRenderer.drawString("Name: " + this.ControlSystem.getRemoteDeviceName(), 15, 30, 4210752);
		this.fontRenderer.drawString("Type:  " + this.ControlSystem.getRemoteDeviceTyp(), 15, 45, 4210752);
		if (this.ControlSystem.getStackInSlot(1) != null)
		{
			RenderItem renderItem = new RenderItem();
			RenderHelper.enableGUIStandardItemLighting();
			renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ZhuYao.itemCardSecurityLink), 40, 59);
			renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ZhuYao.itemCardPowerLink), 100, 59);
			RenderHelper.disableStandardItemLighting();
			if (this.ControlSystem.getRemoteSecurityStationlink())
			{
				this.fontRenderer.drawString("linked", 60, 64, 2263842);
			}
			else
			{
				this.fontRenderer.drawString("linked", 60, 64, 9116186);
			}
			if (this.ControlSystem.getRemotehasPowersource())
			{
				this.fontRenderer.drawString("linked", 120, 64, 2263842);
				this.fontRenderer.drawString("Power left: " + this.ControlSystem.getRemotePowerleft() + "%", 40, 80, 4210752);
			}
			else
			{
				this.fontRenderer.drawString("linked", 120, 64, 9116186);
			}
			if (this.ControlSystem.getRemoteGUIinRange())
			{
				this.fontRenderer.drawString("OK", 40, 107, 2263842);
			}
			else
			{
				this.fontRenderer.drawString("OOR", 40, 107, 9116186);
			}
		}
	}
}
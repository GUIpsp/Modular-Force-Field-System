package mffs.client.gui;

import mffs.client.GuiButtonMFFS;
import mffs.common.ModularForceFieldSystem;
import mffs.common.container.ContainerSecStorage;
import mffs.common.tileentity.TileEntitySecStorage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class GuiSecStorage extends GuiMFFS
{
	private TileEntitySecStorage SecStorage;

	public GuiSecStorage(EntityPlayer player, TileEntitySecStorage tileEntity)
	{
		super(new ContainerSecStorage(player, tileEntity));
		this.SecStorage = tileEntity;
		this.xSize = 185;
		this.ySize = 238;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		// NetworkHandlerClient.fireTileEntityEvent(this.SecStorage, guibutton.id, "");
	}

	@Override
	public void initGui()
	{
		this.controlList.add(new GuiButtonMFFS(0, this.width / 2 + 65, this.height / 2 - 113, this, 0));
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		int textur = this.mc.renderEngine.getTexture(ModularForceFieldSystem.TEXTURE_DIRECTORY + "GuiSecStorage.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(textur);
		int w = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;
		drawTexturedModalRect(w, k, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		// this.fontRenderer.drawString(this.SecStorage.getDeviceName(), 12, 9, 4210752);
		this.fontRenderer.drawString("MFFS Security Storage", 38, 28, 4210752);
	}
}
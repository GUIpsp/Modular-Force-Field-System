package mffs.client.gui;

import mffs.client.GraphicButton;
import mffs.common.MFFSConfiguration;
import mffs.common.ModularForceFieldSystem;
import mffs.common.container.ContainerForcilliumExtractor;
import mffs.common.tileentity.TileEntityExtractor;
import mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;

public class GuiForcilliumExtractor extends GuiMFFS
{
	private TileEntityExtractor tileEntity;
	private boolean editMode = false;

	public GuiForcilliumExtractor(EntityPlayer player, TileEntityExtractor tileentity)
	{
		super(new ContainerForcilliumExtractor(player, tileentity));
		this.tileEntity = tileentity;
	}

	@Override
	public void initGui()
	{
		this.controlList.add(new GraphicButton(0, this.width / 2 + 60, this.height / 2 - 88, this.tileEntity, 0));
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 3.5), 6, 4210752);

		GL11.glPushMatrix();
		GL11.glRotatef(-90, 0, 0, 1);
		this.drawTextWithTooltip("upgrades", 10, 50, x, y);
		GL11.glPopMatrix();

		this.drawTextWithTooltip("progress", "%1:" + this.tileEntity.getWorkDone() + "%", 8, 80, x, y);
		this.drawTextWithTooltip("forcePower", "%1:" + ElectricInfo.getDisplay(this.tileEntity.getForceEnergybuffer(), ElectricUnit.JOULES), 8, 105, x, y);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);

		this.drawSlot(153, 46);

		this.drawElectricity(8, 90, this.tileEntity.getWorkCycle() / MFFSConfiguration.ForcilliumWorkCylce);
		this.drawForce(8, 115, this.tileEntity.getForceEnergybuffer() / this.tileEntity.getMaxForceEnergyBuffer());

		/*
		 * int textur = this.mc.renderEngine.getTexture(ModularForceFieldSystem.TEXTURE_DIRECTORY +
		 * "GuiExtractor.png");
		 * 
		 * GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); this.mc.renderEngine.bindTexture(textur); int w =
		 * (this.width - this.xSize) / 2; int k = (this.height - this.ySize) / 2;
		 * drawTexturedModalRect(w, k, 0, 0, this.xSize, this.ySize);
		 * 
		 * int Workpowerslider = 79 * this.tileEntity.getWorkDone() / 100; drawTexturedModalRect(w +
		 * 49, k + 89, 176, 0, Workpowerslider, 6);
		 * 
		 * int WorkCylce = 32 * ;
		 * 
		 * drawTexturedModalRect(w + 73, k + 50, 179, 81, WorkCylce, 32);
		 * 
		 * drawTexturedModalRect(w + 137, k + 60, 219, 80, 32, ForceEnergy);
		 */

	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		NetworkHandlerClient.fireTileEntityEvent(this.tileEntity, guibutton.id, "");
	}
}
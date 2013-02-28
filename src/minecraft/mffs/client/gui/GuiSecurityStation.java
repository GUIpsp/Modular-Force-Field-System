package mffs.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mffs.common.ModularForceFieldSystem;
import mffs.common.SecurityRight;
import mffs.common.SlotHelper;
import mffs.common.card.ItemCardPersonalID;
import mffs.common.container.ContainerSecurityStation;
import mffs.common.tileentity.TileEntitySecurityStation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.slotID;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import universalelectricity.core.vector.Vector2;

public class GuiSecurityStation extends GuiMFFS
{

	private TileEntitySecurityStation tileEntity;
	private SecurityRight hoverSR;
	private boolean editMode = false;

	public GuiSecurityStation(EntityPlayer player, TileEntitySecurityStation tileentity) {
		super(new ContainerSecurityStation(player, tileentity));
		this.tileEntity = tileentity;
		this.controlList.clear();
	}
	
	@Override
	public void initGui() {
		
		this.textFieldPos = new Vector2(109, 92);
		super.initGui();
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		
		super.drawGuiContainerBackgroundLayer(f, x, y);
	
		this.drawSlot(7, 30);
		
		this.drawSlot(7, 90);

		// Internal Inventory
		for (int var4 = 0; var4 < 9; var4++) {
			this.drawSlot(8 + var4 * 18 - 1, 110);
		}
		
		this.drawPatch(ModularForceFieldSystem.GUI_DIRECTORY + "sub_patch.png", this.containerWidth + 35, this.containerHeight + 20, 125, 63);
		
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
				
		this.fontRenderer.drawString(this.tileEntity.getInvName(), (int) (this.ySize / 2 - this.tileEntity.getInvName().length() * 4), 6, 4210752);

		this.textFieldFrequency.drawTextBox();

		this.drawTextWithTooltip("master", 28, 90 + (this.fontRenderer.FONT_HEIGHT / 2), x, y);
	
		super.drawGuiContainerForegroundLayer(x, y);
	}
}
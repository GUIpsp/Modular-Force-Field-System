package mffs.client.gui;

import java.util.ArrayList;
import java.util.List;

import mffs.common.ModularForceFieldSystem;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import universalelectricity.core.vector.Vector2;
import universalelectricity.prefab.TranslationHelper;

public class GuiMFFS extends GuiContainer
{

	public enum SlotType
	{
		NONE, BATTERY, LIQUID
	}

	public static final int METER_HEIGHT = 49;
	public static final int METER_WIDTH = 14;
	protected GuiTextField textFieldFrequency;
	Vector2 textFieldPos = new Vector2();
	protected String tooltip = "";
	protected int containerWidth;
	protected int containerHeight;

	public GuiMFFS(Container par1Container)
	{
		super(par1Container);
		this.ySize = 217;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.textFieldFrequency = new GuiTextField(this.fontRenderer, this.textFieldPos.intX(), this.textFieldPos.intY(), 60, 12);
		this.textFieldFrequency.setMaxStringLength(4);
		this.textFieldFrequency.setText("0");
	}

	@Override
	public void keyTyped(char par1, int par2)
	{
		if (par2 == 1)// esc
		{
			this.mc.thePlayer.closeScreen();
			return;
		}

		/**
		 * Everytime a key is typed, try to reset the frequency.
		 */
		this.textFieldFrequency.textboxKeyTyped(par1, par2);

		try
		{
			int newFrequency = Math.max(0, Integer.parseInt(this.textFieldFrequency.getText()));
			this.textFieldFrequency.setText(newFrequency + "");

			/**
			 * if (((IItemFrequency) this.itemStack.getItem()).getFrequency(this.itemStack) !=
			 * newFrequency) { ((IItemFrequency)
			 * this.itemStack.getItem()).setFrequency(newFrequency, this.itemStack);
			 * PacketDispatcher .sendPacketToServer(PacketManager
			 * .getPacketWithID(ZhuYaoWanYi.CHANNEL, WanYiPacketType.HUO_LUAN.ordinal(),
			 * newFrequency)); }
			 */
		}
		catch (NumberFormatException e)
		{
		}
	}

	@Override
	public void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		this.textFieldFrequency.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if (this.textFieldFrequency != null)
		{
			if (this.isPointInRegion(textFieldPos.intX(), textFieldPos.intY(), this.textFieldFrequency.getWidth(), 12, mouseX, mouseY))
			{
				this.tooltip = TranslationHelper.getLocal("gui.frequency.tooltip");
			}
		}

		if (this.tooltip != null && this.tooltip != "")
		{
			String[] words = this.tooltip.split(" ");
			List<String> displayLines = new ArrayList<String>();
			int wordsPerLine = 5;

			for (int lineCount = 0; lineCount < Math.ceil((float) words.length / (float) wordsPerLine); lineCount++)
			{
				String stringInLine = "";

				for (int i = lineCount * wordsPerLine; i < Math.min(wordsPerLine + lineCount * wordsPerLine, words.length); i++)
				{
					stringInLine += words[i] + " ";
				}

				displayLines.add(stringInLine.trim());
			}

			this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, displayLines.toArray(new String[] {}));
		}

		this.tooltip = "";

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int x, int y)
	{
		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;

		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_DIRECTORY + "gui_base.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(hua);
		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
	}

	protected void drawSlot(int x, int y, ItemStack itemStack)
	{
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_DIRECTORY + "gui_components.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(hua);
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18);

		this.drawItemStack(itemStack, this.containerWidth + x, this.containerHeight + y);
	}

	protected void drawItemStack(ItemStack itemStack, int x, int y)
	{
		x += 1;
		y += 1;
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);

		// GL11.glEnable(GL11.GL_BLEND);
		// GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, itemStack, x, y);
		// GL11.glDisable(GL11.GL_BLEND);
	}

	protected void drawTextWithTooltip(String textName, String format, int x, int y, int mouseX, int mouseY)
	{
		String name = TranslationHelper.getLocal("gui." + textName + ".name");
		String text = format.replaceAll("%1", name);
		this.fontRenderer.drawString(text, x, y, 4210752);

		String tooltip = TranslationHelper.getLocal("gui." + textName + ".tooltip");

		if (tooltip != null && tooltip != "")
		{
			if (this.isPointInRegion(x, y, (int) (text.length() * 4.8), 12, mouseX, mouseY))
			{
				this.tooltip = tooltip;
			}
		}

	}

	protected void drawTextWithTooltip(String textName, int x, int y, int mouseX, int mouseY)
	{
		this.drawTextWithTooltip(textName, "%1", x, y, mouseX, mouseY);
	}

	protected void drawSlot(int x, int y, SlotType type)
	{
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_DIRECTORY + "gui_components.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(hua);
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 0, 18, 18);

		switch (type)
		{
			default:
				break;
			case BATTERY:
			{
				this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 18, 18, 18);
				break;
			}
			case LIQUID:
			{
				this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 18 * 2, 18, 18);
				break;
			}
		}
	}

	protected void drawSlot(int x, int y)
	{
		this.drawSlot(x, y, SlotType.NONE);
	}

	protected void drawBar(int x, int y, float scale)
	{
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_DIRECTORY + "gui_components.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(hua);
		/**
		 * Draw background progress bar/
		 */
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 18, 0, 22, 15);

		if (scale > 0)
		{
			/**
			 * Draw white color actual progress.
			 */
			this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 18, 15, 22 - (int) (scale * 22), 15);
		}
	}

	protected void drawForce(int x, int y, float scale)
	{
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_DIRECTORY + "gui_components.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(hua);
		/**
		 * Draw background progress bar/
		 */
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11);

		if (scale > 0)
		{
			/**
			 * Draw white color actual progress.
			 */
			this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 11, (int) (scale * 107), 11);
		}
	}

	protected void drawElectricity(int x, int y, float scale)
	{
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_DIRECTORY + "gui_components.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(hua);
		/**
		 * Draw background progress bar/
		 */
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 0, 107, 11);

		if (scale > 0)
		{
			/**
			 * Draw white color actual progress.
			 */
			this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 54, 22, (int) (scale * 107), 11);
		}
	}

	protected void drawMeter(int x, int y, float scale, float r, float g, float b)
	{
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_DIRECTORY + "gui_components.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		/**
		 * Draw the background meter.
		 */
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 0, METER_WIDTH, METER_HEIGHT);

		/**
		 * Draw liquid/gas inside
		 */
		GL11.glColor4f(r, g, b, 1.0F);
		int actualScale = (int) ((METER_HEIGHT - 1) * scale);
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y + (METER_HEIGHT - 1 - actualScale), 40, 49, METER_WIDTH - 1, actualScale);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		/**
		 * Draw measurement lines
		 */
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 49 * 2, METER_WIDTH, METER_HEIGHT);
	}

	public void drawTooltip(int x, int y, String... toolTips)
	{
		if (!this.isShiftKeyDown())
		{
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);

			if (toolTips != null)
			{
				int var5 = 0;
				int var6;
				int var7;

				for (var6 = 0; var6 < toolTips.length; ++var6)
				{
					var7 = this.fontRenderer.getStringWidth((String) toolTips[var6]);

					if (var7 > var5)
					{
						var5 = var7;
					}
				}

				var6 = x + 12;
				var7 = y - 12;
				;
				int var9 = 8;

				if (toolTips.length > 1)
				{
					var9 += 2 + (toolTips.length - 1) * 10;
				}

				if (this.guiTop + var7 + var9 + 6 > this.height)
				{
					var7 = this.height - var9 - this.guiTop - 6;
				}

				this.zLevel = 300.0F;
				int var10 = -267386864;
				this.drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
				this.drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
				this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
				this.drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
				this.drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
				int var11 = 1347420415;
				int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
				this.drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
				this.drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
				this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
				this.drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

				for (int var13 = 0; var13 < toolTips.length; ++var13)
				{
					String var14 = toolTips[var13];

					this.fontRenderer.drawStringWithShadow(var14, var6, var7, -1);

					if (var13 == 0)
					{
						var7 += 2;
					}

					var7 += 10;
				}

				this.zLevel = 0.0F;

				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_LIGHTING);
				RenderHelper.enableGUIStandardItemLighting();
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			}
		}
	}
	
	public void drawPatch(String texture, int x, int y, int width, int height) {
		
		if(width >= 16 && height >= 16) {
			int patch = this.mc.renderEngine.getTexture(texture);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, patch);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(x, y, 0, 0, 8, 8);
			this.drawTexturedModalRect(x+width-8, y, 9, 0, 8, 8);
			this.drawTexturedModalRect(x, y+height-8, 0, 9, 8, 8);
			this.drawTexturedModalRect(x+width-8, y+height-8, 9, 9, 8, 8);
			for(int var1 = 8; var1 < width - 8; var1++){
				this.drawTexturedModalRect(x+var1, y, 8, 0, 1, 8);
				this.drawTexturedModalRect(x+var1, y+height-8, 8, 9, 1, 8);
			}
			for(int var1 = 8; var1 < height - 8; var1++){
				this.drawTexturedModalRect(x, y+var1, 0, 8, 8, 1);
				this.drawTexturedModalRect(x+width-8, y+var1, 9, 8, 8, 1);
			}
			for(int var1 = 8; var1 < width - 8; var1++){
				for(int var2 = 8; var2 < height - 8; var2++){
					this.drawTexturedModalRect(x+var1, y+var2, 9, 9, 1, 1);
				}
			}
		}
		
	}
}

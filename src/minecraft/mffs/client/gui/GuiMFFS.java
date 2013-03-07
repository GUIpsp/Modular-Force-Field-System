package mffs.client.gui;

import icbm.api.IBlockFrequency;
import mffs.common.ModularForceFieldSystem;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.liquids.LiquidStack;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import universalelectricity.core.vector.Vector2;
import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.network.PacketManager;
import cpw.mods.fml.common.network.PacketDispatcher;

public class GuiMFFS extends GuiContainer
{
	public enum SlotType
	{
		NONE, BATTERY, LIQUID, ARR_UP, ARR_DOWN, ARR_LEFT, ARR_RIGHT
	}

	public static final int METER_HEIGHT = 49;
	public static final int METER_WIDTH = 14;
	protected GuiTextField textFieldFrequency;
	Vector2 textFieldPos = new Vector2();
	protected String tooltip = "";
	protected int containerWidth;
	protected int containerHeight;
	protected IBlockFrequency frequencyTile;

	public GuiMFFS(Container container)
	{
		super(container);
		this.ySize = 217;
	}

	public GuiMFFS(Container container, IBlockFrequency frequencyTile)
	{
		this(container);
		this.frequencyTile = frequencyTile;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);

		if (this.frequencyTile != null)
		{
			this.textFieldFrequency = new GuiTextField(this.fontRenderer, this.textFieldPos.intX(), this.textFieldPos.intY(), 60, 12);
			this.textFieldFrequency.setMaxStringLength(6);
			this.textFieldFrequency.setText(frequencyTile.getFrequency() + "");
		}
	}

	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	protected void keyTyped(char par1, int par2)
	{
		// Escape
		if (par2 == 1)
		{
			this.mc.thePlayer.closeScreen();
			return;
		}

		if (this.textFieldFrequency != null)
		{
			/**
			 * Every time a key is typed, try to reset the frequency.
			 */
			this.textFieldFrequency.textboxKeyTyped(par1, par2);

			try
			{
				int newFrequency = Math.max(0, Integer.parseInt(this.textFieldFrequency.getText()));
				this.frequencyTile.setFrequency(newFrequency);
				this.textFieldFrequency.setText(this.frequencyTile.getFrequency() + "");
				PacketDispatcher.sendPacketToServer(PacketManager.getPacket(ModularForceFieldSystem.CHANNEL, (TileEntity) this.frequencyTile, 2, this.frequencyTile.getFrequency()));
			}
			catch (NumberFormatException e)
			{
			}
		}
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (this.textFieldFrequency != null)
		{
			if (!this.textFieldFrequency.isFocused())
			{
				this.textFieldFrequency.setText(this.frequencyTile.getFrequency() + "");
			}
		}
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);
		if (this.textFieldFrequency != null)
		{
			this.textFieldFrequency.mouseClicked(par1 - containerWidth, par2 - containerHeight, par3);
		}
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
			this.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop, ModularForceFieldSystem.splitStringPerWord(this.tooltip, 5).toArray(new String[] {}));
		}

		this.tooltip = "";

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int x, int y)
	{
		this.containerWidth = (this.width - this.xSize) / 2;
		this.containerHeight = (this.height - this.ySize) / 2;

		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_BASE_DIRECTORY);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(hua);
		this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
	}

	protected void drawSlot(int x, int y, ItemStack itemStack)
	{
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_COMPONENTS);
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
		this.drawTextWithTooltip(textName, format, x, y, mouseX, mouseY, 4210752);
	}

	protected void drawTextWithTooltip(String textName, String format, int x, int y, int mouseX, int mouseY, int color)
	{
		String name = TranslationHelper.getLocal("gui." + textName + ".name");
		String text = format.replaceAll("%1", name);
		this.fontRenderer.drawString(text, x, y, color);

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
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_COMPONENTS);
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
			case ARR_UP:
				this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 18 * 5, 18, 18);
				break;
			case ARR_DOWN:
				this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 18 * 6, 18, 18);
				break;
			case ARR_LEFT:
				this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 18 * 7, 18, 18);
				break;
			case ARR_RIGHT:
				this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 0, 18 * 8, 18, 18);
				break;
		}
	}

	protected void drawSlot(int x, int y)
	{
		this.drawSlot(x, y, SlotType.NONE);
	}

	protected void drawBar(int x, int y, float scale)
	{
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_COMPONENTS);
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
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_COMPONENTS);
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
		int hua = this.mc.renderEngine.getTexture(ModularForceFieldSystem.GUI_COMPONENTS);
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

	protected void drawMeter(int x, int y, float scale, LiquidStack liquidStack)
	{
		ForgeHooksClient.bindTexture(ModularForceFieldSystem.GUI_COMPONENTS, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		/**
		 * Draw the background meter.
		 */
		this.drawTexturedModalRect(this.containerWidth + x, this.containerHeight + y, 40, 0, METER_WIDTH, METER_HEIGHT);

		/**
		 * Draw liquid/gas inside
		 */
		this.displayGauge(this.containerWidth + x, this.containerHeight + y, 0, 0, (int) ((METER_HEIGHT - 1) * scale), liquidStack);
		/**
		 * Draw measurement lines
		 */
		ForgeHooksClient.bindTexture(ModularForceFieldSystem.GUI_COMPONENTS, 0);
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

	public void drawPatch(String texture, int x, int y, int width, int height)
	{
		if (width >= 16 && height >= 16)
		{
			int patch = this.mc.renderEngine.getTexture(texture);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, patch);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(x, y, 0, 0, 8, 8);
			this.drawTexturedModalRect(x + width - 8, y, 9, 0, 8, 8);
			this.drawTexturedModalRect(x, y + height - 8, 0, 9, 8, 8);
			this.drawTexturedModalRect(x + width - 8, y + height - 8, 9, 9, 8, 8);
			for (int var1 = 8; var1 < width - 8; var1++)
			{
				this.drawTexturedModalRect(x + var1, y, 8, 0, 1, 8);
				this.drawTexturedModalRect(x + var1, y + height - 8, 8, 9, 1, 8);
			}
			for (int var1 = 8; var1 < height - 8; var1++)
			{
				this.drawTexturedModalRect(x, y + var1, 0, 8, 8, 1);
				this.drawTexturedModalRect(x + width - 8, y + var1, 9, 8, 8, 1);
			}
			for (int var1 = 8; var1 < width - 8; var1++)
			{
				for (int var2 = 8; var2 < height - 8; var2++)
				{
					this.drawTexturedModalRect(x + var1, y + var2, 9, 9, 1, 1);
				}
			}
		}

	}

	protected void displayGauge(int x, int y, int line, int col, int scale, LiquidStack liquidStack)
	{
		int liquidId = liquidStack.itemID;
		int liquidMeta = liquidStack.itemMeta;
		int liquidImgIndex = 0;

		if (liquidId <= 0)
			return;
		if (liquidId < Block.blocksList.length && Block.blocksList[liquidId] != null)
		{
			ForgeHooksClient.bindTexture(Block.blocksList[liquidId].getTextureFile(), 0);
			liquidImgIndex = Block.blocksList[liquidId].blockIndexInTexture;
		}
		else if (Item.itemsList[liquidId] != null)
		{
			ForgeHooksClient.bindTexture(Item.itemsList[liquidId].getTextureFile(), 0);
			liquidImgIndex = Item.itemsList[liquidId].getIconFromDamage(liquidMeta);
		}
		else
			return;

		int imgLine = liquidImgIndex / 16;
		int imgColumn = liquidImgIndex - imgLine * 16;

		int start = 0;

		while (true)
		{
			int a = 0;

			if (scale > 16)
			{
				a = 16;
				scale -= 16;
			}
			else
			{
				a = scale;
				scale = 0;
			}

			this.drawTexturedModalRect(x + col, y + line + 58 - a - start, imgColumn * 16, imgLine * 16 + (16 - a), 16, 16 - (16 - a));
			start = start + 16;

			if (a == 0 || scale == 0)
			{
				break;
			}
		}
	}
}

package mffs.client.gui;

import mffs.common.ZhuYao;
import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import universalelectricity.prefab.TranslationHelper;

public class GuiButtonClickable extends GuiButtonMFFS
{
	private String name = "";

	public GuiButtonClickable(int id, int x, int y, GuiMFFS mainGui, String name)
	{
		super(id, x, y, mainGui, 0);
		this.name = name;
		this.displayString = TranslationHelper.getLocal("gui." + this.name + ".button");
	}

	@Override
	public void drawButton(Minecraft minecraft, int x, int y)
	{
		if (this.drawButton)
		{
			Minecraft.getMinecraft().renderEngine.func_98187_b(ZhuYao.GUI_BUTTON + this.name + ".png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 16, 16, this.width, this.height);
			this.mouseDragged(minecraft, x, y);
		}
	}
}

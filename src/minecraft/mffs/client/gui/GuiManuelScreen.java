package mffs.client.gui;

import java.util.ArrayList;
import java.util.List;

import mffs.common.ModularForceFieldSystem;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiManuelScreen extends GuiContainer
{

    private int page = 0;
    private int maxpage;
    private List pages = new ArrayList();

    public GuiManuelScreen(Container par1Container)
    {
        super(par1Container);
        generateIndex();
        this.maxpage = (this.pages.size() - 1);
        this.xSize = 256;
        this.ySize = 216;
    }

    @Override
    public void initGui()
    {
        this.controlList.add(new GuiButton(0, this.width / 2 + 90, this.height / 2 + 80, 22, 16, "-->"));
        this.controlList.add(new GuiButton(1, this.width / 2 - 110, this.height / 2 + 80, 22, 16, "<--"));
        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if (guibutton.id == 0)
        {
            if (this.page < this.maxpage)
            {
                this.page += 1;
            } else
            {
                this.page = 0;
            }
        }
        if (guibutton.id == 1)
        {
            if (this.page > 0)
            {
                this.page -= 1;
            } else
            {
                this.page = (this.pages.size() - 1);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int textur = this.mc.renderEngine.getTexture(ModularForceFieldSystem.TEXTURE_DIRECTORY + "GuiManuel.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(textur);
        int w = (this.width - 256) / 2;
        int k = (this.height - 216) / 2;
        drawTexturedModalRect(w, k, 0, 0, 256, 216);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        this.fontRenderer.drawString("MFFS Multitool - Guide", 20, 15, 16777215);
        getcontent(this.page);
        this.fontRenderer.drawString("Page [" + this.page + "] :" + (String) this.pages.get(this.page), 45, 193, 16777215);
    }

    private void generateIndex()
    {
        this.pages.clear();
        this.pages.add("Table of Contents");
        this.pages.add("Changelog");
        this.pages.add("Version Check");
        this.pages.add("Monazite/Forcillium/Cell");
        this.pages.add("Card Overview (1)");
        this.pages.add("Card Overview (2)");
    }

    private void getcontent(int page)
    {
        RenderItem renderItem = new RenderItem();
        RenderHelper.enableGUIStandardItemLighting();

        switch (page)
        {
            case 0:
                this.fontRenderer.drawString("Table of Contents", 90, 45, 16777215);
                for (int p = 0; p < this.pages.size(); p++)
                {
                    this.fontRenderer.drawString("[" + p + "]: " + (String) this.pages.get(p), 20, 65 + p * 10, 16777215);
                }
                break;
            case 1:
                this.fontRenderer.drawString("Changelog V2.2.8.3.6", 90, 45, 16777215);
                this.fontRenderer.drawString("Fixed Converter Powerloop.", 20, 65, 16777215);
                this.fontRenderer.drawString("Fixed Texture Bug.", 20, 75, 16777215);
                this.fontRenderer.drawString("Changed Force Field.", 20, 85, 16777215);
                this.fontRenderer.drawString("New Touch Damage System.", 20, 95, 16777215);

                break;
            case 2:
                this.fontRenderer.drawString("Version Check", 90, 45, 16777215);
                // TODO: FIX THIS
                this.fontRenderer.drawString("Current Version: " + ModularForceFieldSystem.VERSION, 20, 65, 16777215);
                this.fontRenderer.drawString("Newest Version : " + ModularForceFieldSystem.VERSION, 20, 75, 16777215);
                break;
            case 3:
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.blockMonaziteOre), 30, 45);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemForcicium), 30, 65);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemForcicumCell), 30, 85);

                this.fontRenderer.drawString("Monazite Ore", 60, 50, 16777215);
                this.fontRenderer.drawString("Forcillium", 60, 70, 16777215);
                this.fontRenderer.drawString("Forcillium Cell", 60, 90, 16777215);

                this.fontRenderer.drawString("Monazite Ore can be found within Y  level 80-0.", 20, 105, 16777215);
                this.fontRenderer.drawString("You can smelt to get 4 Forcillium.", 20, 115, 16777215);
                this.fontRenderer.drawString("You can macerate to get 8 Forcillium.", 20, 125, 16777215);
                this.fontRenderer.drawString("Forcillium Cell can store 1k Forcillium,", 20, 135, 16777215);
                this.fontRenderer.drawString("Right click the Forcillium Cell to", 20, 145, 16777215);
                this.fontRenderer.drawString("remove Forcicium from the player's", 20, 155, 16777215);
                this.fontRenderer.drawString("inventory and store it.", 20, 165, 16777215);
                break;
            case 4:
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardEmpty), 15, 45);
                this.fontRenderer.drawString("A Card <Blank> is craftable and", 35, 45, 16777215);
                this.fontRenderer.drawString("needed for all other cards.", 35, 55, 16777215);

                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardPowerLink), 15, 95);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.blockCapacitor), 35, 105);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardEmpty), 140, 105);
                this.fontRenderer.drawString("Get a Card <Power Link> from right clicking", 35, 95, 16777215);
                this.fontRenderer.drawString("a Capacitor with a Card <Blank>", 55, 110, 16777215);

                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardSecurityLink), 15, 145);
                this.fontRenderer.drawString("Get a Card <Security Station Link> from", 35, 145, 16777215);
                this.fontRenderer.drawString("right clicking a", 35, 160, 16777215);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.blockSecurityStation), 90, 155);
                this.fontRenderer.drawString("Security Station with a Card <Blank>.", 110, 160, 16777215);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardEmpty), 220, 155);

                break;
            case 5:
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardAccess), 15, 45);
                this.fontRenderer.drawString("Create a Card <Access License> inside a", 35, 45, 16777215);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.blockSecurityStation), 35, 55);
                this.fontRenderer.drawString("Security Station with a Card <Blank>.", 55, 60, 16777215);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardEmpty), 170, 55);

                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardID), 15, 85);
                this.fontRenderer.drawString("Create a Card <Personal ID> by right clicking", 35, 85, 16777215);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemMultiToolID), 35, 100);
                this.fontRenderer.drawString("the Multi Tool for yourself", 55, 98, 16777215);
                this.fontRenderer.drawString("or left click for another player.", 55, 110, 16777215);

                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardDataLink), 15, 125);
                this.fontRenderer.drawString("Create a Card <Data Link> by right clicking", 35, 125, 16777215);
                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemMultiToolID), 35, 140);
                this.fontRenderer.drawString("the Multi Tool on any", 55, 138, 16777215);
                this.fontRenderer.drawString("MFFS Machine.", 55, 150, 16777215);

                renderItem.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, new ItemStack(ModularForceFieldSystem.itemCardInfinite), 15, 165);
                this.fontRenderer.drawString("Infinite Force Energy Card", 35, 170, 16777215);
        }

        RenderHelper.disableStandardItemLighting();
    }
}
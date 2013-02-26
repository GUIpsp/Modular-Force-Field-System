package mffs.client.gui;

import mffs.client.GraphicButton;
import mffs.common.ModularForceFieldSystem;
import mffs.common.container.ContainerCapacitor;
import mffs.common.tileentity.TileEntityCapacitor;
import mffs.network.client.NetworkHandlerClient;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.vector.Vector2;

public class GuiCapacitor extends GuiMFFS
{

    private TileEntityCapacitor tileEntity;

    public GuiCapacitor(EntityPlayer player, TileEntityCapacitor tileentity)
    {
        super(new ContainerCapacitor(player, tileentity));
        this.tileEntity = tileentity;
    }

    @Override
    public void initGui()
    {
        this.textFieldPos = new Vector2(30, 76);

        super.initGui();

        this.controlList.clear();
        this.controlList.add(new GraphicButton(0, this.width / 2 + 65, this.height / 2 - 100, this.tileEntity, 0));
        this.controlList.add(new GraphicButton(1, this.width / 2 + 5, this.height / 2 - 35, this.tileEntity, 1));

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
    {
        super.drawGuiContainerBackgroundLayer(f, x, y);

        /**
         * Upgrade Slots
         */
        this.drawSlot(153, 46);
        this.drawSlot(153, 66);
        this.drawSlot(153, 86);

        /**
         * Frequency Card Slot
         */
        this.drawSlot(8, 73, new ItemStack(ModularForceFieldSystem.itemCardEmpty));

        this.drawEnergy(8, 115, 0);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y)
    {
        this.fontRenderer.drawString(this.tileEntity.getInvName(), this.ySize / 2 - this.tileEntity.getInvName().length() * 5, 6, 4210752);

        this.drawTextWithTooltip("linkedDevice", "%1: " + this.tileEntity.getLinketProjector(), 8, 30, x, y);
        this.drawTextWithTooltip("range", "%1: " + this.tileEntity.getTransmitRange(), 8, 45, x, y);
        this.drawTextWithTooltip("frequency", "%1:", 8, 60, x, y);
        this.textFieldFrequency.drawTextBox();
        this.drawTextWithTooltip("forcePower", "%1:", 8, 95, x, y);
        this.fontRenderer.drawString(ElectricInfo.getDisplay(this.tileEntity.getStorageAvailablePower(), ElectricUnit.JOULES) + "/" + ElectricInfo.getDisplay(this.tileEntity.getMaximumPower(), ElectricUnit.JOULES), 8, 105, 4210752);
        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);
        NetworkHandlerClient.fireTileEntityEvent(this.tileEntity, guibutton.id, "");
    }
}
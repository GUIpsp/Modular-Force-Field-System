package mffs.common.upgrade;

import java.util.List;

import mffs.common.MachineTypes;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemUpgradeBooster extends ItemMFFS
{

    public ItemUpgradeBooster(int i)
    {
        super(i, "upgradeBooster");
        setIconIndex(37);
        setMaxStackSize(19);
        this.setNoRepair();

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
    {
        if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
        {
            info.add("Compatible with:");
            info.add("MFFS " + MachineTypes.Extractor.getName());
        } else
        {
            info.add("Compatible with: (Hold Shift)");
        }
    }
}
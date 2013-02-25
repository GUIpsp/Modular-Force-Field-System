package mffs.common.multitool;

import mffs.api.ISwitchable;
import mffs.common.Functions;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemMultitoolSwitch extends ItemMultitool
{

    public ItemMultitoolSwitch(int id)
    {
        super(id, 1, "multitoolSwitch");
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return false;
        }

        TileEntity tileentity = world.getBlockTileEntity(x, y, z);

        if ((tileentity instanceof ISwitchable))
        {
            if (SecurityHelper.isAccessGranted(tileentity, entityplayer, world, SecurityRight.EB))
            {
                if (((ISwitchable) tileentity).isSwitchabel())
                {
                    if (consumePower(itemstack, 1000, true))
                    {
                        consumePower(itemstack, 1000, false);
                        ((ISwitchable) tileentity).toggelSwitchValue();
                        return true;
                    }

                    Functions.ChattoPlayer(entityplayer, "[MultiTool] Fail: not enough FP please charge");
                    return false;
                }

                Functions.ChattoPlayer(entityplayer, "[MultiTool] Fail: Object not in switch enable mode");
                return false;
            }

        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return super.onItemRightClick(itemstack, world, entityplayer);
    }
}
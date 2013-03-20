package mffs.api;

import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * A class to help and see if a block has security access allowed for it.
 * 
 * @author Calclavia
 * 
 */
public class SecurityHelper
{
	public static boolean isAccessGranted(TileEntityMFFS tileEntity, EntityPlayer entityPlayer, World world, SecurityPermission permission)
	{
		return true;
	}
}

package mffs.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

/**
 * A class to help and see if a block has security access allowed for it.
 * 
 * @author Calclavia
 * 
 */
public class SecurityHelper
{
	public static boolean isAccessGranted(EntityPlayer entityPlayer, World world, Vector3 position, SecurityPermission permission)
	{
		/*
		 * for (IFortronFrequency frequency : FortronGrid.INSTANCE.get()) { if (frequency instanceof
		 * ISecurityCenter && ((TileEntity) frequency).worldObj == world) { if
		 * (Vector3.distance(position, new Vector3((TileEntity) frequency)) <= ((ISecurityCenter)
		 * frequency).getRange()) { ((ISecurityCenter)
		 * frequency).isAccessGranted(entityPlayer.username, permission); } } }
		 */

		return false;
	}
}

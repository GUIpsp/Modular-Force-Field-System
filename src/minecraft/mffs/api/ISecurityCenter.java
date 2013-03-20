package mffs.api;


/**
 * Applied to security centers TileEntities.
 */
public interface ISecurityCenter
{
	/**
	 * Is access granted to this specific user?
	 * 
	 * @param username - Minecraft username.
	 * @param permission - The permission.
	 * @return
	 */
	public boolean isAccessGranted(String username, SecurityPermission permission);

	/**
	 * Gets the owner of the security center.
	 * 
	 * @return
	 */
	public String getOwner();
}

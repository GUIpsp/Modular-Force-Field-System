package mffs.api;

public enum SecurityPermission
{
	/**
	 * Warp - Allows going through force fields.
	 * 
	 * Access - Allows to open GUIs and activate blocks.
	 * 
	 * Configure - Allows to configure security stations.
	 * 
	 * Stay - Allows to stay in defense station zone.
	 * 
	 * Bypass Confiscation - Allows the bypassing of defense station confiscation.
	 * 
	 * Remote Control - Allows the usage of a remote control to open GUIs remotely.
	 */
	FORCE_FIELD_WARP, BLOCK_ACCESS, SECURITY_CENTER_CONFIGURE, DEFENSE_STATION_STAY,
	DEFENSE_STATION_CONFISCATION, REMOTE_CONTROL;
}
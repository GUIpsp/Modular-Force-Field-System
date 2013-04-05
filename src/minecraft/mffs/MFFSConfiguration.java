package mffs;

import java.io.File;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Loader;

public class MFFSConfiguration
{
	/**
	 * Blocks
	 */
	public static final int BLOCK_ID_PREFIX = 680;
	public static final int ITEM_ID_PREFIX = 11130;

	private static int NEXT_BLOCK_ID = BLOCK_ID_PREFIX;
	private static int NEXT_ITEM_ID = ITEM_ID_PREFIX;

	public static int getNextBlockID()
	{
		NEXT_BLOCK_ID++;
		return NEXT_BLOCK_ID;
	}

	public static int getNextItemID()
	{
		NEXT_ITEM_ID++;
		return NEXT_ITEM_ID;
	}

	/**
	 * Settings
	 */
	public static int MAX_FORCE_FIELDS_PER_TICK = 1000000;
	public static int MAX_FORCE_FIELD_SCALE = 150;
	public static boolean LOAD_CHUNKS = true;
	public static boolean MOD_INFLUENCE = true;
	public static int DEFENSE_STATION_KILL_ENERGY = 1000;
	public static int DEFENSE_STATION_SEARCH_ENERGY = 100;
	public static int DEFENSE_STATION_SCAN_ENERGY = 10;
	public static String ADMINISTRATORS = "";

	/**
	 * Compatibility Modules
	 */
	public static boolean MODULE_IC2 = false;
	public static boolean MODULE_UE = false;
	public static boolean MODULE_EE = false;
	public static boolean MODULE_BUILDCRAFT = false;
	public static boolean MODULE_THERMAL_EXPANSION = false;

	public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/" + ModularForceFieldSystem.NAME + ".cfg"));

	public static void initialize()
	{
		CONFIGURATION.load();

		Property fieldScale = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Max Force Field Scale", MAX_FORCE_FIELD_SCALE);
		MAX_FORCE_FIELD_SCALE = fieldScale.getInt(MAX_FORCE_FIELD_SCALE);

		Property chunckloader_prop = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Chunkloader", LOAD_CHUNKS);
		chunckloader_prop.comment = "Set this to false to turn off the MFFS Chunkloading abilities.";
		LOAD_CHUNKS = chunckloader_prop.getBoolean(LOAD_CHUNKS);

		Property adminList = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Administrators", ADMINISTRATORS);
		adminList.comment = "Add users to this list to give them admin permissions, split by a semicolon.";
		ADMINISTRATORS = adminList.getString();

		Property influencedByOther = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Mod Influence", MOD_INFLUENCE);
		influencedByOther.comment = "Should MFFS be influenced by other mods? e.g. ICBM's EMP";
		MOD_INFLUENCE = Boolean.valueOf(influencedByOther.getBoolean(true));

		Property maxFFGenPerTick = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldmaxblockpeerTick", MAX_FORCE_FIELDS_PER_TICK);
		maxFFGenPerTick.comment = "How many field blocks can be generated per tick?";
		MAX_FORCE_FIELDS_PER_TICK = maxFFGenPerTick.getInt(MAX_FORCE_FIELDS_PER_TICK);

		Property defStationKillCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Defence Station Kill Fortron", DEFENSE_STATION_KILL_ENERGY);
		defStationKillCost.comment = "How much FE does the Area Defense Station need when killing someone?";
		DEFENSE_STATION_KILL_ENERGY = defStationKillCost.getInt(DEFENSE_STATION_KILL_ENERGY);

		Property defStationSearchCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Defence Station Search Fortron", DEFENSE_STATION_SEARCH_ENERGY);
		defStationSearchCost.comment = "How much FE does the Area Defense Station need when searching someone for banned items?";
		DEFENSE_STATION_SEARCH_ENERGY = defStationSearchCost.getInt(DEFENSE_STATION_SEARCH_ENERGY);

		Property defStationScannCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Defence Station Scan Fortron", DEFENSE_STATION_SCAN_ENERGY);
		defStationScannCost.comment = "How much FE does the Area Defense Station need when scanning for targets? (amount * range / tick)";
		DEFENSE_STATION_SCAN_ENERGY = defStationScannCost.getInt(DEFENSE_STATION_SCAN_ENERGY);
		CONFIGURATION.save();
	}

}

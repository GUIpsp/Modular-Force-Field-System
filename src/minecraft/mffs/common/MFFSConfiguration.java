package mffs.common;

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
	public static int graphicStyle;
	public static boolean chunckLoader = true;
	public static boolean defenseStationNPCNotification;
	public static boolean advancedParticles;
	public static boolean uumatterEnabled;
	public static int forceFieldBlockCost;
	public static int forcefieldblockcreatemodifier;
	public static int forcefieldblockzappermodifier;
	public static int forcefieldtransportcost;
	public static int maxForceFieldPerTick;
	public static boolean forcefieldremoveonlywaterandlava;
	public static boolean influencedByOtherMods;
	public static boolean adventureMap;
	public static int ForcilliumWorkCylce;
	public static int forceciumCellWorkCycle;
	public static int ExtractorPassForceEnergyGenerate;
	public static int defenceStationKillForceEnergy;
	public static int defenceStationSearchForceEnergy;
	public static int DefenceStationScannForceEnergy;
	public static String administrators;

	/**
	 * Compatibility Modules
	 */
	public static boolean MODULE_IC2 = false;
	public static boolean MODULE_UE = false;
	public static boolean MODULE_EE = false;
	public static boolean MODULE_BUILDCRAFT = false;
	public static boolean MODULE_THERMAL_EXPANSION = false;

	public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/" + ZhuYao.NAME + ".cfg"));

	public static void initialize()
	{
		CONFIGURATION.load();

		Property prop_graphicstyle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "GraphicStyle", 1);
		prop_graphicstyle.comment = "Graphical style. 1 : UE Style, 2 : IC2 Style.";
		graphicStyle = prop_graphicstyle.getInt(1);

		Property chunckloader_prop = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Chunkloader", true);
		chunckloader_prop.comment = "Set this to false to turn off the MFFS Chunkloading abilities.";
		chunckLoader = chunckloader_prop.getBoolean(true);

		Property DefSationNPCScannoti = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationNPCScannnotification", false);
		DefSationNPCScannoti.comment = "Set this to true to turn off the Defence Station notification that it is in NPC Mode.";
		defenseStationNPCNotification = DefSationNPCScannoti.getBoolean(false);

		Property zapperParticles = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "renderZapperParticles", true);
		zapperParticles.comment = "Set this to false to turn off the small smoke particles present around Touch Damage enabled Force Fields.";
		advancedParticles = zapperParticles.getBoolean(true);

		Property uumatterForciciumprop = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "uumatterForcicium", true);
		uumatterForciciumprop.comment = "Adds IC2 UU-Matter Recipes for Forcillium.";
		uumatterEnabled = uumatterForciciumprop.getBoolean(true);

		Property adminList = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ForceFieldMaster", "nobody");
		adminList.comment = "Add users to this list to give them admin permissions, split by a semicolon.";
		administrators = adminList.getString();

		Property influencedByOther = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "influencedbyothermods", true);
		influencedByOther.comment = "Should MFFS be influenced by other mods? e.g. ICBM's EMP";
		influencedByOtherMods = Boolean.valueOf(influencedByOther.getBoolean(true));

		Property ffRemoveWaterLavaOnly = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldremoveonlywaterandlava", false);
		ffRemoveWaterLavaOnly.comment = "Should forcefields only remove water and lava when sponge is enabled?";
		forcefieldremoveonlywaterandlava = Boolean.valueOf(ffRemoveWaterLavaOnly.getBoolean(false));

		Property feTransportCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldtransportcost", 10000);
		feTransportCost.comment = "How much FE should it cost to transport through a field?";
		forcefieldtransportcost = feTransportCost.getInt(10000);

		Property feFieldBlockCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcost", 1);
		feFieldBlockCost.comment = "How much upkeep FE cost a default Force Field Block per second?";
		forceFieldBlockCost = feFieldBlockCost.getInt(1);

		Property BlockCreateMod = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcreatemodifier", 10);
		BlockCreateMod.comment = "Energy needed to create a Force Field Block (forcefieldblockcost*forcefieldblockcreatemodifier).";
		forcefieldblockcreatemodifier = BlockCreateMod.getInt(10);

		Property ffZapperMod = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockzappermodifier", 2);
		ffZapperMod.comment = "Energy needed for the multiplier used when the zapper option is enabled.";
		forcefieldblockzappermodifier = ffZapperMod.getInt(2);

		Property maxFFGenPerTick = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldmaxblockpeerTick", 5000);
		maxFFGenPerTick.comment = "How many field blocks can be generated per tick?";
		maxForceFieldPerTick = maxFFGenPerTick.getInt(5000);

		Property fcWorkCycle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ForceciumWorkCylce", 250);
		fcWorkCycle.comment = "Forcillium Work Cycle used inside a Extractor.";
		ForcilliumWorkCylce = fcWorkCycle.getInt(250);

		Property fcCellWorkCycle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forceciumCellWorkCycle", 230);
		fcCellWorkCycle.comment = "Forcecium Cell Work Cycle used inside a Extractor.";
		forceciumCellWorkCycle = fcCellWorkCycle.getInt(230);

		Property extractorPassFEGen = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ExtractorPassForceEnergyGenerate", 12000);
		extractorPassFEGen.comment = "How much Force Energy should generate per Work Cycle?";
		ExtractorPassForceEnergyGenerate = extractorPassFEGen.getInt(12000);
		ExtractorPassForceEnergyGenerate = ExtractorPassForceEnergyGenerate / 4000 * 4000;

		Property defStationKillCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationKillForceEnergy", 10000);
		defStationKillCost.comment = "How much FE does the Area Defense Station need when killing someone?";
		defenceStationKillForceEnergy = defStationKillCost.getInt(10000);

		Property defStationSearchCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationSearchForceEnergy", 1000);
		defStationSearchCost.comment = "How much FE does the Area Defense Station need when searching someone for banned items?";
		defenceStationSearchForceEnergy = defStationSearchCost.getInt(1000);

		Property defStationScannCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationScannForceEnergy", 10);
		defStationScannCost.comment = "How much FE does the Area Defense Station need when scanning for targets? (amount * range / tick)";
		DefenceStationScannForceEnergy = defStationScannCost.getInt(10);

		Property Adventuremap = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "adventureMap", false);
		Adventuremap.comment = "Set MFFS to Adventure Map Mode Extractor, requires no Forcillium and Force Fields have no click damage.";
		adventureMap = Boolean.valueOf(Adventuremap.getBoolean(false));
		CONFIGURATION.save();
	}

}

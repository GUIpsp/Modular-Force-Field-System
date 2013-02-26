package mffs.common;

import java.io.File;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import cpw.mods.fml.common.Loader;

public class MFFSConfiguration {
	
    private static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/" + ModularForceFieldSystem.NAME + ".cfg"));

    public static Configuration getConfiguration() {
    	return CONFIGURATION;
    }
    
    public static void initialize() {
        CONFIGURATION.load();

        Property prop_graphicstyle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "GraphicStyle", 1);
        prop_graphicstyle.comment = "Graphical style. 1 : UE Style, 2 : IC2 Style.";
        MFFSProperties.graphicStyle = prop_graphicstyle.getInt(1);

        Property chunckloader_prop = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Chunkloader", true);
        chunckloader_prop.comment = "Set this to false to turn off the MFFS Chunkloading abilities.";
        MFFSProperties.chunckLoader = chunckloader_prop.getBoolean(true);

        Property DefSationNPCScannoti = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationNPCScannnotification", false);
        DefSationNPCScannoti.comment = "Set this to true to turn off the Defence Station notification that it is in NPC Mode.";
        MFFSProperties.defenseStationNPCNotification = DefSationNPCScannoti.getBoolean(false);

        Property zapperParticles = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "renderZapperParticles", true);
        zapperParticles.comment = "Set this to false to turn off the small smoke particles present around Touch Damage enabled Force Fields.";
        MFFSProperties.advancedParticles = zapperParticles.getBoolean(true);

        Property uumatterForciciumprop = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "uumatterForcicium", true);
        uumatterForciciumprop.comment = "Adds IC2 UU-Matter Recipes for Forcillium.";
        MFFSProperties.uumatterEnabled = uumatterForciciumprop.getBoolean(true);

        Property adminList = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ForceFieldMaster", "nobody");
        adminList.comment = "Add users to this list to give them admin permissions, split by a semicolon.";
        MFFSProperties.Admin = adminList.value;

        Property influencedByOther = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "influencedbyothermods", true);
        influencedByOther.comment = "Should MFFS be influenced by other mods? e.g. ICBM's EMP";
        MFFSProperties.influencedbyothermods = Boolean.valueOf(influencedByOther.getBoolean(true));

        Property ffRemoveWaterLavaOnly = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldremoveonlywaterandlava", false);
        ffRemoveWaterLavaOnly.comment = "Should forcefields only remove water and lava when sponge is enabled?";
        MFFSProperties.forcefieldremoveonlywaterandlava = Boolean.valueOf(ffRemoveWaterLavaOnly.getBoolean(false));

        Property feTransportCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldtransportcost", 10000);
        feTransportCost.comment = "How much FE should it cost to transport through a field?";
        MFFSProperties.forcefieldtransportcost = feTransportCost.getInt(10000);

        Property feFieldBlockCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcost", 1);
        feFieldBlockCost.comment = "How much upkeep FE cost a default Force Field Block per second?";
        MFFSProperties.forcefieldblockcost = feFieldBlockCost.getInt(1);

        Property BlockCreateMod = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcreatemodifier", 10);
        BlockCreateMod.comment = "Energy needed to create a Force Field Block (forcefieldblockcost*forcefieldblockcreatemodifier).";
        MFFSProperties.forcefieldblockcreatemodifier = BlockCreateMod.getInt(10);

        Property ffZapperMod = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockzappermodifier", 2);
        ffZapperMod.comment = "Energy needed for the multiplier used when the zapper option is enabled.";
        MFFSProperties.forcefieldblockzappermodifier = ffZapperMod.getInt(2);

        Property maxFFGenPerTick = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldmaxblockpeerTick", 5000);
        maxFFGenPerTick.comment = "How many field blocks can be generated per tick?";
        MFFSProperties.forcefieldmaxblockpeerTick = maxFFGenPerTick.getInt(5000);

        Property fcWorkCycle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ForceciumWorkCylce", 250);
        fcWorkCycle.comment = "Forcillium Work Cycle used inside a Extractor.";
        MFFSProperties.ForceciumWorkCylce = fcWorkCycle.getInt(250);

        Property fcCellWorkCycle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ForceciumCellWorkCylce", 230);
        fcCellWorkCycle.comment = "Forcecium Cell Work Cycle used inside a Extractor.";
        MFFSProperties.ForceciumCellWorkCylce = fcCellWorkCycle.getInt(230);

        Property extractorPassFEGen = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ExtractorPassForceEnergyGenerate", 12000);
        extractorPassFEGen.comment = "How much Force Energy should generate per Work Cycle?";
        MFFSProperties.ExtractorPassForceEnergyGenerate = extractorPassFEGen.getInt(12000);
        MFFSProperties.ExtractorPassForceEnergyGenerate = MFFSProperties.ExtractorPassForceEnergyGenerate / 4000 * 4000;

        Property defStationKillCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationKillForceEnergy", 10000);
        defStationKillCost.comment = "How much FE does the Area Defense Station need when killing someone?";
        MFFSProperties.DefenceStationKillForceEnergy = defStationKillCost.getInt(10000);

        Property defStationSearchCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationSearchForceEnergy", 1000);
        defStationSearchCost.comment = "How much FE does the Area Defense Station need when searching someone for banned items?";
        MFFSProperties.DefenceStationSearchForceEnergy = defStationSearchCost.getInt(1000);

        Property defStationScannCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationScannForceEnergy", 10);
        defStationScannCost.comment = "How much FE does the Area Defense Station need when scanning for targets? (amount * range / tick)";
        MFFSProperties.DefenceStationScannForceEnergy = defStationScannCost.getInt(10);

        Property Adventuremap = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "adventuremap", false);
        Adventuremap.comment = "Set MFFS to Adventure Map Mode Extractor, requires no Forcillium and Force Fields have no click damage.";
        MFFSProperties.adventuremap = Boolean.valueOf(Adventuremap.getBoolean(false));
        
        Property monazitWorldAmount = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Monazite Ore Generation", 15);
        monazitWorldAmount.comment = "The amount of Monazite Ore generated per chunk.";
        MFFSProperties.monazitWorldAmount = monazitWorldAmount.getInt(15);
    }
    

}

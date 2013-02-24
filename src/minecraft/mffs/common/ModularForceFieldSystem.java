package mffs.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mffs.common.block.BlockCapacitor;
import mffs.common.block.BlockControlSystem;
import mffs.common.block.BlockConverter;
import mffs.common.block.BlockDefenseStation;
import mffs.common.block.BlockExtractor;
import mffs.common.block.BlockForceField;
import mffs.common.block.BlockMonaziteOre;
import mffs.common.block.BlockProjector;
import mffs.common.block.BlockSecurityStation;
import mffs.common.block.BlockSecurityStorage;
import mffs.common.card.ItemAccessCard;
import mffs.common.card.ItemCardDataLink;
import mffs.common.card.ItemCardEmpty;
import mffs.common.card.ItemCardPersonalID;
import mffs.common.card.ItemCardPower;
import mffs.common.card.ItemCardPowerLink;
import mffs.common.card.ItemCardSecurityLink;
import mffs.common.event.EE3Event;
import mffs.common.item.ItemForcePowerCrystal;
import mffs.common.item.ItemForcicium;
import mffs.common.item.ItemForcicumCell;
import mffs.common.modules.ItemProjectorModuleAdvCube;
import mffs.common.modules.ItemProjectorModuleContainment;
import mffs.common.modules.ItemProjectorModuleCube;
import mffs.common.modules.ItemProjectorModuleDeflector;
import mffs.common.modules.ItemProjectorModuleSphere;
import mffs.common.modules.ItemProjectorModuleTube;
import mffs.common.modules.ItemProjectorModuleWall;
import mffs.common.modules.ItemProjectorModulediagonallyWall;
import mffs.common.multitool.ItemDebugger;
import mffs.common.multitool.ItemFieldtransporter;
import mffs.common.multitool.ItemManuelBook;
import mffs.common.multitool.ItemPersonalIDWriter;
import mffs.common.multitool.ItemSwitch;
import mffs.common.multitool.ItemWrench;
import mffs.common.options.ItemProjectorOptionBlockBreaker;
import mffs.common.options.ItemProjectorOptionCamoflage;
import mffs.common.options.ItemProjectorOptionDefenseStation;
import mffs.common.options.ItemProjectorOptionFieldFusion;
import mffs.common.options.ItemProjectorOptionFieldManipulator;
import mffs.common.options.ItemProjectorOptionForceFieldJammer;
import mffs.common.options.ItemProjectorOptionMobDefence;
import mffs.common.options.ItemProjectorOptionSponge;
import mffs.common.options.ItemProjectorOptionTouchDamage;
import mffs.common.tileentity.TileEntityForceField;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.upgrade.ItemCapacitorUpgradeCapacity;
import mffs.common.upgrade.ItemCapacitorUpgradeRange;
import mffs.common.upgrade.ItemExtractorUpgradeBooster;
import mffs.common.upgrade.ItemProjectorFieldModulatorDistance;
import mffs.common.upgrade.ItemProjectorFieldModulatorStrength;
import mffs.common.upgrade.ItemProjectorFocusMatrix;
import mffs.network.client.ForceFieldClientUpdatehandler;
import mffs.network.client.NetworkHandlerClient;
import mffs.network.server.ForceFieldServerUpdatehandler;
import mffs.network.server.NetworkHandlerServer;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;

import org.modstats.ModstatInfo;
import org.modstats.Modstats;

import universalelectricity.prefab.TranslationHelper;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = ModularForceFieldSystem.ID, name = ModularForceFieldSystem.NAME, version = ModularForceFieldSystem.VERSION, dependencies = "after:ThermalExpansion")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerClient.class), serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerServer.class))
@ModstatInfo(prefix = "mffs")
public class ModularForceFieldSystem
{
	public static final String ID = "ModularForceFieldSystem";
	public static final String NAME = "Modular Force Field System";
	public static final String VERSION = "2.2.9";

	public static CreativeTabs TAB;

	public static final String RESOURCE_DIRECTORY = "/mffs/";
	public static final String TEXTURE_DIRECTORY = RESOURCE_DIRECTORY + "textures/";
	public static final String BLOCK_TEXTURE_FILE = TEXTURE_DIRECTORY + "blocks.png";
	public static final String ITEM_TEXTURE_FILE = TEXTURE_DIRECTORY + "items.png";

	public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/" + NAME + ".cfg"));;

	public static int RENDER_ID = 2908;
	public static Block blockCapacitor;
	public static Block blockProjector;
	public static Block blockDefenceStation;
	public static Block blockForceField;
	public static Block blockExtractor;
	public static Block blockMonaziteOre;
	public static Block blockConverter;
	public static Block blockSecurityStorage;
	public static Block blockSecurityStation;
	public static Block blockControlSystem;

	public static Item itemForcicumCell;
	public static Item itemForcicium;
	public static Item itemPowerCrystal;
	public static Item itemCompactForcicium;
	public static Item itemDepletedForcicium;
	public static Item itemFocusMatix;
	public static Item itemSwitch;
	public static Item itemWrench;
	public static Item itemFieldTeleporter;
	public static Item itemMFDidtool;
	public static Item MFFSitemMFDdebugger;
	public static Item MFFSitemcardempty;
	public static Item MFFSitemfc;
	public static Item MFFSItemIDCard;
	public static Item MFFSAccessCard;
	public static Item MFFSItemSecLinkCard;
	public static Item MFFSitemManuelBook;
	public static Item MFFSitemInfinitePowerCard;
	public static Item MFFSitemDataLinkCard;
	public static Item MFFSitemupgradeexctractorboost;
	public static Item MFFSitemupgradecaprange;
	public static Item MFFSitemupgradecapcap;
	public static Item MFFSProjectorTypsphere;
	public static Item MFFSProjectorTypkube;
	public static Item MFFSProjectorTypwall;
	public static Item MFFSProjectorTypdeflector;
	public static Item MFFSProjectorTyptube;
	public static Item MFFSProjectorTypcontainment;
	public static Item MFFSProjectorTypAdvCube;
	public static Item MFFSProjectorTypdiagowall;
	public static Item MFFSProjectorOptionZapper;
	public static Item MFFSProjectorOptionSubwater;
	public static Item MFFSProjectorOptionDome;
	public static Item MFFSProjectorOptionCutter;
	public static Item MFFSProjectorOptionMoobEx;
	public static Item MFFSProjectorOptionDefenceStation;
	public static Item MFFSProjectorOptionForceFieldJammer;
	public static Item MFFSProjectorOptionCamouflage;
	public static Item MFFSProjectorOptionFieldFusion;
	public static Item MFFSProjectorFFDistance;
	public static Item MFFSProjectorFFStrenght;

	public static int MonazitOreworldamount = 4;
	public static int forcefieldblockcost;
	public static int forcefieldblockcreatemodifier;
	public static int forcefieldblockzappermodifier;
	public static int forcefieldtransportcost;
	public static int forcefieldmaxblockpeerTick;
	public static boolean forcefieldremoveonlywaterandlava;
	public static boolean influencedbyothermods;
	public static boolean adventuremap;

	public static int ForceciumWorkCylce;
	public static int ForceciumCellWorkCylce;
	public static int ExtractorPassForceEnergyGenerate;
	public static int DefenceStationKillForceEnergy;
	public static int DefenceStationSearchForceEnergy;
	public static int DefenceStationScannForceEnergy;
	public static String Admin;

	@SidedProxy(clientSide = "mffs.client.ClientProxy", serverSide = "mffs.common.CommonProxy")
	public static CommonProxy proxy;

	@Mod.Instance(ModularForceFieldSystem.ID)
	public static ModularForceFieldSystem instance;

	public static Logger LOGGER = Logger.getLogger(NAME);

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER.setParent(FMLLog.getLogger());

		if (initiateModule("IC2"))
		{
			MFFSProperties.MODULE_IC2 = true;
		}
		if (initiateModule("BasicComponents"))
		{
			MFFSProperties.MODULE_UE = true;
		}
		if (initiateModule("Buildcraft|Core"))
		{
			MFFSProperties.MODULE_BUILDCRAFT = true;
		}
		if (initiateModule("EE3"))
		{
			MFFSProperties.MODULE_EE = true;
		}
		if (initiateModule("ThermalExpansion"))
		{
			MFFSProperties.MODULE_THERMAL_EXPANSION = true;
		}

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(this.proxy);

		Modstats.instance().getReporter().registerMod(this);

		if (MFFSProperties.MODULE_EE)
		{
			MinecraftForge.EVENT_BUS.register(new EE3Event());
		}

		TickRegistry.registerScheduledTickHandler(new ForceFieldClientUpdatehandler(), Side.CLIENT);
		TickRegistry.registerScheduledTickHandler(new ForceFieldServerUpdatehandler(), Side.SERVER);

		try
		{
			CONFIGURATION.load();
			TAB = new MFFSCreativeTab(CreativeTabs.getNextID(), "MFFS");

			Property prop_graphicstyle = CONFIGURATION.get("general", "GraphicStyle", 1);
			prop_graphicstyle.comment = "Graphical style. 1 for UE Style, 2 for IC2 Style.";
			MFFSProperties.graphicstyle = prop_graphicstyle.getInt(1);

			Property chunckloader_prop = CONFIGURATION.get("general", "Chunkloader", true);
			chunckloader_prop.comment = "Set this to false to turn off the MFFS Chuncloader ability";
			MFFSProperties.chunckloader = chunckloader_prop.getBoolean(true);

			Property DefSationNPCScannoti = CONFIGURATION.get("general", "DefenceStationNPCScannnotification", false);
			DefSationNPCScannoti.comment = "Set this to true to turn off the DefenceStation notification is in NPC Mode";
			MFFSProperties.defenseStationNPCNotification = DefSationNPCScannoti.getBoolean(false);

			Property zapperParticles = CONFIGURATION.get("general", "renderZapperParticles", true);
			zapperParticles.comment = "Set this to false to turn off the small smoke particles present around TouchDamage enabled ForceFields.";
			MFFSProperties.showZapperParticles = zapperParticles.getBoolean(true);

			Property uumatterForciciumprop = CONFIGURATION.get("general", "uumatterForcicium", true);
			uumatterForciciumprop.comment = "Add IC2 UU-Matter Recipes for Forcicium";
			MFFSProperties.uumatterForcicium = uumatterForciciumprop.getBoolean(true);

			Property monazitWorldAmount = CONFIGURATION.get("general", "MonazitOreWorldGen", 4);
			monazitWorldAmount.comment = "Controls the size of the ore node that Monazit Ore will generate in";
			MonazitOreworldamount = monazitWorldAmount.getInt(4);

			Property adminList = CONFIGURATION.get("general", "ForceFieldMaster", "nobody");
			adminList.comment = "Add users to this list to give them admin permissions split by ;";
			Admin = adminList.value;

			Property influencedByOther = CONFIGURATION.get("general", "influencedbyothermods", true);
			influencedByOther.comment = "Should MFFS be influenced by other mods. e.g. ICBM's EMP";
			influencedbyothermods = Boolean.valueOf(influencedByOther.getBoolean(true));

			Property ffRemoveWaterLavaOnly = CONFIGURATION.get("general", "forcefieldremoveonlywaterandlava", false);
			ffRemoveWaterLavaOnly.comment = "Should forcefields only remove water and lava when sponge is enabled?";
			forcefieldremoveonlywaterandlava = Boolean.valueOf(ffRemoveWaterLavaOnly.getBoolean(false));

			Property feTransportCost = CONFIGURATION.get("general", "forcefieldtransportcost", 10000);
			feTransportCost.comment = "How much FE should it cost to transport through a field?";
			forcefieldtransportcost = feTransportCost.getInt(10000);

			Property feFieldBlockCost = CONFIGURATION.get("general", "forcefieldblockcost", 1);
			feFieldBlockCost.comment = "How much upkeep FE cost a default ForceFieldblock per second";
			forcefieldblockcost = feFieldBlockCost.getInt(1);

			Property BlockCreateMod = CONFIGURATION.get("general", "forcefieldblockcreatemodifier", 10);
			BlockCreateMod.comment = "Energy need for create a ForceFieldblock (forcefieldblockcost*forcefieldblockcreatemodifier)";
			forcefieldblockcreatemodifier = BlockCreateMod.getInt(10);

			Property ffZapperMod = CONFIGURATION.get("general", "forcefieldblockzappermodifier", 2);
			ffZapperMod.comment = "Energy need multiplier used when the zapper option is installed";
			forcefieldblockzappermodifier = ffZapperMod.getInt(2);

			Property maxFFGenPerTick = CONFIGURATION.get("general", "forcefieldmaxblockpeerTick", 5000);
			maxFFGenPerTick.comment = "How many field blocks can be generated per tick?";
			forcefieldmaxblockpeerTick = maxFFGenPerTick.getInt(5000);

			Property fcWorkCycle = CONFIGURATION.get("general", "ForceciumWorkCylce", 250);
			fcWorkCycle.comment = "WorkCycle amount of Forcecium inside a Extractor";
			ForceciumWorkCylce = fcWorkCycle.getInt(250);

			Property fcCellWorkCycle = CONFIGURATION.get("general", "ForceciumCellWorkCylce", 230);
			fcCellWorkCycle.comment = "WorkCycle amount of Forcecium Cell inside a Extractor";
			ForceciumCellWorkCylce = fcCellWorkCycle.getInt(230);

			Property extractorPassFEGen = CONFIGURATION.get("general", "ExtractorPassForceEnergyGenerate", 12000);
			extractorPassFEGen.comment = "How many ForceEnergy generate per WorkCycle";
			ExtractorPassForceEnergyGenerate = extractorPassFEGen.getInt(12000);

			ExtractorPassForceEnergyGenerate = ExtractorPassForceEnergyGenerate / 4000 * 4000;

			Property defStationKillCost = CONFIGURATION.get("general", "DefenceStationKillForceEnergy", 10000);
			defStationKillCost.comment = "How much FE does the AreaDefenseStation when killing someone";
			DefenceStationKillForceEnergy = defStationKillCost.getInt(10000);

			Property defStationSearchCost = CONFIGURATION.get("general", "DefenceStationSearchForceEnergy", 1000);
			defStationSearchCost.comment = "How much FE does the AreaDefenseStation when searching someone for contraband";
			DefenceStationSearchForceEnergy = defStationSearchCost.getInt(1000);

			Property defStationScannCost = CONFIGURATION.get("general", "DefenceStationScannForceEnergy", 10);
			defStationScannCost.comment = "How much FE does the AreaDefenseStation when Scann for Targets (amount * range / tick)";
			DefenceStationScannForceEnergy = defStationScannCost.getInt(10);

			Property Adventuremap = CONFIGURATION.get("general", "adventuremap", false);
			Adventuremap.comment = "Set MFFS to AdventureMap Mode Extractor need no Forcecium and ForceField have no click damage";
			adventuremap = Boolean.valueOf(Adventuremap.getBoolean(false));

			blockConverter = new BlockConverter(CONFIGURATION.getBlock("MFFSForceEnergyConverter", MFFSProperties.block_Converter_ID).getInt(MFFSProperties.block_Converter_ID)).setBlockName("MFFSForceEnergyConverter");
			blockExtractor = new BlockExtractor(CONFIGURATION.getBlock("MFFSExtractor", MFFSProperties.block_Extractor_ID).getInt(MFFSProperties.block_Extractor_ID)).setBlockName("MFFSExtractor");
			blockMonaziteOre = new BlockMonaziteOre(CONFIGURATION.getBlock("MFFSMonazitOre", MFFSProperties.block_MonazitOre_ID).getInt(MFFSProperties.block_MonazitOre_ID)).setBlockName("MFFSMonazitOre");
			blockDefenceStation = new BlockDefenseStation(CONFIGURATION.getBlock("MFFSDefenceStation", MFFSProperties.block_DefenseStation_ID).getInt(MFFSProperties.block_DefenseStation_ID)).setBlockName("MFFSDefenceStation");
			blockCapacitor = new BlockCapacitor(CONFIGURATION.getBlock("MFFSCapacitor", MFFSProperties.block_Capacitor_ID).getInt(MFFSProperties.block_Capacitor_ID)).setBlockName("MFFSCapacitor");
			blockProjector = new BlockProjector(CONFIGURATION.getBlock("MFFSProjector", MFFSProperties.block_Projector_ID).getInt(MFFSProperties.block_Projector_ID)).setBlockName("MFFSProjector");
			blockForceField = new BlockForceField(CONFIGURATION.getBlock("MFFSFieldblock", MFFSProperties.block_Field_ID).getInt(MFFSProperties.block_Field_ID));
			blockSecurityStorage = new BlockSecurityStorage(CONFIGURATION.getBlock("MFFSSecurtyStorage", MFFSProperties.block_SecureStorage_ID).getInt(MFFSProperties.block_SecureStorage_ID)).setBlockName("MFFSSecurtyStorage");
			blockSecurityStation = new BlockSecurityStation(MFFSProperties.block_SecurityStation_ID, 16);
			blockControlSystem = new BlockControlSystem(CONFIGURATION.getBlock("MFFSControlSystem", MFFSProperties.block_ControlSystem).getInt(MFFSProperties.block_ControlSystem)).setBlockName("MFFSControlSystem");

			MFFSProjectorFFDistance = new ItemProjectorFieldModulatorDistance(CONFIGURATION.getItem("item", "itemProjectorFFDistance", MFFSProperties.item_AltDistance_ID).getInt(MFFSProperties.item_AltDistance_ID)).setItemName("itemProjectorFFDistance");
			MFFSProjectorFFStrenght = new ItemProjectorFieldModulatorStrength(CONFIGURATION.getItem("item", "itemProjectorFFStrength", MFFSProperties.item_AltStrength_ID).getInt(MFFSProperties.item_AltStrength_ID)).setItemName("itemProjectorFFStrength");
			itemFocusMatix = new ItemProjectorFocusMatrix(CONFIGURATION.getItem("item", "itemPorjectorFocusmatrix", MFFSProperties.item_FocusMatrix_ID).getInt(MFFSProperties.item_FocusMatrix_ID)).setItemName("itemPorjectorFocusmatrix");
			itemPowerCrystal = new ItemForcePowerCrystal(CONFIGURATION.getItem("item", "itemForcePowerCrystal", MFFSProperties.item_FPCrystal_ID).getInt(MFFSProperties.item_FPCrystal_ID)).setItemName("itemForcePowerCrystal");
			itemForcicium = new ItemForcicium(CONFIGURATION.getItem("item", "itemForcicium", MFFSProperties.item_Forcicium_ID).getInt(MFFSProperties.item_Forcicium_ID)).setItemName("itemForcicium");
			itemForcicumCell = new ItemForcicumCell(CONFIGURATION.getItem("item", "itemForcicumCell", MFFSProperties.item_ForciciumCell_ID).getInt(MFFSProperties.item_ForciciumCell_ID)).setItemName("itemForcicumCell");

			MFFSProjectorTypdiagowall = new ItemProjectorModulediagonallyWall(CONFIGURATION.getItem("item", "itemProjectorModulediagonallyWall", MFFSProperties.item_ModDiag_ID).getInt(MFFSProperties.item_ModDiag_ID)).setItemName("itemProjectorModulediagonallyWall");
			MFFSProjectorTypsphere = new ItemProjectorModuleSphere(CONFIGURATION.getItem("item", "itemProjectorTypsphere", MFFSProperties.item_ModSphere_ID).getInt(MFFSProperties.item_ModSphere_ID)).setItemName("itemProjectorTypsphere");
			MFFSProjectorTypkube = new ItemProjectorModuleCube(CONFIGURATION.getItem("item", "itemProjectorTypkube", MFFSProperties.item_ModCube_ID).getInt(MFFSProperties.item_ModCube_ID)).setItemName("itemProjectorTypkube");
			MFFSProjectorTypwall = new ItemProjectorModuleWall(CONFIGURATION.getItem("item", "itemProjectorTypwall", MFFSProperties.item_ModWall_ID).getInt(MFFSProperties.item_ModWall_ID)).setItemName("itemProjectorTypwall");
			MFFSProjectorTypdeflector = new ItemProjectorModuleDeflector(CONFIGURATION.getItem("item", "itemProjectorTypdeflector", MFFSProperties.item_ModDeflector_ID).getInt(MFFSProperties.item_ModDeflector_ID)).setItemName("itemProjectorTypdeflector");
			MFFSProjectorTyptube = new ItemProjectorModuleTube(CONFIGURATION.getItem("item", "itemProjectorTyptube", MFFSProperties.item_ModTube_ID).getInt(MFFSProperties.item_ModTube_ID)).setItemName("itemProjectorTyptube");
			MFFSProjectorTypcontainment = new ItemProjectorModuleContainment(CONFIGURATION.getItem("item", "itemProjectorModuleContainment", MFFSProperties.item_ModContainment_ID).getInt(MFFSProperties.item_ModContainment_ID)).setItemName("itemProjectorModuleContainment");
			MFFSProjectorTypAdvCube = new ItemProjectorModuleAdvCube(CONFIGURATION.getItem("item", "itemProjectorModuleAdvCube", MFFSProperties.item_ModAdvCube_ID).getInt(MFFSProperties.item_ModAdvCube_ID)).setItemName("itemProjectorModuleAdvCube");

			MFFSProjectorOptionZapper = new ItemProjectorOptionTouchDamage(CONFIGURATION.getItem("item", "itemupgradeprozapper", MFFSProperties.item_OptTouchHurt_ID).getInt(MFFSProperties.item_OptTouchHurt_ID)).setItemName("itemupgradeprozapper");
			MFFSProjectorOptionSubwater = new ItemProjectorOptionSponge(CONFIGURATION.getItem("item", "itemupgradeprosubwater", MFFSProperties.item_OptSponge_ID).getInt(MFFSProperties.item_OptSponge_ID)).setItemName("itemupgradeprosubwater");
			MFFSProjectorOptionDome = new ItemProjectorOptionFieldManipulator(CONFIGURATION.getItem("item", "itemupgradeprodome", MFFSProperties.item_OptManipulator_ID).getInt(MFFSProperties.item_OptManipulator_ID)).setItemName("itemupgradeprodome");
			MFFSProjectorOptionCutter = new ItemProjectorOptionBlockBreaker(CONFIGURATION.getItem("item", "itemUpgradeprocutter", MFFSProperties.item_OptBlockBreaker_ID).getInt(MFFSProperties.item_OptBlockBreaker_ID)).setItemName("itemUpgradeprocutter");
			MFFSProjectorOptionDefenceStation = new ItemProjectorOptionDefenseStation(CONFIGURATION.getItem("item", "itemProjectorOptiondefencestation", MFFSProperties.item_OptDefense_ID).getInt(MFFSProperties.item_OptDefense_ID)).setItemName("itemProjectorOptiondefencestation");
			MFFSProjectorOptionMoobEx = new ItemProjectorOptionMobDefence(CONFIGURATION.getItem("item", "itemProjectorOptionMoobEx", MFFSProperties.item_OptMobDefense_ID).getInt(MFFSProperties.item_OptMobDefense_ID)).setItemName("itemProjectorOptionMoobEx");
			MFFSProjectorOptionForceFieldJammer = new ItemProjectorOptionForceFieldJammer(CONFIGURATION.getItem("item", "itemProjectorOptionFFJammer", MFFSProperties.item_OptJammer_ID).getInt(MFFSProperties.item_OptJammer_ID)).setItemName("itemProjectorOptionFFJammer");
			MFFSProjectorOptionCamouflage = new ItemProjectorOptionCamoflage(CONFIGURATION.getItem("item", "itemProjectorOptionCamoflage", MFFSProperties.item_OptCamouflage_ID).getInt(MFFSProperties.item_OptCamouflage_ID)).setItemName("itemProjectorOptionCamoflage");
			MFFSProjectorOptionFieldFusion = new ItemProjectorOptionFieldFusion(CONFIGURATION.getItem("item", "itemProjectorOptionFieldFusion", MFFSProperties.item_OptFusion_ID).getInt(MFFSProperties.item_OptFusion_ID)).setItemName("itemProjectorOptionFieldFusion");

			MFFSitemcardempty = new ItemCardEmpty(CONFIGURATION.getItem("item", "itemcardempty", MFFSProperties.item_BlankCard_ID).getInt(MFFSProperties.item_BlankCard_ID)).setItemName("itemcardempty");
			MFFSitemfc = new ItemCardPowerLink(CONFIGURATION.getItem("item", "itemfc", MFFSProperties.item_CardPowerLink_ID).getInt(MFFSProperties.item_CardPowerLink_ID)).setItemName("itemfc");
			MFFSItemIDCard = new ItemCardPersonalID(CONFIGURATION.getItem("item", "itemIDCard", MFFSProperties.item_CardPersonalID_ID).getInt(MFFSProperties.item_CardPersonalID_ID)).setItemName("itemIDCard");
			MFFSItemSecLinkCard = new ItemCardSecurityLink(CONFIGURATION.getItem("item", "itemSecLinkCard", MFFSProperties.item_CardSecurityLink_ID).getInt(MFFSProperties.item_CardSecurityLink_ID)).setItemName("itemSecLinkCard");
			MFFSitemInfinitePowerCard = new ItemCardPower(CONFIGURATION.getItem("item", "itemInfinitePower", MFFSProperties.item_infPowerCard_ID).getInt(MFFSProperties.item_infPowerCard_ID)).setItemName("itemInfPowerCard");
			MFFSAccessCard = new ItemAccessCard(CONFIGURATION.getItem("item", "itemAccessCard", MFFSProperties.item_CardAccess_ID).getInt(MFFSProperties.item_CardAccess_ID)).setItemName("itemAccessCard");
			MFFSitemDataLinkCard = new ItemCardDataLink(CONFIGURATION.getItem("item", "itemCardDataLink", MFFSProperties.item_CardDataLink_ID).getInt(MFFSProperties.item_CardDataLink_ID)).setItemName("itemCardDataLink");

			itemWrench = new ItemWrench(CONFIGURATION.getItem("item", "itemWrench", MFFSProperties.item_MTWrench_ID).getInt(MFFSProperties.item_MTWrench_ID)).setItemName("itemWrench");
			itemSwitch = new ItemSwitch(CONFIGURATION.getItem("item", "itemSwitch", MFFSProperties.item_MTSwitch_ID).getInt(MFFSProperties.item_MTSwitch_ID)).setItemName("itemSwitch");
			itemFieldTeleporter = new ItemFieldtransporter(CONFIGURATION.getItem("item", "itemForceFieldsync", MFFSProperties.item_MTFieldTransporter_ID).getInt(MFFSProperties.item_MTFieldTransporter_ID)).setItemName("itemForceFieldsync");
			itemMFDidtool = new ItemPersonalIDWriter(CONFIGURATION.getItem("item", "ItemMFDIDwriter", MFFSProperties.item_MTIDWriter_ID).getInt(MFFSProperties.item_MTIDWriter_ID)).setItemName("ItemMFDIDwriter");
			MFFSitemMFDdebugger = new ItemDebugger(CONFIGURATION.getItem("item", "itemMFDdebugger", MFFSProperties.item_MTDebugger_ID).getInt(MFFSProperties.item_MTDebugger_ID)).setItemName("itemMFDdebugger");
			MFFSitemManuelBook = new ItemManuelBook(CONFIGURATION.getItem("item", "itemManuelBook", MFFSProperties.item_MTManual_ID).getInt(MFFSProperties.item_MTManual_ID)).setItemName("itemManuelBook");

			MFFSitemupgradeexctractorboost = new ItemExtractorUpgradeBooster(CONFIGURATION.getItem("item", "itemextractorbooster", MFFSProperties.item_upgradeBoost_ID).getInt(MFFSProperties.item_upgradeBoost_ID)).setItemName("itemextractorbooster");
			MFFSitemupgradecaprange = new ItemCapacitorUpgradeRange(CONFIGURATION.getItem("item", "itemupgradecaprange", MFFSProperties.item_upgradeRange_ID).getInt(MFFSProperties.item_upgradeRange_ID)).setItemName("itemupgradecaprange");
			MFFSitemupgradecapcap = new ItemCapacitorUpgradeCapacity(CONFIGURATION.getItem("item", "itemupgradecapcap", MFFSProperties.item_upgradeCap_ID).getInt(MFFSProperties.item_upgradeCap_ID)).setItemName("itemupgradecapcap");
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "ModularForceFieldSystem has a problem loading it's configuration", new Object[0]);

			System.out.println(e.getMessage());
		}
		finally
		{
			CONFIGURATION.save();
		}
	}

	@Mod.Init
	public void load(FMLInitializationEvent evt)
	{
		System.out.println(NAME + " has loaded: " + TranslationHelper.loadLanguages(RESOURCE_DIRECTORY + "language/", new String[] { "en_US" }));

		GameRegistry.registerBlock(blockMonaziteOre, "MFFSMonazitOre");
		GameRegistry.registerBlock(blockForceField, "MFFSFieldblock");
		GameRegistry.registerTileEntity(TileEntityForceField.class, "MFFSForceField");

		MFFSMachine.initialize();
		ProjectorTyp.initialize();
		ProjectorOptions.initialize();

		NetworkRegistry.instance().registerGuiHandler(instance, proxy);

		proxy.registerRenderInformation();
		proxy.registerTileEntitySpecialRenderer();

		GameRegistry.registerWorldGenerator(new MFFSWorldGenerator());

		LanguageRegistry.instance().addNameForObject(MFFSitemInfinitePowerCard, "en_US", "MFFS Infinite Power Card");

		LanguageRegistry.instance().addNameForObject(MFFSitemupgradeexctractorboost, "en_US", "MFFS Extractor Booster");

		LanguageRegistry.instance().addNameForObject(blockMonaziteOre, "en_US", "Monazit Ore");

		LanguageRegistry.instance().addNameForObject(itemForcicumCell, "en_US", "MFFS compact Forcicium Cell");

		LanguageRegistry.instance().addNameForObject(itemForcicium, "en_US", "Forcicium");

		LanguageRegistry.instance().addNameForObject(itemPowerCrystal, "en_US", "MFFS Force Energy Crystal");

		LanguageRegistry.instance().addNameForObject(itemSwitch, "en_US", "MFFS MultiTool <Switch>");

		LanguageRegistry.instance().addNameForObject(itemWrench, "en_US", "MFFS MultiTool <Wrench>");

		LanguageRegistry.instance().addNameForObject(MFFSitemManuelBook, "en_US", "MFFS MultiTool <Guide>");

		LanguageRegistry.instance().addNameForObject(itemFocusMatix, "en_US", "MFFS Projector Focus Matrix");

		LanguageRegistry.instance().addNameForObject(itemFieldTeleporter, "en_US", "MFFS MultiTool <Field Teleporter>");

		LanguageRegistry.instance().addNameForObject(MFFSitemDataLinkCard, "en_US", "MFFS Card <Data Link> ");

		LanguageRegistry.instance().addNameForObject(MFFSAccessCard, "en_US", "MFFS Card <Access license> ");

		LanguageRegistry.instance().addNameForObject(MFFSitemcardempty, "en_US", "MFFS Card <blank> ");

		LanguageRegistry.instance().addNameForObject(MFFSitemfc, "en_US", "MFFS Card <Power Link>");

		LanguageRegistry.instance().addNameForObject(MFFSItemIDCard, "en_US", "MFFS Card <Personal ID>");

		LanguageRegistry.instance().addNameForObject(MFFSItemSecLinkCard, "en_US", "MFFS Card <Security Station Link> ");

		LanguageRegistry.instance().addNameForObject(MFFSitemMFDdebugger, "en_US", "MFFS MultiTool <Debugger>");

		LanguageRegistry.instance().addNameForObject(itemMFDidtool, "en_US", "MFFS MultiTool <PersonalID & Data Link  Coder>");

		LanguageRegistry.instance().addNameForObject(MFFSitemupgradecaprange, "en_US", "MFFS Capacitor Upgrade <Range> ");

		LanguageRegistry.instance().addNameForObject(MFFSitemupgradecapcap, "en_US", "MFFS Capacitor Upgrade <Capacity> ");

		LanguageRegistry.instance().addNameForObject(MFFSProjectorFFDistance, "en_US", "MFFS Projector Field Modulator <distance>");

		LanguageRegistry.instance().addNameForObject(MFFSProjectorFFStrenght, "en_US", "MFFS Projector Field Modulator <strength>");

		LanguageRegistry.instance().addStringLocalization("death.areaDefense", "en_US", "%1$s disregarded warnings and was fried");
		LanguageRegistry.instance().addStringLocalization("death.fieldShock", "en_US", "%1$s was fried by a forcefield");
		LanguageRegistry.instance().addStringLocalization("death.fieldDefense", "en_US", "%1$s was fried");
	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent evt)
	{
		MFFSRecipes.init();
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, new MFFSChunkloadCallback());
	}

	public void initbuildcraftPlugin()
	{
		System.out.println("[ModularForceFieldSystem] Loading module for Buildcraft");
		try
		{
			Class.forName("buildcraft.core.Version");
			MFFSProperties.MODULE_BUILDCRAFT = Boolean.valueOf(true);
		}
		catch (Throwable t)
		{
			System.out.println("[ModularForceFieldSystem] Module not loaded: Buildcraft not found");
		}
	}

	public void ThermalExpansionPlugin()
	{
		System.out.println("[ModularForceFieldSystem] Loading module for ThermalExpansion");
		try
		{
			Class.forName("thermalexpansion.ThermalExpansion");
			MFFSProperties.MODULE_THERMAL_EXPANSION = Boolean.valueOf(true);
		}
		catch (Throwable t)
		{
			System.out.println("[ModularForceFieldSystem] Module not loaded: ThermalExpansion not found");
		}
	}

	public void initEE3Plugin()
	{
		System.out.println("[ModularForceFieldSystem] Loading module for EE3");
		try
		{
			Class.forName("com.pahimar.ee3.event.ActionRequestEvent");
			MFFSProperties.MODULE_EE = Boolean.valueOf(true);
		}
		catch (Throwable t)
		{
			System.out.println("[ModularForceFieldSystem] Module not loaded: EE3 not found");
		}
	}

	public void initUEPlugin()
	{
		System.out.println("[ModularForceFieldSystem] Loading module for Universal Electricity");
		try
		{
			Class.forName("basiccomponents.common.item.ItemBasic");
			MFFSProperties.MODULE_UE = Boolean.valueOf(true);
		}
		catch (Throwable t)
		{
			System.out.println("[ModularForceFieldSystem] Module not loaded: Universal Electricity not found");
		}
	}

	public boolean initiateModule(String modname)
	{
		if (Loader.isModLoaded(modname))
		{
			LOGGER.info("Loaded module for: " + modname);
			return true;
		}
		else
		{
			LOGGER.info("Module not loaded: " + modname);
			return false;
		}
	}

	public class MFFSChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback
	{
		public MFFSChunkloadCallback()
		{
		}

		public void ticketsLoaded(List<Ticket> tickets, World world)
		{
			for (ForgeChunkManager.Ticket ticket : tickets)
			{
				int MaschineX = ticket.getModData().getInteger("MaschineX");
				int MaschineY = ticket.getModData().getInteger("MaschineY");
				int MaschineZ = ticket.getModData().getInteger("MaschineZ");
				TileEntityMFFS Machines = (TileEntityMFFS) world.getBlockTileEntity(MaschineX, MaschineY, MaschineZ);

				Machines.forceChunkLoading(ticket);
			}
		}

		public List ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
		{
			List validTickets = Lists.newArrayList();
			for (ForgeChunkManager.Ticket ticket : tickets)
			{
				int MaschineX = ticket.getModData().getInteger("MaschineX");
				int MaschineY = ticket.getModData().getInteger("MaschineY");
				int MaschineZ = ticket.getModData().getInteger("MaschineZ");

				TileEntity tileEntity = world.getBlockTileEntity(MaschineX, MaschineY, MaschineZ);

				if ((tileEntity instanceof TileEntityMFFS))
				{
					validTickets.add(ticket);
				}
			}
			return validTickets;
		}
	}
}
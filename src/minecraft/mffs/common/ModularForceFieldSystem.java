package mffs.common;

import java.io.File;
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
import mffs.common.modules.ItemModuleAdvancedCube;
import mffs.common.modules.ItemModuleContainment;
import mffs.common.modules.ItemModuleCube;
import mffs.common.modules.ItemModuleDeflector;
import mffs.common.modules.ItemModuleDiagonalWall;
import mffs.common.modules.ItemModuleSphere;
import mffs.common.modules.ItemModuleTube;
import mffs.common.modules.ItemModuleWall;
import mffs.common.multitool.ItemFieldTransporter;
import mffs.common.multitool.ItemManuelBook;
import mffs.common.multitool.ItemMultitoolSwitch;
import mffs.common.multitool.ItemMultitoolWriter;
import mffs.common.multitool.ItemWrench;
import mffs.common.options.ItemOptionAntibiotic;
import mffs.common.options.ItemOptionBlockBreaker;
import mffs.common.options.ItemOptionCamoflage;
import mffs.common.options.ItemOptionDefenseStation;
import mffs.common.options.ItemOptionFieldFusion;
import mffs.common.options.ItemOptionFieldManipulator;
import mffs.common.options.ItemOptionJammer;
import mffs.common.options.ItemOptionShock;
import mffs.common.options.ItemOptionSponge;
import mffs.common.tileentity.TileEntityForceField;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.upgrade.ItemCapacitorUpgradeCapacity;
import mffs.common.upgrade.ItemCapacitorUpgradeRange;
import mffs.common.upgrade.ItemExtractorUpgradeBooster;
import mffs.common.upgrade.ItemModuleDistance;
import mffs.common.upgrade.ItemModuleStrength;
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
	public static final String VERSION = "2.3.0";

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
	public static Item itemModuleSphere;
	public static Item itemModuleCube;
	public static Item itemModuleWall;
	public static Item itemModuleDeflector;
	public static Item itemModuleTube;
	public static Item itemModuleContainment;
	public static Item itemModuleAdvancedCube;
	public static Item itemModuleDiagonalWall;
	public static Item MFFSProjectorOptionZapper;
	public static Item MFFSProjectorOptionSubwater;
	public static Item MFFSProjectorOptionDome;
	public static Item MFFSProjectorOptionCutter;
	public static Item MFFSProjectorOptionMoobEx;
	public static Item MFFSProjectorOptionDefenceStation;
	public static Item MFFSProjectorOptionForceFieldJammer;
	public static Item MFFSProjectorOptionCamouflage;
	public static Item MFFSProjectorOptionFieldFusion;
	public static Item itemModuleDistance;
	public static Item itemModuleStrength;

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

			Property prop_graphicstyle = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "GraphicStyle", 1);
			prop_graphicstyle.comment = "Graphical style. 1 for UE Style, 2 for IC2 Style.";
			MFFSProperties.graphicstyle = prop_graphicstyle.getInt(1);

			Property chunckloader_prop = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "Chunkloader", true);
			chunckloader_prop.comment = "Set this to false to turn off the MFFS Chuncloader ability";
			MFFSProperties.chunckloader = chunckloader_prop.getBoolean(true);

			Property DefSationNPCScannoti = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "DefenceStationNPCScannnotification", false);
			DefSationNPCScannoti.comment = "Set this to true to turn off the DefenceStation notification is in NPC Mode";
			MFFSProperties.defenseStationNPCNotification = DefSationNPCScannoti.getBoolean(false);

			Property zapperParticles = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "renderZapperParticles", true);
			zapperParticles.comment = "Set this to false to turn off the small smoke particles present around TouchDamage enabled ForceFields.";
			MFFSProperties.showZapperParticles = zapperParticles.getBoolean(true);

			Property uumatterForciciumprop = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "uumatterForcicium", true);
			uumatterForciciumprop.comment = "Add IC2 UU-Matter Recipes for Forcicium";
			MFFSProperties.uumatterForcicium = uumatterForciciumprop.getBoolean(true);

			Property monazitWorldAmount = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "MonazitOreWorldGen", 4);
			monazitWorldAmount.comment = "Controls the size of the ore node that Monazit Ore will generate in";
			MonazitOreworldamount = monazitWorldAmount.getInt(4);

			Property adminList = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "ForceFieldMaster", "nobody");
			adminList.comment = "Add users to this list to give them admin permissions split by ;";
			Admin = adminList.value;

			Property influencedByOther = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "influencedbyothermods", true);
			influencedByOther.comment = "Should MFFS be influenced by other mods. e.g. ICBM's EMP";
			influencedbyothermods = Boolean.valueOf(influencedByOther.getBoolean(true));

			Property ffRemoveWaterLavaOnly = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "forcefieldremoveonlywaterandlava", false);
			ffRemoveWaterLavaOnly.comment = "Should forcefields only remove water and lava when sponge is enabled?";
			forcefieldremoveonlywaterandlava = Boolean.valueOf(ffRemoveWaterLavaOnly.getBoolean(false));

			Property feTransportCost = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "forcefieldtransportcost", 10000);
			feTransportCost.comment = "How much FE should it cost to transport through a field?";
			forcefieldtransportcost = feTransportCost.getInt(10000);

			Property feFieldBlockCost = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "forcefieldblockcost", 1);
			feFieldBlockCost.comment = "How much upkeep FE cost a default ForceFieldblock per second";
			forcefieldblockcost = feFieldBlockCost.getInt(1);

			Property BlockCreateMod = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "forcefieldblockcreatemodifier", 10);
			BlockCreateMod.comment = "Energy need for create a ForceFieldblock (forcefieldblockcost*forcefieldblockcreatemodifier)";
			forcefieldblockcreatemodifier = BlockCreateMod.getInt(10);

			Property ffZapperMod = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "forcefieldblockzappermodifier", 2);
			ffZapperMod.comment = "Energy need multiplier used when the zapper option is installed";
			forcefieldblockzappermodifier = ffZapperMod.getInt(2);

			Property maxFFGenPerTick = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "forcefieldmaxblockpeerTick", 5000);
			maxFFGenPerTick.comment = "How many field blocks can be generated per tick?";
			forcefieldmaxblockpeerTick = maxFFGenPerTick.getInt(5000);

			Property fcWorkCycle = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "ForceciumWorkCylce", 250);
			fcWorkCycle.comment = "WorkCycle amount of Forcecium inside a Extractor";
			ForceciumWorkCylce = fcWorkCycle.getInt(250);

			Property fcCellWorkCycle = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "ForceciumCellWorkCylce", 230);
			fcCellWorkCycle.comment = "WorkCycle amount of Forcecium Cell inside a Extractor";
			ForceciumCellWorkCylce = fcCellWorkCycle.getInt(230);

			Property extractorPassFEGen = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "ExtractorPassForceEnergyGenerate", 12000);
			extractorPassFEGen.comment = "How many ForceEnergy generate per WorkCycle";
			ExtractorPassForceEnergyGenerate = extractorPassFEGen.getInt(12000);

			ExtractorPassForceEnergyGenerate = ExtractorPassForceEnergyGenerate / 4000 * 4000;

			Property defStationKillCost = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "DefenceStationKillForceEnergy", 10000);
			defStationKillCost.comment = "How much FE does the AreaDefenseStation when killing someone";
			DefenceStationKillForceEnergy = defStationKillCost.getInt(10000);

			Property defStationSearchCost = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "DefenceStationSearchForceEnergy", 1000);
			defStationSearchCost.comment = "How much FE does the AreaDefenseStation when searching someone for contraband";
			DefenceStationSearchForceEnergy = defStationSearchCost.getInt(1000);

			Property defStationScannCost = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "DefenceStationScannForceEnergy", 10);
			defStationScannCost.comment = "How much FE does the AreaDefenseStation when Scann for Targets (amount * range / tick)";
			DefenceStationScannForceEnergy = defStationScannCost.getInt(10);

			Property Adventuremap = CONFIGURATION.get(CONFIGURATION.CATEGORY_GENERAL, "adventuremap", false);
			Adventuremap.comment = "Set MFFS to AdventureMap Mode Extractor need no Forcecium and ForceField have no click damage";
			adventuremap = Boolean.valueOf(Adventuremap.getBoolean(false));

			blockConverter = new BlockConverter(MFFSProperties.block_Converter_ID);
			blockExtractor = new BlockExtractor(MFFSProperties.block_Extractor_ID);
			blockMonaziteOre = new BlockMonaziteOre(MFFSProperties.block_MonazitOre_ID);
			blockDefenceStation = new BlockDefenseStation(MFFSProperties.block_DefenseStation_ID);
			blockCapacitor = new BlockCapacitor(MFFSProperties.block_Capacitor_ID);
			blockProjector = new BlockProjector(MFFSProperties.block_Projector_ID);
			blockForceField = new BlockForceField(MFFSProperties.block_Field_ID);
			blockSecurityStorage = new BlockSecurityStorage(MFFSProperties.block_SecureStorage_ID);
			blockSecurityStation = new BlockSecurityStation(MFFSProperties.block_SecurityStation_ID, 16);
			blockControlSystem = new BlockControlSystem(MFFSProperties.block_ControlSystem);

			itemModuleDistance = new ItemModuleDistance(MFFSProperties.item_AltDistance_ID);
			itemModuleStrength = new ItemModuleStrength(MFFSProperties.item_AltStrength_ID);
			itemFocusMatix = new ItemProjectorFocusMatrix(MFFSProperties.item_FocusMatrix_ID);
			itemPowerCrystal = new ItemForcePowerCrystal(MFFSProperties.item_FPCrystal_ID);
			itemForcicium = new ItemForcicium(MFFSProperties.item_Forcicium_ID);
			itemForcicumCell = new ItemForcicumCell(MFFSProperties.item_ForciciumCell_ID);

			itemModuleDiagonalWall = new ItemModuleDiagonalWall(MFFSProperties.item_ModDiag_ID);
			itemModuleSphere = new ItemModuleSphere(MFFSProperties.item_ModSphere_ID);
			itemModuleCube = new ItemModuleCube(MFFSProperties.item_ModCube_ID);
			itemModuleWall = new ItemModuleWall(MFFSProperties.item_ModWall_ID);
			itemModuleDeflector = new ItemModuleDeflector(MFFSProperties.item_ModDeflector_ID);
			itemModuleTube = new ItemModuleTube(MFFSProperties.item_ModTube_ID);
			itemModuleContainment = new ItemModuleContainment(MFFSProperties.item_ModContainment_ID);
			itemModuleAdvancedCube = new ItemModuleAdvancedCube(MFFSProperties.item_ModAdvCube_ID);

			MFFSProjectorOptionZapper = new ItemOptionShock(MFFSProperties.item_OptTouchHurt_ID);
			MFFSProjectorOptionSubwater = new ItemOptionSponge(MFFSProperties.item_OptSponge_ID);
			MFFSProjectorOptionDome = new ItemOptionFieldManipulator(MFFSProperties.item_OptManipulator_ID);
			MFFSProjectorOptionCutter = new ItemOptionBlockBreaker(MFFSProperties.item_OptBlockBreaker_ID);
			MFFSProjectorOptionDefenceStation = new ItemOptionDefenseStation(MFFSProperties.item_OptDefense_ID);
			MFFSProjectorOptionMoobEx = new ItemOptionAntibiotic(MFFSProperties.item_OptMobDefense_ID);
			MFFSProjectorOptionForceFieldJammer = new ItemOptionJammer(MFFSProperties.item_OptJammer_ID);
			MFFSProjectorOptionCamouflage = new ItemOptionCamoflage(MFFSProperties.item_OptCamouflage_ID);
			MFFSProjectorOptionFieldFusion = new ItemOptionFieldFusion(MFFSProperties.item_OptFusion_ID);

			MFFSitemcardempty = new ItemCardEmpty(MFFSProperties.item_BlankCard_ID);
			MFFSitemfc = new ItemCardPowerLink(MFFSProperties.item_CardPowerLink_ID);
			MFFSItemIDCard = new ItemCardPersonalID(MFFSProperties.item_CardPersonalID_ID);
			MFFSItemSecLinkCard = new ItemCardSecurityLink(MFFSProperties.item_CardSecurityLink_ID);
			MFFSitemInfinitePowerCard = new ItemCardPower(MFFSProperties.item_infPowerCard_ID);
			MFFSAccessCard = new ItemAccessCard(MFFSProperties.item_CardAccess_ID);
			MFFSitemDataLinkCard = new ItemCardDataLink(MFFSProperties.item_CardDataLink_ID);

			itemWrench = new ItemWrench(MFFSProperties.item_MTWrench_ID);
			itemSwitch = new ItemMultitoolSwitch(MFFSProperties.item_MTSwitch_ID);
			itemFieldTeleporter = new ItemFieldTransporter(MFFSProperties.item_MTFieldTransporter_ID);
			itemMFDidtool = new ItemMultitoolWriter(MFFSProperties.item_MTIDWriter_ID);
			MFFSitemManuelBook = new ItemManuelBook(MFFSProperties.item_MTManual_ID);

			MFFSitemupgradeexctractorboost = new ItemExtractorUpgradeBooster(MFFSProperties.item_upgradeBoost_ID);
			MFFSitemupgradecaprange = new ItemCapacitorUpgradeRange(MFFSProperties.item_upgradeRange_ID);
			MFFSitemupgradecapcap = new ItemCapacitorUpgradeCapacity(MFFSProperties.item_upgradeCap_ID);

			GameRegistry.registerBlock(blockMonaziteOre, "MFFSMonazitOre");
			GameRegistry.registerBlock(blockForceField, "MFFSFieldblock");
			GameRegistry.registerTileEntity(TileEntityForceField.class, "MFFSForceField");

		}
		catch (Exception e)
		{
			LOGGER.severe("Failed to load blocks and configuration!");
			LOGGER.severe(e.getMessage());
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

		MFFSMachine.initialize();
		ProjectorTyp.initialize();
		ProjectorOptions.initialize();

		NetworkRegistry.instance().registerGuiHandler(instance, proxy);

		proxy.registerRenderInformation();
		proxy.registerTileEntitySpecialRenderer();

		GameRegistry.registerWorldGenerator(new MFFSWorldGenerator());

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
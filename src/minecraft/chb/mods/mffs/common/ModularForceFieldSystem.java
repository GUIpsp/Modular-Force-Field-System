/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    Thunderdark - initial implementation
 */

package chb.mods.mffs.common;

import java.util.List;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;

import org.modstats.ModstatInfo;
import org.modstats.Modstats;

import thermalexpansion.api.crafting.CraftingManagers;
import chb.mods.mffs.common.block.BlockAdvSecurtyStation;
import chb.mods.mffs.common.block.BlockAreaDefenseStation;
import chb.mods.mffs.common.block.BlockCapacitor;
import chb.mods.mffs.common.block.BlockControlSystem;
import chb.mods.mffs.common.block.BlockConverter;
import chb.mods.mffs.common.block.BlockExtractor;
import chb.mods.mffs.common.block.BlockForceField;
import chb.mods.mffs.common.block.BlockMonazitOre;
import chb.mods.mffs.common.block.BlockProjector;
import chb.mods.mffs.common.block.BlockSecurtyStorage;
import chb.mods.mffs.common.event.EE3Event;
import chb.mods.mffs.common.item.ItemAccessCard;
import chb.mods.mffs.common.item.ItemCapacitorUpgradeCapacity;
import chb.mods.mffs.common.item.ItemCapacitorUpgradeRange;
import chb.mods.mffs.common.item.ItemCardDataLink;
import chb.mods.mffs.common.item.ItemCardEmpty;
import chb.mods.mffs.common.item.ItemCardPersonalID;
import chb.mods.mffs.common.item.ItemCardPower;
import chb.mods.mffs.common.item.ItemCardPowerLink;
import chb.mods.mffs.common.item.ItemCardSecurityLink;
import chb.mods.mffs.common.item.ItemExtractorUpgradeBooster;
import chb.mods.mffs.common.item.ItemForcePowerCrystal;
import chb.mods.mffs.common.item.ItemForcicium;
import chb.mods.mffs.common.item.ItemForcicumCell;
import chb.mods.mffs.common.item.ItemProjectorFieldModulatorDistance;
import chb.mods.mffs.common.item.ItemProjectorFieldModulatorStrength;
import chb.mods.mffs.common.item.ItemProjectorFocusMatrix;
import chb.mods.mffs.common.modules.ItemProjectorModuleAdvCube;
import chb.mods.mffs.common.modules.ItemProjectorModuleContainment;
import chb.mods.mffs.common.modules.ItemProjectorModuleCube;
import chb.mods.mffs.common.modules.ItemProjectorModuleDeflector;
import chb.mods.mffs.common.modules.ItemProjectorModuleSphere;
import chb.mods.mffs.common.modules.ItemProjectorModuleTube;
import chb.mods.mffs.common.modules.ItemProjectorModuleWall;
import chb.mods.mffs.common.modules.ItemProjectorModulediagonallyWall;
import chb.mods.mffs.common.multitool.ItemDebugger;
import chb.mods.mffs.common.multitool.ItemFieldtransporter;
import chb.mods.mffs.common.multitool.ItemManuelBook;
import chb.mods.mffs.common.multitool.ItemPersonalIDWriter;
import chb.mods.mffs.common.multitool.ItemSwitch;
import chb.mods.mffs.common.multitool.ItemWrench;
import chb.mods.mffs.common.options.ItemProjectorOptionBlockBreaker;
import chb.mods.mffs.common.options.ItemProjectorOptionCamoflage;
import chb.mods.mffs.common.options.ItemProjectorOptionDefenseStation;
import chb.mods.mffs.common.options.ItemProjectorOptionFieldFusion;
import chb.mods.mffs.common.options.ItemProjectorOptionFieldManipulator;
import chb.mods.mffs.common.options.ItemProjectorOptionForceFieldJammer;
import chb.mods.mffs.common.options.ItemProjectorOptionMobDefence;
import chb.mods.mffs.common.options.ItemProjectorOptionSponge;
import chb.mods.mffs.common.options.ItemProjectorOptionTouchDamage;
import chb.mods.mffs.common.tileentity.TileEntityForceField;
import chb.mods.mffs.common.tileentity.TileEntityMachines;
import chb.mods.mffs.network.client.ForceFieldClientUpdatehandler;
import chb.mods.mffs.network.client.NetworkHandlerClient;
import chb.mods.mffs.network.server.ForceFieldServerUpdatehandler;
import chb.mods.mffs.network.server.NetworkHandlerServer;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
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

@Mod(modid = "ModularForceFieldSystem", name = "Modular ForceField System", version = "2.2.8.3.7",dependencies = "after:ThermalExpansion")
@NetworkMod(versionBounds = "[2.2.8.3.7]", clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerClient.class), serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerServer.class))
@ModstatInfo(prefix="mffs")

public class ModularForceFieldSystem {
	
	public static CreativeTabs MFFSTab;

	public static int MFFSRENDER_ID = 2908;

	public static Block MFFSCapacitor;
	public static Block MFFSProjector;
	public static Block MFFSDefenceStation;
	public static Block MFFSFieldblock;
	public static Block MFFSExtractor;
	public static Block MFFSMonazitOre;
	public static Block MFFSForceEnergyConverter;
	public static Block MFFSSecurtyStorage;
	public static Block MFFSSecurtyStation;
	public static Block MFFSControlSystem;

	public static Item MFFSitemForcicumCell;
	public static Item MFFSitemForcicium;
	public static Item MFFSitemForcePowerCrystal;

	public static Item MFFSitemdensifiedForcicium;
	public static Item MFFSitemdepletedForcicium;

	public static Item MFFSitemFocusmatix;
	public static Item MFFSitemSwitch;
	public static Item MFFSitemWrench;

	public static Item MFFSitemFieldTeleporter;
	public static Item MFFSitemMFDidtool;
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

	public static Boolean forcefieldremoveonlywaterandlava;

	public static Boolean influencedbyothermods;
	public static Boolean adventuremap;
	public static Boolean ic2found = false;
	public static Boolean uefound = false;
	public static Boolean ee3found = false;
	public static Boolean buildcraftfound = false;
	public static Boolean ThermalExpansionfound = false;
	public static boolean showZapperParticles;
	public static boolean uumatterForcicium;
	public static boolean chunckloader = true;

	public static int ForceciumWorkCylce;
	public static int ForceciumCellWorkCylce;
	public static int ExtractorPassForceEnergyGenerate;
	
	public static int DefenceStationKillForceEnergy;
	public static int DefenceStationSearchForceEnergy;
	public static int DefenceStationScannForceEnergy;
	
	public static boolean DefenceStationNPCScannsuppressnotification;
	
	public static int graphicstyle;
	

	public static Configuration MFFSconfig;

	public static String Admin;
	public static String Versionlocal;
	public static String Versionremote;
	public static String VersionremoteUrl;
	

	@SidedProxy(clientSide = "chb.mods.mffs.client.ClientProxy", serverSide = "chb.mods.mffs.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance("ModularForceFieldSystem")
	public static ModularForceFieldSystem instance;


	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		
		
		initIC2Plugin();
		inituEPlugin();
		initbuildcraftPlugin();
		initEE3Plugin();
		ThermalExpansionPlugin();

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);
		
		Modstats.instance().getReporter().registerMod(this);
		
		if(ee3found)
		MinecraftForge.EVENT_BUS.register(new EE3Event());

		TickRegistry.registerScheduledTickHandler(
				new ForceFieldClientUpdatehandler(), Side.CLIENT);
		TickRegistry.registerScheduledTickHandler(
				new ForceFieldServerUpdatehandler(), Side.SERVER);		
		

		MFFSconfig = new Configuration(event.getSuggestedConfigurationFile());
		event.getModMetadata().version = Versioninfo.curentversion();
		try {
			MFFSconfig.load();
			MFFSTab = new MFFSCreativeTab(CreativeTabs.getNextID(), "MFFS");
	
			
			Property prop_VersionremoteUrl = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "VersionremoteUrl", "https://raw.github.com/Thunderdark/ModularForceFieldSystem/master/src/minecraft/versioninfo?login=Thunderdark&token=7bd46da10acec0374ad5e5c02acece61");
			prop_VersionremoteUrl.comment = "URL to MFFS VersionInfofile ask Thunderdark if get 'MFFS can't connect to remote version' in Log ";
			VersionremoteUrl = prop_VersionremoteUrl.value;
			
			Property prop_graphicstyle = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "GraphicStyle", 1);
			prop_graphicstyle.comment = "Set Graphic Style for Blocks 0: Default(ugly ;-)) 1: UE-Thema";
			graphicstyle = prop_graphicstyle.getInt(1);
			
			Property chunckloader_prop = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "Chunkloader", true);
			chunckloader_prop.comment = "Set this to false to turn off the MFFS Chuncloader ability";
			chunckloader=chunckloader_prop.getBoolean(true);
			
			Property DefSationNPCScannoti = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "DefenceStationNPCScannnotification", false);
			DefSationNPCScannoti.comment = "Set this to true to turn off the DefenceStation notification is in NPC Mode";
			DefenceStationNPCScannsuppressnotification=DefSationNPCScannoti.getBoolean(false);
			
			Property zapperParticles = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "renderZapperParticles", true);
			zapperParticles.comment = "Set this to false to turn off the small smoke particles present around TouchDamage enabled ForceFields.";
			showZapperParticles=zapperParticles.getBoolean(true);
			
			Property uumatterForciciumprop = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "uumatterForcicium", true);
			uumatterForciciumprop.comment = "Add IC2 UU-Matter Recipes for Forcicium";
			uumatterForcicium=uumatterForciciumprop.getBoolean(true);

			Property monazitWorldAmount = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "MonazitOreWorldGen", 4);
			monazitWorldAmount.comment = "Controls the size of the ore node that Monazit Ore will generate in";
			MonazitOreworldamount = monazitWorldAmount.getInt(4);
			
			Property adminList = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "ForceFieldMaster", "nobody");
			adminList.comment = "Add users to this list to give them admin permissions split by ;";
			Admin = adminList.value;
			
			Property influencedByOther = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "influencedbyothermods", true);
			influencedByOther.comment = "Should MFFS be influenced by other mods. e.g. ICBM's EMP";
			influencedbyothermods = influencedByOther.getBoolean(true);
			
			Property ffRemoveWaterLavaOnly = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldremoveonlywaterandlava", false);
			ffRemoveWaterLavaOnly.comment = "Should forcefields only remove water and lava when sponge is enabled?";
			forcefieldremoveonlywaterandlava = ffRemoveWaterLavaOnly.getBoolean(false);
			
			
			Property feTransportCost = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldtransportcost", 10000);
			feTransportCost.comment = "How much FE should it cost to transport through a field?";
			forcefieldtransportcost = feTransportCost.getInt(10000);
			
			Property feFieldBlockCost = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcost", 1);
			feFieldBlockCost.comment = "How much upkeep FE cost a default ForceFieldblock per second";
			forcefieldblockcost = feFieldBlockCost.getInt(1);
			
			Property BlockCreateMod = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcreatemodifier", 10);
			BlockCreateMod.comment = "Energy need for create a ForceFieldblock (forcefieldblockcost*forcefieldblockcreatemodifier)";
			forcefieldblockcreatemodifier = BlockCreateMod.getInt(10);
			
			Property ffZapperMod = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldblockzappermodifier", 2);
			ffZapperMod.comment = "Energy need multiplier used when the zapper option is installed";
			forcefieldblockzappermodifier = ffZapperMod.getInt(2);
			
			Property maxFFGenPerTick = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "forcefieldmaxblockpeerTick", 5000);
			maxFFGenPerTick.comment = "How many field blocks can be generated per tick?";
			forcefieldmaxblockpeerTick = maxFFGenPerTick.getInt(5000);
			
			
			Property fcWorkCycle = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "ForceciumWorkCylce", 250);
			fcWorkCycle.comment = "WorkCycle amount of Forcecium inside a Extractor";
			ForceciumWorkCylce = fcWorkCycle.getInt(250);
			
			Property fcCellWorkCycle = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "ForceciumCellWorkCylce", 230);
			fcCellWorkCycle.comment = "WorkCycle amount of Forcecium Cell inside a Extractor";
			ForceciumCellWorkCylce = fcCellWorkCycle.getInt(230);
			
			Property extractorPassFEGen = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "ExtractorPassForceEnergyGenerate", 12000);
			extractorPassFEGen.comment = "How many ForceEnergy generate per WorkCycle";
			ExtractorPassForceEnergyGenerate = extractorPassFEGen.getInt(12000);
			
			ExtractorPassForceEnergyGenerate=(ExtractorPassForceEnergyGenerate/4000)*4000;
			
			Property defStationKillCost = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "DefenceStationKillForceEnergy", 10000);
			defStationKillCost.comment = "How much FE does the AreaDefenseStation when killing someone";
			DefenceStationKillForceEnergy = defStationKillCost.getInt(10000);
			
			Property defStationSearchCost = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "DefenceStationSearchForceEnergy", 1000);
			defStationSearchCost.comment = "How much FE does the AreaDefenseStation when searching someone for contraband";
			DefenceStationSearchForceEnergy = defStationSearchCost.getInt(1000);
			
			Property defStationScannCost = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "DefenceStationScannForceEnergy", 10);
			defStationScannCost.comment = "How much FE does the AreaDefenseStation when Scann for Targets (amount * range / tick)";
			DefenceStationScannForceEnergy = defStationScannCost.getInt(10);
			
			Property Adventuremap = MFFSconfig.get(Configuration.CATEGORY_GENERAL, "adventuremap", false);
			Adventuremap.comment = "Set MFFS to AdventureMap Mode Extractor need no Forcecium and ForceField have no click damage";
			adventuremap = Adventuremap.getBoolean(false);
				
			// Machines + Blocks

			MFFSForceEnergyConverter = new BlockConverter(MFFSconfig.getBlock("MFFSForceEnergyConverter", DefaultProps.block_Converter_ID).getInt(DefaultProps.block_Converter_ID)).setBlockName("MFFSForceEnergyConverter");
			MFFSExtractor = new BlockExtractor(MFFSconfig.getBlock("MFFSExtractor", DefaultProps.block_Extractor_ID).getInt(DefaultProps.block_Extractor_ID)).setBlockName("MFFSExtractor");
			MFFSMonazitOre = new BlockMonazitOre(MFFSconfig.getBlock("MFFSMonazitOre", DefaultProps.block_MonazitOre_ID).getInt(DefaultProps.block_MonazitOre_ID)).setBlockName("MFFSMonazitOre");
			MFFSDefenceStation = new BlockAreaDefenseStation(MFFSconfig.getBlock("MFFSDefenceStation", DefaultProps.block_DefenseStation_ID).getInt(DefaultProps.block_DefenseStation_ID)).setBlockName("MFFSDefenceStation");
			MFFSCapacitor = new BlockCapacitor(MFFSconfig.getBlock("MFFSCapacitor", DefaultProps.block_Capacitor_ID).getInt(DefaultProps.block_Capacitor_ID)).setBlockName("MFFSCapacitor");
			MFFSProjector = new BlockProjector(MFFSconfig.getBlock("MFFSProjector", DefaultProps.block_Projector_ID).getInt(DefaultProps.block_Projector_ID)).setBlockName("MFFSProjector");
			MFFSFieldblock = new BlockForceField(MFFSconfig.getBlock("MFFSFieldblock", DefaultProps.block_Field_ID).getInt(DefaultProps.block_Field_ID));
			MFFSSecurtyStorage = new BlockSecurtyStorage(MFFSconfig.getBlock("MFFSSecurtyStorage", DefaultProps.block_SecureStorage_ID).getInt(DefaultProps.block_SecureStorage_ID)).setBlockName("MFFSSecurtyStorage");
			MFFSSecurtyStation = new BlockAdvSecurtyStation(MFFSconfig.getBlock("MFFSSecurtyStation", DefaultProps.block_SecurityStation_ID).getInt(DefaultProps.block_SecurityStation_ID)).setBlockName("MFFSSecurtyStation");
			MFFSControlSystem = new BlockControlSystem(MFFSconfig.getBlock("MFFSControlSystem", DefaultProps.block_ControlSystem).getInt(DefaultProps.block_ControlSystem)).setBlockName("MFFSControlSystem");
			
			// Items
			MFFSProjectorFFDistance = new ItemProjectorFieldModulatorDistance(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorFFDistance", DefaultProps.item_AltDistance_ID).getInt(DefaultProps.item_AltDistance_ID)).setItemName("itemProjectorFFDistance");
			MFFSProjectorFFStrenght = new ItemProjectorFieldModulatorStrength(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorFFStrength", DefaultProps.item_AltStrength_ID).getInt(DefaultProps.item_AltStrength_ID)).setItemName("itemProjectorFFStrength");
			MFFSitemFocusmatix = new ItemProjectorFocusMatrix(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemPorjectorFocusmatrix", DefaultProps.item_FocusMatrix_ID).getInt(DefaultProps.item_FocusMatrix_ID)).setItemName("itemPorjectorFocusmatrix");
			MFFSitemForcePowerCrystal = new ItemForcePowerCrystal(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemForcePowerCrystal", DefaultProps.item_FPCrystal_ID).getInt(DefaultProps.item_FPCrystal_ID)).setItemName("itemForcePowerCrystal");
			MFFSitemForcicium = new ItemForcicium(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemForcicium", DefaultProps.item_Forcicium_ID).getInt(DefaultProps.item_Forcicium_ID)).setItemName("itemForcicium");
			MFFSitemForcicumCell = new ItemForcicumCell(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemForcicumCell", DefaultProps.item_ForciciumCell_ID).getInt(DefaultProps.item_ForciciumCell_ID)).setItemName("itemForcicumCell");
			
			// Modules
			MFFSProjectorTypdiagowall = new ItemProjectorModulediagonallyWall(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorModulediagonallyWall", DefaultProps.item_ModDiag_ID).getInt(DefaultProps.item_ModDiag_ID)).setItemName("itemProjectorModulediagonallyWall");
			MFFSProjectorTypsphere = new ItemProjectorModuleSphere(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorTypsphere", DefaultProps.item_ModSphere_ID).getInt(DefaultProps.item_ModSphere_ID)).setItemName("itemProjectorTypsphere");
			MFFSProjectorTypkube = new ItemProjectorModuleCube(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorTypkube", DefaultProps.item_ModCube_ID).getInt(DefaultProps.item_ModCube_ID)).setItemName("itemProjectorTypkube");
			MFFSProjectorTypwall = new ItemProjectorModuleWall(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorTypwall", DefaultProps.item_ModWall_ID).getInt(DefaultProps.item_ModWall_ID)).setItemName("itemProjectorTypwall");
			MFFSProjectorTypdeflector = new ItemProjectorModuleDeflector(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorTypdeflector", DefaultProps.item_ModDeflector_ID).getInt(DefaultProps.item_ModDeflector_ID)).setItemName("itemProjectorTypdeflector");
			MFFSProjectorTyptube = new ItemProjectorModuleTube(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorTyptube", DefaultProps.item_ModTube_ID).getInt(DefaultProps.item_ModTube_ID)).setItemName("itemProjectorTyptube");
			MFFSProjectorTypcontainment = new ItemProjectorModuleContainment(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorModuleContainment", DefaultProps.item_ModContainment_ID).getInt(DefaultProps.item_ModContainment_ID)).setItemName("itemProjectorModuleContainment");
			MFFSProjectorTypAdvCube = new ItemProjectorModuleAdvCube(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorModuleAdvCube", DefaultProps.item_ModAdvCube_ID).getInt(DefaultProps.item_ModAdvCube_ID)).setItemName("itemProjectorModuleAdvCube");
			
			// Options
			MFFSProjectorOptionZapper = new ItemProjectorOptionTouchDamage(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemupgradeprozapper", DefaultProps.item_OptTouchHurt_ID).getInt(DefaultProps.item_OptTouchHurt_ID)).setItemName("itemupgradeprozapper");
			MFFSProjectorOptionSubwater = new ItemProjectorOptionSponge(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemupgradeprosubwater", DefaultProps.item_OptSponge_ID).getInt(DefaultProps.item_OptSponge_ID)).setItemName("itemupgradeprosubwater");
			MFFSProjectorOptionDome = new ItemProjectorOptionFieldManipulator(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemupgradeprodome", DefaultProps.item_OptManipulator_ID).getInt(DefaultProps.item_OptManipulator_ID)).setItemName("itemupgradeprodome");
			MFFSProjectorOptionCutter = new ItemProjectorOptionBlockBreaker(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemUpgradeprocutter", DefaultProps.item_OptBlockBreaker_ID).getInt(DefaultProps.item_OptBlockBreaker_ID)).setItemName("itemUpgradeprocutter");
			MFFSProjectorOptionDefenceStation = new ItemProjectorOptionDefenseStation(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorOptiondefencestation", DefaultProps.item_OptDefense_ID).getInt(DefaultProps.item_OptDefense_ID)).setItemName("itemProjectorOptiondefencestation");
			MFFSProjectorOptionMoobEx = new ItemProjectorOptionMobDefence(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorOptionMoobEx", DefaultProps.item_OptMobDefense_ID).getInt(DefaultProps.item_OptMobDefense_ID)).setItemName("itemProjectorOptionMoobEx");
			MFFSProjectorOptionForceFieldJammer = new ItemProjectorOptionForceFieldJammer(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorOptionFFJammer", DefaultProps.item_OptJammer_ID).getInt(DefaultProps.item_OptJammer_ID)).setItemName("itemProjectorOptionFFJammer");
			MFFSProjectorOptionCamouflage = new ItemProjectorOptionCamoflage(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorOptionCamoflage", DefaultProps.item_OptCamouflage_ID).getInt(DefaultProps.item_OptCamouflage_ID)).setItemName("itemProjectorOptionCamoflage");
			MFFSProjectorOptionFieldFusion = new ItemProjectorOptionFieldFusion(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemProjectorOptionFieldFusion", DefaultProps.item_OptFusion_ID).getInt(DefaultProps.item_OptFusion_ID)).setItemName("itemProjectorOptionFieldFusion");
			
			// Cards
			MFFSitemcardempty = new ItemCardEmpty(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemcardempty", DefaultProps.item_BlankCard_ID).getInt(DefaultProps.item_BlankCard_ID)).setItemName("itemcardempty");
			MFFSitemfc = new ItemCardPowerLink(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemfc", DefaultProps.item_CardPowerLink_ID).getInt(DefaultProps.item_CardPowerLink_ID)).setItemName("itemfc");
			MFFSItemIDCard = new ItemCardPersonalID(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemIDCard", DefaultProps.item_CardPersonalID_ID).getInt(DefaultProps.item_CardPersonalID_ID)).setItemName("itemIDCard");
			MFFSItemSecLinkCard = new ItemCardSecurityLink(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemSecLinkCard", DefaultProps.item_CardSecurityLink_ID).getInt(DefaultProps.item_CardSecurityLink_ID)).setItemName("itemSecLinkCard");
			MFFSitemInfinitePowerCard = new ItemCardPower(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemInfinitePower", DefaultProps.item_infPowerCard_ID).getInt(DefaultProps.item_infPowerCard_ID)).setItemName("itemInfPowerCard");
			MFFSAccessCard= new ItemAccessCard(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemAccessCard", DefaultProps.item_CardAccess_ID).getInt(DefaultProps.item_CardAccess_ID)).setItemName("itemAccessCard");
			MFFSitemDataLinkCard = new ItemCardDataLink(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemCardDataLink", DefaultProps.item_CardDataLink_ID).getInt(DefaultProps.item_CardDataLink_ID)).setItemName("itemCardDataLink"); 

			// MultiTools
			MFFSitemWrench = new ItemWrench(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemWrench", DefaultProps.item_MTWrench_ID).getInt(DefaultProps.item_MTWrench_ID)).setItemName("itemWrench");
			MFFSitemSwitch = new ItemSwitch(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemSwitch", DefaultProps.item_MTSwitch_ID).getInt(DefaultProps.item_MTSwitch_ID)).setItemName("itemSwitch");
			MFFSitemFieldTeleporter = new ItemFieldtransporter(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemForceFieldsync",DefaultProps.item_MTFieldTransporter_ID).getInt(DefaultProps.item_MTFieldTransporter_ID)).setItemName("itemForceFieldsync");
			MFFSitemMFDidtool = new ItemPersonalIDWriter(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "ItemMFDIDwriter", DefaultProps.item_MTIDWriter_ID).getInt(DefaultProps.item_MTIDWriter_ID)).setItemName("ItemMFDIDwriter");
			MFFSitemMFDdebugger = new ItemDebugger(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemMFDdebugger", DefaultProps.item_MTDebugger_ID).getInt(DefaultProps.item_MTDebugger_ID)).setItemName("itemMFDdebugger");
			MFFSitemManuelBook = new ItemManuelBook(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemManuelBook", DefaultProps.item_MTManual_ID).getInt(DefaultProps.item_MTManual_ID)).setItemName("itemManuelBook"); 
			
			// Upgrades
			MFFSitemupgradeexctractorboost = new ItemExtractorUpgradeBooster(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemextractorbooster", DefaultProps.item_upgradeBoost_ID).getInt(DefaultProps.item_upgradeBoost_ID)).setItemName("itemextractorbooster");
			MFFSitemupgradecaprange = new ItemCapacitorUpgradeRange(MFFSconfig.getItem(Configuration.CATEGORY_ITEM,"itemupgradecaprange", DefaultProps.item_upgradeRange_ID).getInt(DefaultProps.item_upgradeRange_ID)).setItemName("itemupgradecaprange");
			MFFSitemupgradecapcap = new ItemCapacitorUpgradeCapacity(MFFSconfig.getItem(Configuration.CATEGORY_ITEM, "itemupgradecapcap", DefaultProps.item_upgradeCap_ID).getInt(DefaultProps.item_upgradeCap_ID)).setItemName("itemupgradecapcap");
			
			
			

		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e,
					"ModularForceFieldSystem has a problem loading it's configuration");
			System.out.println(e.getMessage());
		} finally {
			MFFSconfig.save();
		}
		
		Versionlocal = Versioninfo.curentversion();
		Versionremote = Versioninfo.newestversion();
	}

	@Init
	public void load(FMLInitializationEvent evt) {

		
		GameRegistry.registerBlock(MFFSMonazitOre,"MFFSMonazitOre");
		GameRegistry.registerBlock(MFFSFieldblock,"MFFSFieldblock");
		GameRegistry.registerTileEntity(TileEntityForceField.class,"MFFSForceField");
		
		MFFSMaschines.initialize();
		ProjectorTyp.initialize();
		ProjectorOptions.initialize();

		NetworkRegistry.instance().registerGuiHandler(instance, proxy);

		proxy.registerRenderInformation();
		proxy.registerTileEntitySpecialRenderer();
		
		GameRegistry.registerWorldGenerator(new MFFSWorldGenerator());

		LanguageRegistry.instance().addNameForObject(MFFSitemInfinitePowerCard, "en_US", "MFFS Infinite Power Card");

		LanguageRegistry.instance().addNameForObject(
				MFFSitemupgradeexctractorboost, "en_US",
				"MFFS Extractor Booster");
		LanguageRegistry.instance().addNameForObject(MFFSMonazitOre, "en_US",
				"Monazit Ore");
		LanguageRegistry.instance().addNameForObject(MFFSitemForcicumCell,
				"en_US", "MFFS compact Forcicium Cell");
		LanguageRegistry.instance().addNameForObject(MFFSitemForcicium,
				"en_US", "Forcicium");
		LanguageRegistry.instance().addNameForObject(MFFSitemForcePowerCrystal,
				"en_US", "MFFS Force Energy Crystal");
		LanguageRegistry.instance().addNameForObject(MFFSitemSwitch, "en_US",
				"MFFS MultiTool <Switch>");
		LanguageRegistry.instance().addNameForObject(MFFSitemWrench, "en_US",
				"MFFS MultiTool <Wrench>");
		LanguageRegistry.instance().addNameForObject(MFFSitemManuelBook, "en_US",
				"MFFS MultiTool <Guide>");
		LanguageRegistry.instance().addNameForObject(MFFSitemFocusmatix,
				"en_US", "MFFS Projector Focus Matrix");
		LanguageRegistry.instance().addNameForObject(MFFSitemFieldTeleporter,
				"en_US", "MFFS MultiTool <Field Teleporter>");
		
		LanguageRegistry.instance().addNameForObject(MFFSitemDataLinkCard,
				"en_US", "MFFS Card <Data Link> ");
		
		LanguageRegistry.instance().addNameForObject(MFFSAccessCard,
				"en_US", "MFFS Card <Access license> ");
		LanguageRegistry.instance().addNameForObject(MFFSitemcardempty,
				"en_US", "MFFS Card <blank> ");
		LanguageRegistry.instance().addNameForObject(MFFSitemfc, "en_US",
				"MFFS Card <Power Link>");
		LanguageRegistry.instance().addNameForObject(MFFSItemIDCard, "en_US",
				"MFFS Card <Personal ID>");
		LanguageRegistry.instance().addNameForObject(MFFSItemSecLinkCard,
				"en_US", "MFFS Card <Security Station Link> ");
		LanguageRegistry.instance().addNameForObject(MFFSitemMFDdebugger,
				"en_US", "MFFS MultiTool <Debugger>");
		LanguageRegistry.instance().addNameForObject(MFFSitemMFDidtool,
				"en_US", "MFFS MultiTool <PersonalID & Data Link  Coder>");
		LanguageRegistry.instance().addNameForObject(MFFSitemupgradecaprange,
				"en_US", "MFFS Capacitor Upgrade <Range> ");
		LanguageRegistry.instance().addNameForObject(MFFSitemupgradecapcap,
				"en_US", "MFFS Capacitor Upgrade <Capacity> ");
	
		LanguageRegistry.instance().addNameForObject(MFFSProjectorFFDistance,
				"en_US", "MFFS Projector Field Modulator <distance>");
		LanguageRegistry.instance().addNameForObject(MFFSProjectorFFStrenght,
				"en_US", "MFFS Projector Field Modulator <strength>");
		
		LanguageRegistry.instance().addStringLocalization("itemGroup.MFFS",
				"en_US", "Modular Force Field System");
		
		LanguageRegistry.instance().addStringLocalization("death.areaDefense", "en_US", "%1$s disregarded warnings and was fried");
		LanguageRegistry.instance().addStringLocalization("death.fieldShock", "en_US", "%1$s was fried by a forcefield");
		LanguageRegistry.instance().addStringLocalization("death.fieldDefense", "en_US", "%1$s was fried");
				
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent evt) {
		
		MFFSRecipes.init();
		ForgeChunkManager.setForcedChunkLoadingCallback(instance,new MFFSChunkloadCallback());
	}
	
	

	public class MFFSChunkloadCallback implements
			ForgeChunkManager.OrderedLoadingCallback {
		@Override
		public void ticketsLoaded(List<Ticket> tickets, World world) {
			for (Ticket ticket : tickets) {
				int MaschineX = ticket.getModData().getInteger("MaschineX");
				int MaschineY = ticket.getModData().getInteger("MaschineY");
				int MaschineZ = ticket.getModData().getInteger("MaschineZ");
				TileEntityMachines Machines = (TileEntityMachines) world
						.getBlockTileEntity(MaschineX, MaschineY, MaschineZ);
				Machines.forceChunkLoading(ticket);

			}
		}

		@Override
		public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world,
				int maxTicketCount) {
			List<Ticket> validTickets = Lists.newArrayList();
			for (Ticket ticket : tickets) {
				int MaschineX = ticket.getModData().getInteger("MaschineX");
				int MaschineY = ticket.getModData().getInteger("MaschineY");
				int MaschineZ = ticket.getModData().getInteger("MaschineZ");

				TileEntity tileEntity = world.getBlockTileEntity(MaschineX,
						MaschineY, MaschineZ);
				if (tileEntity instanceof TileEntityMachines) {
					validTickets.add(ticket);
				}
			}
			return validTickets;
		}

	}


	

	
	public  void initbuildcraftPlugin() {
		
		System.out.println("[ModularForceFieldSystem] Loading module for Buildcraft");

		try {
			
			Class.forName("buildcraft.core.Version");
			buildcraftfound= true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: Buildcraft not found");
		
		}
	}
	
	
	public  void ThermalExpansionPlugin() {
		
		System.out.println("[ModularForceFieldSystem] Loading module for ThermalExpansion");

		try {
			
			    Class.forName("thermalexpansion.ThermalExpansion");
				ThermalExpansionfound= true;
		
		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: ThermalExpansion not found");
		
		}
	}
	
	public  void initEE3Plugin() {
		
		System.out.println("[ModularForceFieldSystem] Loading module for EE3");

		try {
			
			Class.forName("com.pahimar.ee3.event.ActionRequestEvent");
	        ee3found= true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: EE3 not found");
		
		}
	}

	
	public  void inituEPlugin() {
		
		System.out.println("[ModularForceFieldSystem] Loading module for Universal Electricity");

		try {
			
			Class.forName("basiccomponents.common.item.ItemBasic");
	        uefound= true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: Universal Electricity not found");
		
		}
	}
	
	
	
	public  void initIC2Plugin() {
		
		System.out.println("[ModularForceFieldSystem] Loading module for IC2");

		try {
			
			Class.forName("ic2.core.IC2");
	        ic2found= true;

		} catch (Throwable t) {
			System.out.println("[ModularForceFieldSystem] Module not loaded: IC2 not found");
		
		}
	}

}
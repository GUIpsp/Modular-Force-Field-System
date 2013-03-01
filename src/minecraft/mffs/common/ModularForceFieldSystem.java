package mffs.common;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mffs.common.block.BlockControlSystem;
import mffs.common.block.BlockConverter;
import mffs.common.block.BlockDefenseStation;
import mffs.common.block.BlockForceCapacitor;
import mffs.common.block.BlockForceField;
import mffs.common.block.BlockForcilliumExtractor;
import mffs.common.block.BlockFortronite;
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
import mffs.common.item.ItemForcillium;
import mffs.common.item.ItemForcilliumCell;
import mffs.common.item.ItemFortronCrystal;
import mffs.common.item.ItemMFFS;
import mffs.common.mode.ItemModeAdvancedCube;
import mffs.common.mode.ItemModeContainment;
import mffs.common.mode.ItemModeCube;
import mffs.common.mode.ItemModeDeflector;
import mffs.common.mode.ItemModeDiagonalWall;
import mffs.common.mode.ItemModeSphere;
import mffs.common.mode.ItemModeTube;
import mffs.common.mode.ItemModeWall;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemOptionAntibiotic;
import mffs.common.module.ItemOptionCamoflage;
import mffs.common.module.ItemOptionCutter;
import mffs.common.module.ItemOptionDefenseStation;
import mffs.common.module.ItemOptionFieldFusion;
import mffs.common.module.ItemOptionFieldManipulator;
import mffs.common.module.ItemOptionJammer;
import mffs.common.module.ItemOptionShock;
import mffs.common.module.ItemOptionSponge;
import mffs.common.multitool.ItemMultitool;
import mffs.common.tileentity.TileEntityForceField;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.upgrade.ItemModuleDistance;
import mffs.common.upgrade.ItemModuleStrength;
import mffs.common.upgrade.ItemProjectorFocusMatrix;
import mffs.common.upgrade.ItemUpgradeBooster;
import mffs.common.upgrade.ItemUpgradeCapacity;
import mffs.common.upgrade.ItemUpgradeRange;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

import org.modstats.ModstatInfo;
import org.modstats.Modstats;

import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.UEDamageSource;
import universalelectricity.prefab.network.ConnectionHandler;
import universalelectricity.prefab.network.PacketManager;
import universalelectricity.prefab.ore.OreGenBase;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;
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

@Mod(modid = ModularForceFieldSystem.ID, name = ModularForceFieldSystem.NAME, version = ModularForceFieldSystem.VERSION, dependencies = "after:ThermalExpansion")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { ModularForceFieldSystem.CHANNEL }, packetHandler = PacketManager.class, connectionHandler = ConnectionHandler.class)
@ModstatInfo(prefix = "mffs")
public class ModularForceFieldSystem
{

	public static final String CHANNEL = "MFFS";
	public static final String ID = "ModularForceFieldSystem";
	public static final String NAME = "Modular Force Field System";

	public static final String VERSION = "3.0.0";
	public static final String RESOURCE_DIRECTORY = "/mffs/";
	public static final String TEXTURE_DIRECTORY = RESOURCE_DIRECTORY + "textures/";
	public static final String BLOCK_TEXTURE_FILE = TEXTURE_DIRECTORY + "blocks.png";
	public static final String ITEM_TEXTURE_FILE = TEXTURE_DIRECTORY + "items.png";
	public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";
	public static int RENDER_ID = 2908;
	/**
	 * Machines
	 */
	public static Block blockCapacitor;
	public static Block blockProjector;
	public static Block blockDefenceStation;
	public static Block blockForceField;
	public static Block blockExtractor;
	public static Block blockFortronite;
	public static Block blockConverter;
	public static Block blockSecurityStorage;
	public static Block blockSecurityStation;
	public static Block blockControlSystem;

	/**
	 * Fortron related items
	 */
	public static Item itemFortron;
	public static Item itemForcilliumCell;
	public static Item itemForcillium;
	public static Item itemPowerCrystal;
	public static Item itemCompactForcicium;
	public static Item itemDepletedForcicium;
	public static Item itemFocusMatix;
	/**
	 * Multitool
	 */
	public static Item itemMultiTool;
	/**
	 * Cards
	 */
	public static Item itemCardEmpty;
	public static Item itemCardPowerLink;
	public static Item itemCardID;
	public static Item itemCardAccess;
	public static Item itemCardSecurityLink;
	public static Item itemCardInfinite;
	public static Item itemCardDataLink;
	/**
	 * Upgrades
	 */
	public static Item itemUpgradeBoost;
	public static Item itemUpgradeRange;
	public static Item itemUpgradeCapacity;
	
	/**
	 * Modules
	 */
	public static ItemModule itemOptionShock;
	public static ItemModule itemOptionSponge;
	public static ItemModule itemOptionFieldManipulator;
	public static ItemModule itemOptionCutter;
	public static ItemModule itemOptionAntibiotic;
	public static ItemModule itemOptionDefenseeStation;
	public static ItemModule itemOptionJammer;
	public static ItemModule itemOptionCamouflage;
	public static ItemModule itemOptionFieldFusion;
	
	/**
	 * Modes
	 */
	public static Item itemModuleSphere;
	public static Item itemModuleCube;
	public static Item itemModuleWall;
	public static Item itemModuleDeflector;
	public static Item itemModuleTube;
	public static Item itemModuleContainment;
	public static Item itemModuleAdvancedCube;
	public static Item itemModuleDiagonalWall;
	public static Item itemModuleDistance;
	public static Item itemModuleStrength;

	public static OreGenBase monaziteOreGeneration;

	public static DamageSource fieldShock = new UEDamageSource("fieldShock").setDamageBypassesArmor();
	public static DamageSource areaDefense = new UEDamageSource("areaDefense").setDamageBypassesArmor();
	public static DamageSource fieldDefense = new UEDamageSource("fieldDefense").setDamageBypassesArmor();

	@SidedProxy(clientSide = "mffs.client.ClientProxy", serverSide = "mffs.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance(ModularForceFieldSystem.ID)
	public static ModularForceFieldSystem instance;
	public static final Logger LOGGER = Logger.getLogger(NAME);

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER.setParent(FMLLog.getLogger());

		if (initiateModule("IC2"))
		{
			MFFSConfiguration.MODULE_IC2 = true;
		}
		if (initiateModule("BasicComponents"))
		{
			MFFSConfiguration.MODULE_UE = true;
		}
		if (initiateModule("BuildCraft|Core"))
		{
			MFFSConfiguration.MODULE_BUILDCRAFT = true;
		}
		if (initiateModule("EE3"))
		{
			MFFSConfiguration.MODULE_EE = true;
		}
		if (initiateModule("ThermalExpansion"))
		{
			MFFSConfiguration.MODULE_THERMAL_EXPANSION = true;
		}

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);

		Modstats.instance().getReporter().registerMod(this);

		if (MFFSConfiguration.MODULE_EE)
		{
			MinecraftForge.EVENT_BUS.register(new EE3Event());
		}

		try
		{
			MFFSConfiguration.initialize();

			MFFSConfiguration.CONFIGURATION.load();

			blockConverter = new BlockConverter(MFFSConfiguration.block_Converter_ID);
			blockExtractor = new BlockForcilliumExtractor(MFFSConfiguration.block_Extractor_ID);
			blockFortronite = new BlockFortronite(MFFSConfiguration.blockFortronite_ID, "oreFortronite");
			blockDefenceStation = new BlockDefenseStation(MFFSConfiguration.block_DefenseStation_ID);
			blockCapacitor = new BlockForceCapacitor(MFFSConfiguration.block_Capacitor_ID);
			blockProjector = new BlockProjector(MFFSConfiguration.block_Projector_ID);
			blockForceField = new BlockForceField(MFFSConfiguration.block_Field_ID);
			blockSecurityStorage = new BlockSecurityStorage(MFFSConfiguration.block_SecureStorage_ID);
			blockSecurityStation = new BlockSecurityStation(MFFSConfiguration.block_SecurityStation_ID, 16);
			blockControlSystem = new BlockControlSystem(MFFSConfiguration.block_ControlSystem);

			itemModuleDistance = new ItemModuleDistance(MFFSConfiguration.item_AltDistance_ID);
			itemModuleStrength = new ItemModuleStrength(MFFSConfiguration.item_AltStrength_ID);
			itemFocusMatix = new ItemProjectorFocusMatrix(MFFSConfiguration.item_FocusMatrix_ID);
			itemPowerCrystal = new ItemFortronCrystal(MFFSConfiguration.item_FPCrystal_ID);
			itemForcillium = new ItemForcillium(MFFSConfiguration.item_Forcicium_ID);
			itemForcilliumCell = new ItemForcilliumCell(MFFSConfiguration.item_ForciciumCell_ID);

			itemModuleDiagonalWall = new ItemModeDiagonalWall(MFFSConfiguration.item_ModDiag_ID);
			itemModuleSphere = new ItemModeSphere(MFFSConfiguration.item_ModSphere_ID);
			itemModuleCube = new ItemModeCube(MFFSConfiguration.item_ModCube_ID);
			itemModuleWall = new ItemModeWall(MFFSConfiguration.item_ModWall_ID);
			itemModuleDeflector = new ItemModeDeflector(MFFSConfiguration.item_ModDeflector_ID);
			itemModuleTube = new ItemModeTube(MFFSConfiguration.item_ModTube_ID);
			itemModuleContainment = new ItemModeContainment(MFFSConfiguration.item_ModContainment_ID);
			itemModuleAdvancedCube = new ItemModeAdvancedCube(MFFSConfiguration.item_ModAdvCube_ID);

			itemOptionShock = new ItemOptionShock(MFFSConfiguration.item_OptTouchHurt_ID);
			itemOptionSponge = new ItemOptionSponge(MFFSConfiguration.item_OptSponge_ID);
			itemOptionFieldManipulator = new ItemOptionFieldManipulator(MFFSConfiguration.item_OptManipulator_ID);
			itemOptionCutter = new ItemOptionCutter(MFFSConfiguration.item_OptBlockBreaker_ID);
			itemOptionDefenseeStation = new ItemOptionDefenseStation(MFFSConfiguration.item_OptDefense_ID);
			itemOptionAntibiotic = new ItemOptionAntibiotic(MFFSConfiguration.item_OptMobDefense_ID);
			itemOptionJammer = new ItemOptionJammer(MFFSConfiguration.item_OptJammer_ID);
			itemOptionCamouflage = new ItemOptionCamoflage(MFFSConfiguration.item_OptCamouflage_ID);
			itemOptionFieldFusion = new ItemOptionFieldFusion(MFFSConfiguration.item_OptFusion_ID);

			itemCardEmpty = new ItemCardEmpty(MFFSConfiguration.item_BlankCard_ID);
			itemCardPowerLink = new ItemCardPowerLink(MFFSConfiguration.item_CardPowerLink_ID);
			itemCardID = new ItemCardPersonalID(MFFSConfiguration.item_CardPersonalID_ID);
			itemCardSecurityLink = new ItemCardSecurityLink(MFFSConfiguration.item_CardSecurityLink_ID);
			itemCardInfinite = new ItemCardPower(MFFSConfiguration.item_infPowerCard_ID);
			itemCardAccess = new ItemAccessCard(MFFSConfiguration.item_CardAccess_ID);
			itemCardDataLink = new ItemCardDataLink(MFFSConfiguration.item_CardDataLink_ID);

			itemMultiTool = new ItemMultitool(MFFSConfiguration.item_MultiTool_ID);

			itemUpgradeBoost = new ItemUpgradeBooster(MFFSConfiguration.item_upgradeBoost_ID);
			itemUpgradeRange = new ItemUpgradeRange(MFFSConfiguration.item_upgradeRange_ID);
			itemUpgradeCapacity = new ItemUpgradeCapacity(MFFSConfiguration.item_upgradeCap_ID);

			/**
			 * The Fortron Liquid
			 */
			itemFortron = new ItemMFFS(MFFSConfiguration.itemFortronID, "fortron").setTextureFile(BLOCK_TEXTURE_FILE).setCreativeTab(null).setIconIndex(5);
			Fortron.LIQUID_FORTRON = LiquidDictionary.getOrCreateLiquid("Fortron", new LiquidStack(itemFortron, 0));

			monaziteOreGeneration = new OreGenReplaceStone("Fortronite", "oreFortronite", new ItemStack(blockFortronite), 80, 17, 4);
			monaziteOreGeneration.shouldGenerate = MFFSConfiguration.CONFIGURATION.get("Ore Generation", "Generate Fortronite", false).getBoolean(false);
			OreGenerator.addOre(monaziteOreGeneration);
		}
		catch (Exception e)
		{
			LOGGER.severe("Failed to load blocks and configuration!");
			LOGGER.severe(e.getMessage());
		}
		finally
		{
			MFFSConfiguration.CONFIGURATION.save();
		}
	}

	@Init
	public void load(FMLInitializationEvent evt)
	{
		System.out.println(NAME + " has loaded: " + TranslationHelper.loadLanguages(RESOURCE_DIRECTORY + "language/", new String[] { "en_US" }));

		GameRegistry.registerBlock(blockFortronite, "MFFSFortonite");
		GameRegistry.registerBlock(blockForceField, "MFFSForceField");
		GameRegistry.registerTileEntity(TileEntityForceField.class, "MFFSForceField");

		MachineTypes.initialize();
		ProjectorTypes.initialize();
		ProjectorOptions.initialize();

		NetworkRegistry.instance().registerGuiHandler(instance, proxy);

		proxy.init();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent evt)
	{
		MFFSRecipes.init();
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkloadCallback());
	}

	public boolean initiateModule(String modname)
	{
		if (Loader.isModLoaded(modname))
		{
			LOGGER.log(Level.INFO, MessageFormat.format("Loaded module for: {0}", modname));
			return true;
		}
		else
		{
			LOGGER.log(Level.INFO, MessageFormat.format("Module not loaded: {0}", modname));
			return false;
		}
	}

	public class ChunkloadCallback implements ForgeChunkManager.OrderedLoadingCallback
	{
		@Override
		public void ticketsLoaded(List<Ticket> tickets, World world)
		{
			for (Ticket ticket : tickets)
			{
				int x = ticket.getModData().getInteger("xCoord");
				int y = ticket.getModData().getInteger("yCoord");
				int z = ticket.getModData().getInteger("zCoord");
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

				if (tileEntity instanceof TileEntityMFFS)
				{
					((TileEntityMFFS) tileEntity).forceChunkLoading(ticket);
				}
			}
		}

		@Override
		public List ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
		{
			List validTickets = new ArrayList<Ticket>();

			for (Ticket ticket : tickets)
			{
				int x = ticket.getModData().getInteger("xCoord");
				int y = ticket.getModData().getInteger("yCoord");
				int z = ticket.getModData().getInteger("zCoord");

				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

				if (tileEntity instanceof TileEntityMFFS)
				{
					validTickets.add(ticket);
				}
			}
			return validTickets;
		}
	}
}

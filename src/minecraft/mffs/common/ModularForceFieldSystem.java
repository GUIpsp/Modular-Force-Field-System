package mffs.common;

import java.io.File;
import java.util.List;
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
import mffs.common.multitool.ItemMultiToolManual;
import mffs.common.multitool.ItemMultitoolSwitch;
import mffs.common.multitool.ItemMultitoolWriter;
import mffs.common.multitool.ItemWrench;
import mffs.common.options.ItemOptionAntibiotic;
import mffs.common.options.ItemOptionCamoflage;
import mffs.common.options.ItemOptionCutter;
import mffs.common.options.ItemOptionDefenseStation;
import mffs.common.options.ItemOptionFieldFusion;
import mffs.common.options.ItemOptionFieldManipulator;
import mffs.common.options.ItemOptionJammer;
import mffs.common.options.ItemOptionShock;
import mffs.common.options.ItemOptionSponge;
import mffs.common.tileentity.TileEntityForceField;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.upgrade.ItemModuleDistance;
import mffs.common.upgrade.ItemModuleStrength;
import mffs.common.upgrade.ItemProjectorFocusMatrix;
import mffs.common.upgrade.ItemUpgradeBooster;
import mffs.common.upgrade.ItemUpgradeCapacity;
import mffs.common.upgrade.ItemUpgradeRange;
import mffs.network.client.ForceFieldClientUpdatehandler;
import mffs.network.client.NetworkHandlerClient;
import mffs.network.server.ForceFieldServerUpdatehandler;
import mffs.network.server.NetworkHandlerServer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;

import org.modstats.ModstatInfo;
import org.modstats.Modstats;

import universalelectricity.prefab.TranslationHelper;
import universalelectricity.prefab.UEDamageSource;
import universalelectricity.prefab.ore.OreGenBase;
import universalelectricity.prefab.ore.OreGenReplaceStone;
import universalelectricity.prefab.ore.OreGenerator;

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
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import java.text.MessageFormat;
import java.util.logging.Level;

@Mod(modid = ModularForceFieldSystem.ID, name = ModularForceFieldSystem.NAME, version = ModularForceFieldSystem.VERSION, dependencies = "after:ThermalExpansion")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerClient.class), serverPacketHandlerSpec = @NetworkMod.SidedPacketHandler(channels = { "MFFS" }, packetHandler = NetworkHandlerServer.class))
@ModstatInfo(prefix = "mffs")
public class ModularForceFieldSystem
{

    public static final String ID = "ModularForceFieldSystem";
    public static final String NAME = "Modular Force Field System";
    public static final String VERSION = "3.0.0";
    public static final String RESOURCE_DIRECTORY = "/mffs/";
    public static final String TEXTURE_DIRECTORY = RESOURCE_DIRECTORY + "textures/";
    public static final String BLOCK_TEXTURE_FILE = TEXTURE_DIRECTORY + "blocks.png";
    public static final String ITEM_TEXTURE_FILE = TEXTURE_DIRECTORY + "items.png";
    public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";
    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "UniversalElectricity/" + NAME + ".cfg"));
    public static int RENDER_ID = 2908;
    /**
     * Machines
     */
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
    /**
     * General Items
     */
    public static Item itemForcicumCell;
    public static Item itemForcicium;
    public static Item itemPowerCrystal;
    public static Item itemCompactForcicium;
    public static Item itemDepletedForcicium;
    public static Item itemFocusMatix;
    /**
     * Multitool
     */
    public static Item itemMultiToolSwitch;
    public static Item itemMultiToolWrench;
    public static Item itemMultiToolFieldTeleporter;
    public static Item itemMultiToolID;
    /**
     * Cards
     */
    public static Item itemCardEmpty;
    public static Item itemCardPowerLink;
    public static Item itemCardID;
    public static Item itemCardAccess;
    public static Item itemCardSecurityLink;
    public static Item itemMultiToolManual;
    public static Item itemCardInfinite;
    public static Item itemCardDataLink;
    /**
     * Upgrades
     */
    public static Item itemUpgradeBoost;
    public static Item itemUpgradeRange;
    public static Item itemUpgradeCapacity;
    /**
     * Module/Options
     */
    public static Item itemOptionShock;
    public static Item itemOptionSponge;
    public static Item itemOptionFieldManipulator;
    public static Item itemOptionCutter;
    public static Item itemOptionAntibiotic;
    public static Item itemOptionDefenseeStation;
    public static Item itemOptionJammer;
    public static Item itemOptionCamouflage;
    public static Item itemOptionFieldFusion;
    /**
     * Modules
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
    
    @Mod.Instance(ModularForceFieldSystem.ID)
    public static ModularForceFieldSystem instance;
    public static final Logger LOGGER = Logger.getLogger(NAME);

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
        if (initiateModule("BuildCraft|Core"))
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
        MinecraftForge.EVENT_BUS.register(proxy);

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

            Property prop_graphicstyle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "GraphicStyle", 1);
            prop_graphicstyle.comment = "Graphical style. 1 for UE Style, 2 for IC2 Style.";
            MFFSProperties.graphicStyle = prop_graphicstyle.getInt(1);

            Property chunckloader_prop = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Chunkloader", true);
            chunckloader_prop.comment = "Set this to false to turn off the MFFS Chuncloader ability";
            MFFSProperties.chunckLoader = chunckloader_prop.getBoolean(true);

            Property DefSationNPCScannoti = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationNPCScannnotification", false);
            DefSationNPCScannoti.comment = "Set this to true to turn off the DefenceStation notification is in NPC Mode";
            MFFSProperties.defenseStationNPCNotification = DefSationNPCScannoti.getBoolean(false);

            Property zapperParticles = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "renderZapperParticles", true);
            zapperParticles.comment = "Set this to false to turn off the small smoke particles present around TouchDamage enabled ForceFields.";
            MFFSProperties.advancedParticles = zapperParticles.getBoolean(true);

            Property uumatterForciciumprop = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "uumatterForcicium", true);
            uumatterForciciumprop.comment = "Add IC2 UU-Matter Recipes for Forcicium";
            MFFSProperties.uumatterEnabled = uumatterForciciumprop.getBoolean(true);

            Property adminList = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ForceFieldMaster", "nobody");
            adminList.comment = "Add users to this list to give them admin permissions split by ;";
            MFFSProperties.Admin = adminList.value;

            Property influencedByOther = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "influencedbyothermods", true);
            influencedByOther.comment = "Should MFFS be influenced by other mods. e.g. ICBM's EMP";
            MFFSProperties.influencedbyothermods = Boolean.valueOf(influencedByOther.getBoolean(true));

            Property ffRemoveWaterLavaOnly = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldremoveonlywaterandlava", false);
            ffRemoveWaterLavaOnly.comment = "Should forcefields only remove water and lava when sponge is enabled?";
            MFFSProperties.forcefieldremoveonlywaterandlava = Boolean.valueOf(ffRemoveWaterLavaOnly.getBoolean(false));

            Property feTransportCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldtransportcost", 10000);
            feTransportCost.comment = "How much FE should it cost to transport through a field?";
            MFFSProperties.forcefieldtransportcost = feTransportCost.getInt(10000);

            Property feFieldBlockCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcost", 1);
            feFieldBlockCost.comment = "How much upkeep FE cost a default ForceFieldblock per second";
            MFFSProperties.forcefieldblockcost = feFieldBlockCost.getInt(1);

            Property BlockCreateMod = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockcreatemodifier", 10);
            BlockCreateMod.comment = "Energy need for create a ForceFieldblock (forcefieldblockcost*forcefieldblockcreatemodifier)";
            MFFSProperties.forcefieldblockcreatemodifier = BlockCreateMod.getInt(10);

            Property ffZapperMod = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldblockzappermodifier", 2);
            ffZapperMod.comment = "Energy need multiplier used when the zapper option is installed";
            MFFSProperties.forcefieldblockzappermodifier = ffZapperMod.getInt(2);

            Property maxFFGenPerTick = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "forcefieldmaxblockpeerTick", 5000);
            maxFFGenPerTick.comment = "How many field blocks can be generated per tick?";
            MFFSProperties.forcefieldmaxblockpeerTick = maxFFGenPerTick.getInt(5000);

            Property fcWorkCycle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ForceciumWorkCylce", 250);
            fcWorkCycle.comment = "WorkCycle amount of Forcecium inside a Extractor";
            MFFSProperties.ForceciumWorkCylce = fcWorkCycle.getInt(250);

            Property fcCellWorkCycle = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ForceciumCellWorkCylce", 230);
            fcCellWorkCycle.comment = "WorkCycle amount of Forcecium Cell inside a Extractor";
            MFFSProperties.ForceciumCellWorkCylce = fcCellWorkCycle.getInt(230);

            Property extractorPassFEGen = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "ExtractorPassForceEnergyGenerate", 12000);
            extractorPassFEGen.comment = "How many ForceEnergy generate per WorkCycle";
            MFFSProperties.ExtractorPassForceEnergyGenerate = extractorPassFEGen.getInt(12000);

            MFFSProperties.ExtractorPassForceEnergyGenerate = MFFSProperties.ExtractorPassForceEnergyGenerate / 4000 * 4000;

            Property defStationKillCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationKillForceEnergy", 10000);
            defStationKillCost.comment = "How much FE does the AreaDefenseStation when killing someone";
            MFFSProperties.DefenceStationKillForceEnergy = defStationKillCost.getInt(10000);

            Property defStationSearchCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationSearchForceEnergy", 1000);
            defStationSearchCost.comment = "How much FE does the AreaDefenseStation when searching someone for contraband";
            MFFSProperties.DefenceStationSearchForceEnergy = defStationSearchCost.getInt(1000);

            Property defStationScannCost = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "DefenceStationScannForceEnergy", 10);
            defStationScannCost.comment = "How much FE does the AreaDefenseStation when Scann for Targets (amount * range / tick)";
            MFFSProperties.DefenceStationScannForceEnergy = defStationScannCost.getInt(10);

            Property Adventuremap = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "adventuremap", false);
            Adventuremap.comment = "Set MFFS to AdventureMap Mode Extractor need no Forcecium and ForceField have no click damage";
            MFFSProperties.adventuremap = Boolean.valueOf(Adventuremap.getBoolean(false));

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

            itemOptionShock = new ItemOptionShock(MFFSProperties.item_OptTouchHurt_ID);
            itemOptionSponge = new ItemOptionSponge(MFFSProperties.item_OptSponge_ID);
            itemOptionFieldManipulator = new ItemOptionFieldManipulator(MFFSProperties.item_OptManipulator_ID);
            itemOptionCutter = new ItemOptionCutter(MFFSProperties.item_OptBlockBreaker_ID);
            itemOptionDefenseeStation = new ItemOptionDefenseStation(MFFSProperties.item_OptDefense_ID);
            itemOptionAntibiotic = new ItemOptionAntibiotic(MFFSProperties.item_OptMobDefense_ID);
            itemOptionJammer = new ItemOptionJammer(MFFSProperties.item_OptJammer_ID);
            itemOptionCamouflage = new ItemOptionCamoflage(MFFSProperties.item_OptCamouflage_ID);
            itemOptionFieldFusion = new ItemOptionFieldFusion(MFFSProperties.item_OptFusion_ID);

            itemCardEmpty = new ItemCardEmpty(MFFSProperties.item_BlankCard_ID);
            itemCardPowerLink = new ItemCardPowerLink(MFFSProperties.item_CardPowerLink_ID);
            itemCardID = new ItemCardPersonalID(MFFSProperties.item_CardPersonalID_ID);
            itemCardSecurityLink = new ItemCardSecurityLink(MFFSProperties.item_CardSecurityLink_ID);
            itemCardInfinite = new ItemCardPower(MFFSProperties.item_infPowerCard_ID);
            itemCardAccess = new ItemAccessCard(MFFSProperties.item_CardAccess_ID);
            itemCardDataLink = new ItemCardDataLink(MFFSProperties.item_CardDataLink_ID);

            itemMultiToolWrench = new ItemWrench(MFFSProperties.item_MTWrench_ID);
            itemMultiToolSwitch = new ItemMultitoolSwitch(MFFSProperties.item_MTSwitch_ID);
            itemMultiToolFieldTeleporter = new ItemFieldTransporter(MFFSProperties.item_MTFieldTransporter_ID);
            itemMultiToolID = new ItemMultitoolWriter(MFFSProperties.item_MTIDWriter_ID);
            itemMultiToolManual = new ItemMultiToolManual(MFFSProperties.item_MTManual_ID);

            itemUpgradeBoost = new ItemUpgradeBooster(MFFSProperties.item_upgradeBoost_ID);
            itemUpgradeRange = new ItemUpgradeRange(MFFSProperties.item_upgradeRange_ID);
            itemUpgradeCapacity = new ItemUpgradeCapacity(MFFSProperties.item_upgradeCap_ID);

            Property monazitWorldAmount = CONFIGURATION.get(Configuration.CATEGORY_GENERAL, "Monazit Generation", 15);
            monazitWorldAmount.comment = "The amount of monazite to generate per chunk.";
            monaziteOreGeneration = new OreGenReplaceStone("Monazite Ore", "oreMonazite", new ItemStack(blockMonaziteOre), 80, monazitWorldAmount.getInt(15), 4).enable(CONFIGURATION);
            OreGenerator.addOre(monaziteOreGeneration);
        } catch (Exception e)
        {
            LOGGER.severe("Failed to load blocks and configuration!");
            LOGGER.severe(e.getMessage());
        } finally
        {
            CONFIGURATION.save();
        }
    }

    @Mod.Init
    public void load(FMLInitializationEvent evt)
    {
        System.out.println(NAME + " has loaded: " + TranslationHelper.loadLanguages(RESOURCE_DIRECTORY + "language/", new String[]
        {
            "en_US"
        }));

        GameRegistry.registerBlock(blockMonaziteOre, "MFFSMonaziteOre");
        GameRegistry.registerBlock(blockForceField, "MFFSForceField");
        GameRegistry.registerTileEntity(TileEntityForceField.class, "MFFSForceField");

        MachineTypes.initialize();
        ProjectorTypes.initialize();
        ProjectorOptions.initialize();

        NetworkRegistry.instance().registerGuiHandler(instance, proxy);

        proxy.init();
    }

    @Mod.PostInit
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
        } else
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
            for (ForgeChunkManager.Ticket ticket : tickets)
            {
                int MaschineX = ticket.getModData().getInteger("MaschineX");
                int MaschineY = ticket.getModData().getInteger("MaschineY");
                int MaschineZ = ticket.getModData().getInteger("MaschineZ");
                TileEntityMFFS Machines = (TileEntityMFFS) world.getBlockTileEntity(MaschineX, MaschineY, MaschineZ);

                Machines.forceChunkLoading(ticket);
            }
        }

        @Override
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
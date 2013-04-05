package mffs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mffs.api.IDefenseStation;
import mffs.api.SecurityPermission;
import mffs.item.ItemBase;
import mffs.item.ItemFocusMatrix;
import mffs.item.ItemForcillium;
import mffs.item.ItemFortronCell;
import mffs.item.ItemRemote;
import mffs.item.card.ItemCardEmpty;
import mffs.item.card.ItemCardFrequency;
import mffs.item.card.ItemCardID;
import mffs.item.card.ItemCardInfinite;
import mffs.item.card.ItemCardLink;
import mffs.item.card.ItemCardTemporary;
import mffs.item.mode.ItemProjectorMode;
import mffs.item.mode.ItemProjectorModeCube;
import mffs.item.mode.ItemProjectorModeSphere;
import mffs.item.mode.ItemProjectorModeTube;
import mffs.item.module.ItemCapacity;
import mffs.item.module.ItemModule;
import mffs.item.module.ItemModuleRange;
import mffs.item.module.ItemModuleScale;
import mffs.item.module.ItemModuleSpeed;
import mffs.item.module.ItemModuleStabiliser;
import mffs.item.module.ItemModuleTranslation;
import mffs.item.module.ItemRotation;
import mffs.item.module.defence.ItMD;
import mffs.item.module.defence.ItMDAntiFriendly;
import mffs.item.module.defence.ItMDAntiHostile;
import mffs.item.module.defence.ItMDAntiPersonnel;
import mffs.item.module.defence.ItMDConfiscate;
import mffs.item.module.defence.ItMDWarn;
import mffs.item.module.projector.ItemModuleCamoflage;
import mffs.item.module.projector.ItemModuleDisintegration;
import mffs.item.module.projector.ItemModuleFusion;
import mffs.item.module.projector.ItemModuleJammer;
import mffs.item.module.projector.ItemModuleManipulator;
import mffs.item.module.projector.ItemModuleShock;
import mffs.item.module.projector.ItemModuleSponge;
import mffs.machine.BlockCapacitor;
import mffs.machine.BlockDefenceStation;
import mffs.machine.BlockExtractor;
import mffs.machine.BlockForcefield;
import mffs.machine.BlockFortronite;
import mffs.machine.BlockProjector;
import mffs.machine.BlockSecurityStation;
import mffs.machine.tile.TLiQiang;
import mffs.machine.tile.TileDefenceStation;
import mffs.machine.tile.TileMFFS;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.modstats.ModstatInfo;
import org.modstats.Modstats;

import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.CustomDamageSource;
import universalelectricity.prefab.TranslationHelper;
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
@NetworkMod(clientSideRequired = true, channels = { ModularForceFieldSystem.CHANNEL }, packetHandler = PacketManager.class)
@ModstatInfo(prefix = "mffs")
public class ModularForceFieldSystem
{
	public static final String CHANNEL = "MFFS";
	public static final String ID = "ModularForceFieldSystem";
	public static final String NAME = "Modular Force Field System";
	public static final String PREFIX = "mffs:";
	public static final String VERSION = "3.0.0";

	/**
	 * Directories.
	 */
	public static final String RESOURCE_DIRECTORY = "/mods/mffs/";
	public static final String TEXTURE_DIRECTORY = RESOURCE_DIRECTORY + "textures/";
	public static final String BLOCK_DIRECTORY = TEXTURE_DIRECTORY + "blocks/";
	public static final String ITEM_DIRECTORY = TEXTURE_DIRECTORY + "items/";
	public static final String MODEL_DIRECTORY = TEXTURE_DIRECTORY + "models/";
	public static final String GUI_DIRECTORY = TEXTURE_DIRECTORY + "gui/";
	public static final String GUI_BASE_DIRECTORY = GUI_DIRECTORY + "gui_base.png";
	public static final String GUI_COMPONENTS = GUI_DIRECTORY + "gui_components.png";
	public static final String GUI_BUTTON = GUI_DIRECTORY + "gui_button.png";

	/**
	 * Machines/Blocks
	 */
	public static Block blockFortronite;
	public static Block blockForcefield;

	public static Block blockCapacitor;
	public static Block blockProjector;
	public static Block blockDefenceStation;
	public static Block blockExtractor;
	public static Block blockSecurityStation;

	/**
	 * General Items
	 */
	public static Item itemFortron;

	public static Item itemFocusMatix;
	public static Item itemFortronCell;
	public static Item itemForcillium;
	public static Item itemRemote;

	/**
	 * Cards
	 */
	public static Item itemCardInfinite;
	public static Item itemCardEmpty;
	public static Item itemCardFrequency;
	public static ItemCardID itemCardID;
	public static Item itemCardTemporary;
	public static Item itemCardLink;

	/**
	 * Modes
	 */
	public static ItemProjectorMode itemModeSphere;
	public static ItemProjectorMode itemModeCube;
	public static ItemProjectorMode itemModeTube;

	/**
	 * Modules
	 */
	public static ItemModule itMSuDu, itMJuLi, itMRongLiang, itMDian, itemModuleSponge,
			itemModuleManipulator, itemModuleDisintegration, itemModuleJammer, itWeiZhuang,
			itemModuleFusion, itMDaXiao, itemModuleTranslation, itMZhuan, itMGuang, itMZhenDing;

	/**
	 * Defense Station Modules
	 */
	public static ItMD itemModuleAntiHostile, itemModuleAntiFriendly, itemModuleAntiPersonnel,
			itemModuleConfiscate, itemModuleWarn, itMDBA, itMDBPA;

	public static OreGenBase fortroniteOreGeneration;

	public static DamageSource fieldShock = new CustomDamageSource("fieldShock").setDamageBypassesArmor();
	public static DamageSource areaDefense = new CustomDamageSource("areaDefense").setDamageBypassesArmor();
	public static DamageSource fieldDefense = new CustomDamageSource("fieldDefense").setDamageBypassesArmor();

	@SidedProxy(clientSide = "mffs.ClientProxy", serverSide = "mffs.CommonProxy")
	public static CommonProxy proxy;

	@Instance(ModularForceFieldSystem.ID)
	public static ModularForceFieldSystem instance;
	public static final Logger LOGGER = Logger.getLogger(NAME);

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER.setParent(FMLLog.getLogger());
		MinecraftForge.EVENT_BUS.register(this);
		NetworkRegistry.instance().registerGuiHandler(this, ModularForceFieldSystem.proxy);
		Modstats.instance().getReporter().registerMod(this);

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
		if (initiateModule("ThermalExpansion"))
		{
			MFFSConfiguration.MODULE_THERMAL_EXPANSION = true;
		}

		try
		{
			MFFSConfiguration.initialize();
			MFFSConfiguration.CONFIGURATION.load();

			blockFortronite = new BlockFortronite(MFFSConfiguration.getNextBlockID(), "fortronite");
			blockExtractor = new BlockExtractor(MFFSConfiguration.getNextBlockID());
			blockSecurityStation = new BlockSecurityStation(MFFSConfiguration.getNextBlockID());
			blockCapacitor = new BlockCapacitor(MFFSConfiguration.getNextBlockID());
			blockProjector = new BlockProjector(MFFSConfiguration.getNextBlockID());
			blockForcefield = new BlockForcefield(MFFSConfiguration.getNextBlockID());
			blockDefenceStation = new BlockDefenceStation(MFFSConfiguration.getNextBlockID());

			itemForcillium = new ItemForcillium(MFFSConfiguration.getNextItemID());
			itemFortronCell = new ItemFortronCell(MFFSConfiguration.getNextItemID());

			itemFocusMatix = new ItemFocusMatrix(MFFSConfiguration.getNextItemID());

			// Modes
			itemModeSphere = new ItemProjectorModeSphere(MFFSConfiguration.getNextItemID());
			itemModeCube = new ItemProjectorModeCube(MFFSConfiguration.getNextItemID());
			itemModeTube = new ItemProjectorModeTube(MFFSConfiguration.getNextItemID());

			// Modules
			itMDaXiao = new ItemModuleScale(MFFSConfiguration.getNextItemID());
			itemModuleTranslation = new ItemModuleTranslation(MFFSConfiguration.getNextItemID());
			itMZhuan = new ItemRotation(MFFSConfiguration.getNextItemID());

			itMSuDu = new ItemModuleSpeed(MFFSConfiguration.getNextItemID());
			itMJuLi = new ItemModuleRange(MFFSConfiguration.getNextItemID());
			itMRongLiang = new ItemCapacity(MFFSConfiguration.getNextItemID());
			itemRemote = new ItemRemote(MFFSConfiguration.getNextItemID());

			itMDian = new ItemModuleShock(MFFSConfiguration.getNextItemID());
			itemModuleSponge = new ItemModuleSponge(MFFSConfiguration.getNextItemID());
			itemModuleManipulator = new ItemModuleManipulator(MFFSConfiguration.getNextItemID());
			itemModuleDisintegration = new ItemModuleDisintegration(MFFSConfiguration.getNextItemID());
			itemModuleJammer = new ItemModuleJammer(MFFSConfiguration.getNextItemID());
			itWeiZhuang = new ItemModuleCamoflage(MFFSConfiguration.getNextItemID());
			itemModuleFusion = new ItemModuleFusion(MFFSConfiguration.getNextItemID());
			itMGuang = new ItemModule(MFFSConfiguration.getNextItemID(), "moduleGlow").setCost(0.1f);
			itMZhenDing = new ItemModuleStabiliser(MFFSConfiguration.getNextItemID());

			itemModuleAntiFriendly = new ItMDAntiFriendly(MFFSConfiguration.getNextItemID());
			itemModuleAntiHostile = new ItMDAntiHostile(MFFSConfiguration.getNextItemID());
			itemModuleAntiPersonnel = new ItMDAntiPersonnel(MFFSConfiguration.getNextItemID());
			itemModuleConfiscate = new ItMDConfiscate(MFFSConfiguration.getNextItemID());
			itemModuleWarn = new ItMDWarn(MFFSConfiguration.getNextItemID());
			itMDBA = new ItMD(MFFSConfiguration.getNextItemID(), "moduleBlockAccess");
			itMDBPA = new ItMD(MFFSConfiguration.getNextItemID(), "moduleBlockPlaceAccess");

			itemCardEmpty = new ItemCardEmpty(MFFSConfiguration.getNextItemID());
			itemCardFrequency = new ItemCardFrequency(MFFSConfiguration.getNextItemID());
			itemCardInfinite = new ItemCardInfinite(MFFSConfiguration.getNextItemID());
			itemCardID = new ItemCardID(MFFSConfiguration.getNextItemID());
			itemCardTemporary = new ItemCardTemporary(MFFSConfiguration.getNextItemID());
			itemCardLink = new ItemCardLink(MFFSConfiguration.getNextItemID());

			/**
			 * The Fortron Liquid
			 */
			itemFortron = new ItemBase(MFFSConfiguration.getNextItemID(), "fortron").setCreativeTab(null);
			Fortron.LIQUID_FORTRON = LiquidDictionary.getOrCreateLiquid("Fortron", new LiquidStack(itemFortron, 0));

			fortroniteOreGeneration = new OreGenReplaceStone("Fortronite", "oreFortronite", new ItemStack(blockFortronite), 80, 17, 4);
			fortroniteOreGeneration.shouldGenerate = MFFSConfiguration.CONFIGURATION.get("Ore Generation", "Generate Fortronite", false).getBoolean(false);
			OreGenerator.addOre(fortroniteOreGeneration);

			OreDictionary.registerOre("itemForcillium", itemForcillium);
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

		proxy.preInit();
	}

	@Init
	public void load(FMLInitializationEvent evt)
	{
		LOGGER.fine("Language(s) Loaded: " + TranslationHelper.loadLanguages(RESOURCE_DIRECTORY + "yuyan/", new String[] { "en_US" }));
		GameRegistry.registerBlock(blockFortronite, "MFFSFortonite");
		GameRegistry.registerBlock(blockForcefield, "MFFSForceField");
		GameRegistry.registerTileEntity(TLiQiang.class, "MFFSForceField");

		BlockMachine.initialize();

		proxy.init();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent evt)
	{
		/**
		 * Add Recipes
		 */
		MFFSRecipes.init();
		ForgeChunkManager.setForcedChunkLoadingCallback(this, new ChunkloadCallback());
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

	public static List<String> splitStringPerWord(String string, int wordsPerLine)
	{
		String[] words = string.split(" ");
		List<String> lines = new ArrayList<String>();

		for (int lineCount = 0; lineCount < Math.ceil((float) words.length / (float) wordsPerLine); lineCount++)
		{
			String stringInLine = "";

			for (int i = lineCount * wordsPerLine; i < Math.min(wordsPerLine + lineCount * wordsPerLine, words.length); i++)
			{
				stringInLine += words[i] + " ";
			}

			lines.add(stringInLine.trim());
		}

		return lines;
	}

	/**
	 * Prevent protected blocks from being interacted with.
	 */
	@ForgeSubscribe
	public void playerInteractEvent(PlayerInteractEvent evt)
	{
		if (evt.action == Action.RIGHT_CLICK_BLOCK || evt.action == Action.LEFT_CLICK_BLOCK)
		{
			/**
			 * Disable block breaking of force fields.
			 */
			if (evt.action == Action.LEFT_CLICK_BLOCK && evt.entityPlayer.worldObj.getBlockId(evt.x, evt.y, evt.z) == blockForcefield.blockID)
			{
				evt.setCanceled(true);
				return;
			}

			IDefenseStation defenseStation = TileDefenceStation.getNearestDefenseStation(evt.entityPlayer.worldObj, new Vector3(evt.x, evt.y, evt.z));

			if (defenseStation != null && !evt.entityPlayer.capabilities.isCreativeMode)
			{
				if (defenseStation.isActive())
				{
					boolean hasPermission = true;
					TileEntity tileEntity = evt.entityPlayer.worldObj.getBlockTileEntity(evt.x, evt.y, evt.z);

					if (tileEntity != null && evt.action == Action.RIGHT_CLICK_BLOCK)
					{
						if (defenseStation.getModuleCount(ModularForceFieldSystem.itMDBA) > 0)
						{
							hasPermission = false;

							if (defenseStation.getSecurityCenter() != null)
							{
								if (defenseStation.getSecurityCenter().isAccessGranted(evt.entityPlayer.username, SecurityPermission.BLOCK_ACCESS))
								{
									hasPermission = true;
								}
							}
						}

					}
					else if (defenseStation.getModuleCount(ModularForceFieldSystem.itMDBPA) > 0)
					{
						if (new Vector3(evt.x, evt.y, evt.z).modifyPositionFromSide(ForgeDirection.getOrientation(evt.face)).getBlockID(evt.entityPlayer.worldObj) <= 0)
						{
							hasPermission = false;

							if (defenseStation.getSecurityCenter() != null)
							{
								if (defenseStation.getSecurityCenter().isAccessGranted(evt.entityPlayer.username, SecurityPermission.BLOCK_PLACE_ACCESS))
								{
									hasPermission = true;
								}
							}
						}
					}

					if (!hasPermission)
					{
						evt.entityPlayer.sendChatToPlayer("[" + ModularForceFieldSystem.blockDefenceStation.getLocalizedName() + "] You have no permission to do that!");
						evt.setCanceled(true);
					}
				}
			}
		}
	}

	/**
	 * Gets a compound from an itemStack.
	 * 
	 * @param itemStack
	 * @return
	 */
	public static NBTTagCompound getNBTTagCompound(ItemStack itemStack)
	{
		if (itemStack != null)
		{
			if (itemStack.getTagCompound() == null)
			{
				itemStack.setTagCompound(new NBTTagCompound());
			}

			return itemStack.getTagCompound();
		}

		return null;
	}

	public class ChunkloadCallback implements OrderedLoadingCallback
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

				if (tileEntity instanceof TileMFFS)
				{
					((TileMFFS) tileEntity).forceChunkLoading(ticket);
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

				if (tileEntity instanceof TileMFFS)
				{
					validTickets.add(ticket);
				}
			}
			return validTickets;
		}
	}
}

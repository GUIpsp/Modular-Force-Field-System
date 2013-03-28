package mffs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import mffs.api.IDefenseStation;
import mffs.api.SecurityPermission;
import mffs.it.ItemFocusMatrix;
import mffs.it.ItemForcillium;
import mffs.it.ItemFortronCell;
import mffs.it.ItemMFFS;
import mffs.it.ka.ItKaKong;
import mffs.it.ka.ItKaLian;
import mffs.it.ka.ItKaShenFen;
import mffs.it.ka.ItKaShenFenZhanShi;
import mffs.it.ka.ItKaShengBuo;
import mffs.it.ka.ItKaWuXian;
import mffs.it.muo.ItM;
import mffs.it.muo.ItMJuLi;
import mffs.it.muo.ItMRongLiang;
import mffs.it.muo.ItMSuDu;
import mffs.it.muo.ItemModuleRotation;
import mffs.it.muo.ItemModuleScale;
import mffs.it.muo.ItemModuleTranslate;
import mffs.it.muo.fangyingji.ItemModuleCamoflage;
import mffs.it.muo.fangyingji.ItemModuleDisintegration;
import mffs.it.muo.fangyingji.ItemModuleFusion;
import mffs.it.muo.fangyingji.ItemModuleJammer;
import mffs.it.muo.fangyingji.ItemModuleManipulator;
import mffs.it.muo.fangyingji.ItemModuleShock;
import mffs.it.muo.fangyingji.ItemModuleSponge;
import mffs.it.muo.fangyu.ItMD;
import mffs.it.muo.fangyu.ItMDAntiFriendly;
import mffs.it.muo.fangyu.ItMDAntiHostile;
import mffs.it.muo.fangyu.ItMDAntiPersonnel;
import mffs.it.muo.fangyu.ItMDConfiscate;
import mffs.it.muo.fangyu.ItMDWarn;
import mffs.it.xingshi.ItMSphere;
import mffs.it.xingshi.ItMC;
import mffs.it.xingshi.ItMTube;
import mffs.it.xingshi.ItemProjectorMode;
import mffs.jiqi.BLiQiang;
import mffs.jiqi.BlockDefenseStation;
import mffs.jiqi.BlockForcilliumExtractor;
import mffs.jiqi.BlockFortronCapacitor;
import mffs.jiqi.BlockFortronite;
import mffs.jiqi.BlockProjector;
import mffs.jiqi.BlockSecurityCenter;
import mffs.jiqi.t.TFangYu;
import mffs.jiqi.t.TLiQiang;
import mffs.jiqi.t.TileEntityMFFS;
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

@Mod(modid = ZhuYao.ID, name = ZhuYao.NAME, version = ZhuYao.VERSION, dependencies = "after:ThermalExpansion")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { ZhuYao.CHANNEL }, packetHandler = PacketManager.class, connectionHandler = ConnectionHandler.class)
@ModstatInfo(prefix = "mffs")
public class ZhuYao
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
	 * Machines
	 */
	public static Block blockCapacitor;
	public static Block blockProjector;
	public static Block blockDefenceStation;
	public static Block blockForceField;
	public static Block blockExtractor;
	public static Block blockFortronite;
	public static Block blockSecurityStation;

	/**
	 * Fortron related items
	 */
	public static Item itemFortron;
	public static Item itemFortronCell;
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
	public static Item itKaKong;
	public static Item itKaShengBuo;
	public static ItKaShenFen itKaShenFen;
	public static Item itKaShenFenZhanShi;
	public static Item itKaWuXian;
	public static Item itKaLian;

	/**
	 * Modules
	 */
	public static ItM itMSuDu;
	public static ItM itMJuLi;
	public static ItM itMRongLiang;
	public static ItM itemModuleShock;
	public static ItM itemModuleSponge;
	public static ItM itemModuleManipulator;
	public static ItM itemModuleDisintegration;
	public static ItM itemModuleJammer;
	public static ItM itemModuleCamouflage;
	public static ItM itemModuleFusion;
	public static ItM itemModuleScale;
	public static ItM itemModuleTranslation;
	public static ItM itemModuleRotation;

	/**
	 * Defense Station Modules
	 */
	public static ItMD itemModuleAntiHostile, itemModuleAntiFriendly, itemModuleAntiPersonnel,
			itemModuleConfiscate, itemModuleWarn, itMDBA, itMDBPA;

	/**
	 * Modes
	 */
	public static ItemProjectorMode itemModuleSphere;
	public static ItemProjectorMode itemModuleCube;
	public static ItemProjectorMode itemModuleTube;

	public static OreGenBase fortroniteOreGeneration;

	public static DamageSource fieldShock = new CustomDamageSource("fieldShock").setDamageBypassesArmor();
	public static DamageSource areaDefense = new CustomDamageSource("areaDefense").setDamageBypassesArmor();
	public static DamageSource fieldDefense = new CustomDamageSource("fieldDefense").setDamageBypassesArmor();

	@SidedProxy(clientSide = "mffs.PGuKe", serverSide = "mffs.PGongTong")
	public static PGongTong proxy;

	@Instance(ZhuYao.ID)
	public static ZhuYao instance;
	public static final Logger LOGGER = Logger.getLogger(NAME);

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER.setParent(FMLLog.getLogger());
		MinecraftForge.EVENT_BUS.register(this);

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

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(proxy);

		Modstats.instance().getReporter().registerMod(this);

		try
		{
			MFFSConfiguration.initialize();
			MFFSConfiguration.CONFIGURATION.load();

			blockFortronite = new BlockFortronite(MFFSConfiguration.getNextBlockID(), "fortronite");
			blockExtractor = new BlockForcilliumExtractor(MFFSConfiguration.getNextBlockID());
			blockDefenceStation = new BlockDefenseStation(MFFSConfiguration.getNextBlockID());
			blockCapacitor = new BlockFortronCapacitor(MFFSConfiguration.getNextBlockID());
			blockProjector = new BlockProjector(MFFSConfiguration.getNextBlockID());
			blockForceField = new BLiQiang(MFFSConfiguration.getNextBlockID());
			blockSecurityStation = new BlockSecurityCenter(MFFSConfiguration.getNextBlockID());

			itemForcillium = new ItemForcillium(MFFSConfiguration.getNextItemID());
			itemFortronCell = new ItemFortronCell(MFFSConfiguration.getNextItemID());

			itemFocusMatix = new ItemFocusMatrix(MFFSConfiguration.getNextItemID());

			// Modes
			itemModuleSphere = new ItMSphere(MFFSConfiguration.getNextItemID());
			itemModuleCube = new ItMC(MFFSConfiguration.getNextItemID());
			itemModuleTube = new ItMTube(MFFSConfiguration.getNextItemID());

			itemModuleScale = new ItemModuleScale(MFFSConfiguration.getNextItemID());
			itemModuleTranslation = new ItemModuleTranslate(MFFSConfiguration.getNextItemID());
			itemModuleRotation = new ItemModuleRotation(MFFSConfiguration.getNextItemID());
			itMSuDu = new ItMSuDu(MFFSConfiguration.getNextItemID());
			itMJuLi = new ItMJuLi(MFFSConfiguration.getNextItemID());
			itMRongLiang = new ItMRongLiang(MFFSConfiguration.getNextItemID());

			itemModuleShock = new ItemModuleShock(MFFSConfiguration.getNextItemID());
			itemModuleSponge = new ItemModuleSponge(MFFSConfiguration.getNextItemID());
			itemModuleManipulator = new ItemModuleManipulator(MFFSConfiguration.getNextItemID());
			itemModuleDisintegration = new ItemModuleDisintegration(MFFSConfiguration.getNextItemID());
			itemModuleJammer = new ItemModuleJammer(MFFSConfiguration.getNextItemID());
			itemModuleCamouflage = new ItemModuleCamoflage(MFFSConfiguration.getNextItemID());
			itemModuleFusion = new ItemModuleFusion(MFFSConfiguration.getNextItemID());

			itemModuleAntiFriendly = new ItMDAntiFriendly(MFFSConfiguration.getNextItemID());
			itemModuleAntiHostile = new ItMDAntiHostile(MFFSConfiguration.getNextItemID());
			itemModuleAntiPersonnel = new ItMDAntiPersonnel(MFFSConfiguration.getNextItemID());
			itemModuleConfiscate = new ItMDConfiscate(MFFSConfiguration.getNextItemID());
			itemModuleWarn = new ItMDWarn(MFFSConfiguration.getNextItemID());
			itMDBA = new ItMD(MFFSConfiguration.getNextItemID(), "moduleBlockAccess");
			itMDBPA = new ItMD(MFFSConfiguration.getNextItemID(), "moduleBlockPlaceAccess");

			itKaKong = new ItKaKong(MFFSConfiguration.getNextItemID());
			itKaShengBuo = new ItKaShengBuo(MFFSConfiguration.getNextItemID());
			itKaWuXian = new ItKaWuXian(MFFSConfiguration.getNextItemID());
			itKaShenFen = new ItKaShenFen(MFFSConfiguration.getNextItemID());
			itKaShenFenZhanShi = new ItKaShenFenZhanShi(MFFSConfiguration.getNextItemID());
			itKaLian = new ItKaLian(MFFSConfiguration.getNextItemID());

			/**
			 * The Fortron Liquid
			 */
			itemFortron = new ItemMFFS(MFFSConfiguration.getNextItemID(), "fortron").setCreativeTab(null);
			Li.LIQUID_FORTRON = LiquidDictionary.getOrCreateLiquid("Fortron", new LiquidStack(itemFortron, 0));

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
	}

	@Init
	public void load(FMLInitializationEvent evt)
	{
		System.out.println(NAME + " has loaded: " + TranslationHelper.loadLanguages(RESOURCE_DIRECTORY + "yuyan/", new String[] { "en_US" }));

		GameRegistry.registerBlock(blockFortronite, "MFFSFortonite");
		GameRegistry.registerBlock(blockForceField, "MFFSForceField");
		GameRegistry.registerTileEntity(TLiQiang.class, "MFFSForceField");

		MachineTypes.initialize();

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
	 * 
	 * @param evt
	 */
	@ForgeSubscribe
	public void playerInteractEvent(PlayerInteractEvent evt)
	{
		if (evt.action == Action.RIGHT_CLICK_BLOCK || evt.action == Action.LEFT_CLICK_BLOCK)
		{
			IDefenseStation defenseStation = TFangYu.getNearestDefenseStation(evt.entityPlayer.worldObj, new Vector3(evt.x, evt.y, evt.z));

			if (defenseStation != null && !evt.entityPlayer.capabilities.isCreativeMode)
			{
				if (defenseStation.isActive())
				{
					boolean hasPermission = true;
					TileEntity tileEntity = evt.entityPlayer.worldObj.getBlockTileEntity(evt.x, evt.y, evt.z);

					if (tileEntity != null && evt.action == Action.RIGHT_CLICK_BLOCK)
					{
						if (defenseStation.getModuleCount(ZhuYao.itMDBA) > 0)
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
					else if (defenseStation.getModuleCount(ZhuYao.itMDBPA) > 0)
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
						evt.entityPlayer.sendChatToPlayer("[" + ZhuYao.blockDefenceStation.getLocalizedName() + "] You have no permission to do that!");
						evt.setCanceled(true);
					}
				}
			}
		}
	}

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

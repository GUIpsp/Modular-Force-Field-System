package mffs;

import ic2.api.Items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thermalexpansion.api.crafting.CraftingManagers;
import cpw.mods.fml.common.registry.GameRegistry;

public class MFFSRecipes
{
	public static final HashMap<String, ItemStack> RECIPE_CHARACTERS = new HashMap<String, ItemStack>();

	public static void init()
	{
		/**
		 * Register materials.
		 */
		String metalIngotID = "mffsMetal";

		if (!registerItemStacksToDictionary(metalIngotID, "ingotSteel"))
		{
			if (!registerItemStacksToDictionary(metalIngotID, "ingotRefinedIron"))
			{
				registerItemStacksToDictionary(metalIngotID, new ItemStack(Item.ingotIron));
			}
		}

		String wireID = "mffsWire";

		if (!registerItemStacksToDictionary(wireID, "copperWire"))
		{
			if (!registerItemStacksToDictionary(wireID, Items.getItem("copperCableBlock")))
			{
				registerItemStacksToDictionary(wireID, new ItemStack(Item.redstone));
			}
		}

		String batteryID = "mffsBattery";

		if (!registerItemStacksToDictionary(batteryID, "advancedBattery"))
		{
			if (!registerItemStacksToDictionary(batteryID, Items.getItem("energyCrystal")))
			{
				if (!registerItemStacksToDictionary(batteryID, "battery"))
				{
					registerItemStacksToDictionary(batteryID, new ItemStack(Item.redstoneRepeater));
				}
			}
		}

		/**
		 * Add recipes
		 */
		// -- General Items --
		// Focus Matrix
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itFocusMatix, 4), "RMR", "MDM", "RMR", 'M', metalIngotID, 'D', Item.diamond, 'R', Item.redstone));
		// Fortronite
		GameRegistry.addSmelting(ZhuYao.blockFortronite.blockID, new ItemStack(ZhuYao.itemForcillium, 4), 0.5F);
		// Forcillium
		FurnaceRecipes.smelting().addSmelting(Item.dyePowder.itemID, 4, new ItemStack(ZhuYao.itemForcillium, 4), 0.5F);

		if (MFFSConfiguration.MODULE_THERMAL_EXPANSION)
		{
			CraftingManagers.pulverizerManager.addRecipe(100, new ItemStack(ZhuYao.blockFortronite), new ItemStack(ZhuYao.itemForcillium, 8), true);
			CraftingManagers.pulverizerManager.addRecipe(100, new ItemStack(Item.dyePowder, 4), new ItemStack(ZhuYao.itemForcillium, 8), true);
		}
		// Fortron Cell
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itFortronCell), " F ", "FBF", " F ", 'B', batteryID, 'F', ZhuYao.itFocusMatix));
		// Remote
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itFortronCell), "WWW", "MCM", "MCM", 'W', wireID, 'C', ZhuYao.itFortronCell, 'M', metalIngotID));

		// -- Machines --
		// Capacitor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.bDianRong), "MFM", "FCF", "MFM", 'D', Item.diamond, 'C', ZhuYao.itFortronCell, 'F', ZhuYao.itFocusMatix, 'M', metalIngotID));
		// Projector
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.bFangYingJi), " D ", "FFF", "MCM", 'C', ZhuYao.itFortronCell, 'F', ZhuYao.itFocusMatix, 'M', metalIngotID));
		// Defense Station
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.bFangYu), "SSS", "FFF", "FEF", 'S', ZhuYao.itMDian, 'E', Block.enderChest, 'F', ZhuYao.itFocusMatix));
		// Forcillium Extractor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.bChouQi), "M M", "MFM", "MCM", 'C', ZhuYao.itFortronCell, 'M', metalIngotID, 'F', ZhuYao.itFocusMatix));
		// Security Center
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.bAnQuan), "FMF", "MCM", "FMF", 'C', ZhuYao.itKaKong, 'M', metalIngotID, 'F', ZhuYao.itFocusMatix));

		// -- Cards --
		// Blank
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itKaKong), "PPP", "PMP", "PPP", 'P', Item.paper, 'M', metalIngotID));
		// Link
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itKaLian), "BWB", 'B', ZhuYao.itKaKong, 'W', wireID));
		// Frequency
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itKaShengBuo), "WBW", 'B', ZhuYao.itKaKong, 'W', wireID));
		// ID
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itKaShenFen), "RBR", 'B', ZhuYao.itKaKong, 'R', Item.redstone));
		// Temporary ID
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itKaShenFenZhanShi), "RBR", 'B', ZhuYao.itKaShenFen, 'R', Item.redstone));

		// -- Modes --
		// Temporary ID
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMYuan), " F ", "FFF", " F ", 'F', ZhuYao.itFocusMatix));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMFang), "FFF", "FFF", "FFF", 'F', ZhuYao.itFocusMatix));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMGuan), "FFF", "   ", "FFF", 'F', ZhuYao.itFocusMatix));

		// -- Modules --
		// -- -- General -- --
		// Speed TODO: FIX THIS
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMSuDu), "FPF", 'F', ZhuYao.itFocusMatix, 'P', Item.potion));
		// Range
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMJuLi), "FRF", 'F', ZhuYao.itFocusMatix, 'R', Item.redstone));
		// Capacity
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMRongLiang, 2), "FCF", 'C', ZhuYao.itFortronCell));
		// Shock
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMDian), "FWF", 'W', wireID));
		// Sponge
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleSponge), "FWF", 'W', Item.bucketWater));
		// Disintegration
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleDisintegration), " W ", "FBF", " W ", 'W', wireID));
		// Manipulator
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleManipulator), "F", " ", "F", 'F', ZhuYao.itFocusMatix));
		// Jammer
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleJammer), "F", "W", "F", 'F', ZhuYao.itFocusMatix, 'W', wireID));
		// Camouflage
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleCamouflage), "WFW", "FWF", "WFW", 'F', ZhuYao.itFocusMatix, 'W', Block.cloth));
		// Fusion
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleFusion), "FJF", 'F', ZhuYao.itFocusMatix, 'J', ZhuYao.itemModuleJammer));
		// Scale
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMDaXiao), "FRF", 'F', ZhuYao.itFocusMatix, 'R', ZhuYao.itMJuLi));
		// Translate
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMDong), "FSF", 'F', ZhuYao.itFocusMatix, 'S', ZhuYao.itMSuDu));
		// Rotate
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMZhuan), "F  ", " F ", "  F", 'F', ZhuYao.itFocusMatix));
		// Glow
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMGuang, 4), "GGG", "GFG", "GGG", 'F', ZhuYao.itFocusMatix, 'G', Block.glowStone));
		// -- -- Defense Station -- --
		// Anti-Hostile
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleAntiHostile), " R ", "GFB", " S ", 'F', ZhuYao.itFocusMatix, 'G', Item.gunpowder, 'R', Item.rottenFlesh, 'B', Item.bone, 'S', Item.ghastTear));
		// Anti-Friendly
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleAntiFriendly), " R ", "GFB", " S ", 'F', ZhuYao.itFocusMatix, 'G', Item.porkCooked, 'R', Block.cloth, 'B', Item.leather, 'S', Item.slimeBall));
		// Anti-Personnel
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleAntiPersonnel), "BFG", 'F', ZhuYao.itFocusMatix, 'B', ZhuYao.itemModuleAntiHostile, 'G', ZhuYao.itemModuleAntiFriendly));
		// Confiscate
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleConfiscate), "PEP", "EFE", "PEP", 'F', ZhuYao.itFocusMatix, 'E', Item.eyeOfEnder, "P", Item.enderPearl));
		// Warn
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itemModuleWarn), "NFN", 'F', ZhuYao.itFocusMatix, 'N', Block.music));
		// Block Access
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMDBA), " C ", "BFB", " C ", 'F', ZhuYao.itFocusMatix, 'B', Block.blockSteel, 'C', Block.chest));
		// Block Place
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ZhuYao.itMDBPA), " G ", "GFG", " G ", 'F', ZhuYao.itMDBA, 'G', Block.blockGold));
	}

	public static boolean registerItemStacksToDictionary(String name, List<ItemStack> itemStacks)
	{
		boolean returnValue = false;

		if (itemStacks.size() > 0)
		{
			for (ItemStack stack : itemStacks)
			{
				if (stack != null)
				{
					OreDictionary.registerOre(name, stack);
					returnValue = true;
				}
			}
		}

		return returnValue;
	}

	public static boolean registerItemStacksToDictionary(String name, ItemStack... itemStacks)
	{
		return registerItemStacksToDictionary(name, Arrays.asList(itemStacks));
	}

	public static boolean registerItemStacksToDictionary(String name, String stackName)
	{
		return registerItemStacksToDictionary(name, OreDictionary.getOres(stackName));
	}
}
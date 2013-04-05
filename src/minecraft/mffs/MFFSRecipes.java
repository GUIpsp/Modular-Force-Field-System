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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemFocusMatix, 5), "RMR", "MDM", "RMR", 'M', metalIngotID, 'D', Item.diamond, 'R', Item.redstone));
		// Fortronite
		GameRegistry.addSmelting(ModularForceFieldSystem.blockFortronite.blockID, new ItemStack(ModularForceFieldSystem.itemForcillium, 4), 0.5F);
		// Forcillium
		FurnaceRecipes.smelting().addSmelting(Item.dyePowder.itemID, 2, new ItemStack(ModularForceFieldSystem.itemForcillium, 4), 0.5F);

		if (MFFSConfiguration.MODULE_THERMAL_EXPANSION)
		{
			CraftingManagers.pulverizerManager.addRecipe(100, new ItemStack(ModularForceFieldSystem.blockFortronite), new ItemStack(ModularForceFieldSystem.itemForcillium, 8), true);
			CraftingManagers.pulverizerManager.addRecipe(100, new ItemStack(Item.dyePowder, 4), new ItemStack(ModularForceFieldSystem.itemForcillium, 8), true);
		}
		// Fortron Cell
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemFortronCell), " F ", "FBF", " F ", 'B', batteryID, 'F', ModularForceFieldSystem.itemFocusMatix));
		// Remote
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemRemote), "WWW", "MCM", "MCM", 'W', wireID, 'C', ModularForceFieldSystem.itemFortronCell, 'M', metalIngotID));

		// -- Machines --
		// Capacitor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.blockCapacitor), "MFM", "FCF", "MFM", 'D', Item.diamond, 'C', ModularForceFieldSystem.itemFortronCell, 'F', ModularForceFieldSystem.itemFocusMatix, 'M', metalIngotID));
		// Projector
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.blockProjector), " D ", "FFF", "MCM", 'D', Item.diamond, 'C', ModularForceFieldSystem.itemFortronCell, 'F', ModularForceFieldSystem.itemFocusMatix, 'M', metalIngotID));
		// Defense Station
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.blockDefenceStation), "SSS", "FFF", "FEF", 'S', ModularForceFieldSystem.itMDian, 'E', Block.enderChest, 'F', ModularForceFieldSystem.itemFocusMatix));
		// Forcillium Extractor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.blockExtractor), "M M", "MFM", "MCM", 'C', ModularForceFieldSystem.itemFortronCell, 'M', metalIngotID, 'F', ModularForceFieldSystem.itemFocusMatix));
		// Security Center
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.blockSecurityStation), "FMF", "MCM", "FMF", 'C', ModularForceFieldSystem.itemCardEmpty, 'M', metalIngotID, 'F', ModularForceFieldSystem.itemFocusMatix));

		// -- Cards --
		// Blank
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemCardEmpty), "PPP", "PMP", "PPP", 'P', Item.paper, 'M', metalIngotID));
		// Link
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemCardLink), "BWB", 'B', ModularForceFieldSystem.itemCardEmpty, 'W', wireID));
		// Frequency
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemCardFrequency), "WBW", 'B', ModularForceFieldSystem.itemCardEmpty, 'W', wireID));
		// ID
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemCardID), "RBR", 'B', ModularForceFieldSystem.itemCardEmpty, 'R', Item.redstone));
		// Temporary ID
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemCardTemporary), "RBR", 'B', ModularForceFieldSystem.itemCardID, 'R', Item.redstone));

		// -- Modes --
		// Temporary ID
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModeSphere), " F ", "FFF", " F ", 'F', ModularForceFieldSystem.itemFocusMatix));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModeCube), "FFF", "FFF", "FFF", 'F', ModularForceFieldSystem.itemFocusMatix));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModeTube), "FFF", "   ", "FFF", 'F', ModularForceFieldSystem.itemFocusMatix));

		// -- Modules --
		// -- -- General -- --
		// Speed
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMSuDu), "F", "R", "F", 'F', ModularForceFieldSystem.itemFocusMatix, 'R', Item.redstone));
		// Range
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMJuLi), "FRF", 'F', ModularForceFieldSystem.itemFocusMatix, 'R', Item.redstone));
		// Capacity
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMRongLiang, 2), "FCF", 'F', ModularForceFieldSystem.itemFocusMatix, 'C', ModularForceFieldSystem.itemFortronCell));
		// Shock
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMDian), "FWF", 'F', ModularForceFieldSystem.itemFocusMatix, 'W', wireID));
		// Sponge
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleSponge), "BBB", "BFB", "BBB", 'F', ModularForceFieldSystem.itemFocusMatix, 'B', Item.bucketWater));
		// Disintegration
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleDisintegration), " W ", "FBF", " W ", 'F', ModularForceFieldSystem.itemFocusMatix, 'W', wireID, 'B', batteryID));
		// Manipulator
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleManipulator), "F", " ", "F", 'F', ModularForceFieldSystem.itemFocusMatix));
		// Jammer
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleJammer), "F", "W", "F", 'F', ModularForceFieldSystem.itemFocusMatix, 'W', wireID));
		// Camouflage
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itWeiZhuang), "WFW", "FWF", "WFW", 'F', ModularForceFieldSystem.itemFocusMatix, 'W', new ItemStack(Block.cloth, 1, -1)));
		// Fusion
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleFusion), "FJF", 'F', ModularForceFieldSystem.itemFocusMatix, 'J', ModularForceFieldSystem.itemModuleJammer));
		// Scale
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMDaXiao), "FRF", 'F', ModularForceFieldSystem.itemFocusMatix, 'R', ModularForceFieldSystem.itMJuLi));
		// Translate
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleTranslation), "FSF", 'F', ModularForceFieldSystem.itemFocusMatix, 'S', ModularForceFieldSystem.itMSuDu));
		// Rotate
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMZhuan), "F  ", " F ", "  F", 'F', ModularForceFieldSystem.itemFocusMatix));
		// Glow
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMGuang, 4), "GGG", "GFG", "GGG", 'F', ModularForceFieldSystem.itemFocusMatix, 'G', Block.glowStone));
		// Stabilizer
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMZhenDing), "FFF", "PSA", "FFF", 'F', ModularForceFieldSystem.itemFocusMatix, 'P', Item.pickaxeDiamond, 'S', Item.shovelDiamond, 'A', Item.axeDiamond));
		// -- -- Defense Station -- --
		// Anti-Hostile
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleAntiHostile), " R ", "GFB", " S ", 'F', ModularForceFieldSystem.itemFocusMatix, 'G', Item.gunpowder, 'R', Item.rottenFlesh, 'B', Item.bone, 'S', Item.ghastTear));
		// Anti-Friendly
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleAntiFriendly), " R ", "GFB", " S ", 'F', ModularForceFieldSystem.itemFocusMatix, 'G', Item.porkCooked, 'R', new ItemStack(Block.cloth, 1, -1), 'B', Item.leather, 'S', Item.slimeBall));
		// Anti-Personnel
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleAntiPersonnel), "BFG", 'F', ModularForceFieldSystem.itemFocusMatix, 'B', ModularForceFieldSystem.itemModuleAntiHostile, 'G', ModularForceFieldSystem.itemModuleAntiFriendly));
		// Confiscate
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleConfiscate), "PEP", "EFE", "PEP", 'F', ModularForceFieldSystem.itemFocusMatix, 'E', Item.eyeOfEnder, 'P', Item.enderPearl));
		// Warn
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itemModuleWarn), "NFN", 'F', ModularForceFieldSystem.itemFocusMatix, 'N', Block.music));
		// Block Access
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMDBA), " C ", "BFB", " C ", 'F', ModularForceFieldSystem.itemFocusMatix, 'B', Block.blockSteel, 'C', Block.chest));
		// Block Place
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModularForceFieldSystem.itMDBPA), " G ", "GFG", " G ", 'F', ModularForceFieldSystem.itMDBA, 'G', Block.blockGold));
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
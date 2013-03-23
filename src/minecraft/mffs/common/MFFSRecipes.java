package mffs.common;

import ic2.api.Ic2Recipes;
import ic2.api.Items;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thermalexpansion.api.crafting.CraftingManagers;
import cpw.mods.fml.common.registry.GameRegistry;

public class MFFSRecipes
{
	public static final HashMap<String, ItemStack> RECIPE_CHARACTERS = new HashMap<String, ItemStack>();

	public static void init()
	{
		// MFFSRecipes.addRecipe("uuuuiuuuu", 1, 0, null, ZhuYao.itemPowerCrystal);
		MFFSRecipes.addRecipe("vvvvvvvvv", 1, 0, null, ZhuYao.itemModuleTranslation);
		MFFSRecipes.addRecipe("vvv   vvv", 1, 0, null, ZhuYao.itemModuleScale);
		MFFSRecipes.addRecipe("  A EA  A", 1, 0, null, ZhuYao.itemMultiTool);

		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ZhuYao.itemCardEmpty), new Object[] { new ItemStack(ZhuYao.itemCardPowerLink) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ZhuYao.itemCardEmpty), new Object[] { new ItemStack(ZhuYao.itemCardID) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ZhuYao.itemCardEmpty), new Object[] { new ItemStack(ZhuYao.itemCardAccess) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ZhuYao.itemCardEmpty), new Object[] { new ItemStack(ZhuYao.itemCardDataLink) });

		GameRegistry.addSmelting(ZhuYao.blockFortronite.blockID, new ItemStack(ZhuYao.itemForcillium, 4), 0.5F);

		if (MFFSConfiguration.MODULE_THERMAL_EXPANSION)
		{
			CraftingManagers.pulverizerManager.addRecipe(100, new ItemStack(ZhuYao.blockFortronite, 1), new ItemStack(ZhuYao.itemForcillium, 8), true);
		}

		if (MFFSConfiguration.MODULE_IC2)
		{
			Ic2Recipes.addMaceratorRecipe(new ItemStack(ZhuYao.blockFortronite, 1), new ItemStack(ZhuYao.itemForcillium, 8));
			Ic2Recipes.addMatterAmplifier(new ItemStack(ZhuYao.itemForcillium, 1), 5000);

			if (MFFSConfiguration.uumatterEnabled)
			{
				Ic2Recipes.addCraftingRecipe(new ItemStack(ZhuYao.itemForcillium, 8), new Object[] { " RR", "R  ", " R ", 'R', Items.getItem("matter") });
			}

			MFFSRecipes.addRecipe("AAAAxAADA", 1, 1, null, ZhuYao.itemFortronCell);
			MFFSRecipes.addRecipe(" E EBE E ", 4, 1, null, ZhuYao.itemUpgradeSpeed);
			MFFSRecipes.addRecipe(" E ExE E ", 1, 1, null, ZhuYao.itemUpgradeCapacity);
			MFFSRecipes.addRecipe("HHHEIEEDE", 1, 1, null, ZhuYao.itemUpgradeRange);
			MFFSRecipes.addRecipe("AlAlilAlA", 64, 1, null, ZhuYao.itemFocusMatix);
			MFFSRecipes.addRecipe("ooooCoooo", 1, 1, null, ZhuYao.itemCardEmpty);
		}

		if (MFFSConfiguration.MODULE_UE)
		{
			MFFSRecipes.addRecipe("AAAAxAAHA", 1, 2, null, ZhuYao.itemFortronCell);
			MFFSRecipes.addRecipe("C C G C C", 9, 2, null, ZhuYao.itemUpgradeSpeed);
			MFFSRecipes.addRecipe(" C CxC C ", 1, 2, null, ZhuYao.itemUpgradeCapacity);
			MFFSRecipes.addRecipe("NNNCICCEC", 1, 2, null, ZhuYao.itemUpgradeRange);
			MFFSRecipes.addRecipe("BlBlilBlB", 64, 2, null, ZhuYao.itemFocusMatix);
			MFFSRecipes.addRecipe("ooooEoooo", 1, 2, null, ZhuYao.itemCardEmpty);
		}
	}

	public static boolean addRecipe(String Recipe, int count, int forMod, Block block, Item item)
	{
		if (((forMod > 2) && (forMod < 0)) || (count < 0) || ((block == null) && (item == null)) || ((block != null) && (item != null)) || (Recipe.length() != 9))
		{
			ZhuYao.LOGGER.severe("Recipes generating failed for :" + block + "/" + item);
			return false;
		}

		ItemStack itemstack = null;

		if ((block != null) && (item == null))
		{
			itemstack = new ItemStack(block, count);
		}
		if ((block == null) && (item != null))
		{
			itemstack = new ItemStack(item, count);
		}

		String[] recipeSplit = { Recipe.substring(0, 3), Recipe.substring(3, 6), Recipe.substring(6, 9) };

		switch (forMod)
		{
			case 0:
				GameRegistry.addRecipe(itemstack, new Object[] { recipeSplit, 'a', Item.enderPearl, 'b', Item.pickaxeSteel, 'c', Item.bucketEmpty, 'd', Item.bucketLava, 'e', Item.bucketWater, 'f', Item.bone, 'g', Item.blazeRod, 'h', Item.rottenFlesh, 'i', Item.diamond, 'j', Item.spiderEye, 'k', Block.obsidian, 'l', Block.glass, 'm', Item.redstone, 'n', Block.lever, 'o', Item.paper, 'u', ZhuYao.itemForcillium, 'v', ZhuYao.itemFocusMatix, 'w', ZhuYao.itemModuleCube, 'x', ZhuYao.itemFortronCell, 'y', ZhuYao.itemFocusMatix, 'z', ZhuYao.itemCardID });
				return true;
			case 1:
				if (MFFSConfiguration.MODULE_IC2)
				{
					GameRegistry.addRecipe(itemstack, new Object[] { recipeSplit, 'a', Item.enderPearl, 'b', Item.pickaxeSteel, 'c', Item.bucketEmpty, 'd', Item.bucketLava, 'e', Item.bucketWater, 'f', Item.bone, 'g', Item.blazeRod, 'h', Item.rottenFlesh, 'i', Item.diamond, 'j', Item.spiderEye, 'k', Block.obsidian, 'l', Block.glass, 'm', Item.redstone, 'n', Block.lever, 'o', Item.paper, 'u', ZhuYao.itemForcillium, 'v', ZhuYao.itemFocusMatix, 'w', ZhuYao.itemModuleCube, 'x', ZhuYao.itemFortronCell, 'y', ZhuYao.itemFocusMatix, 'z', ZhuYao.itemCardID, 'A', Items.getItem("refinedIronIngot"), 'B', Items.getItem("overclockerUpgrade"), 'C', Items.getItem("electronicCircuit"), 'D', Items.getItem("advancedCircuit"), 'E', Items.getItem("carbonPlate"), 'F', Items.getItem("advancedMachine"), 'G', Items.getItem("extractor"), 'H', Items.getItem("copperCableItem"), 'I', Items.getItem("insulatedCopperCableItem"), 'J', Items.getItem("frequencyTransmitter"), 'K', Items.getItem("advancedAlloy"), 'M', Items.getItem("glassFiberCableItem"), 'N', Items.getItem("lvTransformer"), 'O', Items.getItem("mvTransformer"), 'P', Items.getItem("hvTransformer"), 'Q', Items.getItem("teslaCoil"), 'R', Items.getItem("matter"), 'S', Items.getItem("wrench") });
					return true;
				}
				break;
			case 2:
				if (MFFSConfiguration.MODULE_UE)
				{
					GameRegistry.addRecipe(new ShapedOreRecipe(itemstack, new Object[] { recipeSplit, 'a', Item.enderPearl, 'b', Item.pickaxeSteel, 'c', Item.bucketEmpty, 'd', Item.bucketLava, 'e', Item.bucketWater, 'f', Item.bone, 'g', Item.blazeRod, 'h', Item.rottenFlesh, 'i', Item.diamond, 'j', Item.spiderEye, 'k', Block.obsidian, 'l', Block.glass, 'm', Item.redstone, 'n', Block.lever, 'o', Item.paper, 'u', ZhuYao.itemForcillium, 'v', ZhuYao.itemFocusMatix, 'w', ZhuYao.itemModuleCube, 'x', ZhuYao.itemFortronCell, 'y', ZhuYao.itemFocusMatix, 'z', ZhuYao.itemCardID, 'A', "ingotSteel", 'B', "plateBronze", 'C', "plateSteel", 'D', "plateTin", 'E', "basicCircuit", 'F', "advancedCircuit", 'G', "eliteCircuit", 'H', "motor", 'I', "copperWire", 'J', "batteryBox", 'K', "coalGenerator", 'M', "electricFurnace", 'N', "ingotCopper", 'O', "wrench" }));
					return true;
				}

				break;
		}

		return false;
	}
}
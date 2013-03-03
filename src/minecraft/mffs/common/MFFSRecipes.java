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
		MFFSRecipes.addRecipe("uuuuiuuuu", 1, 0, null, ModularForceFieldSystem.itemPowerCrystal);
		MFFSRecipes.addRecipe("vvvvvvvvv", 1, 0, null, ModularForceFieldSystem.itemModuleTranslation);
		MFFSRecipes.addRecipe("vvv   vvv", 1, 0, null, ModularForceFieldSystem.itemModuleScale);
		MFFSRecipes.addRecipe("  A EA  A", 1, 0, null, ModularForceFieldSystem.itemMultiTool);

		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.itemCardEmpty), new Object[] { new ItemStack(ModularForceFieldSystem.itemCardPowerLink) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.itemCardEmpty), new Object[] { new ItemStack(ModularForceFieldSystem.itemCardID) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.itemCardEmpty), new Object[] { new ItemStack(ModularForceFieldSystem.itemCardSecurityLink) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.itemCardEmpty), new Object[] { new ItemStack(ModularForceFieldSystem.itemCardAccess) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.itemCardEmpty), new Object[] { new ItemStack(ModularForceFieldSystem.itemCardDataLink) });

		GameRegistry.addSmelting(ModularForceFieldSystem.blockFortronite.blockID, new ItemStack(ModularForceFieldSystem.itemForcillium, 4), 0.5F);

		if (MFFSConfiguration.MODULE_THERMAL_EXPANSION)
		{
			CraftingManagers.pulverizerManager.addRecipe(100, new ItemStack(ModularForceFieldSystem.blockFortronite, 1), new ItemStack(ModularForceFieldSystem.itemForcillium, 8), true);
		}

		if (MFFSConfiguration.MODULE_IC2)
		{
			Ic2Recipes.addMaceratorRecipe(new ItemStack(ModularForceFieldSystem.blockFortronite, 1), new ItemStack(ModularForceFieldSystem.itemForcillium, 8));
			Ic2Recipes.addMatterAmplifier(new ItemStack(ModularForceFieldSystem.itemForcillium, 1), 5000);

			if (MFFSConfiguration.uumatterEnabled)
			{
				Ic2Recipes.addCraftingRecipe(new ItemStack(ModularForceFieldSystem.itemForcillium, 8), new Object[] { " RR", "R  ", " R ", 'R', Items.getItem("matter") });
			}

			MFFSRecipes.addRecipe("AAAAxAADA", 1, 1, null, ModularForceFieldSystem.itemForcilliumCell);
			MFFSRecipes.addRecipe(" E EBE E ", 4, 1, null, ModularForceFieldSystem.itemUpgradeBoost);
			MFFSRecipes.addRecipe(" E ExE E ", 1, 1, null, ModularForceFieldSystem.itemUpgradeCapacity);
			MFFSRecipes.addRecipe("HHHEIEEDE", 1, 1, null, ModularForceFieldSystem.itemUpgradeRange);
			MFFSRecipes.addRecipe("AlAlilAlA", 64, 1, null, ModularForceFieldSystem.itemFocusMatix);
			MFFSRecipes.addRecipe("ooooCoooo", 1, 1, null, ModularForceFieldSystem.itemCardEmpty);
		}

		if (MFFSConfiguration.MODULE_UE)
		{
			MFFSRecipes.addRecipe("AAAAxAAHA", 1, 2, null, ModularForceFieldSystem.itemForcilliumCell);
			MFFSRecipes.addRecipe("C C G C C", 9, 2, null, ModularForceFieldSystem.itemUpgradeBoost);
			MFFSRecipes.addRecipe(" C CxC C ", 1, 2, null, ModularForceFieldSystem.itemUpgradeCapacity);
			MFFSRecipes.addRecipe("NNNCICCEC", 1, 2, null, ModularForceFieldSystem.itemUpgradeRange);
			MFFSRecipes.addRecipe("BlBlilBlB", 64, 2, null, ModularForceFieldSystem.itemFocusMatix);
			MFFSRecipes.addRecipe("ooooEoooo", 1, 2, null, ModularForceFieldSystem.itemCardEmpty);
		}
	}

	public static boolean addRecipe(String Recipe, int count, int forMod, Block block, Item item)
	{
		if (((forMod > 2) && (forMod < 0)) || (count < 0) || ((block == null) && (item == null)) || ((block != null) && (item != null)) || (Recipe.length() != 9))
		{
			ModularForceFieldSystem.LOGGER.severe("Recipes generating failed for :" + block + "/" + item);
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
				GameRegistry.addRecipe(itemstack, new Object[] { recipeSplit, 'a', Item.enderPearl, 'b', Item.pickaxeSteel, 'c', Item.bucketEmpty, 'd', Item.bucketLava, 'e', Item.bucketWater, 'f', Item.bone, 'g', Item.blazeRod, 'h', Item.rottenFlesh, 'i', Item.diamond, 'j', Item.spiderEye, 'k', Block.obsidian, 'l', Block.glass, 'm', Item.redstone, 'n', Block.lever, 'o', Item.paper, 'u', ModularForceFieldSystem.itemForcillium, 'v', ModularForceFieldSystem.itemFocusMatix, 'w', ModularForceFieldSystem.itemModuleCube, 'x', new ItemStack(ModularForceFieldSystem.itemPowerCrystal, 1, -1), 'y', ModularForceFieldSystem.itemFocusMatix, 'z', ModularForceFieldSystem.itemCardID });
				return true;
			case 1:
				if (MFFSConfiguration.MODULE_IC2)
				{
					GameRegistry.addRecipe(itemstack, new Object[] { recipeSplit, 'a', Item.enderPearl, 'b', Item.pickaxeSteel, 'c', Item.bucketEmpty, 'd', Item.bucketLava, 'e', Item.bucketWater, 'f', Item.bone, 'g', Item.blazeRod, 'h', Item.rottenFlesh, 'i', Item.diamond, 'j', Item.spiderEye, 'k', Block.obsidian, 'l', Block.glass, 'm', Item.redstone, 'n', Block.lever, 'o', Item.paper, 'u', ModularForceFieldSystem.itemForcillium, 'v', ModularForceFieldSystem.itemFocusMatix, 'w', ModularForceFieldSystem.itemModuleCube, 'x', new ItemStack(ModularForceFieldSystem.itemPowerCrystal, 1, -1), 'y', ModularForceFieldSystem.itemFocusMatix, 'z', ModularForceFieldSystem.itemCardID, 'A', Items.getItem("refinedIronIngot"), 'B', Items.getItem("overclockerUpgrade"), 'C', Items.getItem("electronicCircuit"), 'D', Items.getItem("advancedCircuit"), 'E', Items.getItem("carbonPlate"), 'F', Items.getItem("advancedMachine"), 'G', Items.getItem("extractor"), 'H', Items.getItem("copperCableItem"), 'I', Items.getItem("insulatedCopperCableItem"), 'J', Items.getItem("frequencyTransmitter"), 'K', Items.getItem("advancedAlloy"), 'M', Items.getItem("glassFiberCableItem"), 'N', Items.getItem("lvTransformer"), 'O', Items.getItem("mvTransformer"), 'P', Items.getItem("hvTransformer"), 'Q', Items.getItem("teslaCoil"), 'R', Items.getItem("matter"), 'S', Items.getItem("wrench") });
					return true;
				}
				break;
			case 2:
				if (MFFSConfiguration.MODULE_UE)
				{
					GameRegistry.addRecipe(new ShapedOreRecipe(itemstack, new Object[] { recipeSplit, 'a', Item.enderPearl, 'b', Item.pickaxeSteel, 'c', Item.bucketEmpty, 'd', Item.bucketLava, 'e', Item.bucketWater, 'f', Item.bone, 'g', Item.blazeRod, 'h', Item.rottenFlesh, 'i', Item.diamond, 'j', Item.spiderEye, 'k', Block.obsidian, 'l', Block.glass, 'm', Item.redstone, 'n', Block.lever, 'o', Item.paper, 'u', ModularForceFieldSystem.itemForcillium, 'v', ModularForceFieldSystem.itemFocusMatix, 'w', ModularForceFieldSystem.itemModuleCube, 'x', new ItemStack(ModularForceFieldSystem.itemPowerCrystal, 1, -1), 'y', ModularForceFieldSystem.itemFocusMatix, 'z', ModularForceFieldSystem.itemCardID, 'A', "ingotSteel", 'B', "plateBronze", 'C', "plateSteel", 'D', "plateTin", 'E', "basicCircuit", 'F', "advancedCircuit", 'G', "eliteCircuit", 'H', "motor", 'I', "copperWire", 'J', "batteryBox", 'K', "coalGenerator", 'M', "electricFurnace", 'N', "ingotCopper", 'O', "wrench" }));
					return true;
				}

				break;
		}

		return false;
	}
}
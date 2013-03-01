package mffs.common;

import ic2.api.Ic2Recipes;
import ic2.api.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import thermalexpansion.api.crafting.CraftingManagers;
import cpw.mods.fml.common.registry.GameRegistry;

public class MFFSRecipes
{

	public static void init()
	{
		OreDictionary.registerOre("ForciciumItem", ModularForceFieldSystem.itemForcillium);
		OreDictionary.registerOre("MonazitOre", ModularForceFieldSystem.blockFortronite);

		MFFSRecipes.addRecipe("uuuuiuuuu", 1, 0, null, ModularForceFieldSystem.itemPowerCrystal);
		MFFSRecipes.addRecipe("vvvvvvvvv", 1, 0, null, ModularForceFieldSystem.itemModuleStrength);
		MFFSRecipes.addRecipe("vvv   vvv", 1, 0, null, ModularForceFieldSystem.itemModuleDistance);

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
				Ic2Recipes.addCraftingRecipe(new ItemStack(ModularForceFieldSystem.itemForcillium, 8), new Object[] { " RR", "R  ", " R ", Character.valueOf('R'), Items.getItem("matter") });
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
			System.out.println("[ModularForceFieldSystem] Recipes generating Fail for :" + block + "/" + item);
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
				GameRegistry.addRecipe(itemstack, new Object[] { recipeSplit, Character.valueOf('a'), Item.enderPearl, Character.valueOf('b'), Item.pickaxeSteel, Character.valueOf('c'), Item.bucketEmpty, Character.valueOf('d'), Item.bucketLava, Character.valueOf('e'), Item.bucketWater, Character.valueOf('f'), Item.bone, Character.valueOf('g'), Item.blazeRod, Character.valueOf('h'), Item.rottenFlesh, Character.valueOf('i'), Item.diamond, Character.valueOf('j'), Item.spiderEye, Character.valueOf('k'), Block.obsidian, Character.valueOf('l'), Block.glass, Character.valueOf('m'), Item.redstone, Character.valueOf('n'), Block.lever, Character.valueOf('o'), Item.paper, Character.valueOf('u'), ModularForceFieldSystem.itemForcillium, Character.valueOf('v'), ModularForceFieldSystem.itemFocusMatix, Character.valueOf('w'), ModularForceFieldSystem.itemModuleCube, Character.valueOf('x'), new ItemStack(ModularForceFieldSystem.itemPowerCrystal, 1, -1), Character.valueOf('y'), ModularForceFieldSystem.itemFocusMatix, Character.valueOf('z'), ModularForceFieldSystem.itemCardID });

				return true;
			case 1:
				if (MFFSConfiguration.MODULE_IC2)
				{
					GameRegistry.addRecipe(itemstack, new Object[] { recipeSplit, Character.valueOf('a'), Item.enderPearl, Character.valueOf('b'), Item.pickaxeSteel, Character.valueOf('c'), Item.bucketEmpty, Character.valueOf('d'), Item.bucketLava, Character.valueOf('e'), Item.bucketWater, Character.valueOf('f'), Item.bone, Character.valueOf('g'), Item.blazeRod, Character.valueOf('h'), Item.rottenFlesh, Character.valueOf('i'), Item.diamond, Character.valueOf('j'), Item.spiderEye, Character.valueOf('k'), Block.obsidian, Character.valueOf('l'), Block.glass, Character.valueOf('m'), Item.redstone, Character.valueOf('n'), Block.lever, Character.valueOf('o'), Item.paper, Character.valueOf('u'), ModularForceFieldSystem.itemForcillium, Character.valueOf('v'), ModularForceFieldSystem.itemFocusMatix, Character.valueOf('w'), ModularForceFieldSystem.itemModuleCube, Character.valueOf('x'), new ItemStack(ModularForceFieldSystem.itemPowerCrystal, 1, -1), Character.valueOf('y'), ModularForceFieldSystem.itemFocusMatix, Character.valueOf('z'), ModularForceFieldSystem.itemCardID, Character.valueOf('A'), Items.getItem("refinedIronIngot"), Character.valueOf('B'), Items.getItem("overclockerUpgrade"), Character.valueOf('C'), Items.getItem("electronicCircuit"), Character.valueOf('D'), Items.getItem("advancedCircuit"), Character.valueOf('E'), Items.getItem("carbonPlate"), Character.valueOf('F'), Items.getItem("advancedMachine"), Character.valueOf('G'), Items.getItem("extractor"), Character.valueOf('H'), Items.getItem("copperCableItem"), Character.valueOf('I'), Items.getItem("insulatedCopperCableItem"), Character.valueOf('J'), Items.getItem("frequencyTransmitter"), Character.valueOf('K'), Items.getItem("advancedAlloy"), Character.valueOf('M'), Items.getItem("glassFiberCableItem"), Character.valueOf('N'), Items.getItem("lvTransformer"), Character.valueOf('O'), Items.getItem("mvTransformer"), Character.valueOf('P'), Items.getItem("hvTransformer"), Character.valueOf('Q'), Items.getItem("teslaCoil"), Character.valueOf('R'), Items.getItem("matter"), Character.valueOf('S'), Items.getItem("wrench") });

					return true;
				}
				break;
			case 2:
				if (MFFSConfiguration.MODULE_UE)
				{
					GameRegistry.addRecipe(new ShapedOreRecipe(itemstack, new Object[] { recipeSplit, Character.valueOf('a'), Item.enderPearl, Character.valueOf('b'), Item.pickaxeSteel, Character.valueOf('c'), Item.bucketEmpty, Character.valueOf('d'), Item.bucketLava, Character.valueOf('e'), Item.bucketWater, Character.valueOf('f'), Item.bone, Character.valueOf('g'), Item.blazeRod, Character.valueOf('h'), Item.rottenFlesh, Character.valueOf('i'), Item.diamond, Character.valueOf('j'), Item.spiderEye, Character.valueOf('k'), Block.obsidian, Character.valueOf('l'), Block.glass, Character.valueOf('m'), Item.redstone, Character.valueOf('n'), Block.lever, Character.valueOf('o'), Item.paper, Character.valueOf('u'), ModularForceFieldSystem.itemForcillium, Character.valueOf('v'), ModularForceFieldSystem.itemFocusMatix, Character.valueOf('w'), ModularForceFieldSystem.itemModuleCube, Character.valueOf('x'), new ItemStack(ModularForceFieldSystem.itemPowerCrystal, 1, -1), Character.valueOf('y'), ModularForceFieldSystem.itemFocusMatix, Character.valueOf('z'), ModularForceFieldSystem.itemCardID, Character.valueOf('A'), "ingotSteel", Character.valueOf('B'), "plateBronze", Character.valueOf('C'), "plateSteel", Character.valueOf('D'), "plateTin", Character.valueOf('E'), "basicCircuit", Character.valueOf('F'), "advancedCircuit", Character.valueOf('G'), "eliteCircuit", Character.valueOf('H'), "motor", Character.valueOf('I'), "copperWire", Character.valueOf('J'), "batteryBox", Character.valueOf('K'), "coalGenerator", Character.valueOf('M'), "electricFurnace", Character.valueOf('N'), "ingotCopper", Character.valueOf('O'), "wrench" }));

					return true;
				}

				break;
		}

		return false;
	}
}
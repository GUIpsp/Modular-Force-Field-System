package mffs.common;

import ic2.api.Ic2Recipes;
import ic2.api.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import thermalexpansion.api.crafting.CraftingManagers;
import cpw.mods.fml.common.registry.GameRegistry;

public class MFFSRecipes
{
	public static void init()
	{
		OreDictionary.registerOre("ForciciumItem", ModularForceFieldSystem.itemForcicium);
		OreDictionary.registerOre("MonazitOre", ModularForceFieldSystem.blockMonaziteOre);

		RecipesFactory.addRecipe("uuuuiuuuu", 1, 0, null, ModularForceFieldSystem.itemPowerCrystal);
		RecipesFactory.addRecipe("vvvvvvvvv", 1, 0, null, ModularForceFieldSystem.MFFSProjectorFFStrenght);
		RecipesFactory.addRecipe("vvv   vvv", 1, 0, null, ModularForceFieldSystem.MFFSProjectorFFDistance);

		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty), new Object[] { new ItemStack(ModularForceFieldSystem.MFFSitemfc) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty), new Object[] { new ItemStack(ModularForceFieldSystem.MFFSItemIDCard) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty), new Object[] { new ItemStack(ModularForceFieldSystem.MFFSItemSecLinkCard) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty), new Object[] { new ItemStack(ModularForceFieldSystem.MFFSAccessCard) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty), new Object[] { new ItemStack(ModularForceFieldSystem.MFFSitemDataLinkCard) });

		GameRegistry.addSmelting(ModularForceFieldSystem.blockMonaziteOre.blockID, new ItemStack(ModularForceFieldSystem.itemForcicium, 4), 0.5F);

		if (MFFSProperties.MODULE_THERMAL_EXPANSION)
		{
			CraftingManagers.pulverizerManager.addRecipe(100, new ItemStack(ModularForceFieldSystem.blockMonaziteOre, 1), new ItemStack(ModularForceFieldSystem.itemForcicium, 8), true);
		}

		if (MFFSProperties.MODULE_IC2)
		{
			Ic2Recipes.addMaceratorRecipe(new ItemStack(ModularForceFieldSystem.blockMonaziteOre, 1), new ItemStack(ModularForceFieldSystem.itemForcicium, 8));
			Ic2Recipes.addMatterAmplifier(new ItemStack(ModularForceFieldSystem.itemForcicium, 1), 5000);

			if (MFFSProperties.uumatterForcicium)
			{
				Ic2Recipes.addCraftingRecipe(new ItemStack(ModularForceFieldSystem.itemForcicium, 8), new Object[] { " RR", "R  ", " R ", Character.valueOf('R'), Items.getItem("matter") });
			}

			RecipesFactory.addRecipe("AAAAxAADA", 1, 1, null, ModularForceFieldSystem.itemForcicumCell);
			RecipesFactory.addRecipe(" E EBE E ", 4, 1, null, ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
			RecipesFactory.addRecipe(" E ExE E ", 1, 1, null, ModularForceFieldSystem.MFFSitemupgradecapcap);
			RecipesFactory.addRecipe("HHHEIEEDE", 1, 1, null, ModularForceFieldSystem.MFFSitemupgradecaprange);
			RecipesFactory.addRecipe("AlAlilAlA", 64, 1, null, ModularForceFieldSystem.itemFocusMatix);
			RecipesFactory.addRecipe("ooooCoooo", 1, 1, null, ModularForceFieldSystem.MFFSitemcardempty);
			RecipesFactory.addRecipe("mSnExEEDE", 1, 1, null, ModularForceFieldSystem.itemWrench);
		}

		if (MFFSProperties.MODULE_UE)
		{
			RecipesFactory.addRecipe("AAAAxAAHA", 1, 2, null, ModularForceFieldSystem.itemForcicumCell);
			RecipesFactory.addRecipe("C C G C C", 9, 2, null, ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
			RecipesFactory.addRecipe(" C CxC C ", 1, 2, null, ModularForceFieldSystem.MFFSitemupgradecapcap);
			RecipesFactory.addRecipe("NNNCICCEC", 1, 2, null, ModularForceFieldSystem.MFFSitemupgradecaprange);
			RecipesFactory.addRecipe("BlBlilBlB", 64, 2, null, ModularForceFieldSystem.itemFocusMatix);
			RecipesFactory.addRecipe("ooooEoooo", 1, 2, null, ModularForceFieldSystem.MFFSitemcardempty);
			RecipesFactory.addRecipe("mOnDxDDED", 1, 2, null, ModularForceFieldSystem.itemWrench);
		}
	}
}
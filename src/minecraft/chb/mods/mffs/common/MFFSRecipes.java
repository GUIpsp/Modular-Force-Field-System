/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    
    Matchlighter
    Thunderdark 

 */

package chb.mods.mffs.common;

import thermalexpansion.api.crafting.CraftingManagers;
import ic2.api.Ic2Recipes;
import ic2.api.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;

public class MFFSRecipes {

	

	public static void init()
	{
	
		OreDictionary.registerOre("ForciciumItem", ModularForceFieldSystem.MFFSitemForcicium);
		OreDictionary.registerOre("MonazitOre", ModularForceFieldSystem.MFFSMonazitOre);
		
		RecipesFactory.addRecipe("uuuuiuuuu", 1, 0, null, ModularForceFieldSystem.MFFSitemForcePowerCrystal);
		RecipesFactory.addRecipe("vvvvvvvvv", 1, 0, null, ModularForceFieldSystem.MFFSProjectorFFStrenght);
		RecipesFactory.addRecipe("vvv   vvv", 1, 0, null, ModularForceFieldSystem.MFFSProjectorFFDistance);
		
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),new Object[] { new ItemStack(ModularForceFieldSystem.MFFSitemfc) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),new Object[] { new ItemStack(ModularForceFieldSystem.MFFSItemIDCard) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),new Object[] { new ItemStack(ModularForceFieldSystem.MFFSItemSecLinkCard) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),new Object[] { new ItemStack(ModularForceFieldSystem.MFFSAccessCard) });
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemcardempty),new Object[] { new ItemStack(ModularForceFieldSystem.MFFSitemDataLinkCard) });
		
		GameRegistry.addSmelting(ModularForceFieldSystem.MFFSMonazitOre.blockID, new ItemStack(ModularForceFieldSystem.MFFSitemForcicium, 4), 0.5F);
	
		if(ModularForceFieldSystem.ThermalExpansionfound){
		
		CraftingManagers.pulverizerManager.addRecipe(100, new ItemStack(ModularForceFieldSystem.MFFSMonazitOre, 1), new ItemStack(ModularForceFieldSystem.MFFSitemForcicium, 8));
	
		}
	if(ModularForceFieldSystem.ic2found){
		
		Ic2Recipes.addMaceratorRecipe(new ItemStack(ModularForceFieldSystem.MFFSMonazitOre, 1), new ItemStack(ModularForceFieldSystem.MFFSitemForcicium, 8));
		Ic2Recipes.addMatterAmplifier(new ItemStack(ModularForceFieldSystem.MFFSitemForcicium, 1), 5000);
	
	if(ModularForceFieldSystem.uumatterForcicium)
		Ic2Recipes.addCraftingRecipe(new ItemStack(ModularForceFieldSystem.MFFSitemForcicium, 8),new Object[] {" RR","R  "," R ", Character.valueOf('R'), Items.getItem("matter")});

	
		RecipesFactory.addRecipe("AAAAxAADA", 1, 1, null, ModularForceFieldSystem.MFFSitemForcicumCell);
		RecipesFactory.addRecipe(" E EBE E ", 4, 1, null, ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
		RecipesFactory.addRecipe(" E ExE E ", 1, 1, null, ModularForceFieldSystem.MFFSitemupgradecapcap);
		RecipesFactory.addRecipe("HHHEIEEDE", 1, 1, null, ModularForceFieldSystem.MFFSitemupgradecaprange);
		RecipesFactory.addRecipe("AlAlilAlA", 64, 1, null, ModularForceFieldSystem.MFFSitemFocusmatix);
		RecipesFactory.addRecipe("ooooCoooo", 1, 1, null, ModularForceFieldSystem.MFFSitemcardempty);
		RecipesFactory.addRecipe("mSnExEEDE", 1, 1, null, ModularForceFieldSystem.MFFSitemWrench);
	}

	if(ModularForceFieldSystem.uefound){
		
		RecipesFactory.addRecipe("AAAAxAAHA", 1, 2, null, ModularForceFieldSystem.MFFSitemForcicumCell);
		RecipesFactory.addRecipe("C C G C C", 9, 2, null, ModularForceFieldSystem.MFFSitemupgradeexctractorboost);
		RecipesFactory.addRecipe(" C CxC C ", 1, 2, null, ModularForceFieldSystem.MFFSitemupgradecapcap);
		RecipesFactory.addRecipe("NNNCICCEC", 1, 2, null, ModularForceFieldSystem.MFFSitemupgradecaprange);
		RecipesFactory.addRecipe("BlBlilBlB", 64, 2, null, ModularForceFieldSystem.MFFSitemFocusmatix);
		RecipesFactory.addRecipe("ooooEoooo", 1, 2, null, ModularForceFieldSystem.MFFSitemcardempty);
		RecipesFactory.addRecipe("mOnDxDDED", 1, 2, null, ModularForceFieldSystem.MFFSitemWrench);
	}
	
	
		
	}
	
}

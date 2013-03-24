package mffs.common;

import mffs.api.IProjectorMode;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public enum ProjectorTypes
{
	tube("Tube", "AAA B AAA", ZhuYao.itemModuleTube, false),
	cube("Cube", "B B A B B", ZhuYao.itemModuleCube, false),
	sphere("Sphere", " B BAB B ", ZhuYao.itemModuleSphere, false);

	public String displayName;
	public String recipe;
	public Item item;
	public boolean blockDropper;

	private ProjectorTypes(String dispNm, String recipe, Item item, boolean Blockdropper)
	{
		this.displayName = dispNm;
		this.recipe = recipe;
		this.item = item;
		this.blockDropper = Blockdropper;
	}

	public static ProjectorTypes typeFromItem(IProjectorMode item)
	{
		for (ProjectorTypes mach : values())
		{
			if (mach.item == item)
			{
				return mach;
			}
		}
		return null;
	}

	public static void initialize()
	{
		for (ProjectorTypes match : values())
		{
			addRecipeFor(match);
		}
	}

	public static String getdisplayName(ProjectorTypes mach)
	{
		return "MFFS Projector Module  " + mach.displayName;
	}

	public static void addRecipeFor(ProjectorTypes mach)
	{
		String[] recipeSplit = { mach.recipe.substring(0, 3), mach.recipe.substring(3, 6), mach.recipe.substring(6, 9) };

		GameRegistry.addRecipe(new ItemStack(mach.item, 1), new Object[] { recipeSplit, Character.valueOf('C'), ZhuYao.itemModuleCube, Character.valueOf('B'), Block.obsidian, Character.valueOf('A'), ZhuYao.itemFocusMatix });
	}
}
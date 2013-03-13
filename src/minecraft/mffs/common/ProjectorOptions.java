package mffs.common;

import net.minecraft.item.Item;

public enum ProjectorOptions
{

	Zapper("<touch damage>", ZhuYao.itemModuleShock, " K KQK K ", " C CdC C "),
	Subwater("<Sponge>", ZhuYao.itemModuleSponge, " K KcK K ", " C CcC C "),
	Dome("<Field Manipulator>", ZhuYao.itemModuleManipulator, " K KCK K ", " C CEC C "),
	Cutter("<Block Breaker>", ZhuYao.itemModuleDisintegration, " K KbK K ", " C CbC C "),
	FieldJammer("<Force Field Jammer>", ZhuYao.itemModuleJammer, " J JvJ J ", " a ava a "),
	Camouflage("<Camouflage>", ZhuYao.itemModuleCamouflage, " K KRK K ", " C CGC C "),
	FieldFusion("<Field Fusion>", ZhuYao.itemModuleFusion, " K KDK K ", " C CFC C "),
	MoobEx("<NPC Defense>", ZhuYao.itemModuleAntibiotic, "fgfhQhjgj", "fgfhdhjgj"),
	DefenceStation("<Defense Station>", ZhuYao.itemModuleDefenseeStation, " z CQC z ", " z EdE z ");

	String displayName;
	Item item;
	String recipeic;
	String recipeue;

	private ProjectorOptions(String dispNm, Item item, String recipeic, String recipeue)
	{
		this.displayName = dispNm;
		this.item = item;
		this.recipeic = recipeic;
		this.recipeue = recipeue;
	}

	public static void initialize()
	{
		for (ProjectorOptions mach : values())
		{
			if (MFFSConfiguration.MODULE_IC2)
			{
				MFFSRecipes.addRecipe(mach.recipeic, 1, 1, null, mach.item);
			}
			if (MFFSConfiguration.MODULE_UE)
			{
				MFFSRecipes.addRecipe(mach.recipeue, 1, 2, null, mach.item);
			}
		}
	}
}
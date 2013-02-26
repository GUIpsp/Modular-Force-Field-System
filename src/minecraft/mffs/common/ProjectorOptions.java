package mffs.common;

import net.minecraft.item.Item;

public enum ProjectorOptions
{

	Zapper("<touch damage>", ModularForceFieldSystem.itemOptionShock, " K KQK K ", " C CdC C "),
	Subwater("<Sponge>", ModularForceFieldSystem.itemOptionSponge, " K KcK K ", " C CcC C "),
	Dome("<Field Manipulator>", ModularForceFieldSystem.itemOptionFieldManipulator, " K KCK K ", " C CEC C "),
	Cutter("<Block Breaker>", ModularForceFieldSystem.itemOptionCutter, " K KbK K ", " C CbC C "),
	FieldJammer("<Force Field Jammer>", ModularForceFieldSystem.itemOptionJammer, " J JvJ J ", " a ava a "),
	Camouflage("<Camouflage>", ModularForceFieldSystem.itemOptionCamouflage, " K KRK K ", " C CGC C "),
	FieldFusion("<Field Fusion>", ModularForceFieldSystem.itemOptionFieldFusion, " K KDK K ", " C CFC C "),
	MoobEx("<NPC Defense>", ModularForceFieldSystem.itemOptionAntibiotic, "fgfhQhjgj", "fgfhdhjgj"),
	DefenceStation("<Defense Station>", ModularForceFieldSystem.itemOptionDefenseeStation, " z CQC z ", " z EdE z ");

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
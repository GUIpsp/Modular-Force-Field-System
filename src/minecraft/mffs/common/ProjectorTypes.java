package mffs.common;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public enum ProjectorTypes
{
	wall(1, "Wall", "AA AA BB ", ModularForceFieldSystem.itemModuleWall, true),
	deflector(2, "Deflector", "AAAABAAAA", ModularForceFieldSystem.itemModuleDeflector, true),
	tube(3, "Tube", "AAA B AAA", ModularForceFieldSystem.itemModuleTube, false),
	cube(4, "Cube", "B B A B B", ModularForceFieldSystem.itemModuleCube, false),
	sphere(5, "Sphere", " B BAB B ", ModularForceFieldSystem.itemModuleSphere, false),
	containment(6, "Containment", "BBBBABBBB", ModularForceFieldSystem.itemModuleContainment, false),
	AdvCube(7, "Adv.Cube", "AAAACAAAA", ModularForceFieldSystem.itemModuleAdvancedCube, false),
	diagonallywall(8, "diagonal Wall", "A A B A A", ModularForceFieldSystem.itemModuleDiagonalWall, true);

	public String displayName;
	public String recipe;
	public Item item;
	public int ProTyp;
	public boolean Blockdropper;

	private ProjectorTypes(int ProTyp, String dispNm, String recipe, Item item, boolean Blockdropper)
	{
		this.displayName = dispNm;
		this.recipe = recipe;
		this.item = item;
		this.ProTyp = ProTyp;
		this.Blockdropper = Blockdropper;
	}

	public static ProjectorTypes TypfromItem(Item item)
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
		for (ProjectorTypes mach : values())
		{
			generateRecipesFor(mach);
			addNameForObject(mach);
		}
	}

	public static void addNameForObject(ProjectorTypes mach)
	{
		LanguageRegistry.instance().addNameForObject(mach.item, "en_US", " MFFS Projector Module  " + mach.displayName);
	}

	public static String getdisplayName(ProjectorTypes mach)
	{
		return "MFFS Projector Module  " + mach.displayName;
	}

	public static void generateRecipesFor(ProjectorTypes mach)
	{
		String[] recipeSplit = { mach.recipe.substring(0, 3), mach.recipe.substring(3, 6), mach.recipe.substring(6, 9) };

		GameRegistry.addRecipe(new ItemStack(mach.item, 1), new Object[] { recipeSplit, Character.valueOf('C'), ModularForceFieldSystem.itemModuleCube, Character.valueOf('B'), Block.obsidian, Character.valueOf('A'), ModularForceFieldSystem.itemFocusMatix });
	}
}
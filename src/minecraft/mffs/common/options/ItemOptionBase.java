package mffs.common.options;

import java.util.ArrayList;
import java.util.List;

import mffs.common.ProjectorTyp;
import mffs.common.item.ItemMFFS;
import mffs.common.modules.ItemModuleAdvancedCube;
import mffs.common.modules.ItemModuleContainment;
import mffs.common.modules.ItemModuleCube;
import mffs.common.modules.ItemModuleDeflector;
import mffs.common.modules.ItemModuleSphere;
import mffs.common.modules.ItemModuleTube;
import mffs.common.modules.ItemModuleWall;
import mffs.common.modules.ItemModuleDiagonalWall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public abstract class ItemOptionBase extends ItemMFFS
{
	private static List instances = new ArrayList();

	public ItemOptionBase(int i, String name)
	{
		super(i, name);
		setMaxStackSize(8);
		instances.add(this);
		this.setNoRepair();
	}

	public static List<ItemOptionBase> get_instances()
	{
		return instances;
	}

	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54)))
		{
			info.add("compatible with:");

			if (ItemModuleWall.supportsOption(this))
				info.add(ProjectorTyp.getdisplayName(ProjectorTyp.wall));
			if (ItemModuleDiagonalWall.supportsOption(this))
				info.add(ProjectorTyp.getdisplayName(ProjectorTyp.diagonallywall));
			if (ItemModuleDeflector.supportsOption(this))
				info.add(ProjectorTyp.getdisplayName(ProjectorTyp.deflector));
			if (ItemModuleTube.supportsOption(this))
				info.add(ProjectorTyp.getdisplayName(ProjectorTyp.tube));
			if (ItemModuleSphere.supportsOption(this))
				info.add(ProjectorTyp.getdisplayName(ProjectorTyp.sphere));
			if (ItemModuleCube.supportsOption(this))
				info.add(ProjectorTyp.getdisplayName(ProjectorTyp.cube));
			if (ItemModuleAdvancedCube.supportsOption(this))
				info.add(ProjectorTyp.getdisplayName(ProjectorTyp.AdvCube));
			if (ItemModuleContainment.supportsOption(this))
				info.add(ProjectorTyp.getdisplayName(ProjectorTyp.containment));
		}
		else
		{
			info.add("compatible with: (Hold Shift)");
		}
	}
}
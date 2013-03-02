package mffs.common.mode;

import java.util.ArrayList;
import java.util.List;

import mffs.api.IProjector;
import mffs.api.IProjectorMode;
import mffs.common.Functions;
import mffs.common.ProjectorTypes;
import mffs.common.SecurityHelper;
import mffs.common.SecurityRight;
import mffs.common.block.BlockForceField.ForceFieldType;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class ItemProjectorMode extends ItemMFFS implements IProjectorMode
{
	public ItemProjectorMode(int i, String name)
	{
		super(i, name);
		this.setMaxStackSize(1);
	}

	@Override
	public ForceFieldType getForceFieldType()
	{
		return ForceFieldType.Default;
	}
}
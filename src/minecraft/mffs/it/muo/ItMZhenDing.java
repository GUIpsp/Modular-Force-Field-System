package mffs.it.muo;

import mffs.api.IProjector;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

public class ItMZhenDing extends ItM
{
	public ItMZhenDing(int id)
	{
		super(id, "moduleStabilizer");
		this.setMaxStackSize(1);
		this.setCost(10);
	}

	@Override
	public boolean onProject(IProjector projector, Vector3 position)
	{
		for (int i : projector.getModuleSlots())
		{
			ItemStack checkStack = projector.getStackInSlot(i);

			if (checkStack != null)
			{
				if (checkStack.getItem() instanceof ItemBlock)
				{
					try
					{
						((ItemBlock) checkStack.getItem()).placeBlockAt(checkStack, null, ((TileEntity) projector).worldObj, position.intX(), position.intY(), position.intZ(), 0, 0, 0, 0, checkStack.getItemDamage());
						projector.decrStackSize(i, 1);
						return true;
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		return false;
	}

	@Override
	public float getFortronCost(int amplifier)
	{
		return super.getFortronCost(amplifier) * Math.max(Math.min((amplifier / 500), 1000), 1);
	}
}

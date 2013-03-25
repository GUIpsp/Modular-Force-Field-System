package mffs.common.tileentity;

import java.util.HashSet;
import java.util.Set;

import mffs.api.modules.IModule;
import mffs.api.modules.IModuleAcceptor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class TModuleAcceptor extends TileEntityFortron implements IModuleAcceptor
{
	public int startModuleIndex = 0;
	public int endModuleIndex = this.getSizeInventory() - 1;

	@Override
	public ItemStack getModule(IModule module)
	{
		ItemStack returnStack = new ItemStack((Item) module, 0);

		for (ItemStack comparedModule : getModuleStacks())
		{
			if (comparedModule.getItem() == module)
			{
				returnStack.stackSize += comparedModule.stackSize;
			}
		}

		return returnStack;
	}

	@Override
	public int getModuleCount(IModule module, int... slots)
	{
		int count = 0;

		if (slots != null && slots.length > 0)
		{
			for (int slotID : slots)
			{
				if (this.getStackInSlot(slotID) != null)
				{
					if (this.getStackInSlot(slotID).getItem() == module)
					{
						count += this.getStackInSlot(slotID).stackSize;
					}
				}
			}
		}
		else
		{
			for (ItemStack itemStack : getModuleStacks())
			{
				if (itemStack.getItem() == module)
				{
					count += itemStack.stackSize;
				}
			}
		}

		return count;
	}

	@Override
	public Set<ItemStack> getModuleStacks()
	{
		Set<ItemStack> modules = new HashSet<ItemStack>();

		for (int slotID = startModuleIndex; slotID <= endModuleIndex; slotID++)
		{
			ItemStack itemStack = this.getStackInSlot(slotID);

			if (itemStack != null)
			{
				if (itemStack.getItem() instanceof IModule)
				{
					modules.add(itemStack);
				}
			}
		}

		return modules;
	}

	@Override
	public Set<IModule> getModules()
	{
		Set<IModule> modules = new HashSet<IModule>();

		for (int slotID = startModuleIndex; slotID <= endModuleIndex; slotID++)
		{
			ItemStack itemStack = this.getStackInSlot(slotID);

			if (itemStack != null)
			{
				if (itemStack.getItem() instanceof IModule)
				{
					modules.add((IModule) itemStack.getItem());
				}
			}
		}

		return modules;
	}
}

package mffs.api;

import java.util.Set;

import net.minecraft.item.ItemStack;

public interface IModuleAcceptor
{
	public ItemStack getModule(IModule module);

	public int getModuleCount(IModule module, int... slots);

	public Set<IModule> getModules();

	public Set<ItemStack> getModuleStacks();

}

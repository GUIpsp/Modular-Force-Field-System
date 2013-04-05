package mffs.item.module;

import java.util.List;

import mffs.ModularForceFieldSystem;
import mffs.api.IProjector;
import mffs.api.modules.IModule;
import mffs.item.ItemBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.TranslationHelper;

public class ItemModule extends ItemBase implements IModule
{
	private float fortronCost = 0.5f;

	public ItemModule(int id, String name)
	{
		super(id, name);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		String tooltip = TranslationHelper.getLocal(this.getUnlocalizedName() + ".tooltip");

		if (tooltip != null && tooltip.length() > 0)
		{
			info.addAll(ModularForceFieldSystem.splitStringPerWord(tooltip, 5));
		}
	}

	@Override
	public boolean onCalculate(IProjector projector, Vector3 position)
	{
		return true;
	}

	@Override
	public boolean onProject(IProjector projector)
	{
		return false;
	}

	@Override
	public boolean onProject(IProjector projector, Vector3 position)
	{
		return false;
	}

	@Override
	public boolean canProject(IProjector projector, Vector3 position)
	{
		return true;
	}

	@Override
	public boolean onCollideWithForceField(World world, int x, int y, int z, Entity entity, ItemStack moduleStack)
	{
		return false;
	}

	public ItemModule setCost(float cost)
	{
		this.fortronCost = cost;
		return this;
	}

	@Override
	public float getFortronCost(int amplifier)
	{
		return fortronCost;
	}
}
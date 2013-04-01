package mffs.it.muo;

import java.util.List;

import mffs.ZhuYao;
import mffs.api.IProjector;
import mffs.api.modules.IModule;
import mffs.it.ItB;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.TranslationHelper;

public class ItM extends ItB implements IModule
{
	private float fortronCost = 0.5f;

	public ItM(int id, String name)
	{
		super(id, name);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		String tooltip = TranslationHelper.getLocal(this.getUnlocalizedName() + ".tooltip");

		if (tooltip != null && tooltip.length() > 0)
		{
			info.addAll(ZhuYao.splitStringPerWord(tooltip, 5));
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

	public ItM setCost(float cost)
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
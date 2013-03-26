package mffs.it.muo;

import java.util.List;

import mffs.ZhuYao;
import mffs.api.IProjector;
import mffs.api.modules.IModule;
import mffs.it.ItemMFFS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import universalelectricity.prefab.TranslationHelper;

public abstract class ItM extends ItemMFFS implements IModule
{
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
	public void onProject(IProjector projector, Vector3 position)
	{

	}

	@Override
	public boolean onCollideWithForceField(World world, int x, int y, int z, Entity entity)
	{
		return false;
	}

	@Override
	public float getFortronCost()
	{
		return 0.5f;
	}

}
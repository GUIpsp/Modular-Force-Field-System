package mffs.it.muo.fangyingji;

import mffs.api.IProjector;
import mffs.it.muo.ItM;
import mffs.jiqi.t.TLiQiang;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.core.vector.Vector3;

public class ItemModuleCamoflage extends ItM
{
	public ItemModuleCamoflage(int i)
	{
		super(i, "moduleCamouflage");
		this.setMaxDamage(1);
	}

	@Override
	public float getFortronCost()
	{
		return 1.5f;
	}
}
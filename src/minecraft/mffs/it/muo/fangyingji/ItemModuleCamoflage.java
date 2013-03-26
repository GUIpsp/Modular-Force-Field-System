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
	public void onProject(IProjector projector, Vector3 position)
	{
		for (int i : projector.getModuleSlots())
		{
			ItemStack checkStack = projector.getStackInSlot(i);

			if (checkStack != null)
			{
				if (checkStack.getItem() instanceof ItemBlock)
				{
					TileEntity tileEntity = position.getTileEntity(((TileEntity) projector).worldObj);

					if (tileEntity instanceof TLiQiang)
					{
						((TLiQiang) tileEntity).setFangGe(checkStack.itemID, checkStack.getItemDamage());
					}

					return;
				}
			}
		}
	}
}
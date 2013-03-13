package mffs.common.block;

import mffs.common.MFFSConfiguration;
import mffs.common.MFFSCreativeTab;
import mffs.common.ZhuYao;
import net.minecraft.block.material.Material;
import universalelectricity.prefab.block.BlockAdvanced;

public class BlockMFFS extends BlockAdvanced
{
	public BlockMFFS(int id, String name, Material material)
	{
		super(MFFSConfiguration.CONFIGURATION.getBlock(name, id).getInt(id), material);
		this.setUnlocalizedName(ZhuYao.PREFIX + name);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
	}
}

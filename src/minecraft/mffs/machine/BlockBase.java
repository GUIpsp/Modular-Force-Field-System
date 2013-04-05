package mffs.machine;

import mffs.MFFSConfiguration;
import mffs.MFFSCreativeTab;
import mffs.ModularForceFieldSystem;
import net.minecraft.block.material.Material;
import universalelectricity.prefab.block.BlockAdvanced;

public class BlockBase extends BlockAdvanced
{
	public BlockBase(int id, String name, Material material)
	{
		super(MFFSConfiguration.CONFIGURATION.getBlock(name, id).getInt(id), material);
		this.setUnlocalizedName(ModularForceFieldSystem.PREFIX + name);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
	}
}

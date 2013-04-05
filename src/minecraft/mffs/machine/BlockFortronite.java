package mffs.machine;

import java.util.Random;

import mffs.MFFSConfiguration;
import mffs.MFFSCreativeTab;
import mffs.ZhuYao;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockFortronite extends Block
{
	public BlockFortronite(int id, String name)
	{
		super(MFFSConfiguration.CONFIGURATION.getBlock(name, id).getInt(id), Material.rock);
		this.setUnlocalizedName(ZhuYao.PREFIX + name);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundStoneFootstep);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return ZhuYao.blockFortronite.blockID;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}
}
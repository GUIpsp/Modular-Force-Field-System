package mffs.common.block;

import java.util.Random;

import mffs.common.MFFSConfiguration;
import mffs.common.MFFSCreativeTab;
import mffs.common.ModularForceFieldSystem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockFortronite extends Block
{
	public BlockFortronite(int id, String name)
	{
		super(MFFSConfiguration.CONFIGURATION.getBlock(name, id).getInt(id), 4, Material.rock);
		this.setBlockName(name);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundStoneFootstep);
		this.setCreativeTab(MFFSCreativeTab.INSTANCE);
		this.setTextureFile(ModularForceFieldSystem.BLOCK_TEXTURE_FILE);
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return ModularForceFieldSystem.blockFortronite.blockID;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}
}
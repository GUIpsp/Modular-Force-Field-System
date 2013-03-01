package mffs.common.multitool;

import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MultitoolWrench implements IMultiTool {

	@Override
	public String getName() {
		return "Wrench";
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		return null;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player,
			World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		
		if (world.isRemote) {
			return false;
		}
		
		if(!player.isSneaking() && world.getBlockTileEntity(x, y, z) instanceof TileEntityMFFS) {
			TileEntityMFFS tile = (TileEntityMFFS) world.getBlockTileEntity(x, y, z);
			ItemStack drop = tile.getWrenchDrop(player);
			Block.blocksList[world.getBlockId(x, y, z)].onBlockDestroyedByPlayer(world, x, y, z, world.getBlockMetadata(x, y, z));
			world.setBlock(x, y, z, 0);
			world.spawnEntityInWorld(new EntityItem(world, hitX, hitY, hitZ, drop));
		}
		
		if(player.isSneaking())
			return false;
		else
			return true;
	}

}

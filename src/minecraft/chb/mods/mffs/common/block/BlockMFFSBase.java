/*  
    Copyright (C) 2012 Thunderdark

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Contributors:
    Thunderdark - initial implementation
*/

package chb.mods.mffs.common.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.SecurityHelper;
import chb.mods.mffs.common.SecurityRight;
import chb.mods.mffs.common.item.ItemCardEmpty;
import chb.mods.mffs.common.item.ItemCardPowerLink;
import chb.mods.mffs.common.item.ItemCardSecurityLink;
import chb.mods.mffs.common.modules.ModuleBase;
import chb.mods.mffs.common.multitool.ItemMultitool;
import chb.mods.mffs.common.tileentity.TileEntityAdvSecurityStation;
import chb.mods.mffs.common.tileentity.TileEntityAreaDefenseStation;
import chb.mods.mffs.common.tileentity.TileEntityCapacitor;
import chb.mods.mffs.common.tileentity.TileEntityControlSystem;
import chb.mods.mffs.common.tileentity.TileEntityConverter;
import chb.mods.mffs.common.tileentity.TileEntityExtractor;
import chb.mods.mffs.common.tileentity.TileEntityMachines;
import chb.mods.mffs.common.tileentity.TileEntityProjector;
import chb.mods.mffs.common.tileentity.TileEntitySecStorage;

public abstract class BlockMFFSBase extends BlockContainer {
	private int blockid;

	public BlockMFFSBase(int i) {
		super(i, Material.iron);
		blockid = i;
		setBlockUnbreakable();
		setRequiresSelfNotify();
		setResistance(100F);
		setStepSound(soundMetalFootstep);
		setRequiresSelfNotify();
		setCreativeTab(ModularForceFieldSystem.MFFSTab);
	}

	@Override
	public abstract TileEntity createNewTileEntity(World world);

	@Override
	public abstract String getTextureFile();

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer entityplayer, int par6, float par7, float par8,
			float par9){
		
		
		
		if (entityplayer.isSneaking())
        {
			return false;
        }
		
		if(world.isRemote)
		 return true;


		TileEntityMachines tileentity = (TileEntityMachines) world.getBlockTileEntity(i, j, k);
		
		if (entityplayer.getCurrentEquippedItem() != null
				&& (entityplayer.getCurrentEquippedItem().getItem() instanceof ItemMultitool)) {
			return false;
		}
		
		if (entityplayer.getCurrentEquippedItem() != null
				&& (entityplayer.getCurrentEquippedItem().getItem() instanceof ItemCardEmpty)) {
			return false;
		}
		
		if (entityplayer.getCurrentEquippedItem() != null
				&& (entityplayer.getCurrentEquippedItem().getItem() instanceof ModuleBase)) {
			return false;
		}
		
		if (entityplayer.getCurrentEquippedItem() != null
				&& (entityplayer.getCurrentEquippedItem().getItem() instanceof ItemCardPowerLink)) {
			return false;
		}
		
		if (entityplayer.getCurrentEquippedItem() != null
				&& (entityplayer.getCurrentEquippedItem().getItem() instanceof ItemCardSecurityLink)) {
			return false;
		}
		
		if (entityplayer.getCurrentEquippedItem() != null
				&& entityplayer.getCurrentEquippedItem().itemID == Block.lever.blockID) {
			return false;
		}
		
		if(tileentity instanceof TileEntityAdvSecurityStation){
			if(tileentity.isActive())
			{
				if(!SecurityHelper.isAccessGranted(tileentity, entityplayer, world,SecurityRight.CSR))
				{return true;}
			}
		}
		
		if(tileentity instanceof TileEntityControlSystem){
	
				if(!SecurityHelper.isAccessGranted(tileentity, entityplayer, world,SecurityRight.UCS))
				{return true;}
		}
		
		
		if(!SecurityHelper.isAccessGranted(tileentity, entityplayer, world,SecurityRight.EB))
		{return true;}
		
		
		if (!world.isRemote)
		entityplayer.openGui(ModularForceFieldSystem.instance, 0, world,
				i, j, k);
		
	
		return true;
	}
	
	
	@Override
	public void breakBlock(World world, int i, int j, int k,int a,int b) {
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
		if(tileEntity instanceof TileEntityMachines)
			((TileEntityMachines)tileEntity).dropplugins();
		world.removeBlockTileEntity(i, j, k);
	}



	public int idDropped(int i, Random random) {
		return blockID;
	}

	public static boolean isActive(IBlockAccess iblockaccess, int i, int j,
			int k) {
		TileEntity tileentity = iblockaccess.getBlockTileEntity(i, j, k);
		if (tileentity instanceof TileEntityMachines) {
			return ((TileEntityMachines) tileentity).isActive();
		} else {
			return false;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k,
			EntityLiving entityliving) {
		
		TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
		if(tileEntity instanceof TileEntityMachines)
		{


		int l = MathHelper
				.floor_double((double) ((entityliving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		int i1 = Math.round(entityliving.rotationPitch);
		if (i1 >= 65) {
			((TileEntityMachines)tileEntity).setSide( (short) 1);
		} else if (i1 <= -65) {
			((TileEntityMachines)tileEntity).setSide((short) 0);
		} else if (l == 0) {
			((TileEntityMachines)tileEntity).setSide( (short) 2);
		} else if (l == 1) {
			((TileEntityMachines)tileEntity).setSide( (short) 5);
		} else if (l == 2) {
			((TileEntityMachines)tileEntity).setSide((short) 3);
		} else if (l == 3) {
			((TileEntityMachines)tileEntity).setSide((short) 4);
		}
		}
		
		
	}
	
	@Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k,
			int l) {
		
		int typ = 	0;
		

		TileEntity tileentity = iblockaccess.getBlockTileEntity(i, j, k);

		int facing = (tileentity instanceof TileEntityMachines) ? ((TileEntityMachines) tileentity)
				.getSide() : 1;
		
		ForgeDirection blockfacing = ForgeDirection.getOrientation(l);	
		ForgeDirection TileEntityfacing = ForgeDirection.getOrientation(facing);

		if(tileentity instanceof TileEntityProjector)
			typ = ((TileEntityProjector) tileentity).getProjektor_Typ();
		


		if (isActive(iblockaccess, i, j, k)) {
			if(blockfacing.equals(TileEntityfacing))
				return (typ * 16) + 4 + 1 ;
			
			if(blockfacing.equals(TileEntityfacing.getOpposite()))
				return (typ * 16) + 4 +2 ;
		
			return (typ * 16) + 4 ;
			
		} else {
			
			if(blockfacing.equals(TileEntityfacing))
				return (typ * 16) + 1 ;
			
			if(blockfacing.equals(TileEntityfacing.getOpposite()))
				return (typ * 16) + 2;
		
			return (typ * 16);
		}
	}
	


	@Override
	public float getExplosionResistance(Entity entity,World world, int i, int j,
			int k, double d, double d1, double d2) {
		if (world.getBlockTileEntity(i, j, k) instanceof TileEntityMachines) {
			TileEntity tileentity = world.getBlockTileEntity(i, j, k);
			if (((TileEntityMachines) tileentity).isActive()) {
				return 999F;
			} else {
				return 100F;
			}
		}
		return 100F;
	}
}

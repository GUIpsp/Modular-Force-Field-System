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

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import chb.mods.mffs.api.IForceFieldBlock;
import chb.mods.mffs.api.PointXYZ;
import chb.mods.mffs.common.ForceFieldBlockStack;
import chb.mods.mffs.common.ForceFieldTyps;
import chb.mods.mffs.common.Functions;
import chb.mods.mffs.common.Linkgrid;
import chb.mods.mffs.common.MFFSDamageSource;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.SecurityHelper;
import chb.mods.mffs.common.SecurityRight;
import chb.mods.mffs.common.WorldMap;
import chb.mods.mffs.common.tileentity.TileEntityCapacitor;
import chb.mods.mffs.common.tileentity.TileEntityForceField;
import chb.mods.mffs.common.tileentity.TileEntityProjector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class BlockForceField extends BlockContainer implements IForceFieldBlock{
    public static int renderer;
    public int posx;
    public int posy;
    public int posz;

	public BlockForceField(int i) {
		super(i, i, Material.glass);
		setBlockUnbreakable();
		setResistance(999F);
		setTickRandomly(true);

	}
	
	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/blocks.png";
	}
	

	@Override
	public void onBlockAdded(World world, int i, int j, int k) {
		this.posx = i;
		this.posy = j;
	    this.posz = k;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderBlockPass() {

		if(ModularForceFieldSystem.proxy.getClientWorld().getBlockMetadata(posx , posy, posz) == ForceFieldTyps.Camouflage.ordinal())
		{
			TileEntityForceField ForceField   =	(TileEntityForceField) ModularForceFieldSystem.proxy.getClientWorld().getBlockTileEntity(posx , posy, posz);

	        if(ForceField  != null){
	        	
	        	if(ForceField.getTexturid(1) == 67 || ForceField.getTexturid(1) == 205)
	        	{
	        		return 1;
	        	}else{
	        		return 0;
	        	}
	        	
	        }
	     }
		return 0;
	}

    @Override
    public int getRenderType()
    {
        return ModularForceFieldSystem.MFFSRENDER_ID;
    }

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
    protected boolean canSilkHarvest()
    {
        return false;
    }
	
	
	@Override
	 public void onNeighborBlockChange(World world, int x, int y, int z, int blockid) {
		if(blockid  != ModularForceFieldSystem.MFFSFieldblock.blockID)
	    {
			for(int x1 = -1 ;x1<=1; x1++){
				for(int y1 = -1 ;y1<=1; y1++){
					for(int z1 = -1 ;z1<=1; z1++){
					if(world.getBlockId(x+x1, y+y1,z+z1)!= ModularForceFieldSystem.MFFSFieldblock.blockID )
					{
						if(world.getBlockId(x+x1, y+y1,z+z1)==0)
						{
							breakBlock(world, x+x1, y+y1, z+z1,0,0);
						}
					}
			}
		}
	}
		}
	 }

	@Override
	public void breakBlock(World world, int i, int j, int k,int a,int b){
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world).getForceFieldStackMap(new PointXYZ(i,j,k,world).hashCode());
		
		if (ffworldmap != null) {
				if(!ffworldmap.isEmpty()) {
					TileEntityProjector Projector  =	Linkgrid.getWorldMap(world).getProjektor().get(ffworldmap.getProjectorID());
			if(Projector != null){
				if(!Projector.isActive()){
					ffworldmap.removebyProjector(ffworldmap.getProjectorID());
				}else{
					world.setBlockAndMetadataWithNotify(i, j, k,ModularForceFieldSystem.MFFSFieldblock.blockID,ffworldmap.getTyp());
					world.markBlockForUpdate(i, j, k);
					ffworldmap.setSync(true);
	
						if (ffworldmap.getTyp() == 1) {
							Projector.consumePower(ModularForceFieldSystem.forcefieldblockcost* ModularForceFieldSystem.forcefieldblockcreatemodifier,false);
						} else {
							Projector.consumePower(ModularForceFieldSystem.forcefieldblockcost* ModularForceFieldSystem.forcefieldblockcreatemodifier* ModularForceFieldSystem.forcefieldblockzappermodifier,false);
						}
			}
			}
		}
		}
	}
	

    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
	
    	if (par1World.isRemote)
    		return;

    	ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(par1World).getForceFieldStackMap(new PointXYZ(par2,par3,par4,par1World).hashCode());

    	if(ffworldmap != null && !ModularForceFieldSystem.adventuremap)
    	{
    		
    		TileEntityProjector projector = Linkgrid.getWorldMap(par1World).getProjektor().get(ffworldmap.getProjectorID());
    		if(projector != null){
    			switch(projector.getaccesstyp()){

    			case 0:
        	    par5EntityPlayer.attackEntityFrom(MFFSDamageSource.fieldShock,10);
        	    Functions.ChattoPlayer((EntityPlayer)par5EntityPlayer,"[Force Field] Attention High Energy Field");	
    			break;

    			case 2:
    			case 3:
    	    	if(!SecurityHelper.isAccessGranted(projector, par5EntityPlayer, par1World,SecurityRight.SR))
    	        {
    	    	par5EntityPlayer.attackEntityFrom(MFFSDamageSource.fieldShock,10);
    	    	Functions.ChattoPlayer((EntityPlayer)par5EntityPlayer,"[Force Field] Attention High Energy Field");
    	    	}
    	    	break;
    			}
    		}
    			
    		 if(!SecurityHelper.isAccessGranted(projector, par5EntityPlayer, par1World,SecurityRight.SR))
    		 {
    		par5EntityPlayer.attackEntityFrom(MFFSDamageSource.fieldShock,10);
    		Functions.ChattoPlayer((EntityPlayer)par5EntityPlayer,"[Force Field] Attention High Energy Field");
    		 }
    	}

    	Random random = null;
    	updateTick(par1World, par2, par3, par4,random);
    }
	


	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		if (world.getBlockMetadata(i, j, k) == ForceFieldTyps.Zapper.ordinal()) {
			float f = 0.0625F;
			return AxisAlignedBB.getBoundingBox(i + f, j + f, k + f, i + 1 - f, j + 1 - f, k + 1 - f);
		}
		
		return AxisAlignedBB.getBoundingBox((float) i, j, (float) k, (float) (i + 1), (float) (j + 1), (float) (k + 1));
	
	}

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return AxisAlignedBB.getBoundingBox((float) i, j, (float) k, (float) (i + 0), j + 0, (float) (k + 0));
	}
    

    
    @Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k,
			Entity entity) {
    	
    	if (world.getBlockMetadata(i, j, k) == ForceFieldTyps.Zapper.ordinal()) {
    		if (entity instanceof EntityLiving) {
    			entity.attackEntityFrom(MFFSDamageSource.fieldShock,10);
    		}
    	}else{
    		if (entity instanceof EntityPlayer) {
    			ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world).getorcreateFFStackMap(i, j, k,world);

    			if (ffworldmap != null) {

    				TileEntityProjector  projector = Linkgrid.getWorldMap(world).getProjektor().get(ffworldmap.getProjectorID());

    				if(projector!= null)
    				{

    					boolean passtrue = false;

    					switch(projector.getaccesstyp())
    					{
    					case 0:
    						passtrue = false;	
    						if(ModularForceFieldSystem.Admin.equals(((EntityPlayer)entity).username))
    							passtrue = true;
    						break;
    					case 1:
    						passtrue = true;
    						break;
    					case 2:
    						TileEntityCapacitor  generator = Linkgrid.getWorldMap(world).getCapacitor().get(ffworldmap.getGenratorID());
    						passtrue = SecurityHelper.isAccessGranted(generator, ((EntityPlayer) entity), world,SecurityRight.FFB);
    						break;
    					case 3:
    						passtrue = SecurityHelper.isAccessGranted(projector, ((EntityPlayer) entity), world,SecurityRight.FFB);
    						break;
    					}

    					if(!passtrue)
    					{
    						((EntityPlayer) entity).attackEntityFrom(MFFSDamageSource.fieldShock, 20);

    					}else{
    						((EntityPlayer) entity).attackEntityFrom(MFFSDamageSource.fieldShock,1);
    					}
    					Functions.ChattoPlayer((EntityPlayer)entity,"[Force Field] Attention High Energy Field");
    				}
    			}
    		}
			
     }
				

	}
	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int x, int y, int z, int side) {
    	int xCord = x;
    	int yCord = y;
    	int zCord = z;

		switch(side) {
		case 0: yCord++;
		break;
    	case 1: yCord--;
		break;
		case 2: zCord++;
		break;
		case 3: zCord--;
		break;
		case 4: xCord++;
		break;
		case 5: xCord--;
		break;
		}

		if(blockID == iblockaccess.getBlockId(x, y, z) && iblockaccess.getBlockMetadata(x, y, z) == iblockaccess.getBlockMetadata(xCord, yCord, zCord))
			return false;

		return super.shouldSideBeRendered(iblockaccess, x, y, z, side);
		
	}

    @Override
	public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k,
			int l) {
		TileEntity tileEntity = iblockaccess.getBlockTileEntity(i, j, k);

		if (tileEntity!=null && tileEntity instanceof TileEntityForceField )
		{
			if(l<0 ||l >5) return 0;
			
		return  ((TileEntityForceField)tileEntity).getTexturid(l);
	}else{
		if (iblockaccess.getBlockMetadata(i, j, k) == ForceFieldTyps.Camouflage.ordinal())
		{
			 return 180;
		}else{
			
			if(iblockaccess.getBlockMetadata(i, j, k) == ForceFieldTyps.Default.ordinal()) return 0;
			if(iblockaccess.getBlockMetadata(i, j, k) == ForceFieldTyps.Zapper.ordinal()) return 1;
			if(iblockaccess.getBlockMetadata(i, j, k) == ForceFieldTyps.Area.ordinal()) return 2;
			if(iblockaccess.getBlockMetadata(i, j, k) == ForceFieldTyps.Containment.ordinal()) return 3;
			
			return 5;
		}
	}
	}

	@Override
	public float getExplosionResistance(Entity entity,World world, int i, int j,
			int k, double d, double d1, double d2) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world)
				.getForceFieldStackMap(new PointXYZ(i,j,k,world).hashCode());
		if (ffworldmap != null && !ffworldmap.isEmpty()) {
			
			TileEntity tileEntity = Linkgrid.getWorldMap(world).getProjektor()
					.get(ffworldmap.getProjectorID());
			
			if (tileEntity instanceof TileEntityProjector && tileEntity != null) {
				((TileEntityProjector) tileEntity)
						.consumePower(ModularForceFieldSystem.forcefieldblockcost
								* ModularForceFieldSystem.forcefieldblockcreatemodifier,false);
			}
		}

		return 999F;
	}

	@Override
	public void randomDisplayTick(World world, int i, int j, int k,
			Random random) {
		if (ModularForceFieldSystem.showZapperParticles && world.getBlockMetadata(i, j, k) == ForceFieldTyps.Zapper.ordinal()) {
			double d = i + Math.random()+ 0.2D;
			double d1 = j + Math.random() + 0.2D;
			double d2 = k + Math.random() + 0.2D;

			world.spawnParticle("townaura", d, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
	@Override
	public boolean canConnectRedstone(IBlockAccess iba, int i, int j, int k,
			int dir) {
		return false;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(world).getForceFieldStackMap(new PointXYZ(x,y,z,world).hashCode());

	if(ffworldmap != null)
	{
		if(!ffworldmap.isEmpty()) {
			TileEntityProjector Projector  =	Linkgrid.getWorldMap(world).getProjektor().get(ffworldmap.getProjectorID());
	if(Projector != null){
		if(!Projector.isActive()){
			ffworldmap.removebyProjector(ffworldmap.getProjectorID());
		}
	}
		}
	}

		if(ffworldmap == null || ffworldmap.isEmpty())
		{
			world.removeBlockTileEntity(x, y, z);
			world.setBlockWithNotify(x, y, z, 0);
		}
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		if(meta == ForceFieldTyps.Camouflage.ordinal())
		{
			
				return new TileEntityForceField();

		}

		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return createTileEntity(world,0);
	}
	
	

 @Override
 public void weakenForceField(World world, int x, int y, int z)
 {
   if(ModularForceFieldSystem.influencedbyothermods)
   {
    world.setBlockWithNotify(x, y, z, 0);
   }
 }
}

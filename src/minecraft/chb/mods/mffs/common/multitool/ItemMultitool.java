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
    Matchlighter 
*/

package chb.mods.mffs.common.multitool;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import chb.mods.mffs.api.IForceEnergyItems;
import chb.mods.mffs.common.ForceEnergyItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



public abstract class ItemMultitool extends  ForceEnergyItems  implements IForceEnergyItems{
	private int typ;

	protected ItemMultitool(int id, int typ, boolean addToList) {
		super(id);
		this.typ = typ;
		setIconIndex(typ);
		setMaxStackSize(1);
		setMaxDamage(100);
		if (addToList)
			MTTypes.add(this);
	}
	
	protected ItemMultitool(int id, int typ) {
		this(id, typ, true);
	}

	private static List<ItemMultitool> MTTypes = new ArrayList<ItemMultitool>();
	
	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/items.png";
	}


	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean isDamageable()
	{
	return true;
	}


	@Override
	public abstract boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer){
		if (entityplayer.isSneaking()){
			int modeNum = 0;
			for (int i = 0; i < MTTypes.size(); i++) {
				ItemMultitool MT = MTTypes.get(i);
				if (MT.itemID == itemstack.getItem().itemID){
					if (i+1 < MTTypes.size())
						modeNum=i+1;
					else
						modeNum=0;
				}
			}
			
			int powerleft = getAvailablePower(itemstack);
			ItemStack hand = entityplayer.inventory.getCurrentItem();
			hand= new ItemStack(MTTypes.get(modeNum), 1);
			this.chargeItem(hand, powerleft,false);
			return hand;
		}
		return itemstack;
	}
	

		
	    
	    @Override
	    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	    {
	    	par1ItemStack.setItemDamage(getItemDamage(par1ItemStack));
	    }
	    

	    @Override
	    public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b)
	    {
	        String tooltip = String.format( "%d FE/%d FE ",getAvailablePower(itemStack),getMaximumPower(itemStack));
	        info.add(tooltip);
	    }
	    
		@Override
		public int getPowerTransferrate() {
			
			return 50000;
		}
		
		@Override
		public  int getMaximumPower(ItemStack itemStack) {
			
			return 1000000;
		}
		

		
		@Override
		public int getItemDamage(ItemStack itemStack)
		{
			return 101-((getAvailablePower(itemStack)*100)/getMaximumPower(itemStack));
			

		}

		@SideOnly(Side.CLIENT)
		public void getSubItems(int i, CreativeTabs tabs, List itemList)
		{
			ItemStack charged = new ItemStack(this, 1);
			charged.setItemDamage(1);
			setAvailablePower(charged, getMaximumPower(null));
			itemList.add(charged);
		}
}

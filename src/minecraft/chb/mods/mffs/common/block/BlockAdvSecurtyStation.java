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

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.tileentity.TileEntityAdvSecurityStation;



public class BlockAdvSecurtyStation extends BlockMFFSBase {
	public BlockAdvSecurtyStation(int i) {
		super(i);
	}
	@Override
	public String getTextureFile() {
		
		if(ModularForceFieldSystem.graphicstyle==1)
		return "/chb/mods/mffs/sprites/AdvSecurtyStation_ue.png";
		
		return "/chb/mods/mffs/sprites/AdvSecurtyStation.png";
	}


	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAdvSecurityStation();
	}


}

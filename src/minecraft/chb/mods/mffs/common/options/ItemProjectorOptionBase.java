package chb.mods.mffs.common.options;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import chb.mods.mffs.common.ModularForceFieldSystem;
import chb.mods.mffs.common.ProjectorTyp;
import chb.mods.mffs.common.modules.ItemProjectorModuleAdvCube;
import chb.mods.mffs.common.modules.ItemProjectorModuleContainment;
import chb.mods.mffs.common.modules.ItemProjectorModuleCube;
import chb.mods.mffs.common.modules.ItemProjectorModuleDeflector;
import chb.mods.mffs.common.modules.ItemProjectorModuleSphere;
import chb.mods.mffs.common.modules.ItemProjectorModuleTube;
import chb.mods.mffs.common.modules.ItemProjectorModuleWall;
import chb.mods.mffs.common.modules.ItemProjectorModulediagonallyWall;

public abstract class ItemProjectorOptionBase extends Item{
	public ItemProjectorOptionBase(int i) {
		super(i);
		setMaxStackSize(8);
		setCreativeTab(ModularForceFieldSystem.MFFSTab);
		instances.add(this);
	}
	private static List<ItemProjectorOptionBase> instances = new ArrayList<ItemProjectorOptionBase>();
	public static List<ItemProjectorOptionBase> get_instances(){
		return instances;
	}
	
	@Override
	public String getTextureFile() {
		return "/chb/mods/mffs/sprites/items.png";
	}
	
	@Override
	public boolean isRepairable() {
		return false;
	}
	
	@Override
    public void addInformation(ItemStack itemStack,EntityPlayer player, List info,boolean b)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            info.add("compatible with:");

            if(ItemProjectorModuleWall.supportsOption(this))info.add(ProjectorTyp.getdisplayName(ProjectorTyp.wall));
            if(ItemProjectorModulediagonallyWall.supportsOption(this))info.add(ProjectorTyp.getdisplayName(ProjectorTyp.diagonallywall));
            if(ItemProjectorModuleDeflector.supportsOption(this))info.add(ProjectorTyp.getdisplayName(ProjectorTyp.deflector));
            if(ItemProjectorModuleTube.supportsOption(this))info.add(ProjectorTyp.getdisplayName(ProjectorTyp.tube));
            if(ItemProjectorModuleSphere.supportsOption(this))info.add(ProjectorTyp.getdisplayName(ProjectorTyp.sphere));
            if(ItemProjectorModuleCube.supportsOption(this))info.add(ProjectorTyp.getdisplayName(ProjectorTyp.cube));
            if(ItemProjectorModuleAdvCube.supportsOption(this))info.add(ProjectorTyp.getdisplayName(ProjectorTyp.AdvCube));
            if(ItemProjectorModuleContainment.supportsOption(this))info.add(ProjectorTyp.getdisplayName(ProjectorTyp.containment));
            
        }else{
        	info.add("compatible with: (Hold Shift)");
        }
    }
	
}

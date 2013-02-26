package mffs.common.upgrade;

import java.util.List;

import mffs.common.MachineTypes;
import mffs.common.item.ItemMFFS;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

public class ItemModuleDistance extends ItemMFFS {

	public ItemModuleDistance(int i) {
		super(i, "moduleDistance");
		setIconIndex(64);
		setMaxStackSize(64);
		this.setNoRepair();

	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List info, boolean b) {
		if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54))) {
			info.add("compatible with:");
			info.add("MFFS " + MachineTypes.Projector.getName());
			info.add("MFFS " + MachineTypes.DefenceStation.getName());
		} else {
			info.add("compatible with: (Hold Shift)");
		}
	}
}
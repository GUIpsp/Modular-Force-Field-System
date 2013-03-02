package mffs.common.module;

import java.util.List;

import mffs.common.ProjectorTypes;
import mffs.common.item.ItemMFFS;
import mffs.common.mode.ItemModeContainment;
import mffs.common.mode.ItemModeCube;
import mffs.common.mode.ItemModeDeflector;
import mffs.common.mode.ItemModeDiagonalWall;
import mffs.common.mode.ItemModeSphere;
import mffs.common.mode.ItemModeTube;
import mffs.common.mode.ItemModeWall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import universalelectricity.prefab.TranslationHelper;

public abstract class ItemModule extends ItemMFFS implements IModule
{
	public ItemModule(int i, String name)
	{
		super(i, name);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List info, boolean b)
	{
		String tooltip = TranslationHelper.getLocal(this.getItemName() + ".tooltip");

		if (tooltip != null && tooltip.length() > 0)
		{
			info.add(tooltip);
		}

		/*
		 * if ((Keyboard.isKeyDown(42)) || (Keyboard.isKeyDown(54))) { info.add("compatible with:");
		 * 
		 * if (ItemModeWall.supportsOption(this)) {
		 * info.add(ProjectorTypes.getdisplayName(ProjectorTypes.wall)); } if
		 * (ItemModeDiagonalWall.supportsOption(this)) {
		 * info.add(ProjectorTypes.getdisplayName(ProjectorTypes.diagonallWall)); } if
		 * (ItemModeDeflector.supportsOption(this)) {
		 * info.add(ProjectorTypes.getdisplayName(ProjectorTypes.deflector)); } if
		 * (ItemModeTube.supportsOption(this)) {
		 * info.add(ProjectorTypes.getdisplayName(ProjectorTypes.tube)); } if
		 * (ItemModeSphere.supportsOption(this)) {
		 * info.add(ProjectorTypes.getdisplayName(ProjectorTypes.sphere)); } if
		 * (ItemModeCube.supportsOption(this)) {
		 * info.add(ProjectorTypes.getdisplayName(ProjectorTypes.cube)); } if
		 * (ItemModeContainment.supportsOption(this)) {
		 * info.add(ProjectorTypes.getdisplayName(ProjectorTypes.containment)); } } else {
		 * info.add("compatible with: (Hold Shift)"); }
		 */
	}
}
package mffs.common.module.fangyu;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mffs.api.IDefenseStation;
import mffs.api.IDefenseStationModule;
import mffs.common.MFFSConfiguration;
import mffs.common.ZhuYao;
import mffs.common.module.ItemModule;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

public class ItemModuleWarn extends ItemModule implements IDefenseStationModule
{
	public ItemModuleWarn(int i)
	{
		super(i, "moduleWarn");
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onDefend(IDefenseStation defenseStation, EntityLiving entityLiving)
	{
		// TODO: Add custom message.
		boolean hasPermission = false;
		if (!hasPermission && entityLiving instanceof EntityPlayer)
		{
			((EntityPlayer) entityLiving).addChatMessage("[" + defenseStation.getInvName() + "] Leave this zone immediately. You have no right to enter.");
		}
		return false;
	}
}
package mffs.common.card;

import java.util.List;

import mffs.api.PointXYZ;
import mffs.common.FrequencyGrid;
import mffs.common.MachineTypes;
import mffs.common.NBTTagCompoundHelper;
import mffs.common.tileentity.TileEntityMFFS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ItemCardDataLink extends ItemCard {

	public ItemCardDataLink(int id) {
		super(id, "cardDataLink");
		setMaxStackSize(1);
		setIconIndex(22);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World world, Entity entity,
			int par4, boolean par5) {
		super.onUpdate(itemStack, world, entity, par4, par5);

		if (this.tick > 600) {
			int DeviceID = getValuefromKey("DeviceID", itemStack);
			if (DeviceID != 0) {
				TileEntityMFFS device = FrequencyGrid.getWorldMap(world)
						.getTileEntityMachines(getDeviceTyp(itemStack),
								DeviceID);
				if (device != null) {
					if (!device.getDeviceName().equals(
							getforAreaname(itemStack))) {
						setforArea(itemStack, device.getDeviceName());
					}

				}

			}

			this.tick = 0;
		}
		this.tick += 1;
	}

	public void setInformation(ItemStack itemStack, PointXYZ png, String key,
			int value, TileEntity tileentity) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);
		nbtTagCompound.setString("displayName", MachineTypes.fromTE(tileentity)
				.getName());
		super.setInformation(itemStack, png, key, value);
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player,
			List info, boolean b) {
		NBTTagCompound tag = NBTTagCompoundHelper
				.getTAGfromItemstack(itemStack);

		info.add("DeviceTyp: " + getDeviceTyp(itemStack));
		info.add("DeviceName: " + getforAreaname(itemStack));

		if (tag.hasKey("worldname")) {
			info.add("World: " + tag.getString("worldname"));
		}
		if (tag.hasKey("linkTarget")) {
			info.add("Coords: "
					+ new PointXYZ(tag.getCompoundTag("linkTarget")).toString());
		}
		if (tag.hasKey("valid")) {
			info.add(tag.getBoolean("valid") ? "§2Valid" : "§4Invalid");
		}
	}

	public static String getDeviceTyp(ItemStack itemstack) {
		NBTTagCompound nbtTagCompound = NBTTagCompoundHelper
				.getTAGfromItemstack(itemstack);
		if (nbtTagCompound != null) {
			return nbtTagCompound.getString("displayName");
		}
		return "-";
	}
}
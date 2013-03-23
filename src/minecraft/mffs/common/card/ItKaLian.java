package mffs.common.card;

import java.util.List;

import mffs.common.NBTTagCompoundHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

/**
 * A linking card used to link machines in specific positions.
 * 
 * @author Calclavia
 * 
 */
public class ItKaLian extends ItKa
{
	public ItKaLian(int id)
	{
		super(id, "cardLink");
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
	{
		Vector3 position = this.getLink(itemStack);

		if (position != null)
		{
			int blockId = position.getBlockID(player.worldObj);

			if (Block.blocksList[blockId] != null)
			{
				list.add("Linked with: " + Block.blocksList[blockId].getLocalizedName());
				list.add(position.intX() + ", " + position.intY() + ", " + position.intZ());
				return;
			}
		}

		list.add("Not linked.");
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		this.setLink(itemStack, new Vector3(x, y, z));
		player.addChatMessage("Linked card to position: " + x + ", " + y + ", " + z);

		return true;
	}

	public void setLink(ItemStack itemStack, Vector3 position)
	{
		NBTTagCompound nbt = NBTTagCompoundHelper.get(itemStack);
		nbt.setCompoundTag("position", position.writeToNBT(new NBTTagCompound()));
	}

	public Vector3 getLink(ItemStack itemStack)
	{
		NBTTagCompound nbt = NBTTagCompoundHelper.get(itemStack);
		return Vector3.readFromNBT(nbt.getCompoundTag("position"));
	}
}
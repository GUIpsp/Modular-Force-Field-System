package mffs.common;

import java.lang.reflect.Constructor;

import universalelectricity.core.vector.Vector3;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{

	public void init()
	{
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity == null)
		{
			return null;
		}

		MachineTypes machType = MachineTypes.fromTE(tileEntity);
		try
		{
			Constructor mkGui = machType.gui.getConstructor(new Class[] { EntityPlayer.class, machType.tileEntity });
			return mkGui.newInstance(new Object[] { player, machType.tileEntity.cast(tileEntity) });
		}
		catch (Exception ex)
		{
			System.out.println("Failed to open GUI: " + ex.getLocalizedMessage());
		}

		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null)
		{
			return null;
		}

		MachineTypes machType = MachineTypes.fromTE(tileEntity);
		try
		{
			Constructor mkGui = machType.container.getConstructor(new Class[] { EntityPlayer.class, machType.tileEntity });
			return mkGui.newInstance(new Object[] { player, machType.tileEntity.cast(tileEntity) });
		}
		catch (Exception ex)
		{
			System.out.println("Failed to open GUI: " + ex.getLocalizedMessage());
		}

		return null;
	}

	public World getClientWorld()
	{
		return null;
	}

	public void renderBeam(World world, Vector3 position, Vector3 target, float red, float green, float blue, int age)
	{

	}
}
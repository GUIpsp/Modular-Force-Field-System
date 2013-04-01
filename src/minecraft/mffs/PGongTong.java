package mffs;

import java.lang.reflect.Constructor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.network.IGuiHandler;

public class PGongTong implements IGuiHandler
{
	public void preInit()
	{

	}

	public void init()
	{
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null)
		{
			MachineTypes machineType = MachineTypes.get(tileEntity);

			try
			{
				Constructor mkGui = machineType.container.getConstructor(EntityPlayer.class, machineType.tileEntity);
				return mkGui.newInstance(player, machineType.tileEntity.cast(tileEntity));
			}
			catch (Exception e)
			{
				ZhuYao.LOGGER.severe("Failed to open container: ");
				e.printStackTrace();
			}
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
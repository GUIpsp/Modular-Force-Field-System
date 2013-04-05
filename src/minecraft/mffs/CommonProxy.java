package mffs;

import java.lang.reflect.Constructor;

import mffs.container.ContainerCapacitor;
import mffs.container.ContainerDefenceStation;
import mffs.container.ContainerExtractor;
import mffs.container.ContainerProjector;
import mffs.container.ContainerSecurityStation;
import mffs.machine.tile.TileCapacitor;
import mffs.machine.tile.TileDefenceStation;
import mffs.machine.tile.TileExtractor;
import mffs.machine.tile.TileProjector;
import mffs.machine.tile.TileSecurityStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public void preInit()
	{
		BlockMachine.projector = new BlockMachine(0, ModularForceFieldSystem.blockProjector, TileProjector.class, ContainerProjector.class);
		BlockMachine.extractor = new BlockMachine(1, ModularForceFieldSystem.blockExtractor, TileExtractor.class, ContainerExtractor.class);
		BlockMachine.capacitor = new BlockMachine(2, ModularForceFieldSystem.blockCapacitor, TileCapacitor.class, ContainerCapacitor.class);
		BlockMachine.defenceStation = new BlockMachine(3, ModularForceFieldSystem.blockDefenceStation, TileDefenceStation.class, ContainerDefenceStation.class);
		BlockMachine.securityStation = new BlockMachine(4, ModularForceFieldSystem.blockSecurityStation, TileSecurityStation.class, ContainerSecurityStation.class);
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
			BlockMachine machineType = BlockMachine.get(tileEntity);

			try
			{
				Constructor mkGui = machineType.container.getConstructor(EntityPlayer.class, machineType.tileEntity);
				return mkGui.newInstance(player, machineType.tileEntity.cast(tileEntity));
			}
			catch (Exception e)
			{
				ModularForceFieldSystem.LOGGER.severe("Failed to open container: ");
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
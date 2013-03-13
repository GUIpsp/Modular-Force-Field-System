package mffs.common.mode;

import java.util.Set;

import mffs.api.IProjector;
import mffs.common.ModularForceFieldSystem;
import mffs.common.module.ItemModule;
import mffs.common.module.ItemModuleCamoflage;
import mffs.common.module.ItemModuleDisintegration;
import mffs.common.module.ItemModuleShock;
import mffs.common.tileentity.TileEntityProjector;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;

public class ItemModeWall extends ItemProjectorMode
{
	public ItemModeWall(int i, String name)
	{
		super(i, name);
	}

	public ItemModeWall(int i)
	{
		this(i, "modeWall");
	}

	@Override
	public boolean supportsDistance()
	{
		return true;
	}

	@Override
	public boolean supportsStrength()
	{
		return true;
	}

	@Override
	public boolean supportsMatrix()
	{
		return true;
	}

	@Override
	public void calculateField(IProjector projector, Set fieldDefinition, Set interior)
	{
		TileEntityProjector tileEntity = (TileEntityProjector) projector;

		ForgeDirection direction = tileEntity.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord);

		int zDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yDisplacePos = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yDisplaceNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleScale, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		for (int x = -xDisplaceNeg; x < xDisplacePos + 1; x++)
		{
			for (int z = -zDisplaceNeg; z < zDisplacePos + 1; z++)
			{
				for (int y = -yDisplaceNeg; y <= yDisplacePos; y++)
				{
					if (((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() != 0) && (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() != 1)) || (((x == 0) && (z != 0)) || ((z == 0) && (x != 0)) || ((z == 0) && (x == 0)) || (((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() != 2) && (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() != 3)) || (((x == 0) && (y != 0)) || ((y == 0) && (x != 0)) || ((y == 0) && (x == 0)) || (((projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 4) || (projector.getDirection(((TileEntity)projector).worldObj,((TileEntity)projector).xCoord,((TileEntity)projector).yCoord,((TileEntity)projector).zCoord).ordinal() == 5)) && (((z == 0) && (y != 0)) || ((y == 0) && (z != 0)) || ((y == 0) && (z == 0))))))))
					{
						fieldDefinition.add(new Vector3(x, y, z));
					}
				}
			}
		}
	}

	public static boolean supportsOption(ItemModule item)
	{
		if ((item instanceof ItemModuleDisintegration))
		{
			return true;
		}
		if ((item instanceof ItemModuleCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemModuleShock))
		{
			return true;
		}

		return false;
	}

	@Override
	public boolean supportsOption(Item item)
	{
		if ((item instanceof ItemModuleDisintegration))
		{
			return true;
		}
		if ((item instanceof ItemModuleCamoflage))
		{
			return true;
		}
		if ((item instanceof ItemModuleShock))
		{
			return true;
		}

		return false;
	}
}
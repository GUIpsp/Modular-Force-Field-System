package mffs.item.mode;

import java.awt.geom.AffineTransform;
import java.util.Set;

import mffs.ModularForceFieldSystem;
import mffs.api.IProjector;
import mffs.api.modules.IProjectorMode;
import mffs.item.ItemBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemProjectorMode extends ItemBase implements IProjectorMode
{
	public ItemProjectorMode(int i, String name)
	{
		super(i, name);
		this.setMaxStackSize(1);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(IProjector projector, double x, double y, double z, float f, long ticks)
	{

	}

	/**
	 * Calculates all base translation and rotational values. Then left the specific mode denote the
	 * shape of the field.
	 */
	@Override
	public void calculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior)
	{
		ForgeDirection direction = projector.getDirection(((TileEntity) projector).worldObj, ((TileEntity) projector).xCoord, ((TileEntity) projector).yCoord, ((TileEntity) projector).zCoord);

		int zScaleNeg = projector.getModuleCount(ModularForceFieldSystem.itMDaXiao, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zScalePos = projector.getModuleCount(ModularForceFieldSystem.itMDaXiao, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xScaleNeg = projector.getModuleCount(ModularForceFieldSystem.itMDaXiao, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xScalePos = projector.getModuleCount(ModularForceFieldSystem.itMDaXiao, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yScalePos = projector.getModuleCount(ModularForceFieldSystem.itMDaXiao, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yScaleNeg = projector.getModuleCount(ModularForceFieldSystem.itMDaXiao, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		int overAllIncrease = projector.getModuleCount(ModularForceFieldSystem.itMDaXiao, projector.getModuleSlots());

		zScaleNeg += overAllIncrease;
		zScalePos += overAllIncrease;

		xScaleNeg += overAllIncrease;
		xScalePos += overAllIncrease;

		yScalePos += overAllIncrease;
		yScaleNeg += overAllIncrease;

		int zTranslationNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zTranslationPos = projector.getModuleCount(ModularForceFieldSystem.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xTranslationNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xTranslationPos = projector.getModuleCount(ModularForceFieldSystem.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yTranslationPos = projector.getModuleCount(ModularForceFieldSystem.itemModuleTranslation, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yTranslationNeg = projector.getModuleCount(ModularForceFieldSystem.itemModuleTranslation, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		Vector3 translation = new Vector3(xTranslationPos - xTranslationNeg, yTranslationPos - yTranslationNeg, zTranslationPos - zTranslationNeg);
		Vector3 posScale = new Vector3(xScalePos, yScalePos, zScalePos);
		Vector3 negScale = new Vector3(xScaleNeg, yScaleNeg, zScaleNeg);

		this.doCalculateField(projector, blockDef, blockInterior, direction, translation, posScale, negScale);

		int horizontalRotation = projector.getModuleCount(ModularForceFieldSystem.itMZhuan, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST))) - projector.getModuleCount(ModularForceFieldSystem.itMZhuan, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST))) + projector.getModuleCount(ModularForceFieldSystem.itMZhuan, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH))) - projector.getModuleCount(ModularForceFieldSystem.itMZhuan, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int verticleRotation = projector.getModuleCount(ModularForceFieldSystem.itMZhuan, projector.getSlotsBasedOnDirection(ForgeDirection.UP)) - projector.getModuleCount(ModularForceFieldSystem.itMZhuan, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		for (Vector3 point : blockDef)
		{
			double[] pt = { point.x, point.z };
			AffineTransform.getRotateInstance(Math.toRadians(horizontalRotation), translation.x, translation.z).transform(pt, 0, pt, 0, 1);

			double[] pt2 = { point.x, point.y };
			AffineTransform.getRotateInstance(Math.toRadians(verticleRotation), translation.x, translation.y).transform(pt2, 0, pt2, 0, 1);

			point.x = pt[0];
			point.z = pt[1];
			point.y = pt2[1];
			point = point.round();
		}

		for (Vector3 point : blockInterior)
		{
			double[] pt = { point.x, point.z };
			AffineTransform.getRotateInstance(Math.toRadians(horizontalRotation), translation.x, translation.z).transform(pt, 0, pt, 0, 1);

			double[] pt2 = { point.x, point.y };
			AffineTransform.getRotateInstance(Math.toRadians(verticleRotation), translation.x, translation.y).transform(pt2, 0, pt2, 0, 1);

			point.x = pt[0];
			point.z = pt[1];
			point.y = pt2[1];
			point = point.round();
		}
	}

	public void doCalculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior, ForgeDirection direction, Vector3 translation, Vector3 posScale, Vector3 negScale)
	{

	}
}
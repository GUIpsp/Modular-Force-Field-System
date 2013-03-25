package mffs.it.xingshi;

import java.awt.geom.AffineTransform;
import java.util.Set;

import mffs.ZhuYao;
import mffs.api.IProjector;
import mffs.api.modules.IProjectorMode;
import mffs.it.ItemMFFS;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.core.vector.Vector3;
import universalelectricity.core.vector.VectorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemProjectorMode extends ItemMFFS implements IProjectorMode
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

		int zScaleNeg = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zScalePos = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xScaleNeg = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xScalePos = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yScalePos = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yScaleNeg = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		int overAllIncrease = projector.getModuleCount(ZhuYao.itemModuleScale, projector.getModuleSlots());

		zScaleNeg += overAllIncrease;
		zScalePos += overAllIncrease;

		xScaleNeg += overAllIncrease;
		xScalePos += overAllIncrease;

		yScalePos += overAllIncrease;
		yScaleNeg += overAllIncrease;

		int zTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int zTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH)));

		int xTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST)));
		int xTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST)));

		int yTranslationPos = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(ForgeDirection.UP));
		int yTranslationNeg = projector.getModuleCount(ZhuYao.itemModuleTranslation, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		Vector3 translation = new Vector3(xTranslationPos - xTranslationNeg, yTranslationPos - yTranslationNeg, zTranslationPos - zTranslationNeg);
		Vector3 posScale = new Vector3(xScalePos, yScalePos, zScalePos);
		Vector3 negScale = new Vector3(xScaleNeg, yScaleNeg, zScaleNeg);

		this.doCalculateField(projector, blockDef, blockInterior, direction, translation, posScale, negScale);

		int horizontalRotation = projector.getModuleCount(ZhuYao.itemModuleRotation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.EAST))) - projector.getModuleCount(ZhuYao.itemModuleRotation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.WEST))) + projector.getModuleCount(ZhuYao.itemModuleRotation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.SOUTH))) - projector.getModuleCount(ZhuYao.itemModuleRotation, projector.getSlotsBasedOnDirection(VectorHelper.getOrientationFromSide(direction, ForgeDirection.NORTH)));
		int verticleRotation = projector.getModuleCount(ZhuYao.itemModuleRotation, projector.getSlotsBasedOnDirection(ForgeDirection.UP)) - projector.getModuleCount(ZhuYao.itemModuleRotation, projector.getSlotsBasedOnDirection(ForgeDirection.DOWN));

		for (Vector3 point : blockDef)
		{
			double[] pt = { point.x, point.z };
			AffineTransform.getRotateInstance(Math.toRadians(horizontalRotation), translation.x, translation.z).transform(pt, 0, pt, 0, 1);

			double[] pt2 = { point.x, point.y };
			AffineTransform.getRotateInstance(Math.toRadians(verticleRotation), translation.x, translation.y).transform(pt2, 0, pt2, 0, 1);

			point.x = pt[0];
			point.z = pt[1];
			point.y = pt2[1];
		}
	}

	public void doCalculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior, ForgeDirection direction, Vector3 translation, Vector3 posScale, Vector3 negScale)
	{

	}
}
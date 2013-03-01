package mffs.api;

import java.util.Set;

import mffs.common.block.BlockForceField.ForceFieldType;
import net.minecraft.item.Item;
import universalelectricity.core.vector.Vector3;

public interface IProjectorMode
{
	public void calculateField(IProjector projector, Set<Vector3> blockDef, Set<Vector3> blockInterior);

	public boolean supportsDistance();

	public boolean supportsStrength();

	public boolean supportsMatrix();

	public boolean supportsOption(Item item);

	public ForceFieldType getForceFieldType();

}

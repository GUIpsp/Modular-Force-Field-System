package mffs.api;

import java.util.Set;

import mffs.common.ForceFieldType;
import net.minecraft.item.Item;

public interface IProjectorMode
{
	public void calculateField(IProjector modularProjector, Set paramSet);

	public boolean supportsDistance();

	public boolean supportsStrength();

	public boolean supportsMatrix();

	public boolean supportsOption(Item item);

	public ForceFieldType getForceFieldType();

}

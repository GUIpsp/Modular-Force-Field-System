package mffs.api.modules;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import mffs.api.IProjector;

public interface IModule
{

	/**
	 * The amount of fortron this module consumes per tick.
	 * 
	 * @return
	 */
	public float getFortronCost();

	/**
	 * Called right after the projector creates a force field block.
	 * 
	 * @param projector
	 * @param position
	 */
	public void onProject(IProjector projector, Vector3 position);

	/**
	 * Called when an entity collides with a force field block.
	 * 
	 * @return True to stop the default process of entity collision.
	 */
	public boolean onCollideWithForceField(World world, int x, int y, int z, Entity entity);
}

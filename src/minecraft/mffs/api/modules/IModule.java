package mffs.api.modules;

import mffs.api.IProjector;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;

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

	public boolean canProject(IProjector projector, Vector3 position);

	/**
	 * Called when an entity collides with a force field block.
	 * 
	 * @return True to stop the default process of entity collision.
	 */
	public boolean onCollideWithForceField(World world, int x, int y, int z, Entity entity, ItemStack moduleStack);

	/**
	 * Called in this module when it is being calculated by the projector.
	 * 
	 * @return False if to prevent this position from being added to the projection que.
	 */
	boolean onCalculate(IProjector projector, Vector3 position);

}

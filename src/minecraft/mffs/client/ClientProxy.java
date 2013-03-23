package mffs.client;

import mffs.client.render.FXBeam;
import mffs.client.render.RenderForceField;
import mffs.client.render.RenderForcilliumExtractor;
import mffs.client.render.RenderFortronCapacitor;
import mffs.client.render.RenderProjector;
import mffs.common.CommonProxy;
import mffs.common.tileentity.TChouQi;
import mffs.common.tileentity.TDianRong;
import mffs.common.tileentity.TFangYingJi;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		RenderingRegistry.registerBlockHandler(new RenderForceField());
		ClientRegistry.bindTileEntitySpecialRenderer(TDianRong.class, new RenderFortronCapacitor());
		ClientRegistry.bindTileEntitySpecialRenderer(TChouQi.class, new RenderForcilliumExtractor());
		ClientRegistry.bindTileEntitySpecialRenderer(TFangYingJi.class, new RenderProjector());
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public void renderBeam(World world, Vector3 position, Vector3 target, float red, float green, float blue, int age)
	{
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FXBeam(world, position, target, red, green, blue, age));
	}
}
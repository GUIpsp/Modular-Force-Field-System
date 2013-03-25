package mffs;

import mffs.jiqi.t.TChouQi;
import mffs.jiqi.t.TDianRong;
import mffs.jiqi.t.TFangYingJi;
import mffs.quanran.FXBeam;
import mffs.quanran.RenderForcilliumExtractor;
import mffs.quanran.RenderFortronCapacitor;
import mffs.quanran.RenderProjector;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;

public class PGuKe extends PGongTong
{
	@Override
	public void init()
	{
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
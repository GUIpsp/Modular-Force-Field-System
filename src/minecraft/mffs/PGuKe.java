package mffs;

import java.lang.reflect.Constructor;

import mffs.jiqi.t.TChouQi;
import mffs.jiqi.t.TDianRong;
import mffs.jiqi.t.TFangYingJi;
import mffs.quanran.FXBeam;
import mffs.quanran.RenderForcilliumExtractor;
import mffs.quanran.RenderFortronCapacitor;
import mffs.quanran.RenderProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null)
		{
			MachineTypes machType = MachineTypes.get(tileEntity);

			try
			{
				Constructor mkGui = machType.gui.getConstructor(new Class[] { EntityPlayer.class, machType.tileEntity });
				return mkGui.newInstance(player, machType.tileEntity.cast(tileEntity));
			}
			catch (Exception e)
			{
				ZhuYao.LOGGER.severe("Failed to open GUI");
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void renderBeam(World world, Vector3 position, Vector3 target, float red, float green, float blue, int age)
	{
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(new FXBeam(world, position, target, red, green, blue, age));
	}
}
package mffs;

import java.lang.reflect.Constructor;

import mffs.gui.GuiCapacitor;
import mffs.gui.GuiDefenceStation;
import mffs.gui.GuiExtractor;
import mffs.gui.GuiProjector;
import mffs.gui.GuiSecurityStation;
import mffs.machine.tile.TileExtractor;
import mffs.machine.tile.TileCapacitor;
import mffs.machine.tile.TileProjector;
import mffs.quanran.FXBeam;
import mffs.quanran.RFangYingJi;
import mffs.quanran.RHJiQi;
import mffs.quanran.RenderForcilliumExtractor;
import mffs.quanran.RenderFortronCapacitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void preInit()
	{
		super.preInit();
		MinecraftForge.EVENT_BUS.register(SoundHelper.INSTANCE);
		BlockMachine.projector.gui = GuiProjector.class;
		BlockMachine.extractor.gui = GuiExtractor.class;
		BlockMachine.capacitor.gui = GuiCapacitor.class;
		BlockMachine.defenceStation.gui = GuiDefenceStation.class;
		BlockMachine.securityStation.gui = GuiSecurityStation.class;
	}

	@Override
	public void init()
	{
		RenderingRegistry.registerBlockHandler(new RHJiQi());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCapacitor.class, new RenderFortronCapacitor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileExtractor.class, new RenderForcilliumExtractor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileProjector.class, new RFangYingJi());
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
			BlockMachine machType = BlockMachine.get(tileEntity);

			try
			{
				Constructor mkGui = machType.gui.getConstructor(new Class[] { EntityPlayer.class, machType.tileEntity });
				return mkGui.newInstance(player, machType.tileEntity.cast(tileEntity));
			}
			catch (Exception e)
			{
				ModularForceFieldSystem.LOGGER.severe("Failed to open GUI");
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
package mffs.client;

import mffs.client.renderer.MFFSBlockRenderer;
import mffs.common.CommonProxy;
import mffs.common.ModularForceFieldSystem;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{

	@Override
	public void init()
	{
		MinecraftForgeClient.preloadTexture(ModularForceFieldSystem.BLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(ModularForceFieldSystem.ITEM_TEXTURE_FILE);
		RenderingRegistry.registerBlockHandler(new MFFSBlockRenderer());
	}

	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public boolean isClient()
	{
		return true;
	}
}
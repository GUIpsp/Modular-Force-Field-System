package mffs.client;

import mffs.client.renderer.MFFSBlockRenderer;
import mffs.client.renderer.TECapacitorRenderer;
import mffs.client.renderer.TEExtractorRenderer;
import mffs.common.CommonProxy;
import mffs.common.ModularForceFieldSystem;
import mffs.common.tileentity.TileEntityCapacitor;
import mffs.common.tileentity.TileEntityExtractor;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public void registerRenderInformation()
	{
		MinecraftForgeClient.preloadTexture(ModularForceFieldSystem.BLOCK_TEXTURE_FILE);
		MinecraftForgeClient.preloadTexture(ModularForceFieldSystem.ITEM_TEXTURE_FILE);
	}

	public void registerTileEntitySpecialRenderer()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCapacitor.class, new TECapacitorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityExtractor.class, new TEExtractorRenderer());

		RenderingRegistry.registerBlockHandler(new MFFSBlockRenderer());
	}

	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}

	public boolean isClient()
	{
		return true;
	}
}
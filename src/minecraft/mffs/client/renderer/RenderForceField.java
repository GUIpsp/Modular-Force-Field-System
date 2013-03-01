package mffs.client.renderer;

import mffs.common.ForceFieldType;
import mffs.common.ModularForceFieldSystem;
import mffs.common.tileentity.TileEntityForceField;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderForceField implements ISimpleBlockRenderingHandler
{
	public static int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (block == ModularForceFieldSystem.blockForceField)
		{
			if (world.getBlockMetadata(x, y, z) == ForceFieldType.Camouflage.ordinal())
			{
				TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
				
				if (tileEntity instanceof TileEntityForceField)
				{
					if (((TileEntityForceField) tileEntity).getForcefieldCamoblockID() != -1)
					{
						if ((ForceFieldType.Camouflage.ordinal() == ((TileEntityForceField) tileEntity).getForcefieldCamoblockmeta()) && (((TileEntityForceField) tileEntity).getForcefieldCamoblockID() != 327) && (((TileEntityForceField) tileEntity).getForcefieldCamoblockID() != 326))
						{
							Block customblock = Block.blocksList[((TileEntityForceField) tileEntity).getForcefieldCamoblockID()];
							if (customblock != null)
							{
								ForgeHooksClient.bindTexture(customblock.getTextureFile(), 1);
								renderer.renderBlockByRenderType(customblock, x, y, z);
								return true;
							}
						}
					}
					
					if (((TileEntityForceField) tileEntity).getTextureFile() != null)
					{
						ForgeHooksClient.bindTexture(((TileEntityForceField) tileEntity).getTextureFile(), 0);
						renderer.renderStandardBlock(block, x, y, z);
						return true;
					}

				}

				ForgeHooksClient.bindTexture(block.getTextureFile(), 0);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else
			{
				ForgeHooksClient.bindTexture(ModularForceFieldSystem.BLOCK_TEXTURE_FILE, 0);
				renderer.renderStandardBlock(block, x, y, z);
			}

			return true;
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}

	@Override
	public int getRenderId()
	{
		return RenderForceField.RENDER_ID;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
	}
}
package mffs.client.renderer;

import mffs.common.ForceFieldTyps;
import mffs.common.ModularForceFieldSystem;
import mffs.common.tileentity.TileEntityForceField;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class MFFSBlockRenderer implements ISimpleBlockRenderingHandler {

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		if (block == ModularForceFieldSystem.blockForceField) {
			if (world.getBlockMetadata(x, y, z) == ForceFieldTyps.Camouflage
					.ordinal()) {
				TileEntity te = world.getBlockTileEntity(x, y, z);
				if ((te instanceof TileEntityForceField)) {
					if (((TileEntityForceField) te).getForcefieldCamoblockid() != -1) {
						if ((ForceFieldTyps.Camouflage.ordinal() == ((TileEntityForceField) te)
								.getForcefieldCamoblockmeta())
								&& (((TileEntityForceField) te)
										.getForcefieldCamoblockid() != 327)
								&& (((TileEntityForceField) te)
										.getForcefieldCamoblockid() != 326)) {
							Block customblock = Block.blocksList[((TileEntityForceField) te)
									.getForcefieldCamoblockid()];
							if (customblock != null) {
								ForgeHooksClient.bindTexture(
										customblock.getTextureFile(), 1);
								renderer.renderBlockByRenderType(customblock,
										x, y, z);
								return true;
							}
						}
					}
					if (((TileEntityForceField) te).getTexturefile() != null) {
						ForgeHooksClient
								.bindTexture(((TileEntityForceField) te)
										.getTexturefile(), 0);
						renderer.renderStandardBlock(block, x, y, z);
						return true;
					}

				}

				ForgeHooksClient.bindTexture("/terrain.png", 0);
				renderer.renderStandardBlock(block, x, y, z);
			} else {
				ForgeHooksClient.bindTexture(
						ModularForceFieldSystem.BLOCK_TEXTURE_FILE, 0);
				renderer.renderStandardBlock(block, x, y, z);
			}

			return true;
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	@Override
	public int getRenderId() {
		return ModularForceFieldSystem.RENDER_ID;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
	}
}
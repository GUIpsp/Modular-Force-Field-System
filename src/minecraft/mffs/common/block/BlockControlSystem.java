package mffs.common.block;

import mffs.common.tileentity.TileEntityControlSystem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockControlSystem extends BlockMFFS
{

    public BlockControlSystem(int i)
    {
        super(i, "controlSystem");
        this.blockIndexInTexture = 3 * 16;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityControlSystem();
    }
}
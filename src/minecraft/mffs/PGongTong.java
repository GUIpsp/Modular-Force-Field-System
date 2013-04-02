package mffs;

import java.lang.reflect.Constructor;

import mffs.jiqi.t.TAnQuan;
import mffs.jiqi.t.TChouQi;
import mffs.jiqi.t.TDianRong;
import mffs.jiqi.t.TFangYingJi;
import mffs.jiqi.t.TFangYu;
import mffs.rongqi.CAnQuan;
import mffs.rongqi.CChouQi;
import mffs.rongqi.CDianRong;
import mffs.rongqi.CFangYingJi;
import mffs.rongqi.CFangYu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import universalelectricity.core.vector.Vector3;
import cpw.mods.fml.common.network.IGuiHandler;

public class PGongTong implements IGuiHandler
{
	public void preInit()
	{
		JiQi.fangYingJi = new JiQi(0, ZhuYao.bFangYingJi, TFangYingJi.class, CFangYingJi.class);
		JiQi.chouQi = new JiQi(1, ZhuYao.bChouQi, TChouQi.class, CChouQi.class);
		JiQi.dianRong = new JiQi(2, ZhuYao.bDianRong, TDianRong.class, CDianRong.class);
		JiQi.fangYu = new JiQi(3, ZhuYao.bFangYu, TFangYu.class, CFangYu.class);
		JiQi.anQuan = new JiQi(4, ZhuYao.bAnQuan, TAnQuan.class, CAnQuan.class);
	}

	public void init()
	{
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity != null)
		{
			JiQi machineType = JiQi.get(tileEntity);

			try
			{
				Constructor mkGui = machineType.container.getConstructor(EntityPlayer.class, machineType.tileEntity);
				return mkGui.newInstance(player, machineType.tileEntity.cast(tileEntity));
			}
			catch (Exception e)
			{
				ZhuYao.LOGGER.severe("Failed to open container: ");
				e.printStackTrace();
			}
		}

		return null;
	}

	public World getClientWorld()
	{
		return null;
	}

	public void renderBeam(World world, Vector3 position, Vector3 target, float red, float green, float blue, int age)
	{

	}

}
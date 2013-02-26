package mffs.network.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;

import mffs.api.PointXYZ;
import mffs.common.ForceFieldBlockStack;
import mffs.common.FrequencyGrid;
import mffs.common.ModularForceFieldSystem;
import mffs.common.WorldMap;
import mffs.common.tileentity.TileEntityCapacitor;
import mffs.common.tileentity.TileEntityControlSystem;
import mffs.common.tileentity.TileEntityConverter;
import mffs.common.tileentity.TileEntityDefenseStation;
import mffs.common.tileentity.TileEntityExtractor;
import mffs.common.tileentity.TileEntityForceField;
import mffs.common.tileentity.TileEntityMFFS;
import mffs.common.tileentity.TileEntityProjector;
import mffs.common.tileentity.TileEntitySecStorage;
import mffs.common.tileentity.TileEntitySecurityStation;
import mffs.network.INetworkHandlerEventListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet103SetSlot;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class NetworkHandlerServer implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
		int x = dat.readInt();
		int y = dat.readInt();
		int z = dat.readInt();
		int typ = dat.readInt();

		switch (typ)
		{
			case 2:
				int dimension = dat.readInt();
				String daten = dat.readUTF();
				World serverworld = DimensionManager.getWorld(dimension);
				if (serverworld != null)
				{
					TileEntity servertileEntity = serverworld.getBlockTileEntity(x, y, z);

					if (servertileEntity != null)
					{
						for (String varname : daten.split("/"))
						{
							updateTileEntityField(servertileEntity, varname);
						}
					}

				}

				break;
			case 3:
				int dimension2 = dat.readInt();
				int key = dat.readInt();
				String value = dat.readUTF();

				World serverworld2 = DimensionManager.getWorld(dimension2);
				TileEntity servertileEntity2 = serverworld2.getBlockTileEntity(x, y, z);

				if ((servertileEntity2 instanceof INetworkHandlerEventListener))
				{
					((INetworkHandlerEventListener) servertileEntity2).onNetworkHandlerEvent(key, value);
				}
				break;
			case 10:
				int Dim = dat.readInt();
				String Corrdinsaten = dat.readUTF();

				World worldserver = DimensionManager.getWorld(Dim);

				if (worldserver != null)
				{
					for (String varname : Corrdinsaten.split("#"))
					{
						if (!varname.isEmpty())
						{
							String[] corr = varname.split("/");
							TileEntity servertileEntity = worldserver.getBlockTileEntity(Integer.parseInt(corr[2].trim()), Integer.parseInt(corr[1].trim()), Integer.parseInt(corr[0].trim()));
							if ((servertileEntity instanceof TileEntityForceField))
							{
								ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(worldserver).getForceFieldStackMap(Integer.valueOf(new PointXYZ(servertileEntity.xCoord, servertileEntity.yCoord, servertileEntity.zCoord, worldserver).hashCode()));

								if (ffworldmap != null)
								{
									if (!ffworldmap.isEmpty())
									{
										TileEntityProjector projector = (TileEntityProjector) FrequencyGrid.getWorldMap(worldserver).getProjector().get(Integer.valueOf(ffworldmap.getProjectorID()));

										if (projector != null)
										{
											ForceFieldServerUpdatehandler.getWorldMap(worldserver).addto(servertileEntity.xCoord, servertileEntity.yCoord, servertileEntity.zCoord, Dim, projector.xCoord, projector.yCoord, projector.zCoord);
										}
									}
								}
							}
						}
					}
				}
				break;
		}
	}

	public static void syncClientPlayerinventorySlot(EntityPlayer player, Slot slot, ItemStack itemstack)
	{
		Packet103SetSlot pkt = new Packet103SetSlot();
		pkt.windowId = 0;
		pkt.itemSlot = slot.slotNumber;
		pkt.myItemStack = itemstack;
		PacketDispatcher.sendPacketToPlayer(pkt, (Player) player);
	}

	public static void updateTileEntityField(TileEntity tileEntity, String varname)
	{
		if (tileEntity != null)
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream(140);
			DataOutputStream dos = new DataOutputStream(bos);
			int x = tileEntity.xCoord;
			int y = tileEntity.yCoord;
			int z = tileEntity.zCoord;
			int typ = 1;
			try
			{
				dos.writeInt(x);
				dos.writeInt(y);
				dos.writeInt(z);
				dos.writeInt(typ);
				dos.writeUTF(varname);
			}
			catch (Exception e)
			{
			}

			try
			{
				Field f = ReflectionHelper.findField(tileEntity.getClass(), new String[] { varname });
				f.get(tileEntity);
				dos.writeUTF(String.valueOf(f.get(tileEntity)));
			}
			catch (Exception e)
			{
				ModularForceFieldSystem.LOGGER.severe("Failed to get variable for packet '" + varname + "' in TileEntity: " + tileEntity.getClass().getSimpleName());
			}

			Packet250CustomPayload pkt = new Packet250CustomPayload();
			pkt.channel = "MFFS";
			pkt.data = bos.toByteArray();
			pkt.length = bos.size();
			pkt.isChunkDataPacket = true;

			PacketDispatcher.sendPacketToAllAround(x, y, z, 80.0D, tileEntity.worldObj.provider.dimensionId, pkt);
		}
	}
}
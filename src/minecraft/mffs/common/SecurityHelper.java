package mffs.common;

import mffs.common.tileentity.TileEntityFortronCapacitor;
import mffs.common.tileentity.TileEntityControlSystem;
import mffs.common.tileentity.TileEntityConverter;
import mffs.common.tileentity.TileEntityDefenseStation;
import mffs.common.tileentity.TileEntityForcilliumExtractor;
import mffs.common.tileentity.TileEntityProjector;
import mffs.common.tileentity.TileEntitySecStorage;
import mffs.common.tileentity.TileEntitySecurityStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SecurityHelper
{

	public static boolean isAccessGranted(TileEntity tileEntity, EntityPlayer entityplayer, World world, SecurityRight right)
	{
		return isAccessGranted(tileEntity, entityplayer, world, right, false);
	}

	public static boolean isAccessGranted(TileEntity tileEntity, EntityPlayer entityplayer, World world, SecurityRight right, boolean suppresswarning)
	{
		if ((tileEntity instanceof TileEntitySecStorage))
		{
			TileEntitySecurityStation sec = ((TileEntitySecStorage) tileEntity).getLinkedSecurityStation();

			if (sec != null)
			{
				if (sec.isAccessGranted(entityplayer.username, right))
					return true;

				if (!suppresswarning)
					entityplayer.sendChatToPlayer("[Field Security] Fail: access denied");

				return false;
			}

			if (world.isRemote)
				return false;

			return true;
		}

		if ((tileEntity instanceof TileEntityControlSystem))
		{
			TileEntitySecurityStation sec = ((TileEntityControlSystem) tileEntity).getLinkedSecurityStation();
			if (sec != null)
			{
				if (sec.isAccessGranted(entityplayer.username, right))
				{
					return true;
				}

				if (!suppresswarning)
				{
					entityplayer.sendChatToPlayer("[Field Security] Fail: access denied");
				}
				return false;
			}

			if (world.isRemote)
				return false;

			return true;
		}

		if ((tileEntity instanceof TileEntitySecurityStation))
		{
			if (!((TileEntitySecurityStation) tileEntity).isAccessGranted(entityplayer.username, right))
			{
				if (!suppresswarning)
					Functions.ChattoPlayer(entityplayer, "[Field Security] Fail: access denied");

				return false;
			}

		}

		if ((tileEntity instanceof TileEntityConverter))
		{
			TileEntitySecurityStation sec = ((TileEntityConverter) tileEntity).getLinkedSecurityStation();
			if (sec != null)
			{
				if (sec.isAccessGranted(entityplayer.username, right))
					return true;

				if (!suppresswarning)
					Functions.ChattoPlayer(entityplayer, "[Field Security] Fail: access denied");

				return false;
			}

			return true;
		}

		if ((tileEntity instanceof TileEntityFortronCapacitor))
		{
			TileEntitySecurityStation sec = ((TileEntityFortronCapacitor) tileEntity).getLinkedSecurityStation();
			if (sec != null)
			{
				if (sec.isAccessGranted(entityplayer.username, right))
					return true;

				if (!suppresswarning)
					Functions.ChattoPlayer(entityplayer, "[Field Security] Fail: access denied");

				return false;
			}

			return true;
		}

		if ((tileEntity instanceof TileEntityForcilliumExtractor))
		{
			TileEntitySecurityStation sec = ((TileEntityForcilliumExtractor) tileEntity).getLinkedSecurityStation();
			if (sec != null)
			{
				if (sec.isAccessGranted(entityplayer.username, right))
					return true;

				if (!suppresswarning)
					Functions.ChattoPlayer(entityplayer, "[Field Security] Fail: access denied");

				return false;
			}

			return true;
		}

		if ((tileEntity instanceof TileEntityDefenseStation))
		{
			TileEntitySecurityStation sec = ((TileEntityDefenseStation) tileEntity).getLinkedSecurityStation();

			if (sec != null)
			{
				if (sec.isAccessGranted(entityplayer.username, right))
					return true;

				if (!suppresswarning)
					Functions.ChattoPlayer(entityplayer, "[Field Security] Fail: access denied");

				return false;
			}

			return true;
		}

		if ((tileEntity instanceof TileEntityProjector))
		{
			switch (((TileEntityProjector) tileEntity).getAccessType())
			{
				case 2:
				//	TileEntityCapacitor cap = (TileEntityCapacitor) FrequencyGrid.getWorldMap(world).getCapacitor().get(Integer.valueOf(((TileEntityProjector) tileEntity).getPowerSourceID()));
					/*if (cap != null)
					{
						TileEntitySecurityStation sec = cap.getLinkedSecurityStation();
						if (sec != null)
						{
							if (sec.isAccessGranted(entityplayer.username, right))
								return true;

							if (!suppresswarning)
								Functions.ChattoPlayer(entityplayer, "[Field Security] Fail: access denied");

							return false;
						}
					}*/

					break;
				case 3:
					TileEntitySecurityStation sec = ((TileEntityProjector) tileEntity).getLinkedSecurityStation();
					if (sec != null)
					{
						if (sec.isAccessGranted(entityplayer.username, right))
							return true;

						if (!suppresswarning)
							Functions.ChattoPlayer(entityplayer, "[Field Security] Fail: access denied");

						return false;
					}

					break;
			}

			return true;
		}

		return true;
	}
}
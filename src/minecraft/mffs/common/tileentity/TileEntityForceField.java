package mffs.common.tileentity;

import java.util.Random;

import mffs.api.PointXYZ;
import mffs.common.ForceFieldBlockStack;
import mffs.common.FrequencyGridOld;
import mffs.common.WorldMap;
import net.minecraft.item.ItemStack;
import universalelectricity.prefab.tile.TileEntityAdvanced;

public class TileEntityForceField extends TileEntityAdvanced
{

	private Random random = new Random();
	private String textureFile;
	private int camoID;
	private int camoMeta;

	private int[] texturID = { -76, -76, -76, -76, -76, -76 };

	public int getForcefieldCamoblockmeta()
	{
		return this.camoMeta;
	}

	public void setForcefieldCamoMeta(int forcefieldCamoblockmeta)
	{
		this.camoMeta = forcefieldCamoblockmeta;
	}

	public int getForcefieldCamoblockID()
	{
		return this.camoID;
	}

	public void setForcefieldCamoID(int forcefieldCamoblockid)
	{
		this.camoID = forcefieldCamoblockid;
	}

	public String getTextureFile()
	{
		return this.textureFile;
	}

	public void setTexturefile(String texturfile)
	{
		this.textureFile = texturfile;
	}

	public int[] getTexturid()
	{
		return this.texturID;
	}

	public int getTexturID(int side)
	{
		return this.texturID[side];
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote)
		{
			if (this.ticks % 20 == 0)
			{
				if ((this.texturID[0] == -76) || (this.textureFile == null))
				{
					this.updateTexture();
				}
			}

		}
		else
		{
			if (this.ticks % 40 == 0)
			{
				if ((this.texturID[0] == -76) || (this.textureFile == null))
				{
					// ForceFieldClientUpdatehandler.addto(this.xCoord, this.yCoord, this.zCoord);
				}
			}
		}
	}

	public void setTextureID(String remotetextu)
	{
		String[] textur = remotetextu.split("/");

		this.texturID[0] = Integer.parseInt(textur[0].trim());
		this.texturID[1] = Integer.parseInt(textur[1].trim());
		this.texturID[2] = Integer.parseInt(textur[2].trim());
		this.texturID[3] = Integer.parseInt(textur[3].trim());
		this.texturID[4] = Integer.parseInt(textur[4].trim());
		this.texturID[5] = Integer.parseInt(textur[5].trim());

		this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	public void setTextureID(String texturid, TileEntityProjector proj)
	{
		try
		{
			if (!texturid.equalsIgnoreCase(this.texturID[0] + "/" + this.texturID[1] + "/" + this.texturID[2] + "/" + this.texturID[3] + "/" + this.texturID[4] + "/" + this.texturID[5]))
			{
				String[] textur = texturid.split("/");
				this.texturID[0] = Integer.parseInt(textur[0].trim());
				this.texturID[1] = Integer.parseInt(textur[1].trim());
				this.texturID[2] = Integer.parseInt(textur[2].trim());
				this.texturID[3] = Integer.parseInt(textur[3].trim());
				this.texturID[4] = Integer.parseInt(textur[4].trim());
				this.texturID[5] = Integer.parseInt(textur[5].trim());

				// ForceFieldServerUpdatehandler.getWorldMap(this.worldObj).addto(this.xCoord,
				// this.yCoord, this.zCoord, this.worldObj.provider.dimensionId, proj.xCoord,
				// proj.yCoord, proj.zCoord);
			}
		}
		catch (Exception ex)
		{
		}
	}

	public void updateTexture()
	{
		if (!this.worldObj.isRemote)
		{
			ForceFieldBlockStack ffworldmap = WorldMap.getForceFieldWorld(this.worldObj).getForceFieldStackMap(Integer.valueOf(new PointXYZ(this.xCoord, this.yCoord, this.zCoord, this.worldObj).hashCode()));

			if (ffworldmap != null)
			{
				if (!ffworldmap.isEmpty())
				{
					TileEntityProjector projector = (TileEntityProjector) FrequencyGridOld.getWorldMap(this.worldObj).getProjector().get(Integer.valueOf(ffworldmap.getProjectorID()));

					if (projector != null)
					{
						setTextureID(projector.getForceFieldTextureID(), projector);
						setTexturefile(projector.getForceFieldTextureFile());
						setForcefieldCamoID(projector.getForceFieldCamoblockID());
						setForcefieldCamoMeta(projector.getForceFieldCamoblockMeta());
					}
				}
			}
		}
	}

	public ItemStack[] getContents()
	{
		return null;
	}

	public void setMaxStackSize(int arg0)
	{
	}
}
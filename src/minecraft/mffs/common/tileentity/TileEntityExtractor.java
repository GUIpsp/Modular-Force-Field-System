package mffs.common.tileentity;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import mffs.api.IPowerLinkItem;
import mffs.common.FrequencyGrid;
import mffs.common.MFFSConfiguration;
import mffs.common.ModularForceFieldSystem;
import mffs.common.container.ContainerForceEnergyExtractor;
import mffs.common.item.ItemForcicium;
import mffs.common.item.ItemForcicumCell;
import mffs.common.upgrade.ItemUpgradeCapacity;
import mffs.common.upgrade.ItemUpgradeBooster;
import mffs.network.server.NetworkHandlerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import universalelectricity.core.electricity.ElectricityConnections;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.vector.Vector3;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;

public class TileEntityExtractor extends TileEntityForcePowerMachine implements IPowerReceptor, IEnergySink
{

    private ItemStack[] inventory;
    private int workmode = 0;
    protected int WorkEnergy;
    protected int MaxWorkEnergy;
    private int forceEnergyBuffer;
    private int maxForceEnergyBuffer;
    private int workCycle;
    private int workTicker;
    private int workDone;
    private int maxWorkCycle;
    private int capacity;
    private IPowerProvider powerProvider;
    private boolean addedToEnergyNet;

    public TileEntityExtractor()
    {
        this.inventory = new ItemStack[5];
        this.WorkEnergy = 0;
        this.MaxWorkEnergy = 4000;
        this.forceEnergyBuffer = 0;
        this.maxForceEnergyBuffer = 1000000;
        this.workCycle = 0;
        this.workTicker = 20;
        this.maxWorkCycle = 125;
        this.capacity = 0;
        this.addedToEnergyNet = false;

        if (MFFSConfiguration.MODULE_BUILDCRAFT)
        {
            this.powerProvider = PowerFramework.currentFramework.createPowerProvider();
            this.powerProvider.configure(10, 2, (int) (getMaxWorkEnergy() / 2.5D), (int) (getMaxWorkEnergy() / 2.5D), (int) (getMaxWorkEnergy() / 2.5D));
        }
    }

    @Override
    public void setSide(int i)
    {
        super.setSide(i);
        setUEwireConnection();
    }

    public int getCapacity()
    {
        return this.capacity;
    }

    public void setCapacity(int Capacity)
    {
        if (this.capacity != Capacity)
        {
            this.capacity = Capacity;
            NetworkHandlerServer.updateTileEntityField(this, "capacity");
        }
    }

    public int getMaxWorkCycle()
    {
        return this.maxWorkCycle;
    }

    public void setMaxWorkCycle(int maxWorkCycle)
    {
        this.maxWorkCycle = maxWorkCycle;
    }

    public int getWorkDone()
    {
        return this.workDone;
    }

    public void setWorkDone(int workDone)
    {
        if (this.workDone != workDone)
        {
            this.workDone = workDone;
            NetworkHandlerServer.updateTileEntityField(this, "workDone");
        }
    }

    public int getWorkTicker()
    {
        return this.workTicker;
    }

    public void setWorkTicker(int workTicker)
    {
        this.workTicker = workTicker;
    }

    public int getMaxForceEnergyBuffer()
    {
        return this.maxForceEnergyBuffer;
    }

    public void setMaxForceEnergyBuffer(int maxForceEnergyBuffer)
    {
        this.maxForceEnergyBuffer = maxForceEnergyBuffer;
    }

    public int getForceEnergybuffer()
    {
        return this.forceEnergyBuffer;
    }

    public void setForceEnergyBuffer(int forceEnergyBuffer)
    {
        this.forceEnergyBuffer = forceEnergyBuffer;
    }

    public void setWorkCylce(int i)
    {
        if (this.workCycle != i)
        {
            this.workCycle = i;
            NetworkHandlerServer.updateTileEntityField(this, "workCycle");
        }
    }

    public int getWorkCycle()
    {
        return this.workCycle;
    }

    public int getWorkEnergy()
    {
        return this.WorkEnergy;
    }

    public void setWorkEnergy(int workEnergy)
    {
        this.WorkEnergy = workEnergy;
    }

    public int getMaxWorkEnergy()
    {
        return this.MaxWorkEnergy;
    }

    public void setMaxWorkEnergy(int maxWorkEnergy)
    {
        this.MaxWorkEnergy = maxWorkEnergy;
    }

    @Override
    public void dropPlugins()
    {
        for (int a = 0; a < this.inventory.length; a++)
        {
            dropPlugins(a, this);
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        if (this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistance(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
    }

    public void checkSlots(boolean init)
    {
        if (getStackInSlot(2) != null)
        {
            if (getStackInSlot(2).getItem() == ModularForceFieldSystem.itemUpgradeCapacity)
            {
                setMaxForceEnergyBuffer(1000000 + getStackInSlot(2).stackSize * 100000);
            } else
            {
                setMaxForceEnergyBuffer(1000000);
            }
        } else
        {
            setMaxForceEnergyBuffer(1000000);
        }

        if (getStackInSlot(3) != null)
        {
            if (getStackInSlot(3).getItem() == ModularForceFieldSystem.itemUpgradeBoost)
            {
                setWorkTicker(20 - getStackInSlot(3).stackSize);
            } else
            {
                setWorkTicker(20);
            }
        } else
        {
            setWorkTicker(20);
        }

        if (getStackInSlot(4) != null)
        {
            if (getStackInSlot(4).getItem() == ModularForceFieldSystem.itemForcicumCell)
            {
                this.workmode = 1;
                setMaxWorkEnergy(200000);
            }
        } else
        {
            this.workmode = 0;
            setMaxWorkEnergy(4000);
        }
    }

    private boolean hasPowerToConvert()
    {
        if (this.WorkEnergy >= this.MaxWorkEnergy - 1)
        {
            setWorkEnergy(0);
            return true;
        }
        return false;
    }

    private boolean hasFreeForceEnergyStorage()
    {
        if (this.maxForceEnergyBuffer > this.forceEnergyBuffer)
        {
            return true;
        }
        return false;
    }

    private boolean hasStuffToConvert()
    {
        if (this.workCycle > 0)
            return true;

        if (MFFSConfiguration.adventureMap)
        {
            setMaxWorkCycle(MFFSConfiguration.forceciumCellWorkCycle);
            setWorkCylce(getMaxWorkCycle());
            return true;
        }

        if (getStackInSlot(0) != null)
        {
            if (getStackInSlot(0).getItem() == ModularForceFieldSystem.itemForcicium)
            {
                setMaxWorkCycle(MFFSConfiguration.ForceciumWorkCylce);
                setWorkCylce(getMaxWorkCycle());
                decrStackSize(0, 1);
                return true;
            }

            if ((getStackInSlot(0).getItem() == ModularForceFieldSystem.itemForcicumCell) && (((ItemForcicumCell) getStackInSlot(0).getItem()).useForcecium(1, getStackInSlot(0))))
            {
                setMaxWorkCycle(MFFSConfiguration.forceciumCellWorkCycle);
                setWorkCylce(getMaxWorkCycle());
                return true;
            }

        }

        return false;
    }

    public void transferForceEnergy()
    {
        if (getForceEnergybuffer() > 0)
        {
            if (hasPowerSource())
            {
                int powerTransferRate = getMaximumPower() / 120;
                int freeAmount = (int) (getMaximumPower() - getForcePower());

                if (getForceEnergybuffer() > freeAmount)
                {
                    if (freeAmount > powerTransferRate)
                    {
                        emitPower(powerTransferRate, false);
                        setForceEnergyBuffer(getForceEnergybuffer() - powerTransferRate);
                    } else
                    {
                        emitPower(freeAmount, false);
                        setForceEnergyBuffer(getForceEnergybuffer() - freeAmount);
                    }
                } else if (freeAmount > getForceEnergybuffer())
                {
                    emitPower(getForceEnergybuffer(), false);
                    setForceEnergyBuffer(getForceEnergybuffer() - getForceEnergybuffer());
                } else
                {
                    emitPower(freeAmount, false);
                    setForceEnergyBuffer(getForceEnergybuffer() - freeAmount);
                }
            }
        }
    }

    @Override
    public short getMaxSwitchModi()
    {
        return 3;
    }

    @Override
    public short getMinSwitchModi()
    {
        return 1;
    }

    @Override
    public void updateEntity()
    {
        if (!this.worldObj.isRemote)
        {
            if (this.init)
            {
                checkSlots(true);
                setUEwireConnection();
            }

            if ((!this.addedToEnergyNet) && (MFFSConfiguration.MODULE_IC2))
            {
                MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
                this.addedToEnergyNet = true;
            }

            if ((getSwitchModi() == 1) && (!getSwitchValue()) && (isRedstoneSignal()))
            {
                toggelSwitchValue();
            }
            if ((getSwitchModi() == 1) && (getSwitchValue()) && (!isRedstoneSignal()))
            {
                toggelSwitchValue();
            }

            if ((!isActive()) && (getSwitchValue()))
            {
                setActive(true);
            }
            if ((isActive()) && (!getSwitchValue()))
            {
                setActive(false);
            }

            if (isActive())
            {
                if (MFFSConfiguration.MODULE_BUILDCRAFT)
                {
                    convertMJtoWorkEnergy();
                }
                if (MFFSConfiguration.MODULE_UE)
                {
                    convertUEtoWorkEnergy();
                }
            }

            if (getTicker() >= getWorkTicker())
            {
                checkSlots(false);

                if ((this.workmode == 0) && (isActive()))
                {
                    if (getWorkDone() != getWorkEnergy() * 100 / getMaxWorkEnergy())
                    {
                        setWorkDone(getWorkEnergy() * 100 / getMaxWorkEnergy());
                    }
                    if (getWorkDone() > 100)
                    {
                        setWorkDone(100);
                    }

                    if (getCapacity() != getForceEnergybuffer() * 100 / getMaxForceEnergyBuffer())
                    {
                        setCapacity(getForceEnergybuffer() * 100 / getMaxForceEnergyBuffer());
                    }

                    if ((hasFreeForceEnergyStorage()) && (hasStuffToConvert()))
                    {
                        if (hasPowerToConvert())
                        {
                            setWorkCylce(getWorkCycle() - 1);
                            setForceEnergyBuffer(getForceEnergybuffer() + MFFSConfiguration.ExtractorPassForceEnergyGenerate);
                        }

                    }

                    transferForceEnergy();

                    setTicker((short) 0);
                }

                if ((this.workmode == 1) && (isActive()))
                {
                    if (getWorkDone() != getWorkEnergy() * 100 / getMaxWorkEnergy())
                    {
                        setWorkDone(getWorkEnergy() * 100 / getMaxWorkEnergy());
                    }
                    if (((ItemForcicumCell) getStackInSlot(4).getItem()).getForceciumlevel(getStackInSlot(4)) < ((ItemForcicumCell) getStackInSlot(4).getItem()).getMaxForceciumlevel())
                    {
                        if ((hasPowerToConvert()) && (isActive()))
                        {
                            ((ItemForcicumCell) getStackInSlot(4).getItem()).setForceciumlevel(getStackInSlot(4), ((ItemForcicumCell) getStackInSlot(4).getItem()).getForceciumlevel(getStackInSlot(4)) + 1);
                        }
                    }

                    setTicker((short) 0);
                }
            }

            setTicker((short) (getTicker() + 1));
        }
        super.updateEntity();
    }

    @Override
    public Container getContainer(InventoryPlayer inventoryplayer)
    {
        return new ContainerForceEnergyExtractor(inventoryplayer.player, this);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);

        this.forceEnergyBuffer = nbttagcompound.getInteger("forceEnergyBuffer");
        this.WorkEnergy = nbttagcompound.getInteger("workEnergy");
        this.workCycle = nbttagcompound.getInteger("workCycle");

        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        this.inventory = new ItemStack[getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);

            byte byte0 = nbttagcompound1.getByte("Slot");
            if ((byte0 >= 0) && (byte0 < this.inventory.length))
            {
                this.inventory[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);

        nbttagcompound.setInteger("workCycle", this.workCycle);
        nbttagcompound.setInteger("workEnergy", this.WorkEnergy);
        nbttagcompound.setInteger("forceEnergyBuffer", this.forceEnergyBuffer);

        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.inventory.length; i++)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    @Override
    public ItemStack getStackInSlot(int i)
    {
        return this.inventory[i];
    }

    @Override
    public String getInvName()
    {
        return "Extractor";
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventory.length;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.inventory[i] = itemstack;
        if ((itemstack != null) && (itemstack.stackSize > getInventoryStackLimit()))
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.inventory[i] != null)
        {
            if (this.inventory[i].stackSize <= j)
            {
                ItemStack itemstack = this.inventory[i];
                this.inventory[i] = null;
                return itemstack;
            }
            ItemStack itemstack1 = this.inventory[i].splitStack(j);
            if (this.inventory[i].stackSize == 0)
            {
                this.inventory[i] = null;
            }
            return itemstack1;
        }
        return null;
    }

    @Override
    public int getStartInventorySide(ForgeDirection side)
    {
        return 0;
    }

    @Override
    public int getSizeInventorySide(ForgeDirection side)
    {
        return 1;
    }

    @Override
    public List getFieldsForUpdate()
    {
        List NetworkedFields = new LinkedList();
        NetworkedFields.clear();

        NetworkedFields.addAll(super.getFieldsForUpdate());
        NetworkedFields.add("capacity");
        NetworkedFields.add("workCycle");
        NetworkedFields.add("workEnergy");
        NetworkedFields.add("workDone");

        return NetworkedFields;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack, int slot)
    {
        switch (slot)
        {
            case 0:
                if ((((itemStack.getItem() instanceof ItemForcicium)) || ((itemStack.getItem() instanceof ItemForcicumCell))) && (getStackInSlot(4) == null))
                    return true;

                break;
            case 1:
                if ((itemStack.getItem() instanceof IPowerLinkItem))
                    return true;

                break;
            case 2:
                if ((itemStack.getItem() instanceof ItemUpgradeCapacity))
                    return true;

                break;
            case 3:
                if ((itemStack.getItem() instanceof ItemUpgradeBooster))
                    return true;

                break;
            case 4:
                if (((itemStack.getItem() instanceof ItemForcicumCell)) && (getStackInSlot(0) == null))
                    return true;

                break;
        }
        return false;
    }

    @Override
    public int getSlotStackLimit(int slot)
    {
        switch (slot)
        {
            case 0:
                return 64;
            case 1:
                break;
            case 2:
                return 9;
            case 3:
                return 19;
            case 4:
                break;
        }
        return 1;
    }

    @Override
    public int demandsEnergy()
    {
        if (!isActive())
        {
            return 0;
        }
        return getMaxWorkEnergy() - getWorkEnergy();
    }

    @Override
    public int injectEnergy(Direction directionFrom, int amount)
    {
        int freespace = getMaxWorkEnergy() - getWorkEnergy();

        if (freespace >= amount)
        {
            setWorkEnergy(getWorkEnergy() + amount);
            return 0;
        }

        setWorkEnergy(getMaxWorkEnergy());
        return amount - freespace;
    }

    @Override
    public void invalidate()
    {
        if (this.addedToEnergyNet)
        {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            this.addedToEnergyNet = false;
        }

        FrequencyGrid.getWorldMap(this.worldObj).getExtractor().remove(Integer.valueOf(getDeviceID()));

        super.invalidate();
    }

    @Override
    public boolean isAddedToEnergyNet()
    {
        return this.addedToEnergyNet;
    }

    @Override
    public boolean acceptsEnergyFrom(TileEntity tileentity, Direction direction)
    {
        return true;
    }

    public void convertMJtoWorkEnergy()
    {
        if (getWorkEnergy() < getMaxWorkEnergy())
        {
            float use = this.powerProvider.useEnergy(1.0F, (float) (getMaxWorkEnergy() - getWorkEnergy() / 2.5D), true);

            if (getWorkEnergy() + use * 2.5D > getMaxWorkEnergy())
            {
                setWorkEnergy(getMaxWorkEnergy());
            } else
            {
                setWorkEnergy((int) (getWorkEnergy() + use * 2.5D));
            }
        }
    }

    public void setUEwireConnection()
    {
        if (MFFSConfiguration.MODULE_UE)
        {
            ElectricityConnections.registerConnector(this, EnumSet.of(ForgeDirection.getOrientation(getFacing()).getOpposite()));
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord));
        }
    }

    @Override
    public void setPowerProvider(IPowerProvider provider)
    {
        this.powerProvider = provider;
    }

    @Override
    public IPowerProvider getPowerProvider()
    {
        return this.powerProvider;
    }

    @Override
    public void doWork()
    {
    }

    @Override
    public int powerRequest()
    {
        double workEnergyinMJ = getWorkEnergy() / 2.5D;
        double MaxWorkEnergyinMj = getMaxWorkEnergy() / 2.5D;

        return (int) Math.round(MaxWorkEnergyinMj - workEnergyinMJ);
    }

    public void convertUEtoWorkEnergy()
    {
        ForgeDirection inputDirection = ForgeDirection.getOrientation(getFacing()).getOpposite();

        TileEntity inputTile = Vector3.getTileEntityFromSide(this.worldObj, new Vector3(this), inputDirection);

        if (inputTile != null)
        {
            if ((inputTile instanceof IConductor))
            {
                if (getWorkEnergy() >= getMaxWorkEnergy())
                {
                    ((IConductor) inputTile).getNetwork().stopRequesting(this);
                } else
                {
                    ((IConductor) inputTile).getNetwork().startRequesting(this, 10.0D, 120.0D);

                    setWorkEnergy((int) (getWorkEnergy() + ((IConductor) inputTile).getNetwork().consumeElectricity(this).getWatts() / 50.0D));
                }
            }
        }
    }

    @Override
    public ItemStack getPowerLinkStack()
    {
        return getStackInSlot(getPowerLinkSlot());
    }

    @Override
    public int getPowerLinkSlot()
    {
        return 1;
    }

    @Override
    public int getMaxSafeInput()
    {
        return 2048;
    }

    @Override
    public TileEntitySecurityStation getLinkedSecurityStation()
    {
        TileEntityCapacitor cap = (TileEntityCapacitor) FrequencyGrid.getWorldMap(this.worldObj).getCapacitor().get(Integer.valueOf(getPowerSourceID()));
        if (cap != null)
        {
            TileEntitySecurityStation sec = cap.getLinkedSecurityStation();
            if (sec != null)
                return sec;
        }

        return null;
    }
}
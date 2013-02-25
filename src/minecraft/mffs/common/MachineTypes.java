package mffs.common;

import ic2.api.ExplosionWhitelist;
import mffs.client.gui.GuiAreaDefenseStation;
import mffs.client.gui.GuiCapacitor;
import mffs.client.gui.GuiControlSystem;
import mffs.client.gui.GuiConverter;
import mffs.client.gui.GuiExtractor;
import mffs.client.gui.GuiProjector;
import mffs.client.gui.GuiSecStorage;
import mffs.client.gui.GuiSecurityStation;
import mffs.common.container.ContainerAreaDefenseStation;
import mffs.common.container.ContainerCapacitor;
import mffs.common.container.ContainerControlSystem;
import mffs.common.container.ContainerConverter;
import mffs.common.container.ContainerForceEnergyExtractor;
import mffs.common.container.ContainerProjector;
import mffs.common.container.ContainerSecStorage;
import mffs.common.container.ContainerSecurityStation;
import mffs.common.tileentity.TileEntityCapacitor;
import mffs.common.tileentity.TileEntityControlSystem;
import mffs.common.tileentity.TileEntityConverter;
import mffs.common.tileentity.TileEntityDefenseStation;
import mffs.common.tileentity.TileEntityExtractor;
import mffs.common.tileentity.TileEntityProjector;
import mffs.common.tileentity.TileEntitySecStorage;
import mffs.common.tileentity.TileEntitySecurityStation;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import universalelectricity.prefab.TranslationHelper;
import cpw.mods.fml.common.registry.GameRegistry;

public enum MachineTypes
{

    Projector(TileEntityProjector.class, GuiProjector.class, ContainerProjector.class, ModularForceFieldSystem.blockProjector, "KyKyFyKJK", "ByByKyBaB"),
    Extractor(TileEntityExtractor.class, GuiExtractor.class, ContainerForceEnergyExtractor.class, ModularForceFieldSystem.blockExtractor, " C xFx G ", " E xKx J "),
    Capacitor(TileEntityCapacitor.class, GuiCapacitor.class, ContainerCapacitor.class, ModularForceFieldSystem.blockCapacitor, "xJxCFCxJx", "xaxEKExax"),
    Converter(TileEntityConverter.class, GuiConverter.class, ContainerConverter.class, ModularForceFieldSystem.blockConverter, "ANAJOMAPA", "AKAaJIAMA"),
    DefenceStation(TileEntityDefenseStation.class, GuiAreaDefenseStation.class, ContainerAreaDefenseStation.class, ModularForceFieldSystem.blockDefenceStation, " J aFa E ", " a EKE C "),
    SecurityStation(TileEntitySecurityStation.class, GuiSecurityStation.class, ContainerSecurityStation.class, ModularForceFieldSystem.blockSecurityStation, "KCKCFCKJK", "CECEKECaC"),
    SecurityStorage(TileEntitySecStorage.class, GuiSecStorage.class, ContainerSecStorage.class, ModularForceFieldSystem.blockSecurityStorage, "AAAACAAAA", "AAAAEAAAA"),
    ControlSystem(TileEntityControlSystem.class, GuiControlSystem.class, ContainerControlSystem.class, ModularForceFieldSystem.blockControlSystem, "aCaAFAACA", "aEaAKAAEA");
    
    public Class<? extends TileEntity> tileEntity;
    public Class<? extends GuiScreen> gui;
    public Class<? extends Container> container;
    public Block block;
    public String recipe_ic;
    public String recipe_ue;

    private MachineTypes(Class<? extends TileEntity> tileEntity, Class<? extends GuiScreen> gui, Class<? extends Container> container, Block block, String recipeic, String recipeue)
    {
        this.tileEntity = tileEntity;
        this.gui = gui;
        this.container = container;

        this.recipe_ic = recipeic;
        this.recipe_ue = recipeue;
        this.block = block;
    }

    public String getName()
    {
        return TranslationHelper.getLocal(this.block.getBlockName() + ".name");
    }

    public static MachineTypes get(String name)
    {
        for (MachineTypes machine : values())
        {
            if (machine.block.getBlockName().equals(name))
            {
                return machine;
            }
        }

        return null;
    }

    public static MachineTypes fromTE(TileEntity tem)
    {
        for (MachineTypes mach : values())
        {
            if (mach.tileEntity.isInstance(tem))
            {
                return mach;
            }
        }
        return null;
    }

    public static void initialize()
    {
        for (MachineTypes mach : values())
        {
            GameRegistry.registerBlock(mach.block, mach.block.getBlockName());
            GameRegistry.registerTileEntity(mach.tileEntity, mach.block.getBlockName());

            if (MFFSProperties.MODULE_IC2)
                MFFSRecipes.addRecipe(mach.recipe_ic, 1, 1, mach.block, null);

            if (MFFSProperties.MODULE_UE)
                MFFSRecipes.addRecipe(mach.recipe_ue, 1, 2, mach.block, null);

            ExplosionWhitelist.addWhitelistedBlock(mach.block);
        }
    }
}
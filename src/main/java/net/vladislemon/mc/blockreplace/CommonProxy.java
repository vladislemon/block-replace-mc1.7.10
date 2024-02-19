package net.vladislemon.mc.blockreplace;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.vladislemon.mc.blockreplace.event.ChunkLoadEventListener;
import net.vladislemon.mc.blockreplace.event.PopulateChunkPostEventListener;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        BlockReplacer blockReplacer = new BlockReplacer(
                Config.replaceMap,
                Config.dimensionMap,
                Config.recalculateSkylightInModifiedChunks
        );
        MinecraftForge.EVENT_BUS.register(new PopulateChunkPostEventListener(blockReplacer));
        if (Config.replaceBlocksInGeneratedChunks) {
            MinecraftForge.EVENT_BUS.register(new ChunkLoadEventListener(blockReplacer));
        }
    }

    public void init(FMLInitializationEvent event) {
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
    }

    public void serverStarting(FMLServerStartingEvent event) {
    }

    public void serverStarted(FMLServerStartedEvent event) {
    }

    public void serverStopping(FMLServerStoppingEvent event) {
    }

    public void serverStopped(FMLServerStoppedEvent event) {
    }
}

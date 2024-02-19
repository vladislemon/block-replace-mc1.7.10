package net.vladislemon.mc.blockreplace.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.vladislemon.mc.blockreplace.BlockReplacer;

public class PopulateChunkPostEventListener {
    private final BlockReplacer blockReplacer;

    public PopulateChunkPostEventListener(BlockReplacer blockReplacer) {
        this.blockReplacer = blockReplacer;
    }

    @SubscribeEvent
    public void onPopulateChunkPostEvent(PopulateChunkEvent.Post event) {
        blockReplacer.replaceBlocks(event.world, event.world.getChunkFromChunkCoords(event.chunkX, event.chunkZ));
    }
}

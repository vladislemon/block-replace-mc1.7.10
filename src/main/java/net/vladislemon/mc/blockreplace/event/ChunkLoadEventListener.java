package net.vladislemon.mc.blockreplace.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.vladislemon.mc.blockreplace.BlockReplacer;

public class ChunkLoadEventListener {
    private final BlockReplacer blockReplacer;

    public ChunkLoadEventListener(BlockReplacer blockReplacer) {
        this.blockReplacer = blockReplacer;
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        blockReplacer.replaceBlocks(event.world, event.getChunk());
    }
}

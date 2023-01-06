package net.vladislemon.mc.blockreplace;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.Map;

public class ChunkLoadEventListener {
    private final Map<BlockData, BlockData> replaceMap;

    public ChunkLoadEventListener(Map<BlockData, BlockData> replaceMap) {
        this.replaceMap = replaceMap;
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        long startTime = System.nanoTime();
        if (replaceMap.isEmpty()) {
            return;
        }
        Chunk chunk = event.getChunk();
        boolean modified = false;
        for (ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
            if (storage == null) {
                continue;
            }
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    for (int x = 0; x < 16; x++) {
                        Block block = storage.getBlockByExtId(x, y, z);
                        int metadata = storage.getExtBlockMetadata(x, y, z);
                        String blockName = Block.blockRegistry.getNameForObject(block);
                        BlockData blockData = new BlockData(blockName, metadata);
                        BlockData replacementBlockData = replaceMap.get(blockData);
                        if (replacementBlockData != null) {
                            Block replacementBlock = Block.getBlockFromName(replacementBlockData.getName());
                            storage.func_150818_a(x, y, z, replacementBlock);
                            storage.setExtBlockMetadata(x, y, z, replacementBlockData.getMeta());
                            modified = true;
                        }
                    }
                }
            }
        }
        if (modified) {
            chunk.setChunkModified();
        }
        long endTime = System.nanoTime();
        BlockReplaceMod.debug(String.format("Blocks in chunk %s replaced for %d ns", chunk.getChunkCoordIntPair(), endTime - startTime));
    }
}

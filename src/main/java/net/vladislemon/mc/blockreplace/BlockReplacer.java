package net.vladislemon.mc.blockreplace;

import gnu.trove.TIntCollection;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.util.Map;

public class BlockReplacer {
    private final Map<BlockData, BlockData> replaceMap;
    private final Map<BlockData, TIntCollection> dimensionMap;
    private final boolean recalculateSkylightInModifiedChunks;

    public BlockReplacer(
            Map<BlockData, BlockData> replaceMap,
            Map<BlockData, TIntCollection> dimensionMap,
            boolean recalculateSkylightInModifiedChunks
    ) {
        this.replaceMap = replaceMap;
        this.dimensionMap = dimensionMap;
        this.recalculateSkylightInModifiedChunks = recalculateSkylightInModifiedChunks;
    }

    public void replaceBlocks(World world, Chunk chunk) {
        if (replaceMap.isEmpty()) {
            return;
        }
        boolean modified = false;
        boolean changesLighting = false;
        int oldLightOpacity = 0, newLightOpacity;
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
                            int dimensionId = world.provider.dimensionId;
                            TIntCollection targetDimensionIds = dimensionMap.get(blockData);
                            if (targetDimensionIds == null || targetDimensionIds.contains(dimensionId)) {
                                Block replacementBlock = Block.getBlockFromName(replacementBlockData.getName());
                                int blockX = chunk.xPosition << 4 + x;
                                int blockZ = chunk.zPosition << 4 + z;
                                if (recalculateSkylightInModifiedChunks) {
                                    oldLightOpacity = block.getLightOpacity(world, blockX, y, blockZ);
                                }
                                storage.func_150818_a(x, y, z, replacementBlock);
                                storage.setExtBlockMetadata(x, y, z, replacementBlockData.getMeta());
                                modified = true;
                                if (recalculateSkylightInModifiedChunks) {
                                    newLightOpacity = replacementBlock.getLightOpacity(world, blockX, y, blockZ);
                                    changesLighting |= newLightOpacity != oldLightOpacity;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (modified) {
            if (changesLighting && recalculateSkylightInModifiedChunks) {
                chunk.generateSkylightMap();
            }
            chunk.setChunkModified();
        }
    }
}

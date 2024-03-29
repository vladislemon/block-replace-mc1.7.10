package net.vladislemon.mc.blockreplace;

import gnu.trove.TIntCollection;
import gnu.trove.list.array.TIntArrayList;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    public static Map<BlockData, BlockData> replaceMap;
    public static Map<BlockData, TIntCollection> dimensionMap;
    public static boolean replaceBlocksInGeneratedChunks;
    public static boolean recalculateSkylightInModifiedChunks;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
        configuration.load();
        String[] replaceList = configuration.getStringList("replaceMap", "general", new String[]{},
                "Block ids to replace in format: what-to-replace=replacement, " +
                        "example: minecraft:dirt=minecraft:glass");
        replaceMap = Arrays.stream(replaceList)
                .map(ReplaceListEntry::new)
                .collect(Collectors.toMap(ReplaceListEntry::getWhatReplace, ReplaceListEntry::getReplacement));
        String[] dimensionList = configuration.getStringList("dimensionMap", "general", new String[]{},
                "Block id to dimension id map, that specifies in what dimensions blocks should be replaced, " +
                        "format: what-to-replace=dimension-ids, example: minecraft:dirt=1,-1");
        dimensionMap = Arrays.stream(dimensionList)
                .map(DimensionListEntry::new)
                .collect(Collectors.toMap(DimensionListEntry::getWhatReplace, DimensionListEntry::getDimensionIds));
        replaceBlocksInGeneratedChunks = configuration.getBoolean("replaceBlocksInGeneratedChunks", "general", false,
                "Should blocks be replaced in already generated chunks? If true, replacement will happen at " +
                        "chunk load time (might affect performance). If false, replacement will happen only at " +
                        "world generation time.");
        recalculateSkylightInModifiedChunks = configuration.getBoolean("recalculateSkylightInModifiedChunks", "general", true,
                "Should skylight be recalculated in chunks modified by block replacement? If true, skylight " +
                        "recalculation will happen in chunks with replaced blocks (might affect performance). " +
                        "If false, skylight recalculation will not happen in chunks with replaced blocks " +
                        "(might cause visual glitches if replacement block has different light opacity).");
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    private static final class ReplaceListEntry {
        BlockData whatReplace;
        BlockData replacement;

        ReplaceListEntry(String equalSeparatedPair) {
            String[] parts = equalSeparatedPair.split("=");
            this.whatReplace = BlockData.fromString(parts[0]);
            this.replacement = BlockData.fromString(parts[1]);
        }

        BlockData getWhatReplace() {
            return whatReplace;
        }

        BlockData getReplacement() {
            return replacement;
        }
    }

    private static final class DimensionListEntry {
        BlockData whatReplace;
        TIntCollection dimensionIds;

        DimensionListEntry(String equalSeparatedPair) {
            String[] parts = equalSeparatedPair.split("=");
            this.whatReplace = BlockData.fromString(parts[0]);
            this.dimensionIds = new TIntArrayList(
                    Arrays.stream(parts[1].split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray()
            );
        }

        public BlockData getWhatReplace() {
            return whatReplace;
        }

        public TIntCollection getDimensionIds() {
            return dimensionIds;
        }
    }
}

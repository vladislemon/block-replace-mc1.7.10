package net.vladislemon.mc.blockreplace;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    public static Map<BlockData, BlockData> replaceMap;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
        configuration.load();
        String[] replaceList = configuration.getStringList("replaceMap", "general", new String[]{},
                "Block ids to replace in format: what-to-replace=replacement");
        replaceMap = Arrays.stream(replaceList)
                .map(ReplaceListEntry::new)
                .collect(Collectors.toMap(ReplaceListEntry::getWhatReplace, ReplaceListEntry::getReplacement));
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
}
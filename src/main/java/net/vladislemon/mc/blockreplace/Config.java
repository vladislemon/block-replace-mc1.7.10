package net.vladislemon.mc.blockreplace;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    public static Map<String, String> replaceMap;

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
        String whatReplace;
        String replacement;

        ReplaceListEntry(String equalSeparatedPair) {
            String[] parts = equalSeparatedPair.split("=");
            if (parts[0].isEmpty() || parts[1].isEmpty()) {
                throw new IllegalArgumentException("Block ids to replace must not be empty");
            }
            this.whatReplace = parts[0];
            this.replacement = parts[1];
        }

        String getWhatReplace() {
            return whatReplace;
        }

        String getReplacement() {
            return replacement;
        }
    }
}
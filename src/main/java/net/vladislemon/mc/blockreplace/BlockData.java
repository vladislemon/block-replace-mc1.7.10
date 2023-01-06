package net.vladislemon.mc.blockreplace;

import java.util.Objects;

public class BlockData {
    private final String name;
    private final int meta;

    public static BlockData fromString(String s) {
        String[] parts = s.split(":");
        if (parts.length == 3) {
            return new BlockData(parts[0] + ":" + parts[1], Integer.parseInt(parts[2]));
        }
        if (parts.length == 2) {
            return new BlockData(s);
        }
        throw new IllegalArgumentException(String.format("Invalid block ID format: '%s'", s));
    }

    public BlockData(String name, int meta) {
        this.name = name;
        this.meta = meta;
    }

    public BlockData(String name) {
        this(name, 0);
    }

    public String getName() {
        return name;
    }

    public int getMeta() {
        return meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockData blockData = (BlockData) o;
        return meta == blockData.meta && name.equals(blockData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, meta);
    }
}

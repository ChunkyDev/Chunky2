package org.getchunky.chunkyimpl.object;

import org.bukkit.Chunk;
import org.bukkit.util.BlockVector;

import javax.persistence.Entity;

@Entity
public class ChunkyChunk extends ChunkyCuboid {

    /**
     * Construct a new instance of this cuboid region.
     *
     * @param chunk
     */
    public ChunkyChunk(Chunk chunk) {
        super(
                new BlockVector(
                        chunk.getX() < 0 ? chunk.getX() * 16 + 16 : chunk.getX() * 16,
                        0,
                        chunk.getZ() < 0 ? chunk.getZ() * 16 + 16 : chunk.getZ() * 16),
                new BlockVector(
                        chunk.getX() < 0 ? chunk.getX() * 16 : (chunk.getX() + 1) * 16,
                        chunk.getWorld().getMaxHeight(),
                        chunk.getZ() < 0 ? chunk.getZ() * 16 : (chunk.getZ() + 1) * 16
                )
        );
    }
}

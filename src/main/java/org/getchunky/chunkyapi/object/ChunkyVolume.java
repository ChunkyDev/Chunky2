package org.getchunky.chunkyapi.object;

import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.List;

public interface ChunkyVolume extends Comparable<ChunkyVolume>, ChunkyIdentity {

    /**
     * Sets the minimum and maximum points of the bounding box for a region
     *
     * @param points
     */
    public void setMinMaxPoints(List<Vector> points);

    /**
     * Get the lower point of the cuboid.
     *
     * @return min point
     */
    public BlockVector getMinimumPoint();

    /**
     * Get the upper point of the cuboid.
     *
     * @return max point
     */
    public BlockVector getMaximumPoint();

    /**
     * Gets the 2D points for this region
     *
     * @return
     */
    public abstract List<BlockVector> getPoints();

    /**
     * Get the number of blocks in this region
     *
     * @return
     */
    public abstract int volume();

    /**
     * Check to see if a point is inside this region.
     *
     * @param pt
     * @return
     */
    public abstract boolean contains(Vector pt);

    /**
     * Check to see if a point is inside this region.
     *
     * @param pt
     * @return
     */
    public boolean contains(BlockVector pt);

    /**
     * Check to see if a point is inside this region.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean contains(int x, int y, int z);

    /**
     * Check to see if any of the 2D points are inside this region.
     *
     * @param pts
     * @return
     */
    public boolean containsAny(List<BlockVector> pts);

    /**
     * Get a list of intersecting regions.
     *
     * @param volumes
     * @return
     */
    public abstract List<ChunkyVolume> getIntersectingRegions(List<ChunkyVolume> volumes);

    /**
     * Checks if the bounding box of a region intersects with with the bounding
     * box of this region
     *
     * @param volume
     * @return
     */
    public boolean intersectsBoundingBox(ChunkyVolume volume);

    /**
     * Compares all edges of two regions to see if any of them intersect
     *
     * @param volume
     * @return
     */
    public boolean intersectsEdges(ChunkyVolume volume);

    /**
     * Compares to another region.
     *
     * @param other
     * @return
     */
    public int compareTo(ChunkyVolume other);

    /**
     * @return the priority
     */
    public int getPriority();

    /**
     * @param priority the priority to setFlag
     */
    public void setPriority(int priority);
}

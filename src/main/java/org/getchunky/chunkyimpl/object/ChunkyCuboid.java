package org.getchunky.chunkyimpl.object;

import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import org.getchunky.chunkyapi.object.AbstractChunkyVolume;
import org.getchunky.chunkyapi.object.ChunkyVolume;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ChunkyCuboid extends AbstractChunkyVolume {

    /**
     * Construct a new instance of this cuboid region.
     *
     * @param pt1
     * @param pt2
     */
    public ChunkyCuboid(BlockVector pt1, BlockVector pt2) {
        setMinMaxPoints(pt1, pt2);
    }

    /**
     * Given any two points, sets the minimum and maximum points
     *
     * @param pt1
     * @param pt2
     */
    private void setMinMaxPoints(BlockVector pt1, BlockVector pt2) {
        List<Vector> points = new ArrayList<Vector>();
        points.add(pt1);
        points.add(pt2);
        setMinMaxPoints(points);
    }

    /**
     * Set the lower point of the cuboid.
     *
     * @param pt
     */
    public void setMinimumPoint(BlockVector pt) {
        setMinMaxPoints(pt, this.getMaximumPoint());
    }

    /**
     * Set the upper point of the cuboid.
     *
     * @param pt
     */
    public void setMaximumPoint(BlockVector pt) {
        setMinMaxPoints(this.getMinimumPoint(), pt);
    }

    public List<BlockVector> getPoints() {
        List<BlockVector> pts = new ArrayList<BlockVector>();
        int x1 = this.getMinimumPoint().getBlockX();
        int x2 = this.getMaximumPoint().getBlockX();
        int y1 = this.getMinimumPoint().getBlockY();
        int y2 = this.getMaximumPoint().getBlockY();
        int z1 = this.getMinimumPoint().getBlockZ();
        int z2 = this.getMaximumPoint().getBlockZ();

        pts.add(new BlockVector(x1, y1, z1));
        pts.add(new BlockVector(x2, y1, z1));
        pts.add(new BlockVector(x1, y2, z1));
        pts.add(new BlockVector(x1, y1, z2));
        pts.add(new BlockVector(x2, y2, z1));
        pts.add(new BlockVector(x2, y1, z2));
        pts.add(new BlockVector(x1, y2, z2));
        pts.add(new BlockVector(x2, y2, z2));

        return pts;
    }

    /**
     * Checks to see if a point is inside this region.
     */
    public boolean contains(Vector pt) {
        final double x = pt.getX();
        final double y = pt.getY();
        final double z = pt.getZ();
        return x >= this.getMinimumPoint().getBlockX() && x < this.getMaximumPoint().getBlockX()+1
                && y >= this.getMinimumPoint().getBlockY() && y < this.getMaximumPoint().getBlockY()+1
                && z >= this.getMinimumPoint().getBlockZ() && z < this.getMaximumPoint().getBlockZ()+1;
    }


    /*
    public boolean intersectsWith(ProtectedRegion region) throws UnsupportedIntersectionException {

        if (region instanceof ProtectedCuboidRegion) {
            ProtectedCuboidRegion r1 = (ProtectedCuboidRegion) this;
            ProtectedCuboidRegion r2 = (ProtectedCuboidRegion) region;
            BlockVector min1 = r1.getMinimumPoint();
            BlockVector max1 = r1.getMaximumPoint();
            BlockVector min2 = r2.getMinimumPoint();
            BlockVector max2 = r2.getMaximumPoint();

            return !(min1.getBlockX() > max2.getBlockX()
                    || min1.getBlockY() > max2.getBlockY()
                    || min1.getBlockZ() > max2.getBlockZ()
                    || max1.getBlockX() < min2.getBlockX()
                    || max1.getBlockY() < min2.getBlockY()
                    || max1.getBlockZ() < min2.getBlockZ());
        } else if (region instanceof ProtectedPolygonalRegion) {
            throw new UnsupportedIntersectionException();
        } else {
            throw new UnsupportedIntersectionException();
        }
    }
    */

    public List<ChunkyVolume> getIntersectingRegions(List<ChunkyVolume> regions) {
        List<ChunkyVolume> intersectingRegions = new ArrayList<ChunkyVolume>();

        for (ChunkyVolume region : regions) {
            if (!intersectsBoundingBox(region)) continue;

            // If both regions are Cuboids and their bounding boxes intersect, they intersect
            if (region instanceof ChunkyCuboid) {
                intersectingRegions.add(region);
                continue;
            } /*else if (region instanceof ProtectedPolygonalRegion) {
                // If either region contains the points of the other,
                // or if any edges intersect, the regions intersect
                if (containsAny(region.getPoints())
                        || region.containsAny(getPoints())
                        || intersectsEdges(region)) {
                    intersectingRegions.add(region);
                    continue;
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }*/
        }
        return intersectingRegions;
    }

    /**
     * Get the number of Blocks in this region
     *
     * @return
     */
    public int volume() {
        int xLength = this.getMaximumPoint().getBlockX() - this.getMinimumPoint().getBlockX() + 1;
        int yLength = this.getMaximumPoint().getBlockY() - this.getMinimumPoint().getBlockY() + 1;
        int zLength = this.getMaximumPoint().getBlockZ() - this.getMinimumPoint().getBlockZ() + 1;

        int volume = xLength * yLength * zLength;
        return volume;
    }
}

package org.getchunky.chunkyapi.object;

import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import javax.persistence.Entity;
import java.awt.geom.Line2D;
import java.util.List;

@Entity
public abstract class AbstractChunkyVolume extends AbstractChunkyObject implements ChunkyVolume {

    private BlockVector min;
    private BlockVector max;

    /**
     * Priority.
     */
    private int priority = 0;

    /**
     * Sets the minimum and maximum points of the bounding box for a region
     *
     * @param points
     */
    public void setMinMaxPoints(List<Vector> points) {
        int minX = points.get(0).getBlockX();
        int minY = points.get(0).getBlockY();
        int minZ = points.get(0).getBlockZ();
        int maxX = minX;
        int maxY = minY;
        int maxZ = minZ;

        for (Vector v : points) {
            int x = v.getBlockX();
            int y = v.getBlockY();
            int z = v.getBlockZ();

            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (z < minZ) minZ = z;

            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
            if (z > maxZ) maxZ = z;
        }

        min = new BlockVector(minX, minY, minZ);
        max = new BlockVector(maxX, maxY, maxZ);
    }

    public BlockVector getMinimumPoint() {
        return min;
    }

    public BlockVector getMaximumPoint() {
        return max;
    }

    public boolean contains(BlockVector pt) {
        return contains(new Vector(pt.getBlockX(), min.getBlockY(), pt.getBlockZ()));
    }

    public boolean contains(int x, int y, int z) {
        return contains(new Vector(x, y, z));
    }

    public boolean containsAny(List<BlockVector> pts) {
        for (BlockVector pt : pts) {
            if (contains(pt)) {
                return true;
            }
        }
        return false;
    }

    public boolean intersectsBoundingBox(ChunkyVolume volume) {
        BlockVector rMaxPoint = volume.getMaximumPoint();
        BlockVector min = getMinimumPoint();

        if (rMaxPoint.getBlockX() < min.getBlockX()) return false;
        if (rMaxPoint.getBlockY() < min.getBlockY()) return false;
        if (rMaxPoint.getBlockZ() < min.getBlockZ()) return false;

        BlockVector rMinPoint = volume.getMinimumPoint();
        BlockVector max = getMaximumPoint();

        if (rMinPoint.getBlockX() > max.getBlockX()) return false;
        if (rMinPoint.getBlockY() > max.getBlockY()) return false;
        if (rMinPoint.getBlockZ() > max.getBlockZ()) return false;

        return true;
    }

    public boolean intersectsEdges(ChunkyVolume volume) {
        List<BlockVector> pts1 = getPoints();
        List<BlockVector> pts2 = volume.getPoints();
        BlockVector lastPt1 = pts1.get(pts1.size() - 1);
        BlockVector lastPt2 = pts2.get(pts2.size() - 1);
        for (int i = 0; i < pts1.size(); i++) {
            for (int j = 0; j < pts2.size(); j++) {

                Line2D line1 = new Line2D.Double(
                        lastPt1.getBlockX(),
                        lastPt1.getBlockZ(),
                        pts1.get(i).getBlockX(),
                        pts1.get(i).getBlockZ());

                if (line1.intersectsLine(
                        lastPt2.getBlockX(),
                        lastPt2.getBlockZ(),
                        pts2.get(j).getBlockX(),
                        pts2.get(j).getBlockZ())) {
                    return true;
                }
                lastPt2 = pts2.get(j);
            }
            lastPt1 = pts1.get(i);
        }
        return false;
    }

    public int compareTo(ChunkyVolume other) {
        if (this.getId().equals(other.getId())) {
            return 0;
        } else if (this.getPriority() == other.getPriority()) {
            return 1;
        } else if (this.getPriority() > other.getPriority()) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * @param priority the priority to setFlag
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}

package ru.javajava.mechanics.internal;

import org.springframework.stereotype.Service;
import ru.javajava.mechanics.base.Block;
import ru.javajava.mechanics.base.Coords;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivan on 23.12.16.
 */

@Service
@SuppressWarnings("MagicNumber")
public class BlockService {
    private final List<Block> blocks = new ArrayList<>();

    public BlockService() {
        Coords center = new Coords(100, 15, 40);
        Block block = new Block(center);
        block.setxLength(20);
        block.setyLength(30);
        block.setzLength(60);
        blocks.add(block);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<Coords> getCorners(Block block) {
        Coords center = block.getCenter();
        double xLen = block.getxLength();
        double zLen = block.getzLength();

        Coords A = new Coords(center.x + (xLen / 2), 0, center.z + (zLen / 2));
        Coords B = new Coords(center.x - (xLen / 2), 0, center.z - (zLen / 2));
        Coords C = new Coords(center.x + (xLen / 2), 0, center.z - (zLen / 2));
        Coords D = new Coords(center.x - (xLen / 2), 0, center.z + (zLen / 2));
        List<Coords> points = new ArrayList<>();
        points.add(A);
        points.add(B);
        points.add(C);
        points.add(D);
        return points;
    }

    public List<Coords> getEdgePoints (Block block, Coords player) {
        List<Coords> allPoints = getCorners(block);

        Coords closerPoint = allPoints.get(0);
        double minDistance = player.getDistanceBetween(closerPoint);
        int herIndex = 0;
        for (int i = 1; i < allPoints.size(); ++i) {
            Coords point = allPoints.get(i);
            double distance = player.getDistanceBetween(point);
            if (distance < minDistance) {
                minDistance = distance;
                herIndex = i;
                closerPoint = point;
            }
        }

        List<Coords> result = new ArrayList<>();

        if (herIndex <= 1) {
            result.add(allPoints.get(2));
            result.add(allPoints.get(3));
            return result;
        }

        result.add(allPoints.get(0));
        result.add(allPoints.get(1));
        return result;
    }

}

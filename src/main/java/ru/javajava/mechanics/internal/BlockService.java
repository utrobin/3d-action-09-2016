package ru.javajava.mechanics.internal;

import org.springframework.stereotype.Service;
import ru.javajava.mechanics.base.Block;
import ru.javajava.mechanics.base.Coords;

import java.util.*;

/**
 * Created by ivan on 23.12.16.
 */

@Service
@SuppressWarnings("MagicNumber")
public class BlockService {
    private final Set<Block> blocks = new HashSet<>();

    public BlockService() {
        blocks.add(firstBlock());
        blocks.addAll(fourTowers());
        blocks.addAll(centralBackBlocks());
        blocks.addAll(centralBlocksOnBlocks());
    }

    public Set<Block> getBlocks() {
        return blocks;
    }


    private Block firstBlock() {
        Coords center = new Coords(0, 8, 0);
        Block block = new Block(center);
        block.setxLength(100);
        block.setyLength(16);
        block.setzLength(100);
        return block;
    }

    private Set<Block> fourTowers() {
        Set<Block> result = new HashSet<>();

        Coords center = new Coords(300, 35, 300);
        Block block = new Block(center);
        block.setxLength(200);
        block.setyLength(70);
        block.setzLength(200);
        result.add(block);

        center = new Coords(-300, 35, 300);
        block = new Block(center);
        block.setxLength(200);
        block.setyLength(70);
        block.setzLength(200);
        result.add(block);

        center = new Coords(300, 35, -300);
        block = new Block(center);
        block.setxLength(200);
        block.setyLength(70);
        block.setzLength(200);
        result.add(block);

        center = new Coords(-300, 35, -300);
        block = new Block(center);
        block.setxLength(200);
        block.setyLength(70);
        block.setzLength(200);
        result.add(block);
        return result;
    }

    private Set<Block> centralBackBlocks() {
        Set<Block> result = new HashSet<>();

        Coords center = new Coords(300, 8, 0);
        Block block = new Block(center);
        block.setxLength(200);
        block.setyLength(16);
        block.setzLength(200);
        result.add(block);

        center = new Coords(-300, 8, 0);
        block = new Block(center);
        block.setxLength(200);
        block.setyLength(16);
        block.setzLength(200);
        result.add(block);

        center = new Coords(0, 8, 300);
        block = new Block(center);
        block.setxLength(200);
        block.setyLength(16);
        block.setzLength(200);
        result.add(block);

        center = new Coords(0, 8, -300);
        block = new Block(center);
        block.setxLength(200);
        block.setyLength(16);
        block.setzLength(200);
        result.add(block);
        return result;
    }

    private Collection<Block> centralBlocksOnBlocks() {
        final Collection<Block> result = new HashSet<>();

        Coords center = new Coords(0, 24, 250);
        Block block = new Block(center);
        block.setxLength(200);
        block.setyLength(16);
        block.setzLength(100);
        result.add(block);

        center = new Coords(0, 24, -250);
        block = new Block(center);
        block.setxLength(200);
        block.setyLength(16);
        block.setzLength(100);
        result.add(block);

        center = new Coords(250, 24, 0);
        block = new Block(center);
        block.setxLength(100);
        block.setyLength(16);
        block.setzLength(200);
        result.add(block);

        center = new Coords(-250, 24, 0);
        block = new Block(center);
        block.setxLength(100);
        block.setyLength(16);
        block.setzLength(200);
        result.add(block);

        return result;
    }




}

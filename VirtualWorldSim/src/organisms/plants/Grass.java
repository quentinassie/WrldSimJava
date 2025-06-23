package organisms.plants;

import world.World;
import organisms.Organism;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class Grass extends Plant {
    private final Random rand = new Random();

    public Grass(Point position, World world) {
        super(0, position, world);
    }

    @Override
    public String draw() {
        return "⚶";
    }

}
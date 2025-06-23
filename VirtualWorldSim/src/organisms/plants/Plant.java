package organisms.plants;

import organisms.Organism;
import world.World;
import java.awt.Point;
import java.util.List;
import java.util.Random;

public abstract class Plant extends Organism {
    protected Random rand = new Random();

    public Plant(int strength, Point position, World world) {
        super(strength, 0, position, world);
    }

    @Override
    public void action() {
        if (rand.nextInt(100) < 1) { // 1% chance to spread
            List<Point> freeNeighbors = world.getNeighboringCells(position);
            if (!freeNeighbors.isEmpty()) {
                Point newPos = freeNeighbors.get(rand.nextInt(freeNeighbors.size()));
                world.spawnPlant(this.getClass(), newPos);
            }
        }
    }

    @Override
    public void collision(Organism other, Point newPos, Point oldPos) {
    	world.moveOrganism(other, newPos, oldPos);
        world.removeOrganism(this);
    }
}
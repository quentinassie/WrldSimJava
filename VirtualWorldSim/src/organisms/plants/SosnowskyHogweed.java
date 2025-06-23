package organisms.plants;

import organisms.Organism;
import world.World;

import java.awt.Point;
import java.util.List;

public class SosnowskyHogweed extends Plant {
    public SosnowskyHogweed(Point position, World world) {
        super(10, position, world);
    }

    @Override
    public String draw() {
        return "💀";
    }

    @Override
    public void action() {
        List<Point> neighbors = world.getNeighboringCells(position);
        for (Point p : neighbors) {
            Organism o = world.getOrganismAt(p);
            if (o != null && !(o instanceof Plant)) {
                world.removeOrganism(o);
                world.log(o.getClass().getSimpleName() + " was burned by Hogweed...");

            }
        }
    }

    @Override
    public void collision(Organism attacker, Point newPos, Point oldPos) {
        System.out.println(attacker.getClass().getSimpleName() + " meurt en touchant Hogweed!");
        world.removeOrganism(attacker);
        world.removeOrganism(this);
    }
}

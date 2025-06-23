package organisms.animals;

import world.World;
import organisms.Organism;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Antelope extends Animal {
    private final Random rand = new Random();

    public Antelope(Point position, World world) {
        super(4, 4, position, world);
    }

    @Override
    public String draw() {
        return "🦌"; 
    }

    @Override
    public void action() {
        List<Point> distantMoves = getTwoStepNeighbors(position);

        if (!distantMoves.isEmpty()) {
            Point newPos = distantMoves.get(rand.nextInt(distantMoves.size()));
            Organism target = world.getOrganismAt(newPos);

            if (target == null) {
                world.moveOrganism(this, newPos, position);
            } else {
                target.collision(this, newPos, position);
            }
        }
    }

    @Override
    public void collision(Organism attacker, Point newPos, Point oldPos) {
        if (this.getClass() == attacker.getClass()) {
            world.reproduce(attacker);
            return;
        }

        // 50% chance to escape
        if (rand.nextBoolean()) {
            List<Point> escapeOptions = world.getNeighboringCells(this.position);
            escapeOptions.removeIf(p -> world.isOccupied(p));

            if (!escapeOptions.isEmpty()) {
                Point escape = escapeOptions.get(rand.nextInt(escapeOptions.size()));
                System.out.println("Antelope s'échappe vers " + escape);
                world.moveOrganism(this, escape, position);
                return; // antelope run away
            }
        }

        // normal if no escape
        if (this.strength > attacker.getStrength()) {
            world.removeOrganism(attacker);
        } else {
            world.moveOrganism(attacker, newPos, oldPos);
            world.removeOrganism(this);
        }
    }

    private List<Point> getTwoStepNeighbors(Point pos) {
        List<Point> result = new ArrayList<>();
        int[][] directions = {
        	    {0, -2}, {0, 2}, {-2, 0}, {2, 0}
        	};

        for (int[] d : directions) {
            Point p = new Point(pos.x + d[0], pos.y + d[1]);
            if (world.isInBounds(p)) {
                result.add(p);
            }
        }
        return result;
    }
}

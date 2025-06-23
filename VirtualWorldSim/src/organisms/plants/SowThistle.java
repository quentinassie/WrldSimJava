package organisms.plants;

import world.World;
import organisms.Organism;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class SowThistle extends Plant {
	private final Random rand = new Random();

	public SowThistle(Point position, World world) {
		super(0, position, world);
	}

	@Override
	public String draw() {
		return "ST";
	}

	@Override
	public void action() {
		List<Point> free = world.getNeighboringCells(position);
		free.removeIf(world::isOccupied);
		for (int i = 0; i < 3; i++) {
			if (!free.isEmpty() && rand.nextInt(100) < 20) { // reduction of probabilty to expand otherwise too fast
				Point p = free.remove(rand.nextInt(free.size()));
				world.spawnPlant(SowThistle.class, p);
			}
		}
	}
}
package organisms.animals;

import organisms.Organism;
import world.World;
import java.awt.Point;
import java.util.List;
import java.util.Random;

public abstract class Animal extends Organism {
	protected Random rand = new Random();
	private boolean isReproducing = false;

	public Animal(int strength, int initiative, Point position, World world) {
		super(strength, initiative, position, world);
	}

	@Override
	public void action() {
		if (isReproducing) {
			return;
		}
		List<Point> neighbors = world.getNeighboringCells(position);
		if (!neighbors.isEmpty()) {
			Point newPos = neighbors.get(rand.nextInt(neighbors.size()));
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
			this.isReproducing = true;
		} else if (this.strength > attacker.getStrength()) {
			world.removeOrganism(attacker);
			world.log(this.getClass().getSimpleName() + " killed its attacker " + attacker.getClass().getSimpleName());
		} else {
			world.moveOrganism(attacker, newPos, oldPos);
			world.removeOrganism(this);
			world.log(this.getClass().getSimpleName() + " was kiled by its attacker "
					+ attacker.getClass().getSimpleName());
		}
	}
	public void resetReproduction() {
		this.isReproducing = false;
	}

}
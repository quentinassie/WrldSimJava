package organisms.animals;

import world.World;
import organisms.Organism;

import java.awt.Point;
import java.util.List;
import java.util.Random;

public class Turtle extends Animal {
	private final Random rand = new Random();

	public Turtle(Point position, World world) {
		super(2, 1, position, world);
	}

	@Override
	public String draw() {
		return "🐢";
	}

	@Override
	public void action() {
		// 75% to stay
		if (rand.nextInt(100) < 75) {
			System.out.println("Turtle reste en place.");
			return;
		}
		super.action();
	}

	@Override
	public void collision(Organism other, Point newPos, Point oldPos) {
		if (this.getClass() == other.getClass()) {
			world.reproduce(this);
		} else if (other.getStrength() < 5) {
			// push attacker back
			world.log("Turtle reflects attack from "+ other.getClass().getSimpleName());

		} else {
			if (this.strength >= other.getStrength()) {
				world.removeOrganism(other);
				world.log("Turtle killed its attacker "+ other.getClass().getSimpleName());
			} else {
				world.moveOrganism(other, newPos, oldPos);
				world.removeOrganism(this);
				world.log("Turtle was killed from its attacker "+ other.getClass().getSimpleName());
			}
		}
	}
}

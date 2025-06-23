package organisms;

import world.World;
import java.awt.Point;

public abstract class Organism {
	protected int strength;
	protected int initiative;
	protected Point position;
	protected int age;
	protected World world;

	public Organism(int strength, int initiative, Point position, World world) {
		this.strength = strength;
		this.initiative = initiative;
		this.position = position;
		this.age = 0;
		this.world = world;
	}

	public abstract String draw();

	public abstract void action();

	public abstract void collision(Organism otherOrganism, Point newpos, Point oldPos);

	public int getStrength() {
		return this.strength;
	}

	public int getInitiative() {
		return this.initiative;
	}

	public Point getPosition() {
		return this.position;
	}

	public int getAge() {
		return this.age;
	}

	public void increaseAge() {
		this.age++;
	}
	
	public void setAge(int age) {
		this.age = age;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public void setStrength(int str) {
		this.strength = str;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " at " + position;
	}
}

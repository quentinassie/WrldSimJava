package organisms.animals;

import organisms.Organism;
import world.World;

import java.awt.Point;
import java.util.*;

public class Human extends Animal {
	private int dx = 0;
	private int dy = 0;
	private boolean hasMoveIntent = false;
	private boolean abilityRequested = false;
	private int abilityTurnsLeft = 0;
	private boolean abilityActive = false;
	private int cooldown = 0;

	public Human(Point position, World world) {
		super(5, 4, position, world); // exemple: force 5, initiative 4
	}

	@Override
	public String draw() {
		return "👨";
	}

	public void setDirection(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		this.hasMoveIntent = true;
	}

	public void activateAbility() {
		if (!abilityActive && cooldown == 0) {
			abilityRequested = true;
		}
	}
	
	public boolean isAbilityActive() {
		return abilityActive;
	}

	public List<Point> getAreaAround(Point center, int radius) {
		List<Point> area = new ArrayList<>();
		for (int dy = -radius; dy <= radius; dy++) {
			for (int dx = -radius; dx <= radius; dx++) {

				Point p = new Point(center.x + dx, center.y + dy);
				if (world.isInBounds(p)) {
					area.add(p);
				}
			}
		}
		return area;
	}

	@Override
	public void action() {
		// 1. Si demandé, activer maintenant (même si destruction plus tard)
		if (abilityRequested) {
			abilityActive = true;
			this.initiative = -1;
			abilityTurnsLeft = 5;
			cooldown = 5;
			abilityRequested = false;
			world.log("Ability purification: Human will destroy everything 1 cell around for 5 turns !");
		}

		// 2. Déplacement
		if (!hasMoveIntent)
			return;

		Point newPos = new Point(position.x + dx, position.y + dy);
		hasMoveIntent = false;

		if (!world.isInBounds(newPos))
			return;

		Organism target = world.getOrganismAt(newPos);
		if (target == null) {
			world.moveOrganism(this, newPos, position);
		} else {
			target.collision(this, newPos, position);
		}

		// 3. Si actif → détruire autour de la NOUVELLE position
		if (abilityActive) {
			world.log("Human's power active (" + abilityTurnsLeft + " turns left)");
			

			List<Point> radius1 = getAreaAround(position, 1); // position now updated
			for (Point p : radius1) {
				Organism o = world.getOrganismAt(p);
				if (o != null && o != this) {
					world.removeOrganism(o);
					world.log("🔥 Human slaugthered " + o.getClass().getSimpleName() + " at " + p);
				}
			}

			abilityTurnsLeft--;
			if (abilityTurnsLeft == 0) {
				abilityActive = false;
				world.log("Human's power ended.");
			}
		} else if (cooldown > 0) {
			cooldown--;
		}
	}


}

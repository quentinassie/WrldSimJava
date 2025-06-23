package organisms.plants;

import organisms.Organism;
import world.World;

import java.awt.Point;

public class Belladonna extends Plant {
    public Belladonna(Point position, World world) {
        super(99, position, world);
    }

    @Override
    public String draw() {
        return "☠";
    }

    @Override
    public void collision(Organism attacker, Point newPos, Point oldPos) {
        System.out.println(attacker.getClass().getSimpleName() + " meurt empoisonné en mangeant Belladonna!");
        world.removeOrganism(attacker);
        world.removeOrganism(this);  
        world.log(attacker.getClass().getSimpleName() + " ate a Belladonna and died...");
    }
}

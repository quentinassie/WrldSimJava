package organisms.plants;

import organisms.Organism;
import world.World;

import java.awt.Point;

public class Guarana extends Plant {
    public Guarana(Point position, World world) {
        super(0, position, world);
    }

    @Override
    public String draw() {
        return "➕";
    }

    @Override
    public void collision(Organism attacker, Point newPos, Point oldPos) {
        attacker.setStrength(attacker.getStrength() + 3);
        System.out.println(attacker.getClass().getSimpleName() + " gagne +3 de force en mangeant Guarana!");
        world.log(attacker.getClass().getSimpleName()+" blessed with +3 strength for eating a Guarana");
        super.collision(attacker, newPos, oldPos); 
    }
}

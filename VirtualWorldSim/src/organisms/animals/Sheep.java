package organisms.animals;

import world.World;
import java.awt.Point;

public class Sheep extends Animal {
    public Sheep(Point position, World world) {
        super(4, 4, position, world);
    }

    @Override
    public String draw() {
    	return "🐑";
    }
}

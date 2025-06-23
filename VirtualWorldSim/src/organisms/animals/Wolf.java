package organisms.animals;

import world.World;
import java.awt.Point;

public class Wolf extends Animal {
    public Wolf(Point position, World world) {
        super(9, 5, position, world);
    }

    @Override
    public String draw() {
        return "🐺";
    }
}
	package organisms.animals;
	
	import world.World;
	import organisms.Organism;
	
	import java.awt.Point;
	import java.util.List;
	import java.util.Random;
	
	public class Fox extends Animal {
	    private final Random rand = new Random();
	
	    public Fox(Point position, World world) {
	        super(3, 7, position, world);
	    }
	
	    @Override
	    public String draw() {
	        return "🦊";
	    }
	
	
	    
	    public void actionLog() {
	        List<Point> neighbors = world.getNeighboringCells(position);
	        System.out.println("Fox en " + position);
	        System.out.println("voisins: " + neighbors);
	
	        for (Point p : neighbors) {
	            Organism o = world.getOrganismAt(p);
	            System.out.println("  voisin " + p + " → " + (o == null ? "vide" : o.getClass().getSimpleName()));
	        }
	    }
	    
	    @Override
	    public void action() {
	        List<Point> neighbors = world.getNeighboringCells(position);
	
	        //safe neighbors only
	        List<Point> safeNeighbors = new java.util.ArrayList<>();
	        for (Point p : neighbors) {
	            Organism o = world.getOrganismAt(p);
	            System.out.println("fow voit"+o);
	            if (o == null || o.getStrength() <= this.strength) {
	                safeNeighbors.add(p);
	            }
	        }
	
	        if (!safeNeighbors.isEmpty()) {
	            Point newPos = safeNeighbors.get(rand.nextInt(safeNeighbors.size()));
	            Organism target = world.getOrganismAt(newPos);
	
	            if (target == null) {
	                world.moveOrganism(this, newPos, position);
	            } else {
	                this.collision(target, newPos, position);
	            }
	        }
	    }
	}

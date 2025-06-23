package world;

import organisms.Organism;
import organisms.plants.Plant;
import organisms.animals.Animal;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.*;

import javax.swing.JTextArea;

public class World {
	private final int width;
	private final int height;
	private final Organism[][] grid;
	private final List<Organism> organisms;
	private JTextArea logArea;

	public World(int width, int height) {
		this.width = width;
		this.height = height;
		this.grid = new Organism[height][width];
		this.organisms = new ArrayList<>();
	}
	public void setLogArea(JTextArea logArea) {
		this.logArea = logArea;
	}

	public void log(String message) {
		if (logArea != null) {
			logArea.append(message + "\n");
			logArea.setCaretPosition(logArea.getDocument().getLength());
		}
		System.out.println(message);
	}
	
	public List<Organism> getOrganisms(){
		return this.organisms;
	}


	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public void makeTurn() {
		System.out.println("Tour commence");
		organisms.forEach(Organism::increaseAge);

		List<Organism> sorted = getSortedOrganisms();

		System.out.println("== Ordre d'action ce tour ==");
		for (Organism o : sorted) {
			System.out.println(o.getClass().getSimpleName() + " → init=" + o.getInitiative() + " age=" + o.getAge()
					+ " pos=" + o.getPosition());
		}

		for (Organism org : sorted) {
			if (!organisms.contains(org))
				continue;
			org.action(); 
			if (org instanceof Animal animal) {
				animal.resetReproduction();
			}
		}

		//description();
	}

	public List<Organism> getSortedOrganisms() {
		List<Organism> sorted = new ArrayList<>(organisms);
		sorted.sort(Comparator.comparingInt(Organism::getInitiative).reversed().thenComparingInt(Organism::getAge)
				.reversed());
		return sorted.reversed();
	}

	public void clearWorld() {
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				grid[y][x] = null;
		this.organisms.clear();
		
	}

	public void moveOrganism(Organism org, Point newPos, Point oldPos) {
		if (!isInBounds(newPos))
			return;
		grid[oldPos.y][oldPos.x] = null;
		org.setPosition(newPos);
		grid[newPos.y][newPos.x] = org;
	}

	public void removeOrganism(Organism org) {
		Point pos = org.getPosition();
		if (isInBounds(pos) && grid[pos.y][pos.x] == org) {
			grid[pos.y][pos.x] = null;
		}
		// toRemoveOrganism.add(org);
		organisms.remove(org);
	}

	public void reproduce(Organism parent) {
		List<Point> free = getNeighboringCells(parent.getPosition());
		if (!free.isEmpty()) {
			try {
				Organism child = parent.getClass().getConstructor(Point.class, World.class).newInstance(free.get(0),
						this);
				addOrganism(child);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void spawnPlant(Class<? extends Plant> plantClass, Point pos) {
		try {
			Plant plant = plantClass.getConstructor(Point.class, World.class).newInstance(pos, this);
			addOrganism(plant);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isInBounds(Point p) {
		return p.x >= 0 && p.x < width && p.y >= 0 && p.y < height;
	}

	public boolean isOccupied(Point p) {
		return getOrganismAt(p) != null;
	}

	public Organism getOrganismAt(Point p) {
		if (!isInBounds(p))
			return null;
		Organism o = grid[p.y][p.x];
		return o;
	}

	public List<Point> getNeighboringCells(Point pos) {
		List<Point> result = new ArrayList<>();

		Point neighborUp = new Point(pos.x, pos.y - 1);
		Point neighborDown = new Point(pos.x, pos.y + 1);
		Point neighborLeft = new Point(pos.x - 1, pos.y);
		Point neighborRight = new Point(pos.x + 1, pos.y);

		if (isInBounds(neighborUp)) {
			result.add(neighborUp);
		}
		if (isInBounds(neighborDown)) {
			result.add(neighborDown);
		}
		if (isInBounds(neighborLeft)) {
			result.add(neighborLeft);
		}
		if (isInBounds(neighborRight)) {
			result.add(neighborRight);
		}

		return result;
	}

	public void addOrganism(Organism org) {
		organisms.add(org);
		grid[org.getPosition().y][org.getPosition().x] = org;
	}

	public void description() {
		System.out.println("==== ÉTAT ACTUEL DES ORGANISMES ====");
		int i = 1;
		for (Organism o : organisms) {
			System.out.printf("[%02d] %-12s Pos: (%d,%d) Force: %2d Init: %d Âge: %d%n", i++,
					o.getClass().getSimpleName(), o.getPosition().x, o.getPosition().y, o.getStrength(),
					o.getInitiative(), o.getAge());
		}
		System.out.println("====================================");
	}
	
	public void saveToFile(File file) {
		try (PrintWriter writer = new PrintWriter(file)) {
			for (Organism o : organisms) {
				writer.printf("%s %d %d %d%n",
					o.getClass().getSimpleName(),
					o.getPosition().x,
					o.getPosition().y,
					o.getAge()
				);
			}
			log("World saved to " + file.getName());
		} catch (IOException e) {
			log("Error saving world: " + e.getMessage());
		}
	}
	
	public void loadFromFile(File file) {
		try (Scanner scanner = new Scanner(file)) {
			organisms.clear();
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					grid[y][x] = null;

			while (scanner.hasNext()) {
				String className = scanner.next();
				int x = scanner.nextInt();
				int y = scanner.nextInt();
				int age = scanner.nextInt();

				Class<?> classToGet;
				try {
				    classToGet = Class.forName("organisms.animals." + className);
				} catch (ClassNotFoundException e1) {
				    try {
				        classToGet = Class.forName("organisms.plants." + className);
				    } catch (ClassNotFoundException e2) {
				        log("Class not found: " + className);
				        continue; // skip this organism
				    }
				}
				Constructor<?> constructor = classToGet.getConstructor(Point.class, World.class);
				Organism org = (Organism) constructor.newInstance(new Point(x, y), this);
				org.setAge(age);
				addOrganism(org);
			}
			log("World loaded from " + file.getName());
		} catch (Exception e) {
			log("Error loading world: " + e.getMessage());
		}
	}



}
package gui;

import organisms.Organism;
import organisms.animals.Human;
import world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.List;

public class WorldPanel extends JPanel {
	private final World world;
	public int cellSize = 30;
	private WorldFrame worldFrame;

	public WorldPanel(World world) {
		this.world = world;
		int width = world.getWidth();
		int height = world.getHeight();
		setPreferredSize(new Dimension(width * cellSize, height * cellSize));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!SwingUtilities.isLeftMouseButton(e))
					return;

				int x = e.getX() / cellSize;
				int y = e.getY() / cellSize;
				Point clicked = new Point(x, y);

				if (!world.isInBounds(clicked) || world.isOccupied(clicked))
					return;

				JPopupMenu menu = new JPopupMenu();
				String[] organisms = { "Wolf", "Sheep", "Fox", "Turtle", "Antelope", "Human", "Grass", "Guarana",
						"SowThistle", "Belladonna", "SosnowskyHogweed" };

				for (String name : organisms) {
					JMenuItem item = new JMenuItem(name);
					item.addActionListener(ev -> {
						try {
							if (name.equals("Human")) {
								for (Organism o : world.getOrganisms()) {
									if (o instanceof Human) {
										world.log("Impossible to add new Human : already one.");
										return;
									}
								}
							}

							Class<?> clas;
							try {
								clas = Class.forName("organisms.animals." + name);
							} catch (ClassNotFoundException ex) {
								clas = Class.forName("organisms.plants." + name);
							}

							Constructor<?> ctr = clas.getConstructor(Point.class, World.class);
							Organism o = (Organism) ctr.newInstance(clicked, world);
							world.addOrganism(o);
							repaint();
							world.log("Ajout de " + name + " en " + clicked);

							if (name.equals("Human") && worldFrame != null) {
								worldFrame.setHuman((Human) o);
							}
						} catch (Exception ex) {
							world.log("Erreur : " + ex.getMessage());
						}
					});
					menu.add(item);
				}

				menu.show(WorldPanel.this, e.getX(), e.getY());
			}
		});
	}

	public void setWorldFrame(WorldFrame frame) {
		this.worldFrame = frame;
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

	
		for (int y = 0; y < world.getHeight(); y++) {
			for (int x = 0; x < world.getWidth(); x++) {
				Point p = new Point(x,y);
				Organism o = world.getOrganismAt(new Point(x, y));
				g.setColor(Color.WHITE);
				g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
				
				// Color ability area if active
				Human h = worldFrame.getHuman();
				if (h != null && h.isAbilityActive()) {
					List<Point> abilityZone = h.getAreaAround(h.getPosition(), 1);
					if (abilityZone.contains(p)) {
						g.setColor(new Color(160, 32, 240));
						g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
					}
				}
				
				g.setColor(Color.BLACK);
				g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
				if (o != null) {
					g.drawString(o.draw(), x * cellSize + 10, y * cellSize + 20);

				}
			}
		}
	}
}
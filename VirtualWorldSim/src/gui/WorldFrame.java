package gui;

import organisms.animals.*;
import organisms.plants.*;
import organisms.*;

import world.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;

public class WorldFrame extends JFrame {
	private final World world;
	private final WorldPanel panel;
	private final JTextArea logArea = new JTextArea(8, 40);
	private Human human;

	public WorldFrame() {
		super("Virtual World Simulator");
		world = new World(12, 12);
		panel = new WorldPanel(world);
		panel.setWorldFrame(this);

		JPanel controlPanel = new JPanel(new FlowLayout());
		JButton upButton = new JButton("↑");
		JButton downButton = new JButton("↓");
		JButton leftButton = new JButton("←");
		JButton rightButton = new JButton("→");

		upButton.addActionListener(e -> {
			human.setDirection(0, -1);
			world.makeTurn();
			panel.repaint();
		});
		downButton.addActionListener(e -> {
			human.setDirection(0, 1);
			world.makeTurn();
			panel.repaint();
		});
		leftButton.addActionListener(e -> {
			human.setDirection(-1, 0);
			world.makeTurn();
			panel.repaint();
		});
		rightButton.addActionListener(e -> {
			human.setDirection(1, 0);
			world.makeTurn();
			panel.repaint();
		});

		controlPanel.add(upButton);
		controlPanel.add(leftButton);
		controlPanel.add(downButton);
		controlPanel.add(rightButton);

		JPanel infoPanel = new JPanel();
		infoPanel.add(new JLabel("ASSIE Quentin | 207354"));

		JButton abilityButton = new JButton("Activate Ability");
		abilityButton.addActionListener(e -> human.activateAbility());
		controlPanel.add(abilityButton);

		JButton nextTurnButton = new JButton("Make Turn");
		nextTurnButton.addActionListener(e -> {
			world.makeTurn();
			panel.repaint();
		});
		controlPanel.add(nextTurnButton);

		JButton clearButton = new JButton("Clear World");
		clearButton.addActionListener(e -> {
			world.clearWorld();
			panel.repaint();
		});
		controlPanel.add(clearButton);

		JButton saveButton = new JButton("Save to file");
		saveButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				world.saveToFile(chooser.getSelectedFile());
			}
		});
		infoPanel.add(saveButton);

		JButton loadButton = new JButton("Charger");
		loadButton.addActionListener(e -> {
			JFileChooser chooser = new JFileChooser();
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				logArea.setText("");
				world.loadFromFile(chooser.getSelectedFile());
				panel.repaint();

				for (Organism o : world.getOrganisms()) {
					if (o instanceof Human) {
						this.human.setDirection(0, 0);
						this.human = (Human) o;
						break;
					}
				}
			}
		});
		infoPanel.add(loadButton);

		setLayout(new BorderLayout());
		add(infoPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);

		logArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(logArea);
		add(scrollPane, BorderLayout.EAST);
		world.setLogArea(logArea);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setFocusable(true);
		requestFocusInWindow();
	}

	public void setHuman(Human newHuman) {
		this.human = newHuman;
	}

	public Human getHuman() {
		return human;
	}
}

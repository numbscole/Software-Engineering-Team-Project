package client;

import javax.imageio.ImageIO;
import javax.swing.*;
import messageData.GameActionData;
import server.ServerMessage;
import gameMechanics.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameControl implements ActionListener, MouseListener {
	private JPanel container;
	private GameClient client;
	private JLabel status;

	// Graphic Components
	private Image playerImage;
	private Image opponentImage;

	// GameMap objects
	private Player player;
	private Player opponent;
	private Bullets player_bullets = new Bullets();
	private Bullets opponent_bullets = new Bullets();
	GameMap gameMap;

	// GameMap functionality
	private boolean dPressed, aPressed, sPressed, wPressed;

	public GameControl(JPanel container, GameClient client) {
		this.client = client;
		this.container = container;

	}

	public void initialize(ServerMessage msg) {
		try {
			BufferedImage image = ImageIO.read(new File("guy1.jpg"));
			BufferedImage image2 = ImageIO.read(new File("guy2.jpg"));

			gameMap = (GameMap) ((GamePanel) container.getComponent(4)).getGameMap();
			switch (msg) {
			case JoinGameSuccess:
				opponentImage = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
				playerImage = image2.getScaledInstance(image2.getWidth(), image2.getHeight(), Image.SCALE_SMOOTH);
				player = new Player(400, 300, image2.getWidth(), image2.getHeight());
				break;
			case HostGameSuccess:
				playerImage = image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_SMOOTH);
				opponentImage = image2.getScaledInstance(image2.getWidth(), image2.getHeight(), Image.SCALE_SMOOTH);
				player = new Player(100, 300, image.getWidth(), image.getHeight());
				break;
			default:
				break;
			}
			

			addAllKeyBindings();
			gameMap.startGame();
		} catch (IOException e) {
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();

		GameMap gameMap = (GameMap) ((GamePanel) container.getComponent(4)).getGameMap();

		if (command == "Logout") {
			client.displayLoginPanel();
			gameMap.pauseGame();
		} else if (command == "Exit Game") {
			client.displayGameMenuPanel();
			gameMap.pauseGame();
		}
	}

	public void updateStatus(String updateStatus) {
		GamePanel gamePanel = (GamePanel) container.getComponent(4);
		gamePanel.updateStatus(updateStatus);
	}

	public void displayGamePanel() {
		GameMap gameMap = (GameMap) ((GamePanel) container.getComponent(4)).getGameMap();
		gameMap.startGame();
		CardLayout cLayout = (CardLayout) container.getLayout();
		cLayout.show(container, "game");
	}

	// TODO update positions and send data to server
	public void update() {
		move();
		try {
			GameActionData data = new GameActionData(player, player_bullets);
			System.out.println(data.getPlayer().getX() + ", " + data.getPlayer().getY());
			client.sendToServer(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void paintObjects(Graphics g) {
		GameMap gameMap = (GameMap) ((GamePanel) container.getComponent(4)).getGameMap();
		// Draw the player images
		if (player != null)
			g.drawImage(playerImage, player.getX(), player.getY(), gameMap);
		if (opponent != null)
			g.drawImage(opponentImage, opponent.getX(), opponent.getY(), gameMap);

		// Draw any bullets if they're active
//		if (player_bullets.getFly())
//		{
//			Rectangle bullet = player_bullets.getBox();
//			g.setColor(Color.CYAN);
//			g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
//			g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
//		}
//		if (opponent_bullets.getFly())
//		{
//			Rectangle bullet = opponent_bullets.getBox();
//			g.setColor(Color.RED);
//			g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
//			g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
//		}

	}

	public void move() {
		// these if statements tell the image what to do when a certain key is pressed
		if (dPressed) {
			if (player.getX() + player.getSpeed() < gameMap.getWidth() - player.getXBound()) {
				player.setX(player.getX() + player.getSpeed());
			}
		}
		if (aPressed) {
			if (player.getX() - player.getSpeed() > 0) {
				player.setX(player.getX() - player.getSpeed());
			}
		}
		if (sPressed) {
			if (player.getY() + player.getSpeed() < gameMap.getHeight() - player.getYBound()) {
				player.setY(player.getY() + player.getSpeed());
			}
		}
		if (wPressed) {
			if (player.getY() - player.getSpeed() > 0) {
				player.setY(player.getY() - player.getSpeed());
			}
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getOpponent() {
		return opponent;
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	public Bullets getPlayer_bullets() {
		return player_bullets;
	}

	public void setPlayer_bullets(Bullets player_bullets) {
		this.player_bullets = player_bullets;
	}

	public Bullets getOpponent_bullets() {
		return opponent_bullets;
	}

	public void setOpponent_bullets(Bullets opponent_bullets) {
		this.opponent_bullets = opponent_bullets;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		player_bullets = new Bullets();
		player_bullets.setX(player.getX() + 18);
		player_bullets.setY(player.getY() + 15);
		player_bullets.setBox();
		player_bullets.setFly(true);

		player_bullets.setVelocity(e.getX(), e.getY());

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// reusable method for adding keybinds in a dynamic way
	public void addOneKeyBinding(JComponent comp, int keyCode, boolean bool, String id, ActionListener AL) {

		InputMap im = comp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = comp.getActionMap();

		im.put(KeyStroke.getKeyStroke(keyCode, 0, bool), id);

		am.put(id, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AL.actionPerformed(e);
			}
		});
	}

	// this is where you use addOneKeyBinding to create your keybindings
	public void addAllKeyBindings() {

		GameMap gameMap = (GameMap) ((GamePanel) container.getComponent(4)).getGameMap();

		addOneKeyBinding(gameMap, KeyEvent.VK_W, false, "wPressed", (evt) -> {
			wPressed = true;
		});
		addOneKeyBinding(gameMap, KeyEvent.VK_W, true, "wReleased", (evt) -> {
			wPressed = false;
		});
		addOneKeyBinding(gameMap, KeyEvent.VK_A, false, "aPressed", (evt) -> {
			aPressed = true;
		});
		addOneKeyBinding(gameMap, KeyEvent.VK_A, true, "aReleased", (evt) -> {
			aPressed = false;
		});
		addOneKeyBinding(gameMap, KeyEvent.VK_S, false, "sPressed", (evt) -> {
			sPressed = true;
		});
		addOneKeyBinding(gameMap, KeyEvent.VK_S, true, "sReleased", (evt) -> {
			sPressed = false;
		});
		addOneKeyBinding(gameMap, KeyEvent.VK_D, false, "dPressed", (evt) -> {
			dPressed = true;
		});
		addOneKeyBinding(gameMap, KeyEvent.VK_D, true, "dReleased", (evt) -> {
			dPressed = false;
		});
	}
}

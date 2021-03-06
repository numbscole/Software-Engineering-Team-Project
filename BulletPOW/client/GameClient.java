package client;

import java.io.IOException;
import java.util.ArrayList;

import gameMechanics.Bullet;
import messageData.*;
import ocsf.client.AbstractClient;
import server.ServerMessage;

public class GameClient extends AbstractClient
{
	private LoginControl loginController;
	private CreateAccountControl createController;
	private User currentUser;
	private JoinGameControl joinController;
	private GameMenuControl gameMenuController;
	private GameControl gameController;
	String loginID;

	public GameClient(String host, int port) throws IOException
	{
		super(host, port);
		openConnection();
	}
	
	public GameClient() 
	{
		super("localhost", 8080);
	}

	public void displayLoginPanel()
	{
		loginController.displayLoginPanel();
	}

	public void displayCreateAccountPanel()
	{
		createController.displayCreateAccountPanel();
	}

	public void displayGameMenuPanel()
	{
		gameMenuController.displayGameMenuPanel();
	}

	public void displayJoinGamePanel()
	{
		joinController.displayJoinGamePanel();
	}

	public void displayGamePanel()
	{
		gameController.displayGamePanel();
	}

	public GameMenuControl getGameMenuController()
	{
		return gameMenuController;
	}

	public void setGameMenuController(GameMenuControl gameMenuController)
	{
		this.gameMenuController = gameMenuController;
	}

	public void setCreateController(CreateAccountControl cc)
	{
		createController = cc;
	}

	public void setLoginController(LoginControl lc)
	{
		loginController = lc;
	}

	public void setJoinGameController(JoinGameControl jgc)
	{
		joinController = jgc;
	}

	public void setGameControl(GameControl gc)
	{
		gameController = gc;
	}

	public User getCurrentUser()
	{
		return currentUser;
	}

	public void setCurrentUser(User currentUser)
	{
		this.currentUser = currentUser;
	}

	@Override
	public void handleMessageFromServer(Object msg)
	{
		if (msg instanceof User)
		{
			setCurrentUser((User) msg);
			loginController.loginSuccess();
		}

		else if (msg instanceof JoinGameData)
		{
			joinController.updateGameList(((JoinGameData) msg).getGameList());
		}
		//TODO Add Game Functionality
		else if (msg instanceof GameActionData) 
		{
			gameController.getOpponent().setX(((GameActionData) msg).getPx());
			gameController.getOpponent().setY(((GameActionData) msg).getPy());
			gameController.update();
		}
		else if (msg instanceof BulletData) 
		{
			gameController.setHostBullets(((BulletData) msg).getHostBulletCords());
			gameController.setGuestBullets(((BulletData) msg).getGuestBulletCords());
		}
		else if (msg instanceof ServerMessage)
		{
			switch ((ServerMessage) msg)
			{
			case InvalidLogin:
				loginController.displayError(((ServerMessage) msg).getMessage());
				break;
			case CreateSuccess:
				createController.createSuccess();
				break;
			case ExistingAccount:
				createController.displayError(((ServerMessage) msg).getMessage());
				break;
			case DatabaseError:
				createController.displayError(((ServerMessage) msg).getMessage());
				loginController.displayError(((ServerMessage) msg).getMessage());
				break;
			case JoinGameSuccess:
			case HostGameSuccess:
				gameController.initialize((ServerMessage)msg);
				gameController.displayGamePanel();
				break;
			case StartGame:
				gameController.updateStatus(((ServerMessage)msg).getMessage());
			case GameWon:
			case GameLost:
				gameController.updateStatus(((ServerMessage) msg).getMessage());
			default:
				System.out.println(((ServerMessage) msg).getMessage());
				break;
			}
		}
		
		else
		{
			System.out.println(msg);
		}
	}

	@Override
	protected void connectionException(Exception exception)
	{
		exception.printStackTrace();
		super.connectionException(exception);
		System.exit(0);
	}
}
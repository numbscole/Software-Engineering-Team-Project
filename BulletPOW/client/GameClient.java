package client;

import gameMechanics.Player;
import messageData.*;
import ocsf.client.AbstractClient;
import server.ServerMessage;

public class GameClient extends AbstractClient
{
  
	private LoginControl loginController;
	private CreateAccountControl createController;
	private User currentUser;
	private JoinGameControl joinController;
	private GameControl gameController;
	
  public GameClient()
  {
    super("localhost",8300);
  }

  public void setCreateController(CreateAccountControl cc) {createController = cc;}
  
  public void setLoginController(LoginControl lc) {loginController = lc;}
  
  public void setJoinGameController(JoinGameControl jgc) {joinController = jgc;}
  
  public void setGameControl(GameControl gc) {gameController = gc;}
  
  public User getCurrentUser() {return currentUser;}

  public void setCurrentUser(User currentUser) {this.currentUser = currentUser;}
  
  @Override
  public void handleMessageFromServer(Object msg)
  {
    if(msg instanceof User) 
    {
    	setCurrentUser((User)msg);
    	loginController.loginSuccess();
    }
    else if (msg instanceof ServerMessage) 
    {
    	switch ((ServerMessage)msg) {
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
		default:
			System.out.println(((ServerMessage) msg).getMessage());
			break;
		}
    }

  }
  
  

  
  
  public void connectionException (Throwable exception) 
  {
    //Add your code here
  }
  public void connectionEstablished()
  {
    //Add your code here
  }


  

}
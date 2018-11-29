package client;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class CreateAccountPanel extends JPanel{
	
	  // Private data fields for the important GUI components.
	  private JTextField usernameField;
	  private JPasswordField passwordField;
	  private JPasswordField passwordRetypeField;
	  private JLabel errorLabel;
	  
	  // Getter for the text in the username field.
	  public String getUsername()
	  {
	    return usernameField.getText();
	  }
	  
	  // Getter for the text in the password field.
	  public String getPassword()
	  {
	    return new String(passwordField.getPassword());
	  }
	  
	  // Getter for the text in the password field.
	  public String getPassword2()
	  {
	    return new String(passwordRetypeField.getPassword());
	  }
	  
	  // Setter for the error text.
	  public void setError(String error)
	  {
	    errorLabel.setText(error);
	  }
	  
	  public CreateAccountPanel(CreateAccountControl cc) 
	  {
		    // Create the controller and set it in the game client.
		    //LoginControl controller = new LoginControl(container, client);
		    //client.setLoginControl(controller);
		        
		    // Create a panel for the labels at the top of the GUI.
		    JPanel labelPanel = new JPanel(new GridLayout(3, 1, 5, 5));
		    errorLabel = new JLabel("", JLabel.CENTER);
		    errorLabel.setForeground(Color.RED);
		    JLabel instructionLabel = new JLabel("Enter a username and password to create an account.", JLabel.CENTER);
		    JLabel instructionLabel2 = new JLabel("Your password must be at least 6 characters", JLabel.CENTER);
		    labelPanel.add(errorLabel);
		    labelPanel.add(instructionLabel);
		    labelPanel.add(instructionLabel2);

		    // Create a panel for the login information form.
		    JPanel createPanel = new JPanel(new GridLayout(3, 2, 5, 5));
		    JLabel usernameLabel = new JLabel("Username:", JLabel.RIGHT);
		    usernameField = new JTextField(10);
		    JLabel passwordLabel = new JLabel("Password:", JLabel.RIGHT);
		    passwordField = new JPasswordField(10);
		    JLabel passwordRetypeLabel = new JLabel("Retype Password:", JLabel.RIGHT);
		    passwordRetypeField = new JPasswordField(10);
		    
		    
		    createPanel.add(usernameLabel);
		    createPanel.add(usernameField);
		    createPanel.add(passwordLabel);
		    createPanel.add(passwordField);
		    createPanel.add(passwordRetypeLabel);
		    createPanel.add(passwordRetypeField);
		    
		    // Create a panel for the buttons.
		    JPanel buttonPanel = new JPanel();
		    JButton submitButton = new JButton("Submit");
		    submitButton.addActionListener(cc);
		    JButton cancelButton = new JButton("Cancel");
		    cancelButton.addActionListener(cc);    
		    buttonPanel.add(submitButton);
		    buttonPanel.add(cancelButton);

		    // Arrange the three panels in a grid.
		    JPanel grid = new JPanel(new GridLayout(3, 1, 0, 10));
		    grid.add(labelPanel);
		    grid.add(createPanel);
		    grid.add(buttonPanel);
		    this.add(grid);
	  }

}

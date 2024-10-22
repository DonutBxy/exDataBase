package application;
	
import java.sql.SQLException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import application.DatabaseHelper;
import application.DatabaseHelperArticle;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class Main extends Application {

	String adminUsername;
	String adminPassword;
	
	private static DatabaseHelper databaseHelper;
	private static DatabaseHelperArticle databaseHelper1;
	private static final Scanner scanner = new Scanner(System.in);
	
	private Scene loginScene; // To store the initial login scene

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		databaseHelper = new DatabaseHelper();
		databaseHelper1 = new DatabaseHelperArticle();

				
		try {
			
			databaseHelper.connectToDatabase();
			databaseHelper1.connectToDatabase();

			
			primaryStage.setTitle("Help System");

			// Labels

			Label welcome = new Label("Welcome to our Help System");
			Label clickStart = new Label("Click the button to begin!");
			Label logo = new Label("?");

			// label designs

			welcome.setFont(new Font("Arial", 36));
			logo.setFont(new Font("Arial", 200));
			clickStart.setFont(new Font("Arial", 18));

			// Create the login button
			Button startButton = new Button();
			startButton.setText("Start");

			startButton.setStyle("-fx-font-size: 2em; ");

			// Set the action for when the login button is clicked
			startButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// Create the welcome scene
					try {
						if (databaseHelper.isDatabaseEmpty()) { // if there is no admin, make one

							createAdmin(primaryStage);

						} else {

							login(primaryStage);

						}
					} catch (SQLException e) {

						System.err.println("Database error: " + e.getMessage());
						e.printStackTrace();

					}
				}
			});

			// Pane for Center BorderPane
			VBox middlePane = new VBox(logo, startButton, clickStart);
			middlePane.setAlignment(Pos.CENTER);

			VBox.setMargin(startButton, new Insets(50, 0, 20, 0)); // Adds 20px margin at the top of the button

			// Pane For TOP of BorderPane

			HBox topPane = new HBox(welcome);
			topPane.setAlignment(Pos.CENTER); // Centers the label in the HBox

			HBox.setMargin(welcome, new Insets(50, 0, 20, 0)); // Adds 20px margin at the top of the button

			// Initial layout with the login button
			BorderPane starterScreen = new BorderPane();

			// Set location of elements

			starterScreen.setTop(topPane);
			starterScreen.setCenter(middlePane);

			// Background
			starterScreen.setStyle("-fx-background-color: lightblue;");

			// Store the initial scene
			loginScene = new Scene(starterScreen, 900, 600);

			primaryStage.setScene(loginScene);
			primaryStage.show();
			
		} catch (SQLException e) {

			System.err.println("Database error: " + e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void createAdmin(Stage primaryStage) {

		// Labels and buttons
		Label welcome = new Label("Create an Admin Account");
		Label userName = new Label("Enter Username: ");
		Label password = new Label("Enter Password: ");

		Label invUserName = new Label("Username must be between 6-12 characters");
		Label invPassword = new Label("Password must be at least 6 characters");

		Button loginButton = new Button("Create Account");
		Button quitButton = new Button("Quit");

		TextField userNameText = new TextField();
		TextField passwordText = new TextField();

		// Label design
		welcome.setFont(new Font("Arial", 36));
		userName.setFont(new Font("Arial", 20));
		password.setFont(new Font("Arial", 20));

		invUserName.setFont(new Font("Arial", 20));
		invUserName.setStyle("-fx-text-fill: red;");
		invUserName.setVisible(false);

		invPassword.setFont(new Font("Arial", 20));
		invPassword.setStyle("-fx-text-fill: red;");
		invPassword.setVisible(false);


		// Button design
		loginButton.setStyle("-fx-font-size: 2em;");
		quitButton.setStyle("-fx-font-size: 1.5em;");

		// Login button action
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				// Check username length
				if (userNameText.getText().trim().length() <= 6 || userNameText.getText().trim().length() > 12) {
					invUserName.setVisible(true);
				} else {
					invUserName.setVisible(false);
				}

				// Check password length
				if (passwordText.getText().trim().length() < 6) {
					invPassword.setVisible(true);
				} else {
					invPassword.setVisible(false);
				}

				// Proceed if all conditions are met
				if (passwordText.getText().trim().length() >= 6						
						&& userNameText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() <= 12) {

					String email = userNameText.getText().trim();
					String password = passwordText.getText().trim();
					try {
						databaseHelper.register(email, password, "admin");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					login(primaryStage);
					
				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Layout setup

		// Middle VBoxs
		VBox middleLeftPane = new VBox(userName, password);
		VBox.setMargin(userName, new Insets(50, 20, 20, 40));
		VBox.setMargin(password, new Insets(20, 20, 20, 40));

		VBox middleMiddlePane = new VBox(userNameText, passwordText);
		VBox.setMargin(userNameText, new Insets(50, 20, 20, 20));
		VBox.setMargin(passwordText, new Insets(20, 20, 20, 20));

		VBox middleRightPane = new VBox(invUserName, invPassword);
		VBox.setMargin(invUserName, new Insets(50, 20, 20, 20));
		VBox.setMargin(invPassword, new Insets(20, 20, 20, 20));

		// Combine the middle VBoxs
		HBox middlePane = new HBox(middleLeftPane, middleMiddlePane, middleRightPane);
		middlePane.setAlignment(Pos.CENTER_LEFT);

		// Bottom pane for login button
		HBox bottomPane = new HBox(loginButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(loginButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// BorderPane layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);
	}
	
	private void createUser(Stage primaryStage) {

		// Labels and buttons
		Label welcome = new Label("Create a User");
		Label userName = new Label("Enter Username: ");
		Label password = new Label("Enter Password: ");

		Label invUserName = new Label("Username must be between 6-12 characters");
		Label invPassword = new Label("Password must be at least 6 characters");

		Button loginButton = new Button("Create Account");
		Button quitButton = new Button("Quit");

		TextField userNameText = new TextField();
		TextField passwordText = new TextField();

		// Label design
		welcome.setFont(new Font("Arial", 36));
		userName.setFont(new Font("Arial", 20));
		password.setFont(new Font("Arial", 20));

		invUserName.setFont(new Font("Arial", 20));
		invUserName.setStyle("-fx-text-fill: red;");
		invUserName.setVisible(false);

		invPassword.setFont(new Font("Arial", 20));
		invPassword.setStyle("-fx-text-fill: red;");
		invPassword.setVisible(false);

		// Button design
		loginButton.setStyle("-fx-font-size: 2em;");
		quitButton.setStyle("-fx-font-size: 1.5em;");

		// Login button action
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				// Check username length
				if (userNameText.getText().trim().length() <= 6 || userNameText.getText().trim().length() > 12) {
					invUserName.setVisible(true);
				} else {
					invUserName.setVisible(false);
				}

				// Check password length
				if (passwordText.getText().trim().length() < 6) {
					invPassword.setVisible(true);
				} else {
					invPassword.setVisible(false);
				}
				
				
				// Proceed if all conditions are met
				System.out.println("here1");
				if (passwordText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() >= 6 && userNameText.getText().trim().length() <= 12)
						{

					// ADD USER TO LIST

					String email = userNameText.getText().trim();
					String password = passwordText.getText().trim();
					try {
						databaseHelper.register(email, password, "user");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					

					login(primaryStage);

				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Layout setup

		// Middle VBoxs
		VBox middleLeftPane = new VBox(userName, password);
		VBox.setMargin(userName, new Insets(50, 20, 20, 40));
		VBox.setMargin(password, new Insets(20, 20, 20, 40));

		VBox middleMiddlePane = new VBox(userNameText, passwordText);
		VBox.setMargin(userNameText, new Insets(50, 20, 20, 20));
		VBox.setMargin(passwordText, new Insets(20, 20, 20, 20));

		VBox middleRightPane = new VBox(invUserName, invPassword);
		VBox.setMargin(invUserName, new Insets(50, 20, 20, 20));
		VBox.setMargin(invPassword, new Insets(20, 20, 20, 20));

		// Combine the middle VBoxs
		HBox middlePane = new HBox(middleLeftPane, middleMiddlePane, middleRightPane);
		middlePane.setAlignment(Pos.CENTER_LEFT);

		// Bottom pane for login button
		HBox bottomPane = new HBox(loginButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(loginButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// BorderPane layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);
	}
	
	private void login(Stage primaryStage) {

		// Labels and buttons
		Label welcome = new Label("Log In");
		Label newUser = new Label("Click here to make new account");
		Label userName = new Label("Enter Username: ");
		Label password = new Label("Enter Password: ");
		Label noClick = new Label("Please choose which type of account");


		Label invUserName = new Label("Username does not exist");
		Label invPassword = new Label("Incorrect Password");
		Label checkLogin = new Label("Account not found try again");

		Button loginButton = new Button("Log-In");
		Button quitButton = new Button("Quit");
		Button createAccountButton = new Button("Create Account");

		TextField userNameText = new TextField();
		TextField passwordText = new TextField();
		
		CheckBox studentAccount = new CheckBox("User");
		CheckBox adminAccount = new CheckBox("Admin");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		userName.setFont(new Font("Arial", 20));
		password.setFont(new Font("Arial", 20));
		newUser.setFont(new Font("Arial", 14));
		
		noClick.setFont(new Font("Arial", 20));
		noClick.setStyle("-fx-text-fill: red;");
		noClick.setVisible(false);

		invUserName.setFont(new Font("Arial", 20));
		invUserName.setStyle("-fx-text-fill: red;");
		invUserName.setVisible(false);

		invPassword.setFont(new Font("Arial", 20));
		invPassword.setStyle("-fx-text-fill: red;");
		invPassword.setVisible(false);

		checkLogin.setFont(new Font("Arial", 14));
		checkLogin.setStyle("-fx-text-fill: red;");
		checkLogin.setVisible(false);
		
		// CheckBox design
		studentAccount.setFont(new Font("Arial", 20));
		adminAccount.setFont(new Font("Arial", 20));

		// Button design
		loginButton.setStyle("-fx-font-size: 2em;");
		quitButton.setStyle("-fx-font-size: 1.5em;");
		createAccountButton.setStyle("-fx-font-size: 1.5em;");
		
		// Login button action
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				// Check username length
				if (userNameText.getText().trim().length() <= 6 || userNameText.getText().trim().length() > 12) {
					invUserName.setVisible(true);
				} else {
					invUserName.setVisible(false);
				}

				// Check password length
				if (passwordText.getText().trim().length() < 6) {
					invPassword.setVisible(true);
				} else {
					invPassword.setVisible(false);
				}
				if (!studentAccount.isSelected() && !adminAccount.isSelected()) {
					noClick.setVisible(true);
				} else {
					noClick.setVisible(false);
				}

				// Proceed if all conditions are met
				if (passwordText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() >= 6
						&& userNameText.getText().trim().length() <= 12 &&
				!studentAccount.isSelected() && adminAccount.isSelected() ||
				studentAccount.isSelected() && !adminAccount.isSelected() ){
					
					
					if(studentAccount.isSelected()) {
						String email = userNameText.getText().trim();
						String password1 = passwordText.getText().trim();


						try {
							if (databaseHelper.login(email, password1, "user")) {
								studentPage(primaryStage);


							} else {
								checkLogin.setVisible(true);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if(adminAccount.isSelected()) {
						String email = userNameText.getText().trim();
						String password1 = passwordText.getText().trim();

						try {
							if (databaseHelper.login(email, password1, "admin")) {
								adminPage(primaryStage);

							} else {
								checkLogin.setVisible(true);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Create Account button action
		createAccountButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createUser(primaryStage);
			}
		});

		// Layout setup

		// Middle VBoxs
		VBox middleLeftPane = new VBox(userName, password, studentAccount);
		VBox.setMargin(userName, new Insets(50, 20, 20, 40));
		VBox.setMargin(password, new Insets(20, 20, 20, 40));
		VBox.setMargin(studentAccount, new Insets(20, 20, 20, 40));

		VBox middleMiddlePane = new VBox(userNameText, passwordText, adminAccount);
		VBox.setMargin(userNameText, new Insets(50, 20, 20, 20));
		VBox.setMargin(passwordText, new Insets(20, 20, 20, 20));
		VBox.setMargin(adminAccount, new Insets(20, 20, 20, 20));

		VBox middleRightPane = new VBox(invUserName, invPassword, noClick);
		VBox.setMargin(invUserName, new Insets(50, 20, 20, 20));
		VBox.setMargin(invPassword, new Insets(20, 20, 20, 20));
		VBox.setMargin(noClick, new Insets(20, 20, 20, 20));
		

		// Combine the middle VBoxs
		HBox middlePane = new HBox(middleLeftPane, middleMiddlePane, middleRightPane);
		middlePane.setAlignment(Pos.CENTER_LEFT);

		// Bottom pane for login button
		HBox bottomPane = new HBox(createAccountButton, loginButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);

		HBox.setMargin(createAccountButton, new Insets(0, 100, 0, 30));
		HBox.setMargin(loginButton, new Insets(0, 150, 0, 100));
		HBox.setMargin(quitButton, new Insets(0, 80, 0, 70));

		HBox textBottom = new HBox(newUser, checkLogin);
		HBox.setMargin(newUser, new Insets(10, 50, 50, 30));
		HBox.setMargin(checkLogin, new Insets(10, 50, 50, 95));

		VBox bottomText = new VBox(bottomPane, textBottom);

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// BorderPane layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomText);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	private void studentPage(Stage primaryStage) {

		// Labels and buttons
		Label welcome = new Label("Welcome");
		Label role = new Label("Role: Student");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		role.setFont(new Font("Arial", 20));

		Button quitButton = new Button("Log Out");

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Top pane for welcome label
		HBox topPane = new HBox(role, welcome);
		HBox.setMargin(role, new Insets(20, 0, 0, 20));
		HBox.setMargin(welcome, new Insets(50, 0, 20, 130));

		// Bottom pane for exit
		HBox bottomPane = new HBox(quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(quitButton, new Insets(0, 0, 70, 0));

		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	private void adminPage(Stage primaryStage) {
		Label welcome = new Label("Welcome Admin");

		Button createArticle = new Button("Create Article");
		Button deleteArticle = new Button("Delete Aritcle");
		Button listArticles = new Button("List Articles");
		Button backupArticles = new Button("Back up Articles");
		Button RestoreArticles = new Button("Restore Articles");

		Button logoutButton = new Button("Log Out");

		// Label Design
		welcome.setFont(new Font("Arial", 36));

		// Button Design
		createArticle.setStyle("-fx-font-size: 1.8em;");
		deleteArticle.setStyle("-fx-font-size: 1.8em;");
		listArticles.setStyle("-fx-font-size: 1.8em;");

		backupArticles.setStyle("-fx-font-size: 1.8em;");
		RestoreArticles.setStyle("-fx-font-size: 1.8em;");
		logoutButton.setStyle("-fx-font-size: 1.8em;");

		// Set buttons actions using EventHandler
		createArticle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				createArticle(primaryStage);
			}
		});

		deleteArticle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				deleteArticle(primaryStage);

			}
		});

		listArticles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				try {
					listArticles(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		backupArticles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				backupArticles(primaryStage);
			}
		});

		RestoreArticles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO : ADD NEW PAGE

				restoreArticles(primaryStage);

			}
		});

		logoutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				login(primaryStage); // Log out and go back to login
			}
		});

		// Top Pane
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middleTopPane = new HBox(createArticle, deleteArticle, listArticles);
		HBox.setMargin(createArticle, new Insets(50, 50, 80, 120));
		HBox.setMargin(deleteArticle, new Insets(50, 50, 80, 0));
		HBox.setMargin(listArticles, new Insets(50, 0, 80, 0));

		HBox middleBottomPane = new HBox(backupArticles, RestoreArticles, logoutButton);
		HBox.setMargin(backupArticles, new Insets(50, 50, 80, 200));
		HBox.setMargin(RestoreArticles, new Insets(50, 50, 0, 0));
		HBox.setMargin(logoutButton, new Insets(50, 0, 0, 0));

		VBox middlePane = new VBox(middleTopPane, middleBottomPane);

		// Bottom pane for exit
		HBox bottomPane = new HBox(logoutButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(logoutButton, new Insets(0, 0, 70, 0));

		// Create the BorderPane and set the VBox as the center
		BorderPane adminCreateScreen = new BorderPane();

		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);
	}

	private void createArticle(Stage primaryStage) {

		// Labels and buttons
		Label welcome = new Label("Create Article");
		
		Label email = new Label("Title: ");
		Label firstName = new Label("Author: ");
		Label confFirstName = new Label("Abstract: ");
		Label middleName = new Label("Set of keywords: ");
		Label lastName = new Label("Body of the article: ");
		Label references = new Label("References: ");

		TextField emailText = new TextField();
		TextField firstNameText = new TextField();
		TextField confFirstNameText = new TextField();
		TextField middleNameText = new TextField();
		TextField lastNameText = new TextField();
		TextField referencesText = new TextField();

		Button conButton = new Button("Confirm");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		email.setFont(new Font("Arial", 20));
		firstName.setFont(new Font("Arial", 20));
		confFirstName.setFont(new Font("Arial", 20));
		middleName.setFont(new Font("Arial", 20));
		lastName.setFont(new Font("Arial", 20));
		references.setFont(new Font("Arial", 20));

		// Button design
		conButton.setStyle("-fx-font-size: 2em;");
		quitButton.setStyle("-fx-font-size: 1.5em;");

		// Con button action
		conButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (emailText.getText().isEmpty()) {
					emailText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					emailText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (firstNameText.getText().isEmpty()) {
					firstNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					firstNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (confFirstNameText.getText().isEmpty()) {
					confFirstNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					confFirstNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (middleNameText.getText().isEmpty()) {
					middleNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					middleNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (lastNameText.getText().isEmpty()) {
					lastNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					lastNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}
				
				if (referencesText.getText().isEmpty()) {
					referencesText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
				} else {
					referencesText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (!emailText.getText().isEmpty() && !firstNameText.getText().isEmpty()
						&& !confFirstNameText.getText().isEmpty() && !middleNameText.getText().isEmpty()
						&& !lastNameText.getText().isEmpty() && !referencesText.getText().isEmpty()
						&& !databaseHelper.doesUserExist(emailText.getText())) {
					
					char[] title = emailText.getText().toCharArray();
					char[] author = firstNameText.getText().toCharArray();
					char[] abstract1 = confFirstNameText.getText().toCharArray();
					char[] keywords = middleNameText.getText().toCharArray();
					char[] body = lastNameText.getText().toCharArray();
					char[] references = referencesText.getText().toCharArray();
					
					try {
						databaseHelper1.register(title, author, abstract1, keywords, body, references);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Layout setup

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle VBoxs
		VBox middleOne = new VBox(firstName, middleName, email);
		VBox.setMargin(firstName, new Insets(50, 20, 20, 40));
		VBox.setMargin(middleName, new Insets(20, 20, 20, 40));
		VBox.setMargin(email, new Insets(20, 20, 20, 40));

		VBox middleTwo = new VBox(firstNameText, middleNameText, emailText);
		VBox.setMargin(firstNameText, new Insets(50, 20, 20, 40));
		VBox.setMargin(middleNameText, new Insets(20, 20, 20, 40));
		VBox.setMargin(emailText, new Insets(20, 20, 20, 40));

		VBox middleThree = new VBox(confFirstName, lastName,references);
		VBox.setMargin(confFirstName, new Insets(50, 20, 20, 40));
		VBox.setMargin(lastName, new Insets(20, 20, 20, 40));
		VBox.setMargin(references, new Insets(20, 20, 20, 40));

		VBox middleFour = new VBox(confFirstNameText, lastNameText, referencesText);
		VBox.setMargin(confFirstNameText, new Insets(50, 20, 20, 40));
		VBox.setMargin(lastNameText, new Insets(20, 20, 20, 40));
		VBox.setMargin(referencesText, new Insets(20, 20, 20, 40));

		// Combine VBoxs
		HBox middlePane = new HBox(middleOne, middleTwo, middleThree, middleFour);

		// Bottom pane for login button
		HBox bottomPane = new HBox(conButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(conButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane layout
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}
	
	public void listArticles(Stage primaryStage) throws Exception {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("List Articles");
		Label printList = new Label(databaseHelper1.displayArticles());

		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		printList.setFont(new Font("Arial", 15));

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Button design
		quitButton.setStyle("-fx-font-size: 2em;");

		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Top pane for welcome label
		HBox middlePane = new HBox(printList);
		middlePane.setAlignment(Pos.CENTER);
		HBox.setMargin(printList, new Insets(50, 0, 20, 0));

		// Top pane for welcome label
		HBox bottomPane = new HBox(quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(quitButton, new Insets(50, 0, 20, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void deleteArticle(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Delete an Article");
		Label username = new Label("Username: ");
		Label noExist = new Label("Username does not exist");

		TextField usernameText = new TextField();

		Button deletedButton = new Button("Delete");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		username.setFont(new Font("Arial", 20));

		noExist.setFont(new Font("Arial", 20));
		noExist.setStyle("-fx-text-fill: red;");
		noExist.setVisible(false);

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		deletedButton.setStyle("-fx-font-size: 2em;");


		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (usernameText.getText().isEmpty()) {
					usernameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
					return;
				} else {
					usernameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
				}

				if (databaseHelper1.doesArticleExist(usernameText.getText().strip())) {
					noExist.setVisible(false);
					try {
						databaseHelper1.deleteArticle(usernameText.getText().strip());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					noExist.setVisible(true);

				}
			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(username, usernameText, noExist);
		HBox.setMargin(username, new Insets(80, 80, 0, 130));
		HBox.setMargin(usernameText, new Insets(80, 80, 0, 0));
		HBox.setMargin(noExist, new Insets(80, 80, 0, 0));

		// Bottom pane for login button
		HBox bottomPane = new HBox(deletedButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(deletedButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void backupArticles(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Backup Articles");
		Label username = new Label("File name: ");

		TextField fileNameText = new TextField();

		Button deletedButton = new Button("Backup");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		username.setFont(new Font("Arial", 20));

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		deletedButton.setStyle("-fx-font-size: 2em;");


		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (fileNameText.getText().isEmpty()) {
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
					return;
				} else {
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
					try {
						databaseHelper1.backup(fileNameText.getText().strip());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(username, fileNameText);
		HBox.setMargin(username, new Insets(80, 80, 0, 130));
		HBox.setMargin(fileNameText, new Insets(80, 80, 0, 0));

		// Bottom pane for login button
		HBox bottomPane = new HBox(deletedButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(deletedButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

	public void restoreArticles(Stage primaryStage) {

		// Labels, buttons, textfield, alert, and checkBox
		Label welcome = new Label("Restore Articles");
		Label username = new Label("File name: ");

		TextField fileNameText = new TextField();

		Button deletedButton = new Button("Restore");
		Button quitButton = new Button("Quit");

		// Label design
		welcome.setFont(new Font("Arial", 36));
		username.setFont(new Font("Arial", 20));

		// Button design
		quitButton.setStyle("-fx-font-size: 1.5em;");
		deletedButton.setStyle("-fx-font-size: 2em;");


		// Send button action
		deletedButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (fileNameText.getText().isEmpty()) {
					fileNameText.setStyle("-fx-border-color: red; -fx-border-width: 2;");
					return;
				} else {
					fileNameText.setStyle("-fx-border-color: black; -fx-border-width: 2;");
					try {
						databaseHelper1.restore(fileNameText.getText().strip()); //restores articles with old file
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		// Quit button action
		quitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					start(primaryStage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		// Top pane for welcome label
		HBox topPane = new HBox(welcome);
		topPane.setAlignment(Pos.CENTER);
		HBox.setMargin(welcome, new Insets(50, 0, 20, 0));

		// Middle Pane
		HBox middlePane = new HBox(username, fileNameText);
		HBox.setMargin(username, new Insets(80, 80, 0, 130));
		HBox.setMargin(fileNameText, new Insets(80, 80, 0, 0));

		// Bottom pane for login button
		HBox bottomPane = new HBox(deletedButton, quitButton);
		bottomPane.setAlignment(Pos.CENTER);
		HBox.setMargin(deletedButton, new Insets(0, 220, 80, 280));
		HBox.setMargin(quitButton, new Insets(0, 0, 80, 0));

		// BorderPane stuff
		BorderPane adminCreateScreen = new BorderPane();
		adminCreateScreen.setTop(topPane);
		adminCreateScreen.setCenter(middlePane);
		adminCreateScreen.setBottom(bottomPane);
		adminCreateScreen.setStyle("-fx-background-color: lightblue;");

		// Set the scene
		Scene welcomeScene = new Scene(adminCreateScreen, 900, 600);
		primaryStage.setScene(welcomeScene);

	}

}

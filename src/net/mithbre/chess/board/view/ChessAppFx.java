package net.mithbre.chess.board.view;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class ChessAppFx extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// Load the FXML file.
		Parent parent = FXMLLoader.load(getClass().getResource("FXinterface.fxml"));

		// Build the scene graph.
		Scene scene = new Scene(parent);

		stage.setTitle("ChessFx");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}

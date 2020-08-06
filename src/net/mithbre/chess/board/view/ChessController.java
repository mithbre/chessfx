package net.mithbre.chess.board.view;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.mithbre.chess.board.model.Board;

public class ChessController implements Initializable {	
	String[] quips = {"", "Nothing to move.", "Wrong color being moved.", "Attempted to capture own piece.",
			"Piece is unable to move as requested.", "Queenside rook has already moved.",
			"Kingside rook has already moved.", "Nothing for pawn to capture.",
			"Illegal double push.", "This isn't leapfrog.", "Friendly King in check after moving.",
			"Pawns can't capture like that.", "Castling unavailable, a square\ninvolved is under attack.",
			"50 turn rule; STALEMATE"};
	Board board;
	ImageInput wPawn   = new ImageInput(new Image("file:resources/images/wPawn.png"));
	ImageInput bPawn   = new ImageInput(new Image("file:resources/images/bPawn.png"));
	ImageInput wRook   = new ImageInput(new Image("file:resources/images/wRook.png"));
	ImageInput bRook   = new ImageInput(new Image("file:resources/images/bRook.png"));
	ImageInput wKnight = new ImageInput(new Image("file:resources/images/wKnight.png"));
	ImageInput bKnight = new ImageInput(new Image("file:resources/images/bKnight.png"));
	ImageInput wBishop = new ImageInput(new Image("file:resources/images/wBishop.png"));
	ImageInput bBishop = new ImageInput(new Image("file:resources/images/bBishop.png"));
	ImageInput wQueen  = new ImageInput(new Image("file:resources/images/wQueen.png"));
	ImageInput bQueen  = new ImageInput(new Image("file:resources/images/bQueen.png"));
	ImageInput wKing   = new ImageInput(new Image("file:resources/images/wKing.png"));
	ImageInput bKing   = new ImageInput(new Image("file:resources/images/bKing.png"));

    
	@FXML
    private Rectangle a8, b8, c8, d8, e8, f8, g8, h8,
					  a7, b7, c7, d7, e7, f7, g7, h7,
					  a6, b6, c6, d6, e6, f6, g6, h6,
					  a5, b5, c5, d5, e5, f5, g5, h5,
					  a4, b4, c4, d4, e4, f4, g4, h4,
					  a3, b3, c3, d3, e3, f3, g3, h3,
					  a2, b2, c2, d2, e2, f2, g2, h2,
					  a1, b1, c1, d1, e1, f1, g1, h1;
	
	private ArrayList<Rectangle> allRectangles = new ArrayList<Rectangle>();
	
	private Rectangle previousSquare, endSquare;

    @FXML
    private Button promoteQueen, promoteRook, promoteBishop, promoteKnight;

    @FXML
    private Button newGame, endGame, importGame, exportGame;

    @FXML
    private GridPane promoteBar;

    @FXML
    private Label playerMessage;

    @FXML
    private TableView<Move> moveTable;// = new TableView<>();
    
    private ObservableList<Move> moveList;
    
    
    private boolean promotion = false;
    private char promoteType = ' ';
    private boolean promotionColor;
    private final boolean WHITE = true;
    private final boolean BLACK = false;
    private boolean halfMove = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	promoteBar.setDisable(true);
    	TableColumn<Move, String> colWhite = new TableColumn<Move, String>("White");
    	TableColumn<Move, String> colBlack = new TableColumn<Move, String>("Black");
    	moveTable.getColumns().addAll(colWhite, colBlack);
        colWhite.setCellValueFactory(new PropertyValueFactory<Move, String>("white"));
        colBlack.setCellValueFactory(new PropertyValueFactory<Move, String>("black"));
    }
    
    @FXML
    void newGameListener(ActionEvent event) {
    	moveList = FXCollections.observableArrayList(new Move("",""));

    	moveTable.setItems(moveList);
    	halfMove = false;
    	
    	board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    	previousSquare = null;
    	a1.setEffect(wRook);
    	b1.setEffect(wKnight);
    	c1.setEffect(wBishop);
    	d1.setEffect(wQueen);
    	e1.setEffect(wKing);
    	f1.setEffect(wBishop);
    	g1.setEffect(wKnight);
    	h1.setEffect(wRook);
    	a2.setEffect(wPawn);
    	b2.setEffect(wPawn);
    	c2.setEffect(wPawn);
    	d2.setEffect(wPawn);
    	e2.setEffect(wPawn);
    	f2.setEffect(wPawn);
    	g2.setEffect(wPawn);
    	h2.setEffect(wPawn);
    	
    	// python to the rescue...
    	//for i in ['3','4','5','6']:
    	//  for q in ['a','b','c','d','e','f','g','h']:
    	//	  print(q + i + ".setEffect(null);")
    	a3.setEffect(null);
    	b3.setEffect(null);
    	c3.setEffect(null);
    	d3.setEffect(null);
    	e3.setEffect(null);
    	f3.setEffect(null);
    	g3.setEffect(null);
    	h3.setEffect(null);
    	a4.setEffect(null);
    	b4.setEffect(null);
    	c4.setEffect(null);
    	d4.setEffect(null);
    	e4.setEffect(null);
    	f4.setEffect(null);
    	g4.setEffect(null);
    	h4.setEffect(null);
    	a5.setEffect(null);
    	b5.setEffect(null);
    	c5.setEffect(null);
    	d5.setEffect(null);
    	e5.setEffect(null);
    	f5.setEffect(null);
    	g5.setEffect(null);
    	h5.setEffect(null);
    	a6.setEffect(null);
    	b6.setEffect(null);
    	c6.setEffect(null);
    	d6.setEffect(null);
    	e6.setEffect(null);
    	f6.setEffect(null);
    	g6.setEffect(null);
    	h6.setEffect(null);
    	
    	a7.setEffect(bPawn);
    	b7.setEffect(bPawn);
    	c7.setEffect(bPawn);
    	d7.setEffect(bPawn);
    	e7.setEffect(bPawn);
    	f7.setEffect(bPawn);
    	g7.setEffect(bPawn);
    	h7.setEffect(bPawn);
    	a8.setEffect(bRook);
    	b8.setEffect(bKnight);
    	c8.setEffect(bBishop);
    	d8.setEffect(bQueen);
    	e8.setEffect(bKing);
    	f8.setEffect(bBishop);
    	g8.setEffect(bKnight);
    	h8.setEffect(bRook);
    	playerMessage.setText("");
    	
    	// why hello bodge.
    	// This is strictly for removing a piece from display after it's taken by en passant.
    	// If you have any ideas I'd love to hear them.
    	allRectangles.clear();
    	allRectangles.add(a1);
    	allRectangles.add(b1);
    	allRectangles.add(c1);
    	allRectangles.add(d1);
    	allRectangles.add(e1);
    	allRectangles.add(f1);
    	allRectangles.add(g1);
    	allRectangles.add(h1);
    	allRectangles.add(a2);
    	allRectangles.add(b2);
    	allRectangles.add(c2);
    	allRectangles.add(d2);
    	allRectangles.add(e2);
    	allRectangles.add(f2);
    	allRectangles.add(g2);
    	allRectangles.add(h2);
    	allRectangles.add(a3);
    	allRectangles.add(b3);
    	allRectangles.add(c3);
    	allRectangles.add(d3);
    	allRectangles.add(e3);
    	allRectangles.add(f3);
    	allRectangles.add(g3);
    	allRectangles.add(h3);
    	allRectangles.add(a4);
    	allRectangles.add(b4);
    	allRectangles.add(c4);
    	allRectangles.add(d4);
    	allRectangles.add(e4);
    	allRectangles.add(f4);
    	allRectangles.add(g4);
    	allRectangles.add(h4);
    	allRectangles.add(a5);
    	allRectangles.add(b5);
    	allRectangles.add(c5);
    	allRectangles.add(d5);
    	allRectangles.add(e5);
    	allRectangles.add(f5);
    	allRectangles.add(g5);
    	allRectangles.add(h5);
    	allRectangles.add(a6);
    	allRectangles.add(b6);
    	allRectangles.add(c6);
    	allRectangles.add(d6);
    	allRectangles.add(e6);
    	allRectangles.add(f6);
    	allRectangles.add(g6);
    	allRectangles.add(h6);
    	allRectangles.add(a7);
    	allRectangles.add(b7);
    	allRectangles.add(c7);
    	allRectangles.add(d7);
    	allRectangles.add(e7);
    	allRectangles.add(f7);
    	allRectangles.add(g7);
    	allRectangles.add(h7);
    	allRectangles.add(a8);
    	allRectangles.add(b8);
    	allRectangles.add(c8);
    	allRectangles.add(d8);
    	allRectangles.add(e8);
    	allRectangles.add(f8);
    	allRectangles.add(g8);
    	allRectangles.add(h8);
    	
    	promoteBar.setVisible(false);
    	promoteBar.setDisable(false);
    	
    	// make a fake movelist so I can see it working.
    	//tableView.setItems(moveList);
    	
    	//tableView.getColumns().addAll(columnWhite, columnBlack);
    	//TableColumn whiteMove = new TableColumn("White");
    	
    }

    void togglePromote() {
    	if (promoteBar.isVisible()) {
    		promoteBar.setVisible(false);
    	} else {
    		promoteBar.setVisible(true);
    	}
    }
    
    @FXML
    void endGameListener(ActionEvent event) {
    	System.out.println("Game is Adjourned.");
    	Stage stage = (Stage)endGame.getScene().getWindow();
        stage.close();
    }

    @FXML
    void exportGameListener(ActionEvent event) {
    	//System.out.println("Export game button clicked");
    	// open save window
    	// store as text
    	// one turn per line
    	// 2 moves per turn (white black)
    	FileChooser fileChooser = new FileChooser();
    	Scene scene = exportGame.getScene();
    	Window window = scene.getWindow();
    	Stage stage = (Stage) window;
    	 
        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MGN files (*.mgn)", "*.mgn"); // minimal game notation
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File mgn = fileChooser.showSaveDialog(stage);

        if (mgn != null) {
            saveNotation(board.getPreviousMoves(), mgn);
        }
    }
    
    private void saveNotation(ArrayList<String> content, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            for (String line: content) {
            	writer.write(line + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            System.out.println("Couldn't write file.");
        }
    }

    @FXML
    void importGameListener(ActionEvent event) {
    	System.out.println("Import game button clicked");
    	
    	FileChooser fileChooser = new FileChooser();
    	Scene scene = importGame.getScene();
    	Window window = scene.getWindow();
    	Stage stage = (Stage) window;
    	
    	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MGN files (*.mgn)", "*.mgn"); // minimal game notation
        fileChooser.getExtensionFilters().add(extFilter);
        File mgn = fileChooser.showOpenDialog(stage);
        
        if (mgn != null) {
            loadNotation(mgn);
        }
    }
    
    private void loadNotation(File filehandle) {
    	int lines = 0;
    	String move = "";
    	String[] half;
    	Rectangle start = new Rectangle(), end = new Rectangle();
    	
    	Pattern regex = Pattern.compile("^[a-h][1-8] [a-h][1-8]$");
    	
    	try {
    		newGameListener(null);
    		Scanner file = new Scanner(filehandle); // does line 376 mean nothing to you?
    		while (file.hasNext()) {
    			lines++;
    			move = file.nextLine();
    			
    			Matcher result = regex.matcher(move);
    			if (!result.find()) {
    				newGameListener(null);
    				playerMessage.setText("Error found on line " + lines + "\naborting load.");
    				return;
    			}
    			half = move.split(" ");
    			
    			for(Rectangle rec: allRectangles) {
    				if (half[0].contentEquals(rec.getId())) {
    					start = rec;
    					break;
    				}
    			}
    			
    			for(Rectangle rec: allRectangles) {
    				if (half[01].contentEquals(rec.getId())) {
    					end = rec;
    					break;
    				}
    			}
    			
    			moveHandler(start, end);
    		}
    		file.close();
    	}  catch (IOException ex) {
            System.out.println("Couldn't read file.");
        }
    	
 
    	
    	// for each line ask if it's a legit move pairing
    	// make move for the line if true, respond to the returns
    	// if 0, cool
    	// otherwise report the line and content as being illegal (load new game)
    }
    
    @FXML
    void promoteListener(ActionEvent event) {
    	if (event.getSource() == promoteQueen) {
    		//System.out.println("promoteListener: promote Queen");
    		promoteType = 'Q';
    		if (promotionColor) {
    			endSquare.setEffect(wQueen);
    		} else {
    			endSquare.setEffect(bQueen);
    		}
    	} else if (event.getSource() == promoteRook) {
    		//System.out.println("promoteListener: promote Rook");
    		promoteType = 'R';
    		if (promotionColor) {
    			endSquare.setEffect(wRook);
    		} else {
    			endSquare.setEffect(bRook);
    		}
    	} else if (event.getSource() == promoteBishop) {
    		//System.out.println("promoteListener: promote Bishop");
    		promoteType = 'B';
    		if (promotionColor) {
    			endSquare.setEffect(wBishop);
    		} else {
    			endSquare.setEffect(bBishop);
    		}
    	} else {
    		//System.out.println("promoteListener: promote Knight");
    		promoteType = 'N';
    		if (promotionColor) {
    			endSquare.setEffect(wKnight);
    		} else {
    			endSquare.setEffect(bKnight);
    		}
    	}
    	promoteBar.setVisible(false);
    	
    	moveHandler(previousSquare, endSquare);
    }

    @FXML
    void pieceSelectListener(MouseEvent event) {
    	if (promotion) {
    		playerMessage.setText("Promote your piece.");
    	} else if (event.getButton().equals(MouseButton.PRIMARY)) {
    		if (previousSquare == null) {
    			playerMessage.setText("");
    			// set first square selection
    			previousSquare = (Rectangle) event.getSource();
    			//System.out.printf("ChessController: %s selected\n", previousSquare.getId());
    			// probably should change some colors too...
    		} else if (previousSquare == (Rectangle) event.getSource()) {
    			// same square selected, remove.
    			previousSquare = null;
    			//System.out.printf("ChessController: deselected\n");
    		} else {
    			endSquare = (Rectangle) event.getSource();
    			//System.out.printf("ChessController: %s %s\n", previousSquare.getId(), endSquare.getId());
    			moveHandler(previousSquare, (Rectangle) event.getSource());
    			//previousSquare = null;
    		}
    		
    		// Display first half of the move in the table.
    		if (halfMove) {
    			// black
    			if (previousSquare != null) {
    				moveList.get(moveList.size() - 1).setBlack(previousSquare.getId());
    			} else {
    				moveList.get(moveList.size() - 1).setBlack("");
    			}
    		} else {
    			// white
    			if (previousSquare != null) {
    				moveList.get(moveList.size() - 1).setWhite(previousSquare.getId());
    			} else {
    				moveList.get(moveList.size() - 1).setWhite("");
    			}
    		}
    		moveTable.refresh();
    	}
    }
    
    
    void moveHandler(Rectangle start, Rectangle end) {
    	int response;
    	boolean swapImage = true;
    	
    	if (promotion) {
    		// It's obvious I didn't think of this situation very early on...
    		response = board.continueMove(previousSquare.getId() + " " + endSquare.getId(), promoteType);
    		promoteType = ' ';
    		promotion = false;
    		swapImage = false;
    	} else {
    		response = board.move(start.getId() + " " + end.getId());
    		//System.out.printf("ChessController: move()=%d\n\n\n", response);
    	}

    	// check to see if there's anything special we need to do.
    	String[] special = board.getSpecialMove();
    	if (special[0].contentEquals("")) {
    		//System.out.printf("ChessController: not a special move.\n");
    	} else {
    		if (special[1].contentEquals("")) {
    			// en passant
    			//System.out.printf("ChessController: en passant.\n");
    			for(Rectangle rec: allRectangles) {
    				if (special[0].contentEquals(rec.getId())) {
    					rec.setEffect(null);
    					break;
    				}
    			}
    		} else {
    			// castle
    			//System.out.printf("ChessController: castle.\n");
    			if (special[0].contentEquals(a1.getId())) {			// white queenside
    				d1.setEffect(a1.getEffect());
        			a1.setEffect(null);
    			} else if (special[0].contentEquals(h1.getId())) {	// white kingside
    				f1.setEffect(h1.getEffect());
        			h1.setEffect(null);
    			} else if (special[0].contentEquals(a8.getId())) {	// black queenside
    				d8.setEffect(a8.getEffect());
        			a8.setEffect(null);
    			} else if (special[0].contentEquals(h8.getId())) {	// black kingside
    				f8.setEffect(h8.getEffect());
        			h8.setEffect(null);
    			}
    		}
    		board.consumeSpecialMove();
    	}
    	
    	if (response <= 0) {
    		// Update the move in the table now that we know it's legal.
    		if (halfMove) {
    			// Black has moved
    			halfMove = false;
    			moveList.get(moveList.size() - 1).setBlack(start.getId() + " " + end.getId());
    			moveList.add(new Move("", ""));
    		} else {
    			// White has moved
    			halfMove = true;
    			moveList.get(moveList.size() - 1).setWhite(start.getId() + " " + end.getId());
    			
    		}
    		moveTable.refresh();
    		
    		
    		// prevent it from hiding promoted pieces.
    		if (swapImage) {
    			// swap image representation
    			end.setEffect(start.getEffect());
    			start.setEffect(null);
    		}
    		switch (response) {
    		case  -1: playerMessage.setText("CHECK"); break;
    		case  -2: playerMessage.setText("CHECKMATE"); break;
    		case  -3: playerMessage.setText("STALEMATE"); break;
    		case -10: playerMessage.setText("Promotion time baby!"); promote(WHITE); break;
    		case -11: playerMessage.setText("Promotion time baby!"); promote(BLACK); break;
    		}
    	} else if (response > 0) {
    		playerMessage.setText(quips[response]);
    	}
    	
    	//update table
    	if (!promotion) {
    		previousSquare = null;
    	}
    }

	private void promote(boolean color) {
		if (promoteType == ' ') {
			promoteBar.setVisible(true);
			promotion = true;
			promotionColor = color;
		} else {
			moveHandler(previousSquare, endSquare);
		}	
	}
	
	
	public static class Move {
		private final SimpleStringProperty white;
		private final SimpleStringProperty black;

		public Move(String white, String black) {
			this.white = new SimpleStringProperty(white);
			this.black = new SimpleStringProperty(black);
		}

		public String getWhite() {
			return white.get();
		}

		public String getBlack() {
			return black.get();
		}

		public void setWhite(String move) {
			this.white.set(move);
		}

		public void setBlack(String move) {
			this.black.set(move);
		}
	}
}

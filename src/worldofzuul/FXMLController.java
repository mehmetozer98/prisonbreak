/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worldofzuul;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


public class FXMLController implements Initializable {

    private Rectangle[][] roomNodes;
    private ObservableList itemsObservableList = FXCollections.observableArrayList();
    private ObservableList inventoryObservableList = FXCollections.observableArrayList();

    private Color colorCurrentRoom;
    private Color colorGuard;
    private Color colorGuardNext;
    private Color colorHelper;
    private Color colorFinish;

    @FXML
    private StackPane infopane;
    @FXML
    private TilePane tilePane;
    @FXML
    private ListView<?> inventoryListView;
    @FXML
    private ListView<?> itemsListView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label turnsText;
    @FXML
    private Label headerText;

    private Game game;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colorCurrentRoom = Color.GREEN;
        colorGuard = Color.RED;
        colorGuardNext = Color.PINK;
        colorHelper = Color.BURLYWOOD;
        colorFinish = Color.ORANGE;

        game = new Game();
        initDraw();
        drawGame();
        itemsListView.setItems(itemsObservableList);
        inventoryListView.setItems(inventoryObservableList);

        borderPane.setFocusTraversable(true);
        borderPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                switch (event.getCode()) {
                    case D:
                        game.goRoom(new Command(CommandWord.GO, "east"));
                        break;
                    case A:
                        game.goRoom(new Command(CommandWord.GO, "west"));
                        break;
                    case W:
                        game.goRoom(new Command(CommandWord.GO, "north"));
                        break;
                    case S:
                        game.goRoom(new Command(CommandWord.GO, "south"));
                        break;
                    case N:
                        restartGame();
                        break;
                    case P:
                        highscoreAlert();
                        break;
                    case E:
                        interactWithNpc();
                        break;
                    case I:
                        if (infopane.isDisabled()) {
                            infopane.setVisible(true);
                            infopane.setDisable(false);
                        } else {
                            infopane.setVisible(false);
                            infopane.setDisable(true);
                        }
                        break;
                    default:
                        break;
                }
                drawGame();
            }
        });

        itemsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (itemsListView.getSelectionModel().getSelectedItem() != null) {
                    Item item = (Item) itemsListView.getSelectionModel().getSelectedItem();
                    if (game.getInventory().addItem(item)) {
                        inventoryObservableList.add(itemsListView.getSelectionModel().getSelectedItem());
                        game.getCurrentRoom().removeItem(item);
                        itemsObservableList.remove(itemsListView.getSelectionModel().getSelectedItem());
                    } else {
                        headerText.setText("Inventory is full!");
                    }

                }

            }
        });

        inventoryListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (inventoryListView.getSelectionModel().getSelectedItem() != null) {
                    Item item = (Item) inventoryListView.getSelectionModel().getSelectedItem();
                    if (game.getCurrentRoom().addItem(item)) {
                        itemsObservableList.add(inventoryListView.getSelectionModel().getSelectedItem());
                        game.getInventory().removeItem(item);
                        inventoryObservableList.remove(inventoryListView.getSelectionModel().getSelectedItem());
                    }
                }
            }
        });

    }

    public void initDraw() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int roomsX = game.getRoomsX();
        int roomsY = game.getRoomsY();
        int gameWidth = (int) screenSize.getWidth() / 3;
        int gameHeight = (int) screenSize.getHeight() / 3;
        int roomWidth = gameWidth / roomsX;
        int roomHeight = gameWidth / roomsY;
        roomNodes = new Rectangle[roomsX][roomsY];
        tilePane.setPrefSize(gameWidth, gameHeight);

        // Inits rooms (Rectangles)
        for (int row = 0; row < roomsY; row++) {
            for (int col = 0; col < roomsX; col++) {
                Rectangle r = new Rectangle(roomWidth, roomHeight);
                r.setFill(Color.GREY);
                r.setStroke(Color.BLACK);
                r.setStrokeType(StrokeType.INSIDE);
                roomNodes[row][col] = r;
                tilePane.getChildren().add(r);
            }
        }

    }

    public void clearAll() {
        for (int row = 0; row < game.getRoomsY(); row++) {
            for (int col = 0; col < game.getRoomsX(); col++) {
                roomNodes[row][col].setFill(Color.GREY);
            }
        }
    }

    public void drawCurrentRoom() {
        for (int row = 0; row < game.getRoomsY(); row++) {
            for (int col = 0; col < game.getRoomsX(); col++) {
                if (game.getCurrentRoom() == game.getRooms()[row][col]) {
                    roomNodes[row][col].setFill(colorCurrentRoom);
                }
            }
        }
    }

    public void drawGuards() {
        for (int row = 0; row < game.getRoomsY(); row++) {
            for (int col = 0; col < game.getRoomsX(); col++) {
                if (game.getRooms()[row][col].hasGuards()) {
                    roomNodes[row][col].setFill(colorGuard);
                }
            }
        }
    }

    public void drawHelper() {
        for (int row = 0; row < game.getRoomsY(); row++) {
            for (int col = 0; col < game.getRoomsX(); col++) {
                if (game.getRooms()[row][col].hasHelper()) {
                    roomNodes[row][col].setFill(colorHelper);
                    return;
                }
            }
        }
    }

    public void drawGuardsNext() {
        ArrayList<Room> guardsNext = new ArrayList<>();
        for (NPC npc : game.getNpcs()) {
            if (npc instanceof NPC_Guard) {
                guardsNext.add(((NPC_Guard) npc).getNextRoom());
            }
        }
        for (int row = 0; row < game.getRoomsY(); row++) {
            for (int col = 0; col < game.getRoomsX(); col++) {
                Room r = game.getRooms()[row][col];
                for (Room guardNext : guardsNext) {
                    if (guardNext == r) {
                        roomNodes[row][col].setFill(colorGuardNext);
                    }
                }
            }
        }
    }

    public void drawVoid() {
        for (int row = 0; row < game.getRoomsY(); row++) {
            for (int col = 0; col < game.getRoomsX(); col++) {
                if (game.getRooms()[row][col].getRoomType() == Room.ROOMTYPE.VOID) {
                    roomNodes[row][col].setFill(Color.BLACK);
                }
            }
        }
    }

    public void drawFinish() {
        for (int row = 0; row < game.getRoomsY(); row++) {
            for (int col = 0; col < game.getRoomsX(); col++) {
                if (game.getRooms()[row][col].isIsFinish()) {
                    roomNodes[row][col].setFill(colorFinish);
                }
            }
        }
    }

    public void drawItems() {
        itemsObservableList.clear();
        for (Item item : game.getCurrentRoom().getItems()) {
            itemsObservableList.add(item);
        }
    }

    public void drawInventory() {
        inventoryObservableList.clear();
        for (Item item : game.getInventory().getItems()) {
            inventoryObservableList.add(item);
        }
    }

    public void setTurnsText() {
        turnsText.setText("Turns: " + game.getTimer().getAttemptTurns() + "/" + game.getTimer().getMaxTurns());
    }

    public void resetHeaderText() {
        headerText.setText("This is Prison Break!");
    }

    private void interactWithNpc() {
        Quest q = game.getQuest();
        Item qItem = q.getQuestItem();
        Item qReward = q.getQuestReward();
        if (game.interactWithNPC()) {
            drawInventory();
            showAlert("Quest complete!", "[" + qReward + "] was added to your inventory!");
        } else if (!game.interactWithNPC() && game.getCurrentRoom().hasHelper()) {
            showAlert("Quest not complete!", "Find [" + qItem + "] and give it to me.");
        } else {
            showAlert("Nothing to interact with", "Find [" + qItem + "] and give it to the helper.");
        }
    }

    public void checkBusted() {
        if (game.isBusted()) {
            showAlert("BUSTED!", "You were sent back to your prison cell!");
        }
    }

    private void checkFinished() {
        System.out.println("Is finished: " + game.isFinished());
        if (game.isFinished()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("You won!");
            alert.setHeaderText("You escaped prison in " + game.getTimer().turnsToYears());
            alert.setContentText("Do you want to play again?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                restartGame();
            } else {
                System.exit(0);
            }
        }
    }

    private void checkLost() {
        if (game.isLost()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("You lost!");
            alert.setHeaderText("You used too many turns (" + game.getTimer().getAttemptTurns() + ")");
            alert.setContentText("Do you want to play again?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                restartGame();
            } else {
                System.exit(0);
            }
        }
    }

    private void highscoreAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Highscore");
        alert.setHeaderText(null);
        alert.setContentText("highscore should be here here");

        alert.showAndWait();
    }

    private void restartGame() {
        game = new Game();
        clearLists();
        drawGame();
    }
    
    private void clearLists() {
        itemsObservableList.clear();
        inventoryObservableList.clear();
    }

    private void drawGame() {
        clearAll();
        drawCurrentRoom();
        drawGuards();
        drawGuardsNext();
        drawHelper();
        drawVoid();
        drawFinish();
        drawItems();
        resetHeaderText();
        setTurnsText();
        checkFinished();
        checkLost();
        checkBusted();
    }
    
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        
        alert.showAndWait();
    }

}

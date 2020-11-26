package worldofzuul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Game {

    private Room currentRoom;
    private int roomsX;
    private int roomsY;
    private int startX;
    private int startY;
    private Room startRoom;
    Room[][] rooms;
    private Timer timer;
    private Inventory inventory;
    private ArrayList<NPC> npcs;
    private LevelGenerator levelGenerator;
    private CommandLineUI commandLineUI;
    private boolean finished = false;
    private boolean lost = false;
    private boolean busted = false;
    private Quest quest;

    public Game() {
        inventory = new Inventory(3); // Set default inventory size to 3
        roomsX = 21;
        roomsY = 21;
        startX = 0;
        startY = 0;
        rooms = new Room[roomsY][roomsX];
        levelGenerator = new LevelGenerator(rooms, startX, startY);
        createRooms();
        npcs = new ArrayList<NPC>();
        createNPCs();
        createItems();
        timer = new Timer(500);
        commandLineUI = new CommandLineUI(new Parser());

    }

    private void createRooms() {
        int facultyMaxRadius = 2;
        int facultyAmount = 15;
        levelGenerator.createRooms();
        //sets correct exits for all rooms
        levelGenerator.createExits();
        //creates random points that are within the alowed area disided by max radius of clusters
        ArrayList<int[]> points = levelGenerator.getPoints(facultyMaxRadius, facultyAmount);
        //creates arraylist for each cluster
        ArrayList<ArrayList<Room>> clusters = levelGenerator.getSpacedRooms(points);

        //risizes the clusters to a random size within their max size
        for (int i = 0; i < clusters.size(); i++) {
            clusters.set(i, levelGenerator.ExpandedFaculty(clusters.get(i), facultyMaxRadius));
        }
        // set current room to first faculty
        startRoom =levelGenerator.getRandomRoomFromArray(clusters.get(0));
        currentRoom = startRoom;
        // set finish room
        Room finishRoom = levelGenerator.getRandomRoomFromArray(clusters.get(clusters.size() - 1));
        finishRoom.setFinish(true);

        for (int i = 0; i < points.size() - 1; i++) {
            LevelGenerator.setRoomTypeAll(levelGenerator.getPath(points.get(i), points.get(i + 1)), Room.ROOMTYPE.HALLWAY);
        }

        currentRoom.setRoomType(Room.ROOMTYPE.CELL);
        finishRoom.setRoomType(Room.ROOMTYPE.HALLWAY);
        System.out.println(clusters.get(0).size());
        LevelGenerator.setRoomTypeAll(clusters.get(0), Room.ROOMTYPE.CELL);

        levelGenerator.removeExitsFromVoid();
    }

    private void createNPCs() {

        levelGenerator.createNPC(new NPC_Guard(), npcs);
        levelGenerator.createNPC(new NPC_Guard(), npcs);
        levelGenerator.createNPC(new NPC_Helper("Morgan Freeman"), npcs);

        levelGenerator.setNPCRandomPosition(npcs);

    }

    private void createItems() {
        levelGenerator.placeItemRandom(new Item_Weapon("AK-47", 50));
        levelGenerator.placeItemRandom(new Item_Weapon("M4A1", 35));
        levelGenerator.placeItemRandom(new Item_Weapon("Deagle", 75));
        Item bagOfCoke = new Item("Coke");
        Item keyItem = new Item_Key("Key");
        Quest keyQuest = new Quest("Find the bag with coke and go to the helper, he will give you a key to freedom.", bagOfCoke, keyItem);
        this.quest = keyQuest;
        levelGenerator.placeItemRandom(bagOfCoke);

    }

    public void play() {
        commandLineUI.printWelcome();
        discoverCurrentRoom();

        while (!finished && !lost) {
            Command command = commandLineUI.getParser().getCommand();
            finished = processCommand(command);
            checkFinished();
            checkLost();
        }
        if (lost == true) {
            commandLineUI.print("GAME OVER!\t you've died in prison of old age");
        } else if (finished == true) {
            commandLineUI.print("You've escaped prison in " + timer.turnsToYears() + "\t Well done!");
        }
    }

    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        if (commandWord == CommandWord.UNKNOWN) {
            commandLineUI.print("I don't know what you mean...");
            return false;
        }

        if (commandWord == CommandWord.HELP) {
            commandLineUI.printHelp();
        } else if (commandWord == CommandWord.GO) { // Need to check if valid second word
            goRoom(command);
        } else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        } else if (commandWord == CommandWord.PICKUP) {
            pickupItem(command);
        } else if (commandWord == CommandWord.DROP) {
            dropItem(command);
        } else if (commandWord == CommandWord.INVENTOERY) {
            System.out.println(inventory);
        } else if (commandWord == CommandWord.INTERACT) {
            interactWithNPC();
        }

        return wantToQuit;
    }

    private void moveGuards() {
        for (NPC npc : npcs) {
            if (npc instanceof NPC_Guard) {
                NPC_Guard guard = (NPC_Guard) npc;
                guard.goNextRoom();
            }
        }
    }

    private void pickupItem(Command command) {
        if (!command.hasSecondWord()) {
            commandLineUI.print("Drop what?");
            commandLineUI.printGameLayout(timer, npcs, rooms, currentRoom, quest);
            return;
        }

        try {
            int selectedItemIndex = Integer.valueOf(command.getSecondWord());

            if (selectedItemIndex > currentRoom.getItems().size() - 1) {
                commandLineUI.print("That is not a valid item.");
                return;
            }

            Item selectedItem = currentRoom.getItems().get(selectedItemIndex);
            if (inventory.addItem(selectedItem)) {
                currentRoom.getItems().remove(selectedItem);
                commandLineUI.print(selectedItem.getName() + " was added to your inventory.");
                commandLineUI.print(currentRoom.getItemsString());
            } else {
                commandLineUI.print("You don't have enough space to add that.");
            }

        } catch (NumberFormatException e) {
            commandLineUI.print("That is not a valid item.");
            return;
        }

    }

    private void dropItem(Command command) {
        if (!command.hasSecondWord()) {
            commandLineUI.print("Drop what?");
            return;
        }

        try {
            int selectedItemIndex = Integer.valueOf(command.getSecondWord());

            if (selectedItemIndex > inventory.getSize() - 1) {
                commandLineUI.print("Your inventory is not that big! Try a smaller number");
                return;
            }

            Item selectedItem = inventory.getItem(selectedItemIndex);
            inventory.removeItem(selectedItem);
            currentRoom.addItem(selectedItem);
            commandLineUI.print("You have dropped " + selectedItem.getName());

        } catch (NumberFormatException e) {
            commandLineUI.print("That is not a valid item");
            return;
        }

    }

    public void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            commandLineUI.print("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            commandLineUI.print("There is no door!");
        } else {
            if (checkBusted(nextRoom)) {
                busted();
            } else {
                currentRoom = nextRoom;
                busted = false;
            }
            timer.countAttempt();
            moveGuards();
            discoverCurrentRoom();
            checkFinished();
            checkLost();
        }
    }

    private void discoverCurrentRoom() {
        commandLineUI.printGameLayout(timer, npcs, rooms, currentRoom, quest);

        if (currentRoom.hasItems()) {
            commandLineUI.printRoomItems(currentRoom);
        }
    }

    private boolean quit(Command command) {
        if (command.hasSecondWord()) {
            commandLineUI.print("Quit what?");
            return false;
        } else {
            return true;
        }
    }

    private void busted() {
        // commandLineUI.printBusted();
        busted = true;
        currentRoom = startRoom;
        commandLineUI.printGameLayout(timer, npcs, rooms, currentRoom, quest);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public int getRoomsX() {
        return roomsX;
    }

    public int getRoomsY() {
        return roomsY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public Room[][] getRooms() {
        return rooms;
    }

    public Timer getTimer() {
        return timer;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ArrayList<NPC> getNpcs() {
        return npcs;
    }

    private void checkFinished() {
        if (currentRoom.isIsFinish() == true) {
            if (inventory.hasKey()) {
                finished = true;
            } else {
                commandLineUI.print("You need to find the key, to finish the game!");
            }
        }
    }

    private void checkLost() {
        if (timer.isMax()) {
            lost = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isLost() {
        return lost;
    }

    private boolean checkBusted(Room nextRoom) {
        if (currentRoom.hasGuards()) {
            return true;
        } else if (nextRoom.hasGuards()) {
            for (NPC npc : nextRoom.getNPCs()) {
                if (npc instanceof NPC_Guard) {
                    if (((NPC_Guard) npc).getNextRoom() == currentRoom) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isBusted() {
        return busted;
    }

    public boolean interactWithNPC() {
        if (currentRoom.hasHelper() && NPC_Helper.checkQuest(quest, inventory)) {
            return true;
        }
        return false;
    }
    
    public Quest getQuest() {
        return quest;
    }
}

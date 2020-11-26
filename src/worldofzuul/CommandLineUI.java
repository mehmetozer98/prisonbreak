package worldofzuul;

import java.util.ArrayList;

public class CommandLineUI {
    
    private Parser parser;

    public CommandLineUI(Parser parser) {
        this.parser = parser;
    }

    public void printWelcome() {
        System.out.println();
        System.out.println("Welcome to Prison Break!");
        System.out.println("Prison Break is a new, incredibly funny adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
    }
    
    public void printHelp() {
        System.out.println("You are in a prison a need to escape. Try to dodge the guards.");
        System.out.println("In your cells");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    public void printRoomItems(Room room) {
        System.out.println(room.getItemsString());
    }
    
    public void printGameLayout(Timer timer, ArrayList<NPC> npcs, Room[][] rooms, Room currentRoom, Quest quest) {
        System.out.println("Quest: " + quest.getDescription());
        System.out.println("Turns used: " + timer.getAttemptTurns());
        System.out.println("(P) Player, (G) Guard,  (!) Guard next pos, (F) Finish (H) Helper\n");
        ArrayList<Room> guardsNext = new ArrayList<>();
        for (NPC npc : npcs) {
            if (npc instanceof NPC_Guard) {
                guardsNext.add(((NPC_Guard) npc).getNextRoom());
            }
        }
        for (int row = 0; row < rooms.length; row++) {
            String infoString = "";
            for (int col = 0; col < rooms[row].length; col++) {
                Room r = rooms[row][col];
                String hasPlayer = r == currentRoom ? "(P)" : "";
                String hasGuard = r.hasGuards() ? "(G)" : "";
                String hasHelper = r.hasHelper() ? "(H)" : "";
                String hasNextGuard = "";
                for (Room guardNext : guardsNext) {
                    if (guardNext == r) {
                        hasNextGuard = "(!)";
                    }
                }
                String isFinish = r.isIsFinish() ? "(F)" : "";
                if (r.getRoomType() == Room.ROOMTYPE.VOID) {
                    System.out.printf("%-16s", "o"
                            + "");
                } else {
                    System.out.printf("%-16s", r.getRoomType());
                }
                infoString += String.format("%-16s", (hasPlayer + hasGuard + hasHelper + isFinish + hasNextGuard));  //hasPlayer + hasGuard+ hasHelper + isFinish + hasNextGuard + "\t\t";

            }
            System.out.println("");
            System.out.println(infoString);
            System.out.println("");
        }
    }
    
    public void printBusted() {
        String str = "";
        str += "██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗\n";
        str += "██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔═██╗\n";
        str += "██████╔╝██║   ██║███████╗     ██║     █████╗   ██║  ██║\n";
        str += "██╔══██╗██║   ██║╚════██║     ██║     ██╔══╝   ██║  ██║\n";
        str += "██████╔╚██████╔╝███████║     ██║     ███████╗█████╔╝\n";
        str += "╚═════╝  ╚═════╝ ╚══════╝     ╚═╝     ╚══════╝╚════╝\n";
        str += "Moving you back your own cell in ";
        System.out.print(str);
        int secondsCount = 5;
        while (secondsCount > 0) {
            System.out.print(secondsCount + "... ");
            try {
                Thread.sleep(1000);
                secondsCount--;
            } catch (InterruptedException ex) {
                System.out.println("Error in countdown");
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("");
    }
    
    public void print(String msg) {
        System.out.println(msg);
    }

    public Parser getParser() {
        return parser;
    }
}

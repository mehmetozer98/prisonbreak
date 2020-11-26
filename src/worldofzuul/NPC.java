package worldofzuul;

import interfaces.Named;
import java.util.ArrayList;

public class NPC implements Named{

    private String name;
    private Room currentRoom;

    //For naming the NPC
    public NPC(String name) {
        this.name = name;
    }
    //For randomly naming the NPC
    public NPC() {
        this.name = Named.giveName();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    
}

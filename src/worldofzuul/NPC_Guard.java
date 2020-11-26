package worldofzuul;

import java.util.ArrayList;
import java.util.List;

public class NPC_Guard extends NPC {
    
    private Room nextRoom;
    private Room previousRoom;
    
    public void setNextRoom() {
        ArrayList<Room> exits = new ArrayList<Room>(this.getCurrentRoom().getExits().values());
        if (exits.size() > 1) {
            exits.remove(previousRoom);
        }
        int exitsCount = exits.size();
        int randomExit = (int)(Math.random() * exitsCount);
        nextRoom = exits.get(randomExit);
    }

    public void goNextRoom() {
        previousRoom = getCurrentRoom();
        getCurrentRoom().removeNPC(this);
        nextRoom.addNPC(this);
        setCurrentRoom(nextRoom);
        setNextRoom();
    }

    public Room getNextRoom() {
        return nextRoom;
    }
    
    
    // Use this when u need a guard with a specific name
    public NPC_Guard(String name) {
        super(name);
    }
public NPC_Guard() {
        super();
    }
}

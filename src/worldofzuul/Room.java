package worldofzuul;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

public class Room {

    private String description;
    private HashMap<String, Room> exits;
    private ArrayList<NPC> NPCs;
    private ArrayList<Item> items;
    private boolean isFinish;
    private ROOMTYPE roomType;

    public enum ROOMTYPE {
        VOID(0),
        HALLWAY(1),
        CELL(2),
        KITCHEN(3);

        private final int value;

        private ROOMTYPE(int newValue) {
            this.value = newValue;
        }

        public int getValue() {
            return value;
        }
    }

    public Room(String description) {
        this.roomType = roomType;
        this.description = description;
        exits = new HashMap<String, Room>();
        NPCs = new ArrayList<>();
        items = new ArrayList<>();
        roomType = Room.ROOMTYPE.VOID;
    }

    public boolean setFinish(boolean b) {
        isFinish = b;
        return isFinish;
    }

    public boolean isIsFinish() {
        return isFinish;
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public void removeExit(String direction) {
        exits.remove(direction);
    }

    public String getShortDescription() {
        return description + " [" + roomType + "]";
    }

    public String getLongDescription() {
        return "You are " + getShortDescription() + ".\n" + getExitString();
    }

    private String getExitString() {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for (String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public void addNPC(NPC npc) {
        this.NPCs.add(npc);
        npc.setCurrentRoom(this);
    }

    public void removeNPC(NPC npc) {
        this.NPCs.remove(npc);
    }

    public boolean hasNPCs() {
        return NPCs.size() > 0 ? true : false;
    }

    public String getNPCString() {
        return "These NPC(s) are in this room: " + NPCs.toString();
    }

    public boolean addItem(Item item) {
        return items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public boolean hasItems() {
        return items.size() > 0 ? true : false;
    }

    public String getItemsString() {
        String returnString = "Items in this room:";
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            returnString += " (" + i + ") " + item;
        }
        return returnString;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public HashMap<String, Room> getExits() {
        return exits;
    }

    public boolean hasGuards() {
        for (NPC npc : NPCs) {
            if (npc instanceof NPC_Guard) {
                return true;
            }
        }
        return false;
    }

    public boolean hasHelper() {
        for (NPC npc : NPCs) {
            if (npc instanceof NPC_Helper) {
                return true;
            }
        }
        return false;
    }

    public boolean hasKey() {
        for (Item item : items) {
            if (item instanceof Item_Key) {
                return true;
            }
        }
        return false;
    }
    public boolean hasWeapon() {
        for (Item item : items) {
            if (item instanceof Item_Weapon) {
                return true;
            }
        }
        return false;
    }
    

    public ArrayList<NPC> getNPCs() {
        return NPCs;
    }

    public ROOMTYPE getRoomType() {
        return roomType;
    }

    public void setRoomType(ROOMTYPE roomType) {
        this.roomType = roomType;
    }

}

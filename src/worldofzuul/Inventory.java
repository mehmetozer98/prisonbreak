package worldofzuul;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Item> items;
    private int size;
    
    

    public Inventory(int size) {
        this.items = new ArrayList<>();
        this.size = size;
    }
    
    public boolean addItem(Item item) {
        if (items.size() < size) {
            return items.add(item);
            
        } else {
            return false;
        }
        
    }
    
    public Item getItem(int index) {
            return items.get(index);
    }
    
    public void removeItem(Item item) {
        items.remove(item);
    }
    
    public int getSize() {
        return items.size();
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "Your inventory is empty.";
        }
        String str = "Inventory: ";
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            str += " (" + i + ") " + item;
        }
        return str;
    }
    
    public boolean hasKey() {
        for (Item item : items) {
            if (item instanceof Item_Key) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    
    public boolean hasItem(Item item) {
        for (Item itemInventory : items) {
            if (itemInventory == item) {
                return true;
            }
        }
        return false;
    }
    
    
}

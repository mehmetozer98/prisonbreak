  package worldofzuul;

public class Item {
    private String name;
    private int id;
    private static int idCount = 0;

    public Item(String name) {
        this.name = name;
        this.id = idCount;
        idCount++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}

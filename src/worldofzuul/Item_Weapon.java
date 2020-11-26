package worldofzuul;

public class Item_Weapon extends Item {
    
    private int damage;
    
    public Item_Weapon(String name, int damage) {
        super(name);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
    
}

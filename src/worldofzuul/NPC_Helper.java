package worldofzuul;

public class NPC_Helper extends NPC {

    public NPC_Helper(String name) {
        super(name);
    }

    public static boolean checkQuest(Quest quest, Inventory inventory) {
        if (inventory.hasItem(quest.getQuestItem())) {
            inventory.removeItem(quest.getQuestItem());
            inventory.addItem(quest.getQuestReward());
            return true;
        }
        return false;
    }

}

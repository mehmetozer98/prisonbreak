package worldofzuul;

public class Quest {

    private String description;
    private Item questItem;
    private Item questReward;

    public Quest(String description, Item questItem, Item questReward) {
        this.description = description;
        this.questItem = questItem;
        this.questReward = questReward;
    }

    public Item getQuestReward() {
        return questReward;
    }

    public String getDescription() {
        return description;
    }

    public Item getQuestItem() {
        return questItem;
    }

    public void questReward() {
        
    }

}

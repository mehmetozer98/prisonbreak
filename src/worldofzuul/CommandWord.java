package worldofzuul;

public enum CommandWord
{
    INVENTOERY("inventory"), PICKUP("pickup"), DROP("drop"), GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), INTERACT("interact");
    
    private String commandString;
    
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }
    
    public String toString()
    {
        return commandString;
    }
}

package Command;

abstract public class AbstractCommand {
    protected String command;

    public AbstractCommand(String command) {
        this.command = command;
    }

    abstract public void execute();
}

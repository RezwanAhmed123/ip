package mortalreminder;

import mortalreminder.backend.Processor;
import mortalreminder.backend.Storage;
import mortalreminder.backend.TaskList;
import mortalreminder.io.FormattedPrinting;

/**
 * Main class of the MortalReminder App.
 */
public class MortalReminder {

    private final Processor processor = new Processor();

    private TaskList taskList = new TaskList();

    /**
     * This method loads all tasks from storage and greets the user.
     *
     * @return Welcome message with initial greeting to the user.
     */
    public String welcome() {
        this.taskList = Storage.loadTaskListFromFile();
        return FormattedPrinting.welcome();
    }

    public Processor getProcessor() {
        return processor;
    }

    public TaskList getTaskList() {
        return taskList;
    }

}

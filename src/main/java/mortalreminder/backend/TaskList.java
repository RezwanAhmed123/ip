package mortalreminder.backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import mortalreminder.MortalReminder;
import mortalreminder.errorhandling.MortalReminderException;
import mortalreminder.io.FormattedPrinting;
import mortalreminder.tasks.Task;

// The JavaDocs were generated using ChatGPT with minor edits

/**
 * Manages a list of tasks and provides operations to add, delete, retrieve, and clear tasks.
 * <p>
 * The {@code TaskList} class encapsulates an {@link ArrayList} of {@link Task} objects and
 * offers methods to manipulate the list, such as adding new tasks, deleting existing ones,
 * and retrieving tasks by index. It also handles the interaction with storage to add the task to
 * long term storage and formatted printing for printing list to the user.
 */
public class TaskList {
    protected final ArrayList<Task> taskList = new ArrayList<>();

    /**
     * Retrieves the task at the specified index from the list.
     * <p>
     * If the index is out of bounds or the list is empty, an appropriate message is printed
     * and {@code null} is returned.
     *
     * @param index the index of the task to retrieve.
     * @return the {@link Task} at the specified index, or {@code null} if the index is invalid.
     */
    public Task getTask(int index) throws MortalReminderException {
        try {
            return this.taskList.get(index);
        } catch (IndexOutOfBoundsException e) {
            if (this.getSize() == 0) {
                throw new MortalReminderException("List is empty!");
            }
            throw new MortalReminderException("Invalid task number!\n"
                    + "Please input a number between 1 and "
                    + this.getSize());
        }
    }

    public boolean isEmpty() {
        return this.taskList.isEmpty();
    }

    /**
     * Adds a task to the list and updates the storage file.
     * <p>
     * If the task's description is not empty, the task is added to the list,
     * appended to the storage file, and a confirmation message is printed.
     * Otherwise, an error message is printed.
     *
     * @param task the {@link Task} to add.
     * @return string of confirmation message if adding was successful and an error message if not.
     */
    public String addTask(Task task) throws MortalReminderException {
        if (!Objects.equals(task.getDescription().trim(), "")) {
            Storage.appendToListFile(task);
            this.taskList.add(task);
            return FormattedPrinting.addTask(task, this);
        } else {
            throw new MortalReminderException("Description cannot be empty!");
        }
    }

    /**
     * Loads a task directly into the list from the storage file. Thus, in this method, the task is not added
     * back into the storage file.
     * <p>
     * This method is typically used only internally when the task has already been added to the app
     * before such as when loading tasks from a file or creating a {@link TaskList} of upcoming tasks.
     *
     * @param task the {@link Task} to load into the list.
     */
    public void loadTask(Task task) {
        this.taskList.add(task);
    }


    /**
     * Deletes a task from the list and updates the storage file.
     * <p>
     * If the task's description is not empty, the task is removed from the list,
     * deleted from the storage file, and a confirmation message is printed.
     * Otherwise, an error message is printed.
     *
     * @param task the {@link Task} to delete.
     * @return a string of confirmation or error message if the task cannot be deleted.
     */
    public String deleteTask(Task task) throws MortalReminderException {
        if (!Objects.equals(task.getDescription().trim(), "")) {
            this.taskList.remove(task);
            Storage.refreshStorageFile(this);
            return FormattedPrinting.deleteTask(task, this);
        } else {
            throw new MortalReminderException("Invalid Task to be deleted!");
        }
    }

    /**
     * Finds and returns the task based on descriptions matching the descriptions passed in.
     *
     * @param descriptions string argument(s) we are looking for in all matching tasks.
     * @return TaskList of matching tasks that contain the description string passed into the method call.
     */
    public TaskList findTasks(String... descriptions) {
        TaskList similarTasks = new TaskList();
        for (Task task : this.taskList) {
            for (String description : descriptions) {
                if (task.getDescription().contains(description.trim())
                        && !similarTasks.taskList.contains(task)) {
                    similarTasks.loadTask(task);
                }
            }
        }
        return similarTasks;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return the size of the task list.
     */
    public int getSize() {
        return this.taskList.size();
    }

    /**
     * Clears all tasks from the list and prints a confirmation message.
     *
     * @return A string of the confirmation message of the tasks being cleared.
     */
    public String clearList() {
        this.taskList.clear();
        return FormattedPrinting.clearList();
    }

    /**
     * Returns a string representation of the task list in a format suitable for saving to a file.
     * <p>
     * Each task is converted to its file format and appended to the output string.
     *
     * @return a string representation of the task list.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Task task : this.taskList) {
            output.append(task.convertToFileFormat()).append("\n");
        }
        return output.toString();
    }
}

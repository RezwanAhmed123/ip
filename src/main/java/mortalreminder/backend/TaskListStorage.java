package mortalreminder.backend;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import mortalreminder.backend.tasklistmanager.TaskList;
import mortalreminder.errorhandling.MortalReminderException;
import mortalreminder.io.Parser;
import mortalreminder.tasks.Task;

// The javadocs were autogenerated using ChatGPT with minor edits

/**
 * Handles the manipulation of data to and from a specified storage file.
 * <p>
 * The {@code Storage} class provides methods to append tasks to a file, clear the file,
 * delete specific tasks, and load tasks from the file. It interacts with the file system
 * and ensures that tasks are stored and retrieved correctly.
 */
public class TaskListStorage {
    protected static final String STORAGE_LIST_FILE_PATH = "src/main/resources/data/listStorage.txt";

    /**
     * Initialises the storage on first start of the App.
     */
    public static void initialise() throws MortalReminderException {
        try {
            File f = new File(STORAGE_LIST_FILE_PATH);
            f.getParentFile().mkdirs();
            f.createNewFile();
            f.exists();
        } catch (IOException e) {
            throw new MortalReminderException("File Creation Problem!");
        }
    }

    /**
     * Appends a task to the storage file.
     * <p>
     * This method opens the storage file in append mode and writes the task in its file format
     * to the file. If an {@link IOException} occurs, an error message is printed.
     *
     * @param task the {@link Task} to append to the file.
     * @throws MortalReminderException if the file cannot be found.
     */
    public static void appendToListFile(Task task) throws MortalReminderException {
        try {
            FileWriter fw = new FileWriter(STORAGE_LIST_FILE_PATH, true);
            String textToAdd = task.convertToFileFormat();
            fw.write(textToAdd + System.lineSeparator());
            fw.close();
        } catch (IOException e) {
            throw new MortalReminderException("Corrupted storage file! Please refresh using clear_tasks.");
        }
    }

    /**
     * Clears the storage file.
     * <p>
     * This method deletes all content from the storage file by opening it in write mode
     * and writing an empty string. If an {@link IOException} occurs, an error message is printed.
     *
     * @throws MortalReminderException if the file cannot be found from the hardcoded path.
     */
    public static void clearListFile() throws MortalReminderException {
        try {
            FileWriter fw = new FileWriter(STORAGE_LIST_FILE_PATH);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            throw new MortalReminderException("File cannot be found!");
        }
    }

    /**
     * Deletes all tasks from the storage file and re-appends tasks from the given {@link TaskList}.
     * <p>
     * This method refreshes the file after mark, unmark or delete operations by first clearing the file
     * and adding all the tasks back into the file.
     * This effectively updates the file to reflect the current state of the task list.
     * The method was inspired from the
     * <a href="https://stackoverflow.com/questions/5800603/delete-specific-line-from-java-text-file">
     * following post.</a>
     *
     * @param taskList the {@link TaskList} containing tasks to re-append to the file.
     * @throws MortalReminderException if there is an error inside the clearListFile() method.
     */
    public static void refreshStorageFile(TaskList taskList) throws MortalReminderException {
        clearListFile();
        for (int i = 0; i < taskList.getSize(); i++) {
            appendToListFile(taskList.getTask(i));
        }
    }

    /**
     * Loads tasks from the storage file into a {@link TaskList}.
     * <p>
     * This method reads each line from the storage file, converts it into a {@link Task},
     * and loads it into a new {@link TaskList} object. If the file does not exist or cannot be created,
     * an error message is printed, and a new, empty {@link TaskList} is returned.
     *
     * @return a {@link TaskList} containing tasks loaded from the file, or an empty {@link TaskList} if loading fails.
     * @throws MortalReminderException if file is unreadable or cannot be created.
     */
    public static TaskList loadTaskListFromFile() throws MortalReminderException {
        try {
            File f = new File(STORAGE_LIST_FILE_PATH);
            boolean checkFileExists = f.getParentFile().mkdirs() || f.createNewFile() || f.exists();
            Scanner s = new Scanner(f);
            TaskList taskList = new TaskList();
            while (s.hasNextLine()) {
                String input = s.nextLine();
                Task task = Parser.parseInputFromFile(input);
                taskList.loadTask(task);
            }
            s.close();
            return taskList;
        } catch (RuntimeException | IOException e) {
            throw new MortalReminderException("Corrupted storage file!");
        }
    }
}

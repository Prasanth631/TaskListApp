import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class TaskListApp {
    public static void main(String[] args) {
        TaskList taskList = new TaskList();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    taskList.addTask(getTaskDetails(scanner));
                    break;
                case 2:
                    if (!taskList.isEmpty()) {
                        taskList.listTasks();
                        int taskNumber = getUserInput(scanner, "Enter the task number to remove: ");
                        if (taskList.isValidTaskNumber(taskNumber)) {
                            taskList.removeTask(taskNumber);
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } else {
                        System.out.println("No tasks to remove.");
                    }
                    break;
                case 3:
                    if (!taskList.isEmpty()) {
                        taskList.listTasks();
                    } else {
                        System.out.println("No tasks to list.");
                    }
                    break;
                case 4:
                    if (!taskList.isEmpty()) {
                        taskList.listTasks();
                        int taskNumber = getUserInput(scanner, "Enter the task number to mark as complete: ");
                        if (taskList.isValidTaskNumber(taskNumber)) {
                            taskList.markTaskAsComplete(taskNumber);
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } else {
                        System.out.println("No tasks to mark as complete.");
                    }
                    break;
                case 5:
                    if (!taskList.isEmpty()) {
                        taskList.listTasks();
                        int taskNumber = getUserInput(scanner, "Enter the task number to edit: ");
                        if (taskList.isValidTaskNumber(taskNumber)) {
                            Task updatedTask = getTaskDetails(scanner);
                            taskList.editTask(taskNumber, updatedTask);
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } else {
                        System.out.println("No tasks to edit.");
                    }
                    break;
                case 6:
                    System.out.print("Enter search term: ");
                    String searchTerm = scanner.next();
                    taskList.searchTasks(searchTerm);
                    break;
                case 7:
                    if (!taskList.isEmpty()) {
                        taskList.listTasks();
                        int taskNumber = getUserInput(scanner, "Enter the task number to set priority: ");
                        if (taskList.isValidTaskNumber(taskNumber)) {
                            int priority = getUserInput(scanner, "Enter priority (1-5, 1 being highest): ");
                            taskList.setPriority(taskNumber, priority);
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } else {
                        System.out.println("No tasks to set priority.");
                    }
                    break;
                case 8:
                    if (!taskList.isEmpty()) {
                        taskList.listTasks();
                        int taskNumber = getUserInput(scanner, "Enter the task number to set due date: ");
                        if (taskList.isValidTaskNumber(taskNumber)) {
                            LocalDate dueDate = getDate(scanner);
                            if (dueDate != null) {
                                taskList.setDueDate(taskNumber, dueDate);
                            }
                        } else {
                            System.out.println("Invalid task number.");
                        }
                    } else {
                        System.out.println("No tasks to set due date.");
                    }
                    break;
                case 9:
                    taskList.generateReport();
                    break;
                case 10:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nTask List Application");
        System.out.println("1. Add Task");
        System.out.println("2. Remove Task");
        System.out.println("3. List Tasks");
        System.out.println("4. Mark Task as Complete");
        System.out.println("5. Edit Task");
        System.out.println("6. Search Tasks");
        System.out.println("7. Set Task Priority");
        System.out.println("8. Set Task Due Date");
        System.out.println("9. Generate Report");
        System.out.println("10. Quit");
        System.out.print("Select an option: ");
    }

    private static int getUserChoice(Scanner scanner) {
        return scanner.nextInt();
    }

    private static Task getTaskDetails(Scanner scanner) {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter task name: ");
        String name = scanner.nextLine();
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        return new Task(name, description);
    }

    private static int getUserInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextInt();
    }

    private static LocalDate getDate(Scanner scanner) {
        scanner.nextLine(); // Consume newline
        System.out.print("Enter due date (YYYY-MM-DD): ");
        String dateString = scanner.nextLine();
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }
}

class Task {
    private String name;
    private String description;
    private boolean isComplete;
    private int priority;
    private LocalDate dueDate;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.isComplete = false;
        this.priority = 3; // Default priority
        this.dueDate = null;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isComplete() { return isComplete; }
    public void setComplete(boolean complete) { isComplete = complete; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    @Override
    public String toString() {
        String status = isComplete ? "Completed" : "Pending";
        String dueDateStr = dueDate != null ? dueDate.toString() : "Not set";
        return String.format("%s (Priority: %d, Due: %s, Status: %s)\n   %s", 
                             name, priority, dueDateStr, status, description);
    }
}

class TaskList {
    private ArrayList<Task> tasks = new ArrayList<>();

    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Task added.");
    }

    public void removeTask(int taskNumber) {
        tasks.remove(taskNumber - 1);
        System.out.println("Task removed.");
    }

    public void listTasks() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public boolean isValidTaskNumber(int taskNumber) {
        return taskNumber >= 1 && taskNumber <= tasks.size();
    }

    public void markTaskAsComplete(int taskNumber) {
        Task task = tasks.get(taskNumber - 1);
        task.setComplete(true);
        System.out.println("Task marked as complete.");
    }

    public void editTask(int taskNumber, Task updatedTask) {
        Task task = tasks.get(taskNumber - 1);
        task.setName(updatedTask.getName());
        task.setDescription(updatedTask.getDescription());
        System.out.println("Task edited.");
    }

    public void searchTasks(String searchTerm) {
        boolean found = false;
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                task.getDescription().toLowerCase().contains(searchTerm.toLowerCase())) {
                System.out.println((i + 1) + ". " + task);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No tasks found matching the search term.");
        }
    }

    public void setPriority(int taskNumber, int priority) {
        if (priority < 1 || priority > 5) {
            System.out.println("Invalid priority. Please use a value between 1 and 5.");
            return;
        }
        Task task = tasks.get(taskNumber - 1);
        task.setPriority(priority);
        System.out.println("Task priority updated.");
    }

    public void setDueDate(int taskNumber, LocalDate dueDate) {
        Task task = tasks.get(taskNumber - 1);
        task.setDueDate(dueDate);
        System.out.println("Task due date updated.");
    }

    public void generateReport() {
        System.out.println("\nTask Report");
        System.out.println("===========");
        System.out.println("Total tasks: " + tasks.size());
        
        long completedTasks = tasks.stream().filter(Task::isComplete).count();
        System.out.println("Completed tasks: " + completedTasks);
        System.out.println("Pending tasks: " + (tasks.size() - completedTasks));
        
        System.out.println("\nUpcoming due dates:");
        LocalDate today = LocalDate.now();
        tasks.stream()
             .filter(task -> !task.isComplete() && task.getDueDate() != null && !task.getDueDate().isBefore(today))
             .sorted(Comparator.comparing(Task::getDueDate))
             .limit(5)
             .forEach(task -> System.out.println("- " + task.getName() + " (Due: " + task.getDueDate() + ")"));
        
        System.out.println("\nHigh priority tasks:");
        tasks.stream()
             .filter(task -> !task.isComplete() && task.getPriority() <= 2)
             .forEach(task -> System.out.println("- " + task.getName() + " (Priority: " + task.getPriority() + ")"));
    }
}
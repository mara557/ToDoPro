
# ToDoPro - Simple Android Task Manager

ToDoPro is a lightweight Android application designed to help you manage your tasks and to-do lists effortlessly. It provides a clean and intuitive interface for adding, editing, and deleting tasks, making it easy to stay organized and on top of your daily activities.

## Key Features

- **Add New Tasks**: Quickly add tasks to your to-do list using the input field and "ADD" button.
- **Edit Existing Tasks**: Modify task descriptions to keep your to-do list up to date.
- **Delete Tasks**: Remove tasks that are no longer needed with ease.
- **Mark Tasks as Completed**: Tap on a task to mark it as completed, helping you track your progress.
- **User-Friendly Interface**: Enjoy a straightforward interface with an input field for tasks and a list view for displaying and managing them.
- **Persistent Storage**: Tasks are stored locally using Room database, ensuring your to-do list is always available even after closing the application.

## How to Use

- **Add a Task**: Enter a task description in the input field at the top of the app and tap the "ADD" button.
- **View Tasks**: All added tasks will appear in the list below, displayed in order of addition.
- **Edit a Task**:
  1. Tap the options menu (three dots) next to a task.
  2. Select "Edit" to modify the task description.
  3. Confirm your changes to update the task.
- **Delete a Task**:
  1. Tap the options menu next to a task.
  2. Select "Delete" to remove it from your list.
  3. Confirm the deletion when prompted.
- **Mark Task as Completed**: Simply tap on a task to mark it as completed. Completed tasks may appear with a strikethrough or different styling.

## Code Structure

- **MainActivity**: Handles the user interface and interactions, including adding, editing, and deleting tasks. It sets up the RecyclerView and manages the input field and buttons.
- **Todo**: A data class representing a task, annotated with `@Entity` for Room database.
- **TodoDao**: An interface defining database operations for tasks, including queries for retrieving, inserting, updating, and deleting tasks.
- **TodoDatabase**: An abstract class extending `RoomDatabase`, providing a singleton instance of the database.
- **TodoRepository**: Acts as a mediator between the `TodoDao` and the `TodoViewModel`, handling data operations.
- **TodoViewModel**: Provides data to the UI and acts as a communication center between the Repository and the UI.
- **TodoAdapter**: A `RecyclerView` adapter that binds task data to the UI components in each list item.

## Getting Started

### Prerequisites

- **Android Studio**: [Download here](https://developer.android.com/studio)
- **Android Device or Emulator**: To run and test the application.

### Installation

#### Clone the Repository:

```bash
git clone https://github.com/mara557/todopro.git
```

#### Open in Android Studio:

1. Open Android Studio.
2. Click on `File -> Open`.
3. Navigate to the cloned repository folder and select it.

#### Build the Project:

1. Sync Gradle files if prompted.
2. Build the project by clicking on `Build -> Make Project`.

#### Run the Application:

1. Connect your Android device or start an emulator.
2. Click the **Run** button and select your device.

## Screenshots

(COMING SOON)

## Dependencies

- **AndroidX Libraries**: For modern app components and features.
- **Room Database**: For local data persistence.
- **LiveData and ViewModel**: For lifecycle-aware data handling.
- **RecyclerView**: For displaying the list of tasks.


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- [Android Developers Documentation](https://developer.android.com/docs)
- [Material Design Guidelines](https://material.io/design)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)

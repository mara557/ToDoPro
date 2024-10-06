
# To Do Pro

**To Do Pro** is a modern task management app built with Android’s latest architecture components. The app supports adding tasks with categories, descriptions, completion dates, and allows for task filtering, selection, and deletion. It also features options for simulating concurrent inserts for testing purposes.

## Features

- Add tasks with a title, description, and a category
- Set a completion time and date for each task
- Filter tasks by categories
- Select all or multiple tasks for deletion
- Simulate concurrent inserts (for testing purposes)
- Task persistence with Room Database
- MVVM architecture
- View binding for efficient UI handling
- RecyclerView for displaying tasks
- Material Design for an elegant and responsive UI

## Project Structure

```bash
app
├── manifests
├── java
│   └── com.example.todopro
│       ├── adapter
│       │   └── TodoAdapter.kt
│       ├── data
│       │   ├── Todo.kt
│       │   ├── TodoDao.kt
│       │   └── TodoDatabase.kt
│       ├── repository
│       │   └── TodoRepository.kt
│       ├── viewmodel
│       │   ├── TodoViewModel.kt
│       │   └── TodoViewModelFactory.kt
│       ├── MainActivity.kt
│       └── FileHelper.kt
└── ...
```

## Dependencies

Your project depends on the following libraries:

- Kotlin Standard Library
- AndroidX Core, AppCompat, RecyclerView
- Room components (Room Database, Room KTX)
- ViewModel and LiveData components
- Coroutines
- Material Design components

The Gradle setup for these dependencies can be found in `build.gradle.kts`.

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/todopro.git
   cd todopro
   ```

2. Open the project in Android Studio.

3. Ensure you have the correct JDK and SDK configurations (target SDK 34).

4. Sync the project with Gradle.

5. Run the project on an emulator or device.

## Screenshots

_Add some screenshots here to showcase the app UI._

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

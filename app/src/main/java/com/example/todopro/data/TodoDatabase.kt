package com.example.todopro.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todopro.data.Todo

@Database(entities = [Todo::class], version = 4, exportSchema = true)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        @Volatile
        private var INSTANCE: TodoDatabase? = null

        // Migration from version 1 to 2: Add 'description' column
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE todo_table ADD COLUMN description TEXT NOT NULL DEFAULT ''")
            }
        }

        // Migration from version 2 to 3: Rename 'task' to 'title'
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE todo_table RENAME COLUMN task TO title")
            }
        }

        // Migration from version 3 to 4: Adding 'completionTime' column
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE todo_table ADD COLUMN completionTime INTEGER")
            }
        }

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

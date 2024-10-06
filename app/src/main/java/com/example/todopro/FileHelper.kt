package com.example.todopro

import android.content.Context
import java.io.*

class FileHelper {

    private val FILENAME = "listinfo.dat"

    fun writeData(item: ArrayList<String>, context: Context) {
        try {
            val fos: FileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)
            val oas = ObjectOutputStream(fos)
            oas.writeObject(item)
            oas.close()
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle the exception as needed (e.g., show a toast or log)
        }
    }

    fun readData(context: Context): ArrayList<String> {
        return try {
            val fis: FileInputStream = context.openFileInput(FILENAME)
            val ois = ObjectInputStream(fis)
            ois.readObject() as ArrayList<String>
        } catch (e: FileNotFoundException) {
            ArrayList()
        } catch (e: IOException) {
            e.printStackTrace()
            ArrayList()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            ArrayList()
        }
    }
}

package com.example.proyecto

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.*
//
//@Entity(tableName = "usuarios")   // nombre personalizado de la tabla
//data class User(
//    @PrimaryKey(autoGenerate = true)
//    val idUser: Int = 0,          // Columna IDUSER (autogenerada)
//    val username: String,         // Columna USERNAME
//    val password: String          // Columna PASSWORD
//)
//@Dao
//interface UserDao {
//    // Insertar un nuevo usuario
//    @Insert
//    suspend fun insertUser(user: User)
//
//    // Traer un usuario por su nombre
//    @Query("SELECT * FROM usuarios WHERE username = :username LIMIT 1")
//    suspend fun getUserByUsername(username: String): User?
//}
//@Database(entities = [User::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun userDao(): UserDao
//}
//fun getDatabase(context: Context): AppDatabase {
//    return Room.databaseBuilder(
//        context.applicationContext,
//        AppDatabase::class.java,
//        "mi_base_datos"
//    ).build()
//}
//fun probarRoom(context: Context) {
//    val db = getDatabase(context)
//    val userDao = db.userDao()
//
//    CoroutineScope(Dispatchers.IO).launch {
//        // Insertar usuario
//        val nuevoUsuario = User(username = "Rowinzon", password = "12345")
//        userDao.insertUser(nuevoUsuario)
//
//        // Buscar usuario
//        val usuarioEncontrado = userDao.getUserByUsername("Rowinzon")
//
//        if (usuarioEncontrado != null) {
//            println("Usuario encontrado: ${usuarioEncontrado.username} - ${usuarioEncontrado.password}")
//        } else {
//            println("Usuario no encontrado")
//        }
//    }
//}
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
@Entity(tableName = "usuarios")
data class User(
    @PrimaryKey(autoGenerate = true)
    val idUser: Int = 0,           // Columna IDUSER
    val usuario: String,           // Columna USUARIO
    val password: String,          // Columna PASSWORD
    val nivelDePermiso: Int        // 0 = admin, 1 = usuario normal, 2 = solo consulta
)
@Dao
interface UserDao {
    // Insertar un nuevo usuario
    @Insert
    suspend fun insertUser(user: User)
    // Verificar si el usuario ya existe
    @Query("SELECT COUNT(*) FROM usuarios WHERE usuario = :usuarioIngresado")
    suspend fun userExists(usuarioIngresado: String): Int
    // Buscar un usuario por su nombre
    @Query("SELECT * FROM usuarios WHERE usuario = :usuarioIngresado LIMIT 1")
    suspend fun getUserByUsuario(usuarioIngresado: String): User?
}
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

// Función para obtener una instancia de la base de datos
fun getDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "INVENTORYAPPDB"
    ).build()
}
//crear el usuario administrador
fun crearUsuarioAdministrador(context: Context) {
    val db = getDatabase(context)              // Obtiene la base de datos
    val userDao = db.userDao()                 // Accede al DAO
    CoroutineScope(Dispatchers.IO).launch {    // Ejecuta en hilo secundario (Room no permite usar el hilo principal)

        val usuarioAdmin = "Administrador"     // Nombre del usuario administrador
        val passwordAdmin = "12345"            // Contraseña por defecto
        val nivelAdmin = 0                     // 0 = Administrador
        // Verificar si el usuario administrador ya existe
        val existe = userDao.userExists(usuarioAdmin)
        if (existe == 0) {
            // Si no existe, lo insertamos
                val nuevoUsuario = User(
                usuario = usuarioAdmin,
                password = passwordAdmin,
                nivelDePermiso = nivelAdmin
            )
            userDao.insertUser(nuevoUsuario)
            println("Usuario Administrador creado correctamente.")
        } else {
            println("El usuario Administrador ya existe. No se creó otro.")
        }
    }
}
package com.example.proyecto

import android.R
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
import androidx.room.Delete
import androidx.room.ForeignKey

@Entity(tableName = "usuarios")
data class User(
    @PrimaryKey(autoGenerate = true)
    val idUser: Int = 0,           // Columna IDUSER
    val usuario: String,           // Columna USUARIO
    val password: String,          // Columna PASSWORD
    val nivelDePermiso: Int        // 0 = admin, 1 = usuario normal, 2 = solo consulta
)
@Entity(tableName = "grupo_productos")
data class GrupoProducto(
    @PrimaryKey(autoGenerate = true) val idGrupo: Int = 0,
    val nombreGrupo: String,
    val codigoNum: Int,
    val codigo: String
)

@Entity(
    tableName = "subgrupo_productos",
    foreignKeys = [ForeignKey(
        entity = GrupoProducto::class,
        parentColumns = ["idGrupo"],
        childColumns = ["idGrupo"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SubgrupoProducto(
    @PrimaryKey(autoGenerate = true) val idSubgrupo: Int = 0,
    val idGrupo: Int,
    val nombreSubgrupo: String,
    val codigoNum: Int,
    val codigo: String
)

@Entity(
    tableName = "productos",
    foreignKeys = [
        ForeignKey(
            entity = GrupoProducto::class,
            parentColumns = ["idGrupo"],
            childColumns = ["idGrupo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SubgrupoProducto::class,
            parentColumns = ["idSubgrupo"],
            childColumns = ["idSubgrupo"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Producto(
    @PrimaryKey(autoGenerate = true) val idProducto: Int = 0,
    val idGrupo: Int,
    val idSubgrupo: Int,
    val nombreProducto: String,
    val numeroProducto: Int,
    val marcaModelosCompatibles: String,
    val ubicacionBodega: String,
    val observaciones: String? = null
)

@Entity(
    tableName = "existencias",
    foreignKeys = [ForeignKey(
        entity = Producto::class,
        parentColumns = ["idProducto"],
        childColumns = ["idProducto"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Existencia(
    @PrimaryKey val idProducto: Int,
    val nombre: String,
    val existencia: Int = 0,
    val valor: Double = 0.0
)

@Entity(
    tableName = "historial_inventario",
    foreignKeys = [ForeignKey(
        entity = Producto::class,
        parentColumns = ["idProducto"],
        childColumns = ["idProducto"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HistorialInventario(
    @PrimaryKey(autoGenerate = true) val idHistorial: Int = 0,
    val fecha: Long, // Usar timestamp en milisegundos
    val idProducto: Int,
    val cantidad: Int,
    val entradas: Int = 0,
    val salidas: Int = 0,
    val valor: Double
)

@Entity(
    tableName = "salidas",
    foreignKeys = [ForeignKey(
        entity = Producto::class,
        parentColumns = ["idProducto"],
        childColumns = ["idProducto"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Salida(
    @PrimaryKey(autoGenerate = true) val idSalida: Int = 0,
    val fecha: Long,
    val idProducto: Int,
    val cantidad: Int,
    val responsable: String,
    val cliente: String,
    val observaciones: String? = null
)

@Entity(tableName = "entradas")
data class Entrada(
    @PrimaryKey(autoGenerate = true) val idEntrada: Int = 0,
    val fecha: Long,
    val proveedor: String,
    val observaciones: String? = null
)

@Entity(
    tableName = "detalle_entradas",
    foreignKeys = [
        ForeignKey(
            entity = Entrada::class,
            parentColumns = ["idEntrada"],
            childColumns = ["idEntrada"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Producto::class,
            parentColumns = ["idProducto"],
            childColumns = ["idProducto"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DetalleEntrada(
    @PrimaryKey(autoGenerate = true) val idDetalle: Int = 0,
    val idEntrada: Int,
    val idProducto: Int,
    val cantidad: Int,
    val valorEntrada: Double
)

@Dao
interface UserDao {
    //eliminar usuario
    @Delete
    suspend fun DeletetUser(user: User)
    // Insertar un nuevo usuario
    @Insert
    suspend fun insertUser(user: User)
    // Verificar si el usuario ya existe
    @Query("SELECT COUNT(*) FROM usuarios WHERE usuario = :usuarioIngresado")
    suspend fun userExists(usuarioIngresado: String): Int
    // Buscar un usuario por su nombre
    @Query("SELECT * FROM usuarios WHERE usuario = :usuarioIngresado LIMIT 1")
    suspend fun getUserByUsuario(usuarioIngresado: String): User?
    //buscar todos los usuarios creados
    @Query("SELECT usuario FROM usuarios ")
    suspend fun GetAllusers ():  List<String>
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
fun crearNuevoUsuario(nombre: String, password: String, nivel: Int, context: Context, onResult: (String) -> Unit ) {
    val db = getDatabase(context)
    val userDao = db.userDao()

    CoroutineScope(Dispatchers.IO).launch {
        val existe = userDao.userExists(nombre)
        if (existe == 0) {
            userDao.insertUser(
                User(
                    usuario = nombre,
                    password = password,
                    nivelDePermiso = nivel
                )
            )
            onResult("Usuario $nombre creado correctamente.")
        } else {
            onResult("El usuario $nombre ya existe.")
        }
    }
}


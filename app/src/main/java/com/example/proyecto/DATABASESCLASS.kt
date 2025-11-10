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
import androidx.room.Delete

@Entity(tableName = "usuarios")
data class User(
    @PrimaryKey(autoGenerate = true)
    val idUser: Int = 0,           // Columna IDUSER
    val usuario: String,           // Columna USUARIO
    val password: String,          // Columna PASSWORD
    val nivelDePermiso: Int        // 0 = admin, 1 = usuario normal, 2 = solo consulta
)
@Entity(tableName = "GrupoProducto")
data class GrupoProducto(
    @PrimaryKey(autoGenerate = true)
    val idGrupo: Int = 0,
    val nombreGrupo: String,
    val codigoNum: Int,
    val codigo: String
)
@Entity(tableName = "SubgrupoProducto")
data class SubgrupoProducto(
    @PrimaryKey(autoGenerate = true) val idSubgrupo: Int = 0,
    val idGrupo: Int,
    val nombreSubgrupo: String,
    val codigoNum: Int,
    val codigo: String
)

@Entity(tableName = "Producto")
data class Producto(
    @PrimaryKey(autoGenerate = true) val idProducto: Int = 0,
    val idGrupo: Int,
    val idSubgrupo: Int,
    val nombreProducto: String,
    val numeroProducto: Int,
    val ubicacionBodega: String,
    val observaciones: String? = null
)
@Entity(tableName = "Existencia")
data class Existencia(
    @PrimaryKey val idProducto: Int,
    val nombre: String,
    val existencia: Int = 0,
    val valor: Double = 0.0
)
@Entity(tableName = "HistorialInventario")
data class HistorialInventario(
    @PrimaryKey(autoGenerate = true) val idHistorial: Int = 0,
    val fecha: Long, // Usar timestamp en milisegundos
    val idProducto: Int,
    val cantidad: Int,
    val entradas: Int = 0,
    val salidas: Int = 0,
    val valor: Double
)
@Entity(tableName = "Salida")
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
@Entity(tableName = "DetalleEntrada")
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
@Dao
interface CrearElementosDao{
    //crear Grupo
    @Insert
    suspend fun InsertGrupo(grupoIngresado: GrupoProducto)
    @Query("SELECT COUNT(*) FROM grupoproducto WHERE nombreGrupo = :GrupoIngresado")
    suspend fun GrupoExists(GrupoIngresado: String): Int
    @Query("SELECT * FROM GRUPOPRODUCTO WHERE nombreGrupo= :GrupoIngresado Limit 1")
    suspend fun getgrupobygrupo(GrupoIngresado: String): GrupoProducto?
    @Query("Select nombreGrupo from grupoproducto")
    suspend fun GetallGrupos (): List<String>

    // Crear Subgrupo
    @Insert
    suspend fun InsertSubGrupo(SubgrupoIngresado: SubgrupoProducto)
    @Query("SELECT idGrupo FROM grupoproducto WHERE nombreGrupo = :nombre")
    suspend fun GetIdgrupo(nombre: String): Int
    @Query("SELECT COUNT(*) FROM subgrupoproducto WHERE nombreSubgrupo = :nombre")
    suspend fun SubGrupoExists(nombre: String): Int
    @Query("SELECT * FROM subgrupoproducto WHERE nombreSubgrupo= :nombre Limit 1")
    suspend fun getSubgrupobygrupo(nombre: String): SubgrupoProducto?
    @Query("Select nombreSubgrupo from subgrupoproducto")
    suspend fun GetallSubGrupos (): List<String>
    //Crear Elementos
    @Query("Select nombreSubgrupo from subgrupoproducto where idGrupo=:Idgrupoingresado")
    suspend fun GetallSubGruposbyGrupos (Idgrupoingresado: Int ): List<String>
    @Query("SELECT idSubgrupo FROM subgrupoproducto WHERE nombreSubgrupo = :nombre")
    suspend fun GetIdSubgrupo(nombre: String): Int
    @Query("SELECT COUNT(*) FROM producto ")
    suspend fun GetnumeroProductos(): Int
    @Query("SELECT COUNT(*) FROM producto WHERE nombreProducto = :nombre")
    suspend fun ProductoExiste(nombre: String): Int
    @Insert
    suspend fun InsertProducto(ProductoIngresado: Producto): Long
    @Insert
    suspend fun InsertExistencia(ProductoIngresado: Existencia)
    @Query("SELECT idProducto FROM producto WHERE nombreProducto = :nombre")
    suspend fun GetIdProducto(nombre: String): Int
    @Query("SELECT nombreProducto FROM producto")
    suspend fun getallProductos (): List<String>
    //actualizar existencia
    // Obtiene la existencia (cantidad) actual por nombre de producto
    @Query("SELECT existencia FROM Existencia WHERE nombre = :nombre LIMIT 1")
    suspend fun getExistenciaByProductoname(nombre: String): Int?

    // Actualiza existencia y valor del producto por idProducto
    @Query("UPDATE Existencia SET existencia = :nuevaExistencia, valor = :nuevoValor WHERE idProducto = :id")
    suspend fun actualizarExistenciaYValor(id: Int, nuevaExistencia: Int, nuevoValor: Double)

    // Si prefieres obtener la entidad completa:
    @Query("SELECT * FROM Existencia WHERE nombre = :nombre LIMIT 1")
    suspend fun getExistenciaEntityByProductoname(nombre: String): Existencia?
}
@Database(
    entities = [
        User::class,
        GrupoProducto::class,
        SubgrupoProducto::class,
        Producto::class,
        Existencia::class,
        HistorialInventario::class,
        Salida::class,
        Entrada::class,
        DetalleEntrada::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun crearelementosDao(): CrearElementosDao
    abstract fun entradaDao(): EntradaDao
}

// Función para obtener una instancia de la base de datos
fun getDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "INVENTORYAPPDB"
    )
        .fallbackToDestructiveMigration()
        .build()
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
fun crearNuevoUsuario(nombre: String, password: String, nivel: Int, context: Context,
                      onResult: (String) -> Unit ) {
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
fun CrearGrupo(Nombregrupoing: String, CodigoNumerico: Int, CodigoGrupo: String, context: Context,
               onResult: (String) -> Unit ){
    val db = getDatabase(context)
    val CrearElementos = db.crearelementosDao()
    CoroutineScope(Dispatchers.IO).launch {
        val existe = CrearElementos.GrupoExists(Nombregrupoing)
        if (existe == 0) {
            CrearElementos.InsertGrupo(
                GrupoProducto(
                    nombreGrupo = Nombregrupoing,
                    codigoNum = CodigoNumerico,
                    codigo = CodigoGrupo
                )
            )
            onResult("Grupo $Nombregrupoing creado correctamente.")
        } else {
            onResult("El Grupo $Nombregrupoing ya existe.")
        }
    }
}
fun CrearSubGrupo(Idsubgrupo: Int ,NombreSubgrupoing: String, CodigoNumerico: Int,
                  CodigoSubGrupo: String, context: Context, onResult: (String) -> Unit ){
    val db = getDatabase(context)
    val CrearElementos = db.crearelementosDao()
    CoroutineScope(Dispatchers.IO).launch {
        val existe = CrearElementos.SubGrupoExists(NombreSubgrupoing)
        if (existe == 0) {
            CrearElementos.InsertSubGrupo(
                SubgrupoProducto(
                    idGrupo = Idsubgrupo,
                    nombreSubgrupo = NombreSubgrupoing,
                    codigoNum = CodigoNumerico,
                    codigo = CodigoSubGrupo
                )
            )
            onResult("SubGrupo $NombreSubgrupoing creado correctamente.")
        } else {
            onResult("El SubGrupo $NombreSubgrupoing ya existe.")
        }
    }
}
fun CrearElemento(NombreGrupo: String,Nombresubgrupo: String ,nombreProducto: String,ubicacionBodega: String,
                  observaciones: String,context: Context, onResult: (String) -> Unit ){
    val db = getDatabase(context)
    val CrearElementos = db.crearelementosDao()
    CoroutineScope(Dispatchers.IO).launch {
        val Gexiste = CrearElementos.GetIdgrupo(NombreGrupo)
        val SGGexiste = CrearElementos.GetIdSubgrupo(Nombresubgrupo)
        val ProExxiste = CrearElementos.ProductoExiste(nombreProducto)
        if (ProExxiste==0){
            if (Gexiste > 0) {
                if (SGGexiste > 0){
                    val NumeroProductos = CrearElementos.GetnumeroProductos()
                    val nuevoProductoId = CrearElementos.InsertProducto(
                        Producto (idGrupo= Gexiste,idSubgrupo=SGGexiste,
                        nombreProducto=nombreProducto, numeroProducto=NumeroProductos,
                        ubicacionBodega=ubicacionBodega, observaciones =observaciones))
                    CrearElementos.InsertExistencia(
                        Existencia ( idProducto = nuevoProductoId.toInt(),
                            nombre=nombreProducto,existencia=0,valor=0.0))
                }else{
                    onResult("El Grupo $Nombresubgrupo No existe.")
                }
            } else {
                onResult("El Grupo $NombreGrupo No existe.")
            }
        }else {
            onResult("El Producto $nombreProducto existe.")
        }

    }
}
data class ProductoEntrada(
    val nombre: String,
    val cantidad: Int,
    val valorEntrada: Double
)
@Dao
interface EntradaDao {
    @Insert
    suspend fun insertarEntrada(entrada: Entrada): Long
    @Insert
    suspend fun insertarDetalles(detalles: List<DetalleEntrada>)
}

package com.example.proyecto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.setValue
import kotlinx.coroutines.*
@Composable
fun CreateNewUser(onBack: () -> Unit,onHome: () -> Unit) {
    val context = LocalContext.current
    var userNameNew by remember { mutableStateOf("") }
    var userPasswordNew by remember { mutableStateOf("") }
    var userNivelNew by remember { mutableStateOf(2) }
    var loginMessage by remember { mutableStateOf("") }
    TopBarButtons(onBack = onBack, onHome = onHome)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                iconos_contexto(iconRes = R.drawable.username, label = "New Username")
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userNameNew,
                    onValueChange = { userNameNew = it },
                    label = { Text("Nombre de usuario") }
                )
                iconos_contexto(iconRes = R.drawable.password, label = "Password")
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userPasswordNew,
                    onValueChange = { userPasswordNew = it },
                    label = { Text("Contraseña") }
                )
                DropdownSelector(
                    label = "Nivel de permiso",
                    options = listOf("Administrador", "Normal", "Consultas",),
                    selectedIndex = userNivelNew,
                    onOptionSelected = { index ->
                        userNivelNew = index
                    }
                )
                Button(
                    onClick = {
                        crearNuevoUsuario(userNameNew, userPasswordNew,
                            userNivelNew, context)
                        { mensaje -> loginMessage = mensaje }
                    }
                ) {
                    Text("Crear")
                }
                if (loginMessage.isNotEmpty()) {
                    Text(
                        text = loginMessage,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
@Composable
fun DeleteUser(onBack: () -> Unit, onHome: () -> Unit) {
    val context = LocalContext.current
    var userToDelete by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    val listaUsuarios = remember { mutableStateListOf<String>() }
    val db = getDatabase(context)
    val userDao = db.userDao()
    TopBarButtons(onBack = onBack, onHome = onHome)

    // Cargar usuarios desde la DB
    LaunchedEffect(Unit) {
        val db = getDatabase(context)
        val users = db.userDao().GetAllusers()
        listaUsuarios.clear()
        listaUsuarios.addAll(users)
    }

    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ,contentAlignment = Alignment.Center) {
        Column {
            DropdownSelector(
                label = "Listado de Usuarios",
                options = listaUsuarios,
                selectedIndex = 0,
                onOptionSelected = { index ->
                    userToDelete = listaUsuarios[index]
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Eliminar usuario en background
                val db = getDatabase(context)
                val userDao = db.userDao()
                CoroutineScope(Dispatchers.IO).launch {
                    val user = userDao.getUserByUsuario(userToDelete)
                    val mensaje = if (user != null) {
                        userDao.DeletetUser(user)
                        "Usuario $userToDelete eliminado correctamente."
                    } else {
                        "El usuario $userToDelete no existe."
                    }
                    withContext(Dispatchers.Main) {
                        loginMessage = mensaje
                        // Actualizar lista
                        val updated = userDao.GetAllusers()
                        listaUsuarios.clear()
                        listaUsuarios.addAll(updated)
                    }
                }
            }) {
                Text("Eliminar Usuario")
            }

            if (loginMessage.isNotEmpty()) {
                Text(
                    text = loginMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
@Composable
fun Elementos(onBack: () -> Unit, onHome: () -> Unit,onNavigateTo: (String) -> Unit){
    TopBarButtons(onBack = onBack, onHome = onHome)
    var isSelected by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                val opciones = listOf(
                    "Crear Grupo",
                    "Crear Subgrupo",
                    "Crear Elementos"
                )
                opciones.forEach { opcion ->
                    TextButton(
                        onClick = {
                            when (opcion) {
                                "Crear Grupo" -> { onNavigateTo("CreateGrupo")}
                                "Crear Subgrupo" -> { onNavigateTo("CreateSubgrupo") }
                                "Crear Elementos" -> { onNavigateTo("CreateElement") }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = opcion,
                            fontSize = 20.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        LogoUan(
            modifier = Modifier.size(240.dp).align(Alignment.BottomCenter).padding(16.dp)
        )
    }
}
@Composable
fun CreateGrupo(onBack: () -> Unit, onHome: () -> Unit){
    var GrupoNametocreate by remember { mutableStateOf("") }
    var GrupoCodigoNumtocreate by remember { mutableStateOf("") }
    var GrupoCodigotocreate by remember { mutableStateOf("") }
    var Message by remember { mutableStateOf("") }
    val context = LocalContext.current
    TopBarButtons(onBack = onBack, onHome = onHome)
    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nombre Grupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = GrupoNametocreate,
                    onValueChange = { GrupoNametocreate = it },
                    label = { Text("Nombre Grupo a crear") }
                )
                Text(
                    text = "Codigo Numerico del grupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = GrupoCodigoNumtocreate,
                    onValueChange = { GrupoCodigoNumtocreate = it },
                    label = { Text("Codigo Numerico del grupo a crear") }
                )
                Text(
                    text = "Codigo Grupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = GrupoCodigotocreate,
                    onValueChange = { GrupoCodigotocreate = it },
                    label = { Text("Codigo Grupo a crear") }
                )
                Button(
                    onClick = {
                        CrearGrupo(GrupoNametocreate,
                            GrupoCodigoNumtocreate.toInt(),
                            GrupoCodigotocreate, context)
                        { mensaje -> Message = mensaje }
                    }
                ){
                    Text("Crear")
                }
                if (Message.isNotEmpty()) {
                    Text(
                        text = Message,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        LogoUan(modifier = Modifier.size(240.dp).align(Alignment.BottomCenter)
            .padding(16.dp))
    }
}
@Composable
fun CreateSubgrupo(onBack: () -> Unit, onHome: () -> Unit){
    var Grupopertenece by remember { mutableStateOf("") }
    var SubGrupoNametocreate by remember { mutableStateOf("") }
    var SubGrupoCodigoNumtocreate by remember { mutableStateOf("") }
    var SubGrupoCodigotocreate by remember { mutableStateOf("") }
    var IdGrupopertenece by remember { mutableStateOf<Int?>(null) }
    val listaGrupos = remember { mutableStateListOf<String>() }
    val context = LocalContext.current
    val db = getDatabase(context)
    var Message by remember { mutableStateOf("") }
    TopBarButtons(onBack = onBack, onHome = onHome)
    LaunchedEffect(Unit) {
        val db = getDatabase(context)
        val grupos = db.crearelementosDao().GetallGrupos()
        listaGrupos.clear()
        listaGrupos.addAll(grupos)
    }
    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBarButtons(onBack = onBack, onHome = onHome)
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gupo al que pertenecera",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                //lista pegable de los grupos creados
                DropdownSelector(
                    label = "Listado de Grupos",
                    options = listaGrupos,
                    selectedIndex = 0,
                    onOptionSelected = { index ->
                        Grupopertenece = listaGrupos[index]
                    }
                )
                LaunchedEffect(Grupopertenece) {
                    if (Grupopertenece.isNotEmpty()) {
                        IdGrupopertenece = db.crearelementosDao().GetIdgrupo(Grupopertenece)
                    }
                }
                Text(
                    text = "Nombre SubGrupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = SubGrupoNametocreate,
                    onValueChange = { SubGrupoNametocreate = it },
                    label = { Text("Nombre SubGrupo a crear") }
                )
                Text(
                    text = "Codigo Numerico del Subgrupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = SubGrupoCodigoNumtocreate,
                    onValueChange = { SubGrupoCodigoNumtocreate = it },
                    label = { Text("Codigo Numerico del Subgrupo") }
                )
                Text(
                    text = "Codigo SubGrupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = SubGrupoCodigotocreate,
                    onValueChange = { SubGrupoCodigotocreate = it },
                    label = { Text("Codigo SubGrupo a crear") }
                )
                Button(onClick = {
                    IdGrupopertenece?.let { id ->
                        CrearSubGrupo(
                            id,
                            SubGrupoNametocreate,
                            SubGrupoCodigoNumtocreate.toInt(),
                            SubGrupoCodigotocreate,
                            context
                        ) { mensaje -> Message = mensaje }
                    } ?: run {
                        Message = "Debe seleccionar un grupo válido."
                    }
                }) {
                    Text("Crear")
                }
                if (Message.isNotEmpty()) {
                    Text(
                        text = Message,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        LogoUan(modifier = Modifier.size(240.dp).padding(16.dp))
    }
    }
}
@Composable
fun CreateElement(onBack: () -> Unit, onHome: () -> Unit){
    var Grupopertenece by remember { mutableStateOf("") }
    var IdGrupopertenece by remember { mutableStateOf<Int?>(null) }
    var SubGrupoName by remember { mutableStateOf("") }
    var IdSubGrupopertenece by remember { mutableStateOf<Int?>(null) }
    var NameProducto by remember { mutableStateOf("") }
    var UbicacionAlmacen by remember { mutableStateOf("") }
    var Observaciones by remember { mutableStateOf("") }
    val listaGrupos = remember { mutableStateListOf<String>() }
    val listaSubGrupos = remember { mutableStateListOf<String>() }
    val context = LocalContext.current
    val db = getDatabase(context)
    var Message by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarButtons(onBack = onBack, onHome = onHome)
            Card(modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                elevation = CardDefaults.cardElevation(8.dp)
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LaunchedEffect(Unit) {
                        val db = getDatabase(context)
                        val grupos = db.crearelementosDao().GetallGrupos()
                        listaGrupos.clear()
                        listaGrupos.addAll(grupos)
                    }
                    Text(
                        text = "Seleccione Grupo",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    DropdownSelector(
                        label = "Listado de Grupos",
                        options = listaGrupos,
                        selectedIndex = 0,
                        onOptionSelected = { index ->
                            Grupopertenece = listaGrupos[index]
                        }
                    )
                    Text(
                        text = "Seleccione SubGrupo",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    //lista deplegable de Subgrupos  SubGrupoName
                    DropdownSelector(
                        label = "Listado de SubGrupos",
                        options = listaSubGrupos,
                        selectedIndex = 0,
                        onOptionSelected = { index ->
                            SubGrupoName = listaSubGrupos[index]
                        }
                    )
                    LaunchedEffect(Grupopertenece) {
                        if (Grupopertenece.isNotEmpty()) {
                            // 1. Obtener ID del grupo seleccionado
                            val idGrupo = db.crearelementosDao().GetIdgrupo(Grupopertenece)

                            // 2. Guardar el ID
                            IdGrupopertenece = idGrupo

                            // 3. Cargar subgrupos asociados
                            val subgrupos = db.crearelementosDao().GetallSubGruposbyGrupos(idGrupo)
                            listaSubGrupos.clear()
                            listaSubGrupos.addAll(subgrupos)
                        }
                    }
                    Text(
                        text = "Nombre Del Producto",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = NameProducto,
                        onValueChange = { NameProducto = it },
                        label = { Text("Nombre Del Producto") }
                    )
                    Text(
                        text = "Ubicacion en el Almacen",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = UbicacionAlmacen,
                        onValueChange = { UbicacionAlmacen = it },
                        label = { Text("Ubicacion") }
                    )
                    Text(
                        text = "Observaciones",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = Observaciones,
                        onValueChange = { Observaciones = it },
                        label = { Text("Observaciones") }
                    )
                    Button(
                        onClick = {
                            CrearElemento(Grupopertenece,
                                SubGrupoName,
                                NameProducto,
                                UbicacionAlmacen,
                                Observaciones ,context
                            ) { mensaje -> Message = mensaje }
                        }
                    ){
                        Text("Crear")
                    }
                    if (Message.isNotEmpty()) {
                        Text(
                            text = Message,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            LogoUan(modifier = Modifier.size(240.dp).padding(16.dp))
        }
    }
}
@Composable
fun CreateEntrada(onBack: () -> Unit, onHome: () -> Unit){
    var CantidadEntrante by remember { mutableStateOf("") }
    var Observaciones by remember { mutableStateOf("") }
    var Proveedor by remember { mutableStateOf("") }
    var Message by remember { mutableStateOf("") }
    TopBarButtons(onBack = onBack, onHome = onHome)
    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Seleccione el elemento",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                )
                //lista pegable de elementos
                Text(
                    text = "Cantidad",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = CantidadEntrante,
                    onValueChange = { CantidadEntrante = it },
                    label = { Text("Cantidad que va a ingresar") }
                )
                Text(
                    text = "Proveedor",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = Proveedor,
                    onValueChange = { Proveedor = it },
                    label = { Text("Proveedor") }
                )
                Text(
                    text = "Observaciones",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = Observaciones,
                    onValueChange = { Observaciones = it },
                    label = { Text("Observaciones") }
                )
                Button(
                    onClick = {
                    }
                ){
                    Text("Crear")
                }
                if (Message.isNotEmpty()) {
                    Text(
                        text = Message,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        LogoUan(modifier = Modifier.size(240.dp).align(Alignment.BottomCenter)
            .padding(16.dp))
    }
}
@Composable
fun CreateSalida(onBack: () -> Unit, onHome: () -> Unit){
    var CantidadSaliente by remember { mutableStateOf("") }
    var Observaciones by remember { mutableStateOf("") }
    var Entrego by remember { mutableStateOf("") }
    var Cliente by remember { mutableStateOf("") }
    var Message by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarButtons(onBack = onBack, onHome = onHome)
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight(),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Seleccione el elemento",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    //lista pegable de elementos
                    Text(
                        text = "Cantidad",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = CantidadSaliente,
                        onValueChange = { CantidadSaliente = it },
                        label = { Text("Cantidad que va a Salir") }
                    )
                    Text(
                        text = "Entrega",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = Entrego,
                        onValueChange = { Entrego = it },
                        label = { Text("Quien Entrega") }
                    )
                    Text(
                        text = "Cliente",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = Cliente,
                        onValueChange = { Cliente = it },
                        label = { Text("Quien Recibe") }
                    )
                    Text(
                        text = "Observaciones",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = Observaciones,
                        onValueChange = { Observaciones = it },
                        label = { Text("Observaciones") }
                    )
                    Button(
                        onClick = {
                        }
                    ) {
                        Text("Crear")
                    }
                    if (Message.isNotEmpty()) {
                        Text(
                            text = Message,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            LogoUan(modifier = Modifier.size(240.dp).padding(16.dp))
        }
    }
}
@Composable
fun Stock(onBack: () -> Unit, onHome: () -> Unit) {
    val listaElementos = remember { mutableStateListOf<String>() }
    var ProductoName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = getDatabase(context)

    // Estado para guardar la cantidad
    var cantidad by remember { mutableStateOf(0) }
    var Valor by remember { mutableStateOf("") }

    // Cargar lista de productos
    LaunchedEffect(Unit) {
        val grupos = db.crearelementosDao().getallProductos()
        listaElementos.clear()
        listaElementos.addAll(grupos)
    }

    // ✅ Cargar la cantidad al seleccionar un producto
    LaunchedEffect(ProductoName) {
        if (ProductoName.isNotEmpty()) {
            val existencia = db.crearelementosDao()
                .getExistenciaByProductoname(ProductoName)
            cantidad=existencia.existencia
            Valor = existencia.valor.toString()
        }
    }

    TopBarButtons(onBack = onBack, onHome = onHome)

    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(0.9f),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    DropdownSelector(
                        label = "Elementos",
                        options = listaElementos,
                        selectedIndex = 0,
                        onOptionSelected = { index ->
                            ProductoName = listaElementos[index]
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Cantidad disponible", color = Color.Black)
                        Text( cantidad.toString(), color = Color.Black)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Valor Actual", color = Color.Black)
                        Text( Valor, color = Color.Black)
                    }
                }
            }
            LogoUan(modifier = Modifier.size(240.dp).padding(16.dp))
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaAMovimientosPreview() {
    //CreateNewUser(onBack = {}, onHome = {})
    //DeleteUser(onBack = {}, onHome = {})
    //Elementos(onBack = {}, onHome = {},onNavigateTo = {})
    //CreateGrupo(onBack = {}, onHome = {})
    //CreateSubgrupo(onBack = {}, onHome = {})
    //CreateElement(onBack = {}, onHome = {})
    //CreateEntrada(onBack = {}, onHome = {})
    //CreateSalida (onBack = {}, onHome = {})
    Stock (onBack = {}, onHome = {})
}
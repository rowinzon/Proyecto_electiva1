@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.proyecto

//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.sp
//import androidx.compose.foundation.layout.padding
//import androidx.compose.ui.unit.dp
//import androidx.compose.foundation.layout.Arrangement
//
//@Composable
//fun TopBarButtons(
//    onBack: () -> Unit,
//    onHome: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        TextButton(onClick = { onBack() }) {
//            Text(
//                text = "volver",
//                color = Color.Black,
//                fontSize = 18.sp
//            )
//        }
//
//        TextButton(onClick = { onHome() }) {
//            Text(
//                text = "home",
//                color = Color.Black,
//                fontSize = 18.sp
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PantallaComponentesPreview() {
//    TopBarButtons(onBack = {}, onHome = {})
//}
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun TopBarButtons(
    onBack: () -> Unit,
    onHome: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("INVENTORI-APP") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }
        },
        actions = {
            IconButton(onClick = onHome) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home"
                )
            }
        }
    )
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaComponentesPreview() {
    TopBarButtons(onBack = {}, onHome = {})
}
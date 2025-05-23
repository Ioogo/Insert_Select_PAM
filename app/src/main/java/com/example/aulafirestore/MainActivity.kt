package com.example.aulafirestore

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aulafirestore.ui.theme.AulaFireStoreTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AulaFireStoreTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray), // <- isso é chave
                    containerColor = Color.LightGray
                ) {
                    AppNavigation()
                }
            }
        }

    }
}

data class Usuario(
    val nome: String = "",
    val idade: String = "",
    val especie: String = "",
    val time: String = "",
    val senha: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun appAula(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }

    val db = Firebase.firestore

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("App Cadastro") })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(Color.LightGray)
            ) {
                Text(
                    "App Cadastro",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Cursive,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )

                // Campos de texto
                TextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
                TextField(
                    value = idade,
                    onValueChange = { idade = it },
                    label = { Text("Idade") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
                TextField(
                    value = especie,
                    onValueChange = { especie = it },
                    label = { Text("Espécie") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
                TextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )
                TextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha do Cartão de Crédito") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Botão Cadastrar
                Button(
                    onClick = {
                        val usuario = hashMapOf(
                            "nome" to nome,
                            "idade" to idade,
                            "especie" to especie,
                            "time" to time,
                            "senha" to senha
                        )

                        db.collection("users")
                            .add(usuario)
                            .addOnSuccessListener {
                                resultado = "Usuário cadastrado com sucesso!"
                                nome = ""
                                idade = ""
                                especie = ""
                                time = ""
                                senha = ""
                            }
                            .addOnFailureListener {
                                resultado = "Erro ao cadastrar: ${it.message}"
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Cadastrar", fontSize = 18.sp)
                }

                // Botão Exibir Dados
                Button(
                    onClick = {
                        resultado = ""
                        db.collection("users")
                            .get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    val nome = document.getString("nome") ?: "?"
                                    val idade = document.getString("idade") ?: "?"
                                    val especie = document.getString("especie") ?: "?"
                                    val time = document.getString("time") ?: "?"
                                    resultado += "Nome: $nome | Idade: $idade | Espécie: $especie | Time: $time\n\n"
                                }
                            }
                            .addOnFailureListener {
                                resultado = "Erro ao buscar dados: ${it.message}"
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Exibir Dados", fontSize = 18.sp)
                }

                // Botão Navegar para outra tela
                Button(
                    onClick = {
                        navController.navigate("tela_exibicao")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Mostrar todos", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Exibição dos dados
                Text(
                    text = resultado.trim(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    color = Color.Black,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            }
        }
    )
}



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "tela_cadastro") {
        composable("tela_cadastro") {
            appAula(navController)
        }
        composable("tela_exibicao") {
            TelaExibicao()
        }
    }
}

@Composable
fun TelaExibicao() {
    var usuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }

    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                usuarios = result.map { doc ->
                    Usuario(
                        nome = doc.getString("nome") ?: "",
                        idade = doc.getString("idade") ?: "",
                        especie = doc.getString("especie") ?: "",
                        time = doc.getString("time") ?: "",
                        senha = doc.getString("senha") ?: ""
                    )
                }
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(usuarios) { usuario ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(Color.White)
                    .padding(10.dp)
            ) {
                Text("Nome: ${usuario.nome}")
                Text("Idade: ${usuario.idade}")
                Text("Espécie: ${usuario.especie}")
                Text("Time: ${usuario.time}")
                Text("Senha: ${usuario.senha}")
            }
        }
    }
}

@Composable
fun appAulaPreviewLight() {
    Column(
        Modifier
            .background(Color.LightGray)
            .fillMaxSize()
    ) {
        // Só a parte do layout da tela sem a navegação
        Text("Tela de Cadastro - Preview")
    }
}


@Preview
@Composable
fun appAulaPreview() {
    appAulaPreviewLight() // Chamando a versão sem navegação para o Preview
}
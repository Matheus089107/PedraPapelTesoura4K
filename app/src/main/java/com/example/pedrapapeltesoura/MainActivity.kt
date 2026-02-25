package com.example.pedrapapeltesoura

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pedrapapeltesoura.ui.theme.PedraPapelTesouraTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PedraPapelTesouraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppEminemJokenpo()
                }
            }
        }
    }
}

// GERENCIADOR DE TELAS
@Composable
fun AppEminemJokenpo() {
    var telaAtual by remember { mutableStateOf("inicio") }

    when (telaAtual) {
        "inicio" -> TelaInicial(aoIniciarJogo = { telaAtual = "jogo" })
        "jogo" -> PedraPapelTesoura4k(
            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
            aoVoltarMenu = { telaAtual = "inicio" }
        )
    }
}

// TELA INICIAL
@Composable
fun TelaInicial(aoIniciarJogo: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Epic Rap Battle of History:", fontSize = 22.sp, color = Color.Gray)
        Text(
            text = "JOKENPÔ vs EMINEM",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Image(
            painter = painterResource(id = R.drawable.eminem3),
            contentDescription = "Eminem com pedra,papel e tesoura",
            modifier = Modifier.size(450.dp)
        )

        Button(
            onClick = aoIniciarJogo,
            modifier = Modifier
                .padding(16.dp)
                .height(60.dp)
                .fillMaxWidth(0.7f)
        ) {
            Text(text = "DESAFIAR EMINEM", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// TELA DO JOGO
@Composable
fun PedraPapelTesoura4k(modifier: Modifier = Modifier, aoVoltarMenu: () -> Unit) {
    val context = LocalContext.current
    var podeClicar by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf(0) }
    var escolhaJogador by remember { mutableStateOf(0) }
    var contador by remember { mutableStateOf(0) }

    LaunchedEffect(contador) {
        if (contador in 1..3) {
            delay(750)
            if (contador < 3) {
                contador += 1
            } else {
                try {
                    val mp = MediaPlayer.create(context, R.raw.rizz)
                    if (mp != null) {
                        mp.setVolume(5.0f, 5.0f)
                        mp.setOnCompletionListener {
                            it.stop()
                            it.release()
                        }
                        mp.start()
                    } else {
                        Log.e("ErroSom", "MediaPlayer retornou null. O arquivo R.raw.rizz existe?")
                    }
                } catch (e: Exception) {
                    Log.e("ErroSom", "Erro ao inicializar som: ${e.message}")
                }

                result = (1..3).random()
                contador = 0
            }
        }
    }

    val imagemResult = when (result) {
        1 -> R.drawable.pedra
        2 -> R.drawable.papel
        3 -> R.drawable.tesoura
        else -> R.drawable.de_costas
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (contador > 0) {
            Text(
                text = contador.toString(),
                fontSize = 80.sp,
                color = Color.Red,
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            Spacer(modifier = Modifier.height(96.dp))
        }

        Image(
            painter = painterResource(imagemResult),
            contentDescription = "Jogada do Eminem",
            modifier = Modifier.size(400.dp),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.height(20.dp))


        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val iniciarProcesso = { escolha: Int ->
                escolhaJogador = escolha
                result = 0
                podeClicar = false
                contador = 1
            }

            Button(onClick = { iniciarProcesso(1) }, enabled = podeClicar) {
                Text(text = "Pedra", fontSize = 18.sp)
            }
            Button(onClick = { iniciarProcesso(2) }, enabled = podeClicar) {
                Text(text = "Papel", fontSize = 18.sp)
            }
            Button(onClick = { iniciarProcesso(3) }, enabled = podeClicar) {
                Text(text = "Tesoura", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // RESULTADO
        if (contador == 0 && result != 0) {
            val mensagem = when {
                escolhaJogador == result -> "Empate!"
                (escolhaJogador == 1 && result == 3) ||
                        (escolhaJogador == 2 && result == 1) ||
                        (escolhaJogador == 3 && result == 2) -> "Você Ganhou!!!!"
                else -> "Você Perdeu!!"
            }

            Text(
                text = mensagem,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (mensagem.contains("Ganhou")) Color(0xFF4CAF50)
                else if (mensagem.contains("Empate")) Color(0xFFDAA520)
                else Color.Red
            )
        } else {
            Spacer(modifier = Modifier.height(38.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // REINICIAR/SAIR
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    result = 0
                    escolhaJogador = 0
                    podeClicar = true
                },
                enabled = !podeClicar && contador == 0
            ) {
                Text(text = "Jogar Novamente", fontSize = 18.sp)
            }

            if (!podeClicar && contador == 0) {
                OutlinedButton(onClick = aoVoltarMenu) {
                    Text(text = "Sair", fontSize = 18.sp)
                }
            }
        }
    }
}
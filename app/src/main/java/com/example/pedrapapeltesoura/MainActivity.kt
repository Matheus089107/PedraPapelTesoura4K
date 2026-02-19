package com.example.pedrapapeltesoura

import android.os.Bundle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
                    PedraPapelTesoura4k(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                    )
                }
            }
        }
    }
}

@Composable
fun PedraPapelTesoura4k(modifier: Modifier = Modifier) {
    // ESTADOS: Variáveis que o Compose monitora para atualizar a tela
    var podeClicar by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf(0) } // Escolha do Computador (Eminem)
    var escolhaJogador by remember { mutableStateOf(0) } // Escolha do Usuário
    var contador by remember { mutableStateOf(0) } // Timer 1, 2, 3

    // Lógica do Timer (Iniciado quando contador muda para 1)
    LaunchedEffect(contador) {
        if (contador in 1..3) {
            delay(1000)
            if (contador < 3) {
                contador += 1
            } else {
                // O sorteio só acontece DEPOIS que o timer chega no 3
                result = (1..3).random()
                contador = 0
            }
        }
    }

    // Define qual imagem mostrar baseada no resultado do sorteio
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
        // Exibição do Timer
        if (contador > 0) {
            Text(
                text = contador.toString(),
                fontSize = 80.sp,
                color = Color.Red,
                style = MaterialTheme.typography.headlineLarge
            )
        } else {
            // Espaço vazio para manter o layout estável quando o timer some
            Spacer(modifier = Modifier.height(96.dp))
        }

        Image(
            painter = painterResource(imagemResult),
            contentDescription = "Jogada do Eminem",
            modifier = Modifier.size(400.dp),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botões de Jogada
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val iniciarProcesso = { escolha: Int ->
                escolhaJogador = escolha
                result = 0 // Reseta a imagem para "de costas"
                podeClicar = false
                contador = 1 // Inicia o timer
            }

            Button(onClick = { iniciarProcesso(1) }, enabled = podeClicar) {
                Text(text = stringResource(R.string.Pedra), fontSize = 20.sp)
            }
            Button(onClick = { iniciarProcesso(2) }, enabled = podeClicar) {
                Text(text = stringResource(R.string.Papel), fontSize = 20.sp)
            }
            Button(onClick = { iniciarProcesso(3) }, enabled = podeClicar) {
                Text(text = stringResource(R.string.Tesoura), fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Exibição do Resultado (Vitória/Derrota)
        if (contador == 0 && result != 0) {
            val mensagem = when {
                escolhaJogador == result -> "!Empate!"
                (escolhaJogador == 1 && result == 3) || // Pedra vence Tesoura
                        (escolhaJogador == 2 && result == 1) || // Papel vence Pedra
                        (escolhaJogador == 3 && result == 2) -> "Você Ganhou!!!!"
                else -> "!!Você Perdeu!!"
            }

            Text(
                text = mensagem,
                fontSize = 32.sp,
                color = if (mensagem.contains("Ganhou")) Color.Green
                else if (mensagem.contains("Empate")) Color(0xFFDAA520) else Color.Red
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Botão Reiniciar
        Button(
            onClick = {
                result = 0
                escolhaJogador = 0
                podeClicar = true
            },
            enabled = !podeClicar && contador == 0
        ) {
            Text(text = stringResource(R.string.reinicializar), fontSize = 22.sp)
        }
    }
}
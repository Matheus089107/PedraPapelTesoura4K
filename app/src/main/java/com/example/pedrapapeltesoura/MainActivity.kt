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
                    // Chama o nosso gerenciador de telas ao invés do jogo direto
                    AppEminemJokenpo()
                }
            }
        }
    }
}

// --- GERENCIADOR DE TELAS ---
@Composable
fun AppEminemJokenpo() {
    // Controla qual tela estamos vendo no momento
    var telaAtual by remember { mutableStateOf("inicio") }

    when (telaAtual) {
        "inicio" -> TelaInicial(aoIniciarJogo = { telaAtual = "jogo" })
        "jogo" -> PedraPapelTesoura4k(
            modifier = Modifier.fillMaxSize().safeDrawingPadding(),
            aoVoltarMenu = { telaAtual = "inicio" }
        )
    }
}

// --- TELA INICIAL ---
@Composable
fun TelaInicial(aoIniciarJogo: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Rap Battle:",
            fontSize = 24.sp,
            color = Color.Gray
        )
        Text(
            text = "JOKENPÔ vs EMINEM",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.de_costas),
            contentDescription = "Eminem de Costas",
            modifier = Modifier.size(300.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(20.dp))

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

// --- TELA DO JOGO (Sua base, levemente adaptada) ---
@Composable
fun PedraPapelTesoura4k(modifier: Modifier = Modifier, aoVoltarMenu: () -> Unit) {
    var podeClicar by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf(0) }
    var escolhaJogador by remember { mutableStateOf(0) }
    var contador by remember { mutableStateOf(0) }

    LaunchedEffect(contador) {
        if (contador in 1..3) {
            delay(1000)
            if (contador < 3) {
                contador += 1
            } else {
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
            modifier = Modifier.size(350.dp), // Diminuí levemente para caber o novo botão
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
                Text(text = stringResource(R.string.Pedra), fontSize = 18.sp)
            }
            Button(onClick = { iniciarProcesso(2) }, enabled = podeClicar) {
                Text(text = stringResource(R.string.Papel), fontSize = 18.sp)
            }
            Button(onClick = { iniciarProcesso(3) }, enabled = podeClicar) {
                Text(text = stringResource(R.string.Tesoura), fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (contador == 0 && result != 0) {
            val mensagem = when {
                escolhaJogador == result -> "!Empate!"
                (escolhaJogador == 1 && result == 3) ||
                        (escolhaJogador == 2 && result == 1) ||
                        (escolhaJogador == 3 && result == 2) -> "Você Ganhou!!!!"
                else -> "!!Você Perdeu!!"
            }

            Text(
                text = mensagem,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (mensagem.contains("Ganhou")) Color(0xFF4CAF50) // Verde mais agradável
                else if (mensagem.contains("Empate")) Color(0xFFDAA520) else Color.Red
            )
        } else {
            // Mantém o layout estável enquanto o resultado não sai
            Spacer(modifier = Modifier.height(38.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Adicionamos os botões em linha quando o jogo acaba
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = {
                    result = 0
                    escolhaJogador = 0
                    podeClicar = true
                },
                enabled = !podeClicar && contador == 0
            ) {
                Text(text = stringResource(R.string.reinicializar), fontSize = 18.sp)
            }

            // Botão para voltar ao Menu Inicial
            if (!podeClicar && contador == 0) {
                OutlinedButton(onClick = aoVoltarMenu) {
                    Text(text = "Sair", fontSize = 18.sp)
                }
            }
        }
    }
}
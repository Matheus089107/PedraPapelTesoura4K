package com.example.pedrapapeltesoura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.pedrapapeltesoura.ui.theme.PedraPapelTesouraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PedraPapelTesouraTheme {
                PedraPapelTesoura4k(modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding() )
            }
        }
    }
}

@Composable
fun PedraPapelTesoura4k(modifier: Modifier = Modifier){
       var result by remember{ mutableStateOf(0)}
    var imagemResult= when(result){
    0 ->R.drawable.de_costas
    1 ->R.drawable.pedra
    2 ->R.drawable.papel
    else ->R.drawable.tesoura
    }
    Column(modifier = Modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
    ){
    Image(painter = painterResource(imagemResult), contentDescription = "EMINEM")
    }


    Button(onClick = {}){
        Text(text = stringResource(R.string.Pedra))
    }

    Button(onClick = {}){
        Text(text = stringResource(R.string.Papel))
    }

    Button(onClick = {}){
        Text(text = stringResource(R.string.Tesoura))
    }

}




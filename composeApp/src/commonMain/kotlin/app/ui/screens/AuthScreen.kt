@file:OptIn(KoinExperimentalAPI::class)

package app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.theme.FireflyAppTheme
import app.viewmodel.AuthScreenState
import app.viewmodel.AuthViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onAuthSuccess: () -> Unit,
    onLoginClick: (String, String) -> Unit,
    runDemoClick: () -> Unit,
) {
    val uiState = authViewModel.uiState.collectAsState()
    val dimensions = FireflyAppTheme.dimensions
//    var url by rememberSaveable { mutableStateOf(BuildConfig.URL) }
//    var token by rememberSaveable { mutableStateOf(BuildConfig.token) }
    var url by rememberSaveable { mutableStateOf("http://192.168.86.24:7182") }
    var token by rememberSaveable { mutableStateOf("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyNiIsImp0aSI6IjgyYTkxNmVlMmQ2OGIzYTgyOTBlYjI0MmRmYmIzMzFkNzIxYjUwODI3NjBjOTk0YmI1MDMyMjQxZjNiMTA0NTAxYmFmNjVlZGM2NTQ1MDg5IiwiaWF0IjoxNzQ3NDE2NTIyLjgzMjQ3NywibmJmIjoxNzQ3NDE2NTIyLjgzMjQ4MSwiZXhwIjoxNzc4OTUyNTIxLjA2MDkwMiwic3ViIjoiMSIsInNjb3BlcyI6W119.CcktDjP8zX0wF23QRpBcRU5z93eiXu9B3rSoj40fFrVcBHG5GhrSMdRg24ZrUkUcWzZn9Hf118roErzpRB6hCThNEJATh8FLOUYrh4zeI4JxWGL800NaWauN_JknFpVSyjsiJvfRlZryYEcQSKJ7biCKj0KPOsOqi0k9JYC-YtSpgtjuDAGAAnbP-NbIC8b0sFv6SHhMBBRJkptgG_Ytq0CPb9XxvLGERMXZxFevyvKJK9G3OdZfOhs9w6u2uO4j25FU87CRXSX_6VhnmVKeQXA7NvH2GoHFYZO3S93B6dQiLOarTjGduyTf7tzmLwLkE3M_lOGKsrKb6xwzGqZzk9aXogXfouBz_YqOhuQdecf-FLAAcIiRestFRCYXLH20Qk-dUH3BL_7ibNw1pOWSestNskNWi-SNffCeAEv0R5gvoYBeoMiHCxEj_NMoVLQg4gNhEhZ10hMO8wbAwgSAB1vNE1UHAVEXkcmQsLxdqxOurqeM1ydGbXw7yhdClGf4iH-Ggsr-FrS59QUx3KBGszvDijJOLlCAcAcAiuY_breTRPT4a9LjJxiEL6i9W5sYUkFRH_WpYoQ6QHvS7Dq1p8DFkWrs2GdVn2l7XnoPPzWtVW-mxbb-_6FD1QMa3bSwM8B08OnJTa4aUNZ_C5gZ06EUd-1PRJml_1hETEEVTj8") }
    LaunchedEffect(Unit) {
        authViewModel.checkAuth()
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FireflyAppTheme.colorScheme.background
    ) {
        if (uiState.value is AuthScreenState.Idle || uiState.value is AuthScreenState.Error) {
            Column(modifier = Modifier.padding(dimensions.contentMargin)) {
                Spacer(modifier = Modifier.height(64.dp))
                OutlinedTextField(
                    modifier = Modifier.padding(horizontal = dimensions.horizontalPadding),
                    label = { Text(text = "URL") },
                    value = url,
                    onValueChange = { url = it }
                )
                Spacer(modifier = Modifier.height(40.dp))
                OutlinedTextField(
                    modifier = Modifier.padding(horizontal = dimensions.horizontalPadding),
                    label = { Text(text = "Token") },
                    value = token,
                    onValueChange = { token = it },
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(64.dp))
//                Button(
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
//                    onClick = { onLoginClick.invoke(url, token) }) {
//                    Text(text = "Login")
//                }
                ElevatedButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.elevatedButtonColors()
                        .copy(
                            containerColor = FireflyAppTheme.colorScheme.secondary,
                            contentColor = androidx.compose.ui.graphics.Color.White
                        ),
                    onClick = { onLoginClick.invoke(url, token) }
                ) {
                    Text(text = "Login")
                }
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    text = "uiState:${uiState.value}",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                ElevatedButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.elevatedButtonColors()
                        .copy(
                            containerColor = FireflyAppTheme.colorScheme.secondary,
                            contentColor = androidx.compose.ui.graphics.Color.White
                        ),
                    onClick = runDemoClick
                ) {
                    Text(text = "Run Demo")
                }
            }
        } else if (uiState.value is AuthScreenState.InProgress) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(96.dp)
                        .wrapContentHeight()
                        .align(Alignment.Center),
                    color = FireflyAppTheme.colorScheme.secondary,
                    trackColor = FireflyAppTheme.colorScheme.secondaryContainer
                )
            }
        }
    }

    if (uiState.value == AuthScreenState.Authenticated) {
        onAuthSuccess.invoke()
    }
}

//@Preview
//@Composable
//fun AuthPreview() {
//    FireflyAppTheme(darkTheme = false) {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = FireflyAppTheme.colorScheme.background
//        ) {
//            AuthScreen(
//                onAuthSuccess = {}, runDemoClick = {}, onLoginClick = { _, _ -> }
//            )
//        }
//    }
//}

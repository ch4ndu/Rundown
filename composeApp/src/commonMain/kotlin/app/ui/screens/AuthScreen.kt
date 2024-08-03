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
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.unit.dp
import app.theme.FireflyAppTheme
import app.viewmodel.AuthScreenState
import app.viewmodel.AuthViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    onAuthSuccess: () -> Unit,
    onLoginClick: (String, String) -> Unit,
) {
    val uiState = authViewModel.uiState.collectAsState()
    val dimensions = FireflyAppTheme.dimensions
//    var url by rememberSaveable { mutableStateOf(BuildConfig.URL) }
//    var token by rememberSaveable { mutableStateOf(BuildConfig.token) }
    var url by rememberSaveable { mutableStateOf("http://192.168.86.24:7182") }
    var token by rememberSaveable { mutableStateOf("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIyNiIsImp0aSI6Ijk1NzUwOGEzMTcxMDg3OGE3Y2M4MmVkOTg3YmQ0M2I5OGEzNTExMWU5MWE3ODgzODQ4MjhkNDkzNWFiODg4MzkxYjZjYzhjOTRlODVkMDYwIiwiaWF0IjoxNzE1NzQ3MjMxLjc3Nzg1OCwibmJmIjoxNzE1NzQ3MjMxLjc3Nzg2MiwiZXhwIjoxNzQ3MjgzMjMxLjc0ODE0LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.Kcd_l7kevEcwO5TooTjMHL2zqjP5Cpa5EobHqdTagF30cNz2y-xH8ZhNBwAPhu_cX2EDfyVuknaduUumsRJ-mbqWDZxAv6w-a8wKW0zpQEhwl5zv_FtBzr3fPvHM25A1wkx767gsk46W-AlqvM_RZwuwbluhbP_Ax9F-tOeYvTCkmAEIMiQC7lz8yn0Vy1sUdAgRJWK-Bp2n6ZljR0KMZ7REEsX05xLrjI58VK56i4EJd5a_FgJ956mihkwQjDIC2j-5gYbo8QVxPBDF_HbffScQuKUk13pT426yWmhoV5MlJN-aATsk_RNVicAWUIL1m_jAqbm4JpTDdudHY_NGrl5a7XAgEn2ceMeEkW4TL7WUjAfS-X1J-IR0O2fKLKFZDmYW5XnPZkpFncHp2fSqwgNa62Yx4229mBaPIQskWyHyqFVcC3Cw1Ynf9eMhaEpkU8eyb-rqaYCasktNWAYjW3ykULroeskVCBXytl07rmHEp_wVgI-3Ky0_cj-a6HSQUNQCB0fiiyj0rwOgeVmzwwGxGs8ftdnPaFlMXXhUFochFRIaNFMaaHNA9eM2cxGQ7W59MBOuVhEis_l2P4i25NXsJr0zAf5HtQGhP2DA1F4VrIAXzR2A6QC9HHB3klMfU0JQcXK8Y_PXC41cQO1XOlmfVqJRa2GqThDeZspT6Kg") }
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
                Text(text = "uiState:${uiState.value}")
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

@Preview
@Composable
fun AuthPreview() {
    FireflyAppTheme(darkTheme = false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FireflyAppTheme.colorScheme.background
        ) {
            AuthScreen(onAuthSuccess = {}) { _, _ ->

            }
        }
    }
}

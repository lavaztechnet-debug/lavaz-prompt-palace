package com.lavaz.promptpalace.ui.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
@Composable
fun SettingsScreen(apiKey: String, selectedModel: String, darkMode: Boolean, temperature: Float, onApiKeyChange: (String) -> Unit, onModelChange: (String) -> Unit, onDarkModeChange: (Boolean) -> Unit, onTemperatureChange: (Float) -> Unit, onReset: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(value = apiKey, onValueChange = onApiKeyChange, modifier = Modifier.fillMaxWidth(), label = { Text("OpenRouter API Key") }, visualTransformation = PasswordVisualTransformation())
        OutlinedTextField(value = selectedModel, onValueChange = onModelChange, modifier = Modifier.fillMaxWidth(), label = { Text("Model") })
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Dark mode")
            Switch(checked = darkMode, onCheckedChange = onDarkModeChange)
        }
        Text("Temperature: ${"%.1f".format(temperature)}")
        Slider(value = temperature, onValueChange = onTemperatureChange, valueRange = 0f..1f)
        Button(onClick = onReset, modifier = Modifier.fillMaxWidth()) { Text("Reset Local Data") }
    }
}

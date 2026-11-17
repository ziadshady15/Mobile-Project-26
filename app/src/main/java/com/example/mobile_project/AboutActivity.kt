package com.example.mobile_project

import android.os.Bundle
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val PrimaryBlue = Color(0xFF6366F1)
private val Background = Color(0xFFF8FAFC)
private val Surface = Color(0xFFFFFFFF)
private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)
private val teamMembers = listOf(
    "Ziad Shady Nabil – 2023170235",
    "Fayrouz Waleed Farid – 2023170433",
    "Abdalah Mohamed Abdo – 2023170360",
    "Marina Joseph Ammar – 2023170465",
    "Ronaldo Rafik Rene – 2023170228",
    "Mina Maged Adel – 2023170654")
class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContent replaces setContentView: the UI is built by @Composable functions
        setContent {
            MaterialTheme {
                AboutScreen(onBack = { finish() })
            }
        }
    }
}
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(PrimaryBlue)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "About",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Contact Manager",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Version 1.0",
                color = TextSecondary,
                fontSize = 14.sp
            )
            Text(
                text = "Contact Manager is a simple offline app to save, search and call your contacts. Data is stored on the device with Room.",
                color = TextPrimary,
                fontSize = 16.sp
            )
            Text(
                text = "FNI-421 Mobile Computing",
                color = PrimaryBlue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Ain Shams University",
                color = TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            TeamCard()
        }
    }
}
@Composable
fun TeamCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Team",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)
            teamMembers.forEachIndexed { index, member ->
                Text(
                    text = "${index + 1}. $member",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    MaterialTheme {
        AboutScreen(onBack = {})
    }
}

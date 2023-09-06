package com.godzuche.recircu.feature.seller.seller_dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun AccountBalance() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 37.dp, end = 20.dp, bottom = 23.dp)
        ) {
            Text(
                "Balance",
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "N12,000",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
//                color = Color.White
            )
            Spacer(modifier = Modifier.height(19.dp))
            Surface(
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
//                    .height(80.dp)
            ) {
                AchievementSummary()
            }
        }
    }
}

@Composable
fun AchievementSummary() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Waste Recycled: 20kg",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Total Points: 239",
                style = MaterialTheme.typography.labelMedium
            )
        }
        Text(
            text = "Total Recircoin earned: 27 Rec",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

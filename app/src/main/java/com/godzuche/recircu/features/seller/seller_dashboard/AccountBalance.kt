package com.godzuche.recircu.features.seller.seller_dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
//                color = Color.White
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
                    .height(80.dp)
            ) {
            }
        }
    }
}
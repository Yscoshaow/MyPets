package com.chsteam.mypets.core.objectives

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chsteam.mypets.api.Objective
import com.chsteam.mypets.core.Instruction

class SelfObjective(instruction: Instruction) : Objective(instruction) {

    override val typeName: String
        get() = "自监督任务"

    private val text: String

    init {
        text = instruction.next() ?: ""
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TaskCard() {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = typeName, style = MaterialTheme.typography.displaySmall)
                Spacer(Modifier.height(12.dp))
                TextField(value = text, onValueChange = {}, readOnly = true)
                Spacer(Modifier.height(24.dp))
                
                Row {

                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Check, contentDescription = "Check")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Close, contentDescription = "Close")
                    }
                }
            }
        }
    }
}
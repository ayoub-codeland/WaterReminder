package waterly.drinkwater.reminder.features.settings.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import waterly.drinkwater.reminder.core.domain.model.AgeGroup
import waterly.drinkwater.reminder.core.domain.model.BiologicalSex
import waterly.drinkwater.reminder.core.ui.components.AgeGroupCard
import waterly.drinkwater.reminder.core.ui.components.AppScaffold
import waterly.drinkwater.reminder.core.ui.components.BiologicalSexCard
import waterly.drinkwater.reminder.core.ui.extensions.shadowSm

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel
) {
    val state by viewModel.state.collectAsState()

    EditProfileScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun EditProfileScreenContent(
    state: EditProfileState,
    onEvent: (EditProfileEvent) -> Unit
) {
    AppScaffold(
        topBar = {
            Column {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        IconButton(
                            onClick = { onEvent(EditProfileEvent.OnCancelClick) },
                            modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "Edit Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Profile Image Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                Box {
                    // Profile Image
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(
                                width = 4.dp,
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape
                            )
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    // Camera button
                    Surface(
                        onClick = { onEvent(EditProfileEvent.OnProfileImageClick) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(32.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 4.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Change photo",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Display Name
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(start = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            "Display Name",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            "(Optional)",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    OutlinedTextField(
                        value = state.displayName,
                        onValueChange = { onEvent(EditProfileEvent.OnNameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Enter your name",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }

                // Biological Sex
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Biological Sex",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        BiologicalSexCard(
                            sex = BiologicalSex.MALE,
                            isSelected = state.biologicalSex == BiologicalSex.MALE,
                            onClick = {
                                onEvent(
                                    EditProfileEvent.OnBiologicalSexChanged(
                                        BiologicalSex.MALE
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )

                        BiologicalSexCard(
                            sex = BiologicalSex.FEMALE,
                            isSelected = state.biologicalSex == BiologicalSex.FEMALE,
                            onClick = {
                                onEvent(
                                    EditProfileEvent.OnBiologicalSexChanged(
                                        BiologicalSex.FEMALE
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Age Group
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Age Group",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AgeGroupCard(
                                ageGroup = AgeGroup.AGE_18_30,
                                isSelected = state.ageGroup == AgeGroup.AGE_18_30,
                                onClick = { onEvent(EditProfileEvent.OnAgeGroupChanged(AgeGroup.AGE_18_30)) },
                                modifier = Modifier.weight(1f)
                            )

                            AgeGroupCard(
                                ageGroup = AgeGroup.AGE_31_50,
                                isSelected = state.ageGroup == AgeGroup.AGE_31_50,
                                onClick = { onEvent(EditProfileEvent.OnAgeGroupChanged(AgeGroup.AGE_31_50)) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AgeGroupCard(
                                ageGroup = AgeGroup.AGE_51_60,
                                isSelected = state.ageGroup == AgeGroup.AGE_51_60,
                                onClick = { onEvent(EditProfileEvent.OnAgeGroupChanged(AgeGroup.AGE_51_60)) },
                                modifier = Modifier.weight(1f)
                            )

                            AgeGroupCard(
                                ageGroup = AgeGroup.AGE_60_PLUS,
                                isSelected = state.ageGroup == AgeGroup.AGE_60_PLUS,
                                onClick = { onEvent(EditProfileEvent.OnAgeGroupChanged(AgeGroup.AGE_60_PLUS)) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

            // Save button at bottom (matching Activity Level/Daily Goal style)
            Button(
                onClick = { onEvent(EditProfileEvent.OnSaveClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        "Save",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

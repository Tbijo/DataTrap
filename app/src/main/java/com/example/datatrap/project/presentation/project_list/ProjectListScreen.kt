package com.example.datatrap.project.presentation.project_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.datatrap.core.presentation.LoadingScreen
import com.example.datatrap.core.presentation.components.NavigationScaffold
import com.example.datatrap.core.presentation.components.SearchTextField
import com.example.datatrap.project.presentation.project_list.components.ProjectListItem

@Composable
fun ProjectListScreen(
    onEvent: (ProjectListScreenEvent) -> Unit,
    state: ProjectListUiState,
) {
    when (state.isLoading) {
        true -> LoadingScreen()
        false -> ScreenContent(onEvent, state)
    }
}

@Composable
private fun ScreenContent(
    onEvent: (ProjectListScreenEvent) -> Unit,
    state: ProjectListUiState,
) {
    NavigationScaffold(
        title = "Projects",
        errorState = state.error,
        onDrawerItemClick = {
            onEvent(
                ProjectListScreenEvent.OnDrawerItemClick(it)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(
                    ProjectListScreenEvent.OnAddButtonClick
                )
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add buton")
            }
        },
        actions = {
            IconButton(onClick = {
                onEvent(
                    ProjectListScreenEvent.OnLogoutButtonClick
                )
            }) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "logout button",
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            SearchTextField(
                text = state.searchTextFieldValue,
                hint = state.searchTextFieldHint,
                onValueChange = { text ->
                    onEvent(
                        ProjectListScreenEvent.OnSearchTextChange(text)
                    )
                },
                onFocusChange = { focusState ->
                    onEvent(
                        ProjectListScreenEvent.ChangeTitleFocus(focusState)
                    )
                },
                isHintVisible = state.isSearchTextFieldHintVisible,
            )

            LazyColumn {
                items(state.projectList) { project ->
                    ProjectListItem(
                        projectEntity = project,
                        onListItemClick = {
                            onEvent(
                                ProjectListScreenEvent.OnItemClick(project)
                            )
                        },
                        onUpdateClick = {
                            onEvent(
                                ProjectListScreenEvent.OnUpdateButtonClick(project)
                            )
                        },
                        onDeleteClick = {
                            onEvent(
                                ProjectListScreenEvent.OnDeleteClick(project)
                            )
                        },
                    )
                }
            }
        }
    }
}
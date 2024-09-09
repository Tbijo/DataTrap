package com.example.datatrap.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datatrap.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class DrawerItem(
    val id: DrawerScreens,
    val title: String,
    val contentDescription: String,
    val iconVector: ImageVector?,
    val iconPainter: Int? = null,
)

enum class DrawerScreens {
    PROJECTS, SPECIES, SETTINGS, ABOUT, SYNCHRONIZE
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Row {
            Image(painter = painterResource(id = R.drawable.ikona_web), contentDescription = "mouse icon")
            Image(painter = painterResource(id = R.drawable.ki), contentDescription = "ukf icon")
        }
        Text(text = "Digital Automatized Trapping Protocol")
        Text(text = "The Department of Ecology and Environmental Sciences", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun DrawerBody(
    items: List<DrawerItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (DrawerItem) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable {
                        onItemClick(item)
                    }
            ) {
                item.iconVector?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = item.contentDescription
                    )
                }
                ?: item.iconPainter?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = item.contentDescription
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScaffold(
    title: String,
    errorState: String?,
    onDrawerItemClick: (DrawerScreens) -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable (PaddingValues) -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerHeader()

            DrawerBody(
                items = listOf(
                    DrawerItem(
                        id = DrawerScreens.PROJECTS,
                        title = "Projects",
                        contentDescription = "Go to projects screen",
                        iconVector = Icons.Default.List,
                    ),
                    DrawerItem(
                        id = DrawerScreens.SPECIES,
                        title = "Species",
                        contentDescription = "Go to species screen",
                        iconVector = null,
                        iconPainter = R.drawable.ic_mouse,
                    ),
                    DrawerItem(
                        id = DrawerScreens.SETTINGS,
                        title = "Settings",
                        contentDescription = "Go to settings screen",
                        iconVector = Icons.Default.Settings,
                    ),
                    DrawerItem(
                        id = DrawerScreens.ABOUT,
                        title = "About",
                        contentDescription = "Go to about screen",
                        iconVector = Icons.Default.Info,
                    ),
                    DrawerItem(
                        id = DrawerScreens.SYNCHRONIZE,
                        title = "Synchronize",
                        contentDescription = "Go to synchronize screen",
                        iconVector = Icons.Filled.Share, // TODO cloud
                    ),
                ),
                onItemClick = {
                    onDrawerItemClick(it.id)
                },
            )
        },
        content = {
            MyScaffold(
                title = title,
                errorState = errorState,
                floatingActionButton = floatingActionButton,
                actions = actions,
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Toggle drawer",
                        )
                    }
                },
                content = content,
            )
        },
    )
}
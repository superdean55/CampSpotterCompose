package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import hr.ferit.dejanmihic.campspottercompose.ui.graphs.AuthScreen

@Composable
fun CampSpotterNavigationRail(
    currentTab: CampSpotNavigationType,
    onTabPressed: ((CampSpotNavigationType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationRailItem(
                selected = currentTab == navItem.campSpotType,
                onClick = { onTabPressed(navItem.campSpotType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}
@Composable
fun CampSpotterBottomNavigationBar(
    currentTab: CampSpotNavigationType,
    onTabPressed: ((CampSpotNavigationType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inversePrimary,
        modifier = modifier
    ) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                selected = currentTab == navItem.campSpotType,
                onClick = { onTabPressed(navItem.campSpotType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

data class NavigationItemContent(
    val campSpotType: CampSpotNavigationType,
    val icon: ImageVector,
    val text: String
)

enum class CampSpotNavigationType{
    ALL_CAMP_SPOTS, MY_CAMP_SPOTS, SKETCHES
}

sealed class CampSpotType(val text: String) {
    object Sketch : CampSpotType(text = "SKETCH")
    object Published : CampSpotType(text = "PUBLISHED")
}
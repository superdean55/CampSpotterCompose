package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CampSpotterNavigationRail(
    currentTab: CampSpotType,
    onTabPressed: ((CampSpotType) -> Unit),
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
    currentTab: CampSpotType,
    onTabPressed: ((CampSpotType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
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
    val campSpotType: CampSpotType,
    val icon: ImageVector,
    val text: String
)

enum class CampSpotType{
    ALL_CAMP_SPOTS, MY_CAMP_SPOTS, SKETCHES
}
package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.overview.OverviewBody
import com.example.compose.rally.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class OverviewScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overviewScreen_alertsDisplayed() {
        composeTestRule.setContent {
            RallyTheme {
                OverviewBody()
            }
        }

        composeTestRule
            .onNodeWithText("Alerts")
            .assertIsDisplayed()

        //  테스트코드의 finder를 호출하기전 앱이 유휴상태가 될 때까지 기다린다.
        //  Composing이 덜 됐는데 검색하면 안되기 때문
        //  근데 이렇게 애니메이션이 무한정 돌아가면 유휴상태가 안되기 때문에 테스트를 할 수가 없다.(ComposeNotIdleException)

        //  쏠루션1. 개발자 옵션에서 애니메이션을 끈다.
        //  쏠루션2. 애니메이션을 무한 재시작하는 대신 InfiniteTransition을 사용한다.
        //      이건 애초에 테스트를 고려해 설계되어, 유휴상태를 보장한다.

        //  쏠루션 적용은 OverviewScreen.kt를 참고
    }
}

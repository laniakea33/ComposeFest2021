package com.example.compose.rally

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.compose.rally.ui.components.RallyTopAppBar
import com.example.compose.rally.ui.theme.RallyTheme
import org.junit.Rule
import org.junit.Test

class TopAppBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

//    @get:Rule
//    val composeTestRule = createAndroidComposeRule(RallyActivity::class.java)

    @Test
    fun rallyTopAppBarTest() {
        val allScreens = RallyScreen.values().toList()
        composeTestRule.setContent {
            RallyTheme {
                RallyTopAppBar(
                    //  기본 MaterialTheme에 맞게 생성됨
                    allScreens = allScreens,
                    onTabSelected = {},
                    currentScreen = RallyScreen.Accounts,
                )
            }
        }

        //  기본적인 패턴
//        composeTestRule{.finder}{.assertion}{.action}

        //  테스트할 때 사용가능한 툴들은 여기 정리 돼 있음
        //  https://developer.android.com/jetpack/compose/testing-cheatsheet?hl=ko

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)    //  content Description으로 특정 노드를 검색함
            .assertIsSelected() //   selected인지 검증

        //  시멘틱 트리를 로그캣에서 조회한다.
//        composeTestRule.onRoot().printToLog("currentLabelExists")
        //  Modifier.clearAndSetSemantics는 기존 시멘틱을 clear하고,
        //  새로운 시멘틱 속성을 첨부한다.

        composeTestRule
            .onNodeWithContentDescription(RallyScreen.Accounts.name)
            .assertExists()

        //  시멘틱 트리는 기본적으로 딱 필요한(고수준의) 정보만 제공한다.
        //  그래서 하위 Composable들의 시멘틱 트리들을 가져와서 merge하면
        //  저레벨의 정보들 까지 확인할 수 있다.
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")

        composeTestRule
            .onNode(
                //  특정 Text속성을 가진 and 특정 조건을 만족하는 부모를 가진 Node를 검색
                hasText(RallyScreen.Accounts.name.uppercase()) and
                        hasParent(
                            hasContentDescription(RallyScreen.Accounts.name)
                        ),
                useUnmergedTree = true
            )
            .assertExists()
        //  테스트의 고립을 위해서 부모를 이용한 매칭은 안하는게 좋긴한데
        //  여러 조건을 이용해 특정 노드를 정확히 찾는 법을 알려주려고 이런거라함
    }

    @Test
    fun clickTab_tabSelected() {
        //  Optional exercise
        composeTestRule.setContent {
            RallyTheme {
                RallyApp()
            }
        }

        composeTestRule.onNodeWithContentDescription(RallyScreen.Bills.name)
            .performClick()
            .assertIsSelected()
    }
}

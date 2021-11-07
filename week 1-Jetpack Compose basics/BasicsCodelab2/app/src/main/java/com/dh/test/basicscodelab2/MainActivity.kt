package com.dh.test.basicscodelab2

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dh.test.basicscodelab2.ui.theme.BasicsCodelab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelab2Theme {
                MyApp()
            }
        }
    }
}

@Composable
private fun MyApp() {
    //  위임 오퍼레이터 by 를 사용하여 getValue( = .value)를 생략함
    //  remember 함수로 저장된 State는 영구적이지 않기 때문에 액티비티가 재생성되면(onConfigureChange or background kill) 다시 최초의 상태로 돌아간다.
    //  var shouldShowOnboarding by remember { mutableStateOf(true) }
    //  remember {} 대신 rememberSaveable{}을 사용하면 이렇게 액티비티 재생성 상황에서 상태를 유지시킬 수 있다.
    //  영구보존은 아니고 그냥 재생성때만 보존해 줌
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    if (shouldShowOnboarding) {
        OnboardingScreen {
            shouldShowOnboarding = false
        }
    } else {
        Greetings()
    }

}

@Composable
fun Greetings(names: List<String> = List(1000) { "$it"}) {
    //  LazyColumn/Row : 뷰 시스템의 RecyclerView와 대응된다.
    //  그러나 뷰를 재활용하는건 아니고 스크롤 할때 새로 Composing 하지만
    //  뷰 시스템과는 다르게 성능에는 큰 이슈가 없다.
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(names) { name ->
            Greeting(name)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Card(
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        CardContent(name)
    }
}

@Composable
fun CardContent(name: String) {
    var expanded by remember { mutableStateOf(false) }

    //  기본적인 애니메이션 사용법
    //  by animateDpAsState() : targetValue가 될때 까지 value를 계속 변경한다
    //  내부적으로 remember된 state가 변경되므로 계속 recompose가 발생함
    //  이 케이스에서는 LazyColumn 내에서 스크롤다운 -> 업하면 뷰 트리에서 나가리됐다가 다시 생기기 때문에
    //  값이 보존되지 않는다. 하려면 rememberSavable을 사용해야 함
    //  animateXXAsState 함수는 인터럽터블 하다. 언제든 재시작 가능
//    val extraPadding by animateDpAsState(
//        targetValue = if (expanded) 48.dp else 0.dp,
//        //  animationSpec : 애니메이션에 여러가지 부가효과를 준다. 여기서는 스프링처럼 튕기게 함.
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        )
//    )

    Row(modifier = Modifier.padding(24.dp)
        //  recompose될 때 컨텐트의 크기에 따라 애니메이션을 알아서 적용해준다.
        //  extraPadding는 제거 가능
        .animateContentSize()
    ) {
        //  Composable은 State<T>를 구독하다가 값이 변경되면 recompose된다.
        //  이런 State를 각 Compose의 내부적인 private 변수처럼 활용하기 위해서 remember 함수로 State를 래핑해준다.
        //  그럼 recompose할때 값이 변경되지 않고 유지됨
        Column(
            modifier = Modifier
                .weight(1f, true)
            //  coerceAtLeast(Int) : 주식으로 따지면 지지선
            //  extraPadding이 0.dp 밑으로 못내려가게 함.
            //  이 예제에서는 음수로 가면 앱이 죽으므로 사용
//                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
        ) {
            Text(text = "Hello,")
            Text(text = "$name", style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.ExtraBold
            ))
            if (expanded) {
                Text(text = (stringResource(R.string.content_text)).repeat(4))
            }
        }
        IconButton(
            onClick = { expanded = !expanded}) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = stringResource(id = if (expanded) R.string.show_less else R.string.show_more)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)
@Composable
fun DefaultPreview() {
    BasicsCodelab2Theme {
        Greetings()
    }
}

@Composable
fun OnboardingScreen(
    onClick: () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = { onClick() }
            ) {
                Text("Continue")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelab2Theme {
        OnboardingScreen {}
    }
}
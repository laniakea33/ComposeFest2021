package com.dh.test.layoutincompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.dh.test.layoutincompose.ui.theme.LayoutInComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutInComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
//                    PhotographerCard()
//                    LayoutsCodelab()
//                    SimpleList()
                    LazyList()
//                    MyOwnColumn(Modifier.padding(8.dp)) {
//                        Text("MyOwnColumn")
//                        Text("places items")
//                        Text("vertically.")
//                        Text("We've done it by hand!")
//                    }
//                    ChipsInStraggeredGrid()
                }
            }
        }
    }
}

//  Caller가 해당 Composable을 더 유연하게 사용할 수 있도록
//  직접 Composable을 만들 때는 Modifier를 파라미터로 가지고 있는게 권장된다.
//  첫번째 파라미터로 두는게 컨벤션임
@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    //  Modifier 체이닝은 위에서부터 적용되기 때문에 순서가 중요함.
    //  아래처럼 clickble -> padding순으로 체이닝하면 clickable된 상태에서 padding이 붙어 전 영역이 clickable해지지만
    //  반대로 padding -> clickable순으로 체이닝하면 padding이 적용된 영역을 제외하고 content영역만 clickable이 적용된다.
    Row(modifier = modifier
        .padding(8.dp)
        .clip(RoundedCornerShape(4.dp))
        .background(MaterialTheme.colors.surface)
        .clickable { }
        .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),    //  사이즈 지정 Modifier
            shape = CircleShape,    //  동그란 모양
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {

        }

        //  Column이나 Row와 같은 레이아웃의 내부 스코프에서는
        //  해당 레이아웃에만 어울리는 특정한 modifier에 접근할 수 있다.
        //  따라서 해당 스코프에서 사용가능한 modifier를 쉽게 찾을 수 있음.
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = "Alfred Sisley", fontWeight = FontWeight.Bold)
            //  CompositionLocalProvider는 특정 데이터를 하위 Composition트리에 암묵적으로 전달한다.
            //  여기서 전달되는 데이터는 ContentAlpha.medium이다.
            //  자식 Composable에 동일한 alpha값을 설정한다.
            //  LocalContentAlpha provides ContentAlpha.medium는
            //  LocalContentAlpha.provides(ContentAlpha.medium)와 같다.(infix function)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium,) {
                Text("3 minute ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerCardPreview() {
    LayoutInComposeTheme {
        PhotographerCard()
    }
}

@Composable
fun LayoutsCodelab() {
    //  Material Design 정책의 가장 상위레벨 Composable
    //  여러가지 Slot을 가지고 있으며 그 중 body content는 필수임
    Scaffold(
        topBar = {
            CustomTopAppBar("LayoutsCodelab")
        },
        bottomBar = {
            var selectedItem by rememberSaveable { mutableStateOf(0) }
            val items = listOf("아침", "점심", "저녁", "야밤")
            BottomNavigation {
                items.forEachIndexed { index, item ->
                    BottomNavigationItem(
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        icon = {
                            Icon(Icons.Default.MailOutline, contentDescription = null)
                        },
                        label = {
                            Text(item)
                        }
                    )
                }
            }
        }
    ) {
        BodyContent(
            Modifier
                .padding(it)
                .padding(8.dp))
    }
}

@Composable
fun CustomTopAppBar(text: String) {
    TopAppBar(  //  일반적인 AppBar를 위한 Composable
        title = {
            Text(text = text)
        },
        actions = {
            var like by rememberSaveable { mutableStateOf(false) }
            IconButton(onClick = {
                like = !like
            }) {
                if (like) {
                    Icon(Icons.Filled.Favorite, contentDescription = null)
                } else {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                }
                //  추가적인 아이콘 라이브러리
                //  androidx.compose.material:material-icons-extended:$compose_version
            }
        },
    )
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    //  modifier의 값이 완전 고유의 값이라 변동 가능성이 없다면 Composable내의 루트 Child에게 주는게 더 좋을 듯.
    //  아니면 그냥 호출할 때 정해주는게 좋을 것 같다.
    Column(modifier = modifier) {
        Text("Hi there!")
        Text("Thanks for going through the Layouts codelab")
    }
}

@Composable
@Preview(showBackground = true)
fun LayoutsCodelabPreview() {
    LayoutInComposeTheme {
        LayoutsCodelab()
    }
}

@Composable
fun SimpleList() {
    //  Column은 기본적으로 스크롤을 지원하지 않는다.
    //  스크롤 기능은 아래처럼 modifier.verticalScroll를 설정해줘야 함.
    val scrollState = rememberScrollState()

    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text("Item #$it")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SimpleListPreview() {
    LayoutInComposeTheme {
        SimpleList()
    }
}

@Composable
fun LazyList() {
    //  LazyList는 스크롤을 지원하지만, 직접 조작하기 위해 state를 사용할 수 있다.
    val listSize = 100
    val scrollState = rememberLazyListState()
    //  scrollState를 조작할 때는, 스크롤이 동작하는 동안 UI렌더링을 방해하지 않기 위해
    //  코루틴 스코프를 사용해야 한다. 이 스코프 내에서 조작할 것.
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row {
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Text("Scroll to the top")
            }
            Button(onClick = {
                coroutineScope.launch {
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }) {
                Text("Scroll to the end")
            }
        }

        LazyColumn(state = scrollState) {
            items(listSize) {
                ImageListItem(it)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LazyListPreview() {
    LayoutInComposeTheme {
        LazyList()
    }
}

@Composable
fun ImageListItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            //  네트워크를 통한 이미지 다운 및 렌더링을 위한 라이브러리 coil 사용
            //  you can choose Glide via an Accompanist wrapper if you prefer.
            painter = rememberImagePainter(
                data = "https://www.saraminbanner.co.kr/app_promote/banner/2020/11/qkaijp_py2r-ukjlzn_bannersaraminappdownload.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$index", style = MaterialTheme.typography.subtitle1)
    }
}

/*
각 Composable은 하나의 부모와 여러 자식을 갖고, 또한 x,y 위치와 가로, 세로 사이즈를 갖는다.
Composable은 각각 스스로를 측정하는데, 이 때 가로, 세로의 최대/최소값을 정의한 제약조건을 만족시켜야 한다.
이 과정에서 만약 Composalbe이 자식을 갖고있을 때, 그 자식들을 모두 측정해야 한다. Composable이 측정을 완료하면,
각 자식들을 배치할 수 있게 된다.

Compose UI는 자식을 다중 측정하지 않는다.
 */

/*
레이아웃 커스텀 방법1. Modifier 커스텀

FirstBaseline : Text내부에서 Top부터 BaseLine까지의 거리
FirstBaseLineToTop : 레이아웃의 상단으로부터 그 안에 들어갈 Text의 BaseLine까지의 거리
이 Modifier는 그 거리를 조절한다. 왜 그걸 하고 싶어하는지 모르겠다.
 */
fun Modifier.firstBaseLineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->
        //  measurable을 제약조건을 이용해 측정하면 placeable이 리턴된다.
        //  측정은 한번만 해야함
        val placeable = measurable.measure(constraints)

        //  해당 Composable이 FirstBaseline을 가지고 있는지 체크

        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]

        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            //  이거 호출안하면 렌더링이 안된다.
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Preview(showBackground = true)
@Composable
fun TextWithPaddingToBaselinePreview() {
    LayoutInComposeTheme {
        Text("Hi There!", Modifier.firstBaseLineToTop(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TextWithNormalPaddingPreview() {
    LayoutInComposeTheme {
        Text("Hi There!", Modifier.padding(top = 32.dp))
    }
}

/*
레이아웃 커스텀 방법2. Layout Composable 사용

Column을 직접 만들어본다.
Layout의 람다 내에서 직접 자식을 측정하고, 위치시키는 방식을 지정한다.
 */
@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var yPosition = 0

        //  layout 함수를 통해 레이아웃 자체의 크기를 지정
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach {
                it.placeRelative(x = 0, y = yPosition)
                yPosition += it.height
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MyOwnColumnPreview() {
    LayoutInComposeTheme {
        MyOwnColumn {

        }
    }
}

//  더더 복잡한 커스텀 레이아웃
@Composable
fun ChipsInStraggeredGrid(modifier: Modifier = Modifier) {
    val topics = listOf(
        "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
        "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
        "Religion", "Social sciences", "Technology", "TV", "Writing"
    )

    Row(
//        modifier = modifier //  일단 사이즈가 200으로 제한된 다음 패딩이 붙으므로 실제 Row의 사이즈는 168x168
//            .background(color = Color.LightGray)
//            .size(200.dp)
//            .padding(16.dp)
//            .horizontalScroll(rememberScrollState())
        modifier = modifier //  일단 패딩이 붙은 후 사이즈가 200이 됨. 이 때 사이즈는 패딩된 영역은 제외하고 적용된다.
            .background(color = Color.LightGray)
            .padding(16.dp)
            .size(200.dp)
            .horizontalScroll(rememberScrollState())
    //  modifier는 순서대로 실행되며, 사이즈는 역순으로 적용된다.
    ) {
        StraggeredGrid {
            for(topic in topics) {
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

@Preview
@Composable
fun ChipsInStraggeredGridPreview() {
    LayoutInComposeTheme {
        ChipsInStraggeredGrid()
    }
}

@Composable
fun StraggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)

            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable
        }

        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        layout(width, height) {
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }

        //  Rtl, LtR을 조작하기 위해 layoutDirection을 사용한다.
        //  이 때는 placeRelative말고 place를 통해 배치해야 함
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = Color.Black, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    LayoutInComposeTheme {
        Chip(text = "Hi there")
    }
}

@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        val (button1, button2, text) = createRefs()

        Button(
            onClick = {

            },
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) {
            Text("Button1")
        }

        Text("Text", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerHorizontallyTo(parent)
        })

        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = {

            },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text("Button2")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConstraintLayoutContentPreview() {
    LayoutInComposeTheme {
        ConstraintLayoutContent()
    }
}

@Composable
fun LargeConstraintLayout() {
    ConstraintLayout(
//        modifier = Modifier.fillMaxWidth()
    ) {
        val text = createRef()

        //  barrier, guideline 비슷한 개념인 듯
        val guideline = createGuidelineFromStart(fraction = 0.5f)
        Text(
            "This is a very very very very very very very long text",
            Modifier.constrainAs(text) {
                linkTo(start = guideline, end = parent.end)
                width = Dimension.preferredWrapContent //  종류가 많아서 헷갈리니까 구글링할 것
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LargeConstraintLayoutPreview() {
    LayoutInComposeTheme {
        LargeConstraintLayout()
    }
}

@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp)
        } else {
            decoupledConstraints(margin = 32.dp)
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = {

                },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin = margin)
        }
        constrain(text) {
            top.linkTo(parent.top, margin = margin)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DecoupledConstraintLayoutPreview() {
    LayoutInComposeTheme {
        DecoupledConstraintLayout()
    }
}


//  Intrinsic : 고유한, 본질적인
//  (min|max)IntrinsicWidth : Height가 주어졌을 때, 가질 수 있는 Width의 최대/최소값
//  (min|max)IntrinsicHeight : Width가 주어졌을 때, 가질 수 있는 Height의 최대/최소값
@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )

        Divider(
            color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp)
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),

            text = text2
        )
    }
}

@Preview
@Composable
fun TwoTextsPreview() {
    LayoutInComposeTheme {
        Surface {
            TwoTexts(text1 = "Hi", text2 = "there")
        }
    }
}
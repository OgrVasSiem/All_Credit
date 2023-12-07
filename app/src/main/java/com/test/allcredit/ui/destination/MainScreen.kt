package com.test.allcredit.ui.destination

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.test.allcredit.R
import com.test.allcredit.ui.destination.components.MicroLoanOffer
import com.test.allcredit.ui.destinations.destinations.SelectCountryDestination
import com.test.allcredit.ui.navGraphs.RootNavigator

@Composable
@Destination
@RootNavGraph(start = true)
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    rootNavigator: RootNavigator
) {
    MainScreenContent(
        viewModel = viewModel,
        rootNavigator = rootNavigator
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    viewModel: MainViewModel,
    rootNavigator: RootNavigator
) {
    val storeInfo = viewModel.storeInfo.collectAsState().value

    val activeCountryCode = viewModel.activeCountryCode.collectAsState().value

    val countries = viewModel.countries.collectAsState().value

    val activeCountryFlagUrl = countries?.find { it.code == activeCountryCode }?.flagUrl ?: ""

    Scaffold(
        modifier = Modifier
            .background(Color.White)
            .statusBarsPadding(),
        topBar = {
            TopAppBar(
                iconUri = activeCountryFlagUrl,
                rootNavigator = rootNavigator
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFFBFAFF)),
        ) {
            if (storeInfo != null) {
                Filters(
                    activeFilter = viewModel.activeFilterType,
                    onActiveFilterChange = viewModel::onActiveFilterChange,
                    filterList = FilterType.values().toList()
                )

                Spacer(modifier = Modifier.height(10.dp))

                infoBox()

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn {
                    items(storeInfo ?: listOf()) { offer ->
                        MicroLoanOffer(offer = offer)
                    }
                }
            } else {
                LoadingBox()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(
    iconUri: String,
    rootNavigator: RootNavigator
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (iconUri.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(Uri.parse(iconUri)),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .size(34.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            rootNavigator.navigate(SelectCountryDestination)
                        }
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.microlaons),
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight(600)
            )
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = Color(0xFFFBFAFF),
            scrolledContainerColor = Color(0xFFFBFAFF),
            titleContentColor = Color.Black
        )
    )
}

@Composable
private fun Filters(
    activeFilter: FilterType,
    onActiveFilterChange: (FilterType) -> Unit,
    filterList: List<FilterType>
) {
    LazyRow {
        item {
            Spacer(modifier = Modifier.width(14.dp))
        }

        items(filterList) { filter ->
            FilterBox(
                isActive = filter == activeFilter,
                name = filter.labelId
            ) {
                onActiveFilterChange(filter)
            }
        }

        item {
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
private fun FilterBox(
    isActive: Boolean,
    @StringRes name: Int,
    onClick: () -> Unit,
) {
    val (backgroundColor, borderColor, nameColor) = remember(isActive) {
        if (isActive) {
            Triple(Color(0xFFC2FFE9), Color(0xFF26CFA2), Color.Black)
        } else {
            Triple(Color.White, Color(0xFFF2F2F8), Color(0xB3343D4E))
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .defaultMinSize(minHeight = 36.dp)
            .clip(RoundedCornerShape(30.dp))
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(30.dp))
            .background(color = backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false)
            ) {
                onClick()
            }
    ) {
        Text(
            text = stringResource(name),
            color = nameColor,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(500),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun LoadingBox() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            color = Color.Black,
            strokeWidth = 3.dp,
            modifier = Modifier
                .size(46.dp)
        )
    }
}

@Composable
private fun infoBox() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 17.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFECDF))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.info_text),
                modifier = Modifier
                    .padding(start = 14.dp)
                    .weight(0.7f),
                color = Color.Black,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(600),
                maxLines = 3,
            )

            Image(
                painter = painterResource(id = R.drawable.img_money),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .weight(0.3f)
            )
        }
    }
}


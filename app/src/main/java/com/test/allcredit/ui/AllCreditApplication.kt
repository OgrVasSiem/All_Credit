package com.test.allcredit.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.test.allcredit.ui.destinations.NavGraphs
import com.test.allcredit.ui.destinations.destinations.MainScreenDestination
import com.test.allcredit.ui.destinations.destinations.SelectCountryDestination
import com.test.allcredit.ui.navGraphs.RootNavigator


@Composable
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class
)
fun AllCreditApplication(
    viewModel: AllCreditApplicationViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()

    val navController = rememberNavController()

    val isFirstStart by viewModel.isFirstStart.collectAsState()

    val startDestination = when (isFirstStart) {
        true -> SelectCountryDestination
        false -> MainScreenDestination
        else -> null
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = true
        )
    }

    if (startDestination != null) {
        DestinationsNavHost(
            modifier = Modifier.fillMaxSize(),
            navGraph = NavGraphs.root,
            startRoute = startDestination,
            engine = rememberAnimatedNavHostEngine(),
            navController = navController,
            dependenciesContainerBuilder = {
                dependency(RootNavigator(destinationsNavigator))
            }
        )
    }
}


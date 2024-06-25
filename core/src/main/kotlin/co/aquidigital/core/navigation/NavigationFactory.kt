package co.aquidigital.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface NavigationFactory {
    fun create(builder: NavGraphBuilder, navController: NavController)
}

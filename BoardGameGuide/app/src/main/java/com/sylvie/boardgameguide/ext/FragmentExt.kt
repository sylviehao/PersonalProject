package com.sylvie.boardgameguide.ext

import androidx.fragment.app.Fragment
import com.sylvie.boardgameguide.GameApplication
import com.sylvie.boardgameguide.factory.ViewModelFactory

fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as GameApplication).gameRepository
    return ViewModelFactory(repository)
}

//fun Fragment.getVmFactory(user: User?): ProfileViewModelFactory {
//    val repository = (requireContext().applicationContext as GameApplication).gameRepository
//    return ProfileViewModelFactory(repository, user)
//}
//
//fun Fragment.getVmFactory(product: Product): ProductViewModelFactory {
//    val repository = (requireContext().applicationContext as GameApplication).gameRepository
//    return ProductViewModelFactory(repository, product)
//}
//
//fun Fragment.getVmFactory(catalogType: CatalogTypeFilter): CatalogItemViewModelFactory {
//    val repository = (requireContext().applicationContext as GameApplication).gameRepository
//    return CatalogItemViewModelFactory(repository, catalogType)
//}
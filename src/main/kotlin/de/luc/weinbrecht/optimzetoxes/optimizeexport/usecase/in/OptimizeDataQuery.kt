package de.luc.weinbrecht.optimzetoxes.optimizeexport.usecase.`in`

import de.luc.weinbrecht.optimzetoxes.optimizeexport.domain.model.OptimizeData

fun interface OptimizeDataQuery {
    fun fetchData(): OptimizeData
}
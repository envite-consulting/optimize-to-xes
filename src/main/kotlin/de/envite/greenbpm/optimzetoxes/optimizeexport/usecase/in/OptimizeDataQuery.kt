package de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.`in`

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData

fun interface OptimizeDataQuery {
    fun fetchData(): OptimizeData
}
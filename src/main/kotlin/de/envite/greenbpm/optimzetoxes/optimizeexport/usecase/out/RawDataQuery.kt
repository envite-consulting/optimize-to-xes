package de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.out

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData

fun interface RawDataQuery {
    fun queryData(): OptimizeData
}
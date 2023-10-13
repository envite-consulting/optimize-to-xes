package de.luc.weinbrecht.optimzetoxes.optimizeexport.usecase.out

import de.luc.weinbrecht.optimzetoxes.optimizeexport.domain.model.OptimizeData

fun interface RawDataQuery {
    fun queryData(): OptimizeData
}
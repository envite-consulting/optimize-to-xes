package de.luc.weinbrecht.optimzetoxes.optimizeexport.domain

import de.luc.weinbrecht.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.luc.weinbrecht.optimzetoxes.optimizeexport.usecase.`in`.OptimizeDataQuery
import de.luc.weinbrecht.optimzetoxes.optimizeexport.usecase.out.RawDataQuery

class DownloadService(
    private val rawDataQuery: RawDataQuery,
): OptimizeDataQuery {
    override fun fetchData(): OptimizeData = rawDataQuery.queryData()
}
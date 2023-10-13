package de.envite.greenbpm.optimzetoxes.optimizeexport.domain

import de.envite.greenbpm.optimzetoxes.optimizeexport.domain.model.OptimizeData
import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.`in`.OptimizeDataQuery
import de.envite.greenbpm.optimzetoxes.optimizeexport.usecase.out.RawDataQuery

class DownloadService(
    private val rawDataQuery: RawDataQuery,
): OptimizeDataQuery {
    override fun fetchData(): OptimizeData = rawDataQuery.queryData()
}
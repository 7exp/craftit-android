package com.sevenexp.craftit.domain.usecase

import com.sevenexp.craftit.data.repository.HandicraftRepository
import com.sevenexp.craftit.data.response.SearchResponse
import com.sevenexp.craftit.data.response.items.HandicraftItems
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class SearchUseCase(private val handicraftRepository: HandicraftRepository) {
    private lateinit var searchFunction: Flow<SearchResponse>
    operator fun invoke(
        query: String? = null, image: File? = null
    ): Flow<ResultState<List<HandicraftItems>>> = flow<ResultState<List<HandicraftItems>>> {
        if (query != null) {
            searchFunction = handicraftRepository.searchWithQuery(query)
        } else if (image != null) {
            searchFunction = handicraftRepository.searchWithImage(image)
        } else {
            emit(ResultState.Error("Search for nothing"))
        }

        searchFunction.catch {
            emit(ResultState.Error((it.localizedMessage ?: it.message).toString()))
        }.collect { res -> emit(ResultState.Success(res.data)) }
    }.flowOn(Dispatchers.IO)
}
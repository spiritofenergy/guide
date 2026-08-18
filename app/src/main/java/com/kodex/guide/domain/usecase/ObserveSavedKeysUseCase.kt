package com.kodex.guide.domain.usecase

import com.kodex.guide.domain.repository.SavedPostsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSavedKeysUseCase @Inject constructor(
        private val repository: SavedPostsRepo
    ) {

        operator fun invoke(): Flow<Set<String>> {
            return repository.observeSavedKeys()
        }
    }

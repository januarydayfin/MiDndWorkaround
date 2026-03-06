package com.krayapp.dndworkaround.data

import android.content.Context
import com.krayapp.dndworkaround.mvi.BackgroundWorkType
import com.krayapp.dndworkaround.mvi.DndMode
import kotlinx.coroutines.flow.first

interface DndRepository {
    suspend fun saveDndMode(mode: DndMode)
    suspend fun saveBackgroundMode(type: BackgroundWorkType)
    suspend fun getDndMode(): DndMode
    suspend fun getBackgroundMode(): BackgroundWorkType
}

class DndRepositoryImpl(private val context: Context) : DndRepository {
    override suspend fun saveDndMode(mode: DndMode) {
        context.setDndMode(mode.name)
    }

    override suspend fun saveBackgroundMode(type: BackgroundWorkType) {
        context.setBackgroundWorkType(type.toString())
    }

    override suspend fun getDndMode(): DndMode {
        return DndMode.valueOf(context.dndMode().first())
    }

    override suspend fun getBackgroundMode(): BackgroundWorkType {
        return BackgroundWorkType.valueOf(context.backgroundWorkType().first())
    }
}

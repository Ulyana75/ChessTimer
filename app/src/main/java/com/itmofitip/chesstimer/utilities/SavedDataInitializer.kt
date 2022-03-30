package com.itmofitip.chesstimer.utilities

import com.itmofitip.chesstimer.repository.WorkerWithSavedData

class SavedDataInitializer(
    private val repositories: List<WorkerWithSavedData>
) {

    fun initSavedData() {
        repositories.forEach {
            it.initData()
        }
    }

    fun saveData() {
        repositories.forEach {
            it.saveData()
        }
    }
}
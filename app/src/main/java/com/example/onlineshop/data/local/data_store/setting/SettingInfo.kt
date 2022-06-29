package com.example.onlineshop.data.local.data_store.setting

data class SettingInfo(
    val workerDurationIndex: Int,
) {
    fun workerHasBeenSetup() = workerDurationIndex != -1
}
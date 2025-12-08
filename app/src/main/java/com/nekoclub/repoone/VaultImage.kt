package com.nekoclub.repoone

import java.io.File

data class VaultImage(
    val id: String,
    val file: File,
    val timestamp: Long = System.currentTimeMillis()
)

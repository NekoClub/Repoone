package com.nekoclub.repoone

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

class VaultManager(private val context: Context) {
    
    private val vaultDir: File = File(context.getExternalFilesDir(null), "vault")
    
    init {
        if (!vaultDir.exists()) {
            vaultDir.mkdirs()
        }
    }
    
    fun importImage(inputStream: InputStream): VaultImage {
        val imageId = UUID.randomUUID().toString()
        val imageFile = File(vaultDir, "$imageId.jpg")
        
        inputStream.use { input ->
            FileOutputStream(imageFile).use { output ->
                input.copyTo(output)
            }
        }
        
        return VaultImage(imageId, imageFile)
    }
    
    fun getAllImages(): List<VaultImage> {
        return vaultDir.listFiles()?.filter { it.isFile && it.extension == "jpg" }
            ?.map { file ->
                VaultImage(
                    id = file.nameWithoutExtension,
                    file = file,
                    timestamp = file.lastModified()
                )
            }?.sortedByDescending { it.timestamp } ?: emptyList()
    }
    
    fun deleteImage(image: VaultImage): Boolean {
        return image.file.delete()
    }
    
    fun getImageFile(imageId: String): File {
        return File(vaultDir, "$imageId.jpg")
    }
}

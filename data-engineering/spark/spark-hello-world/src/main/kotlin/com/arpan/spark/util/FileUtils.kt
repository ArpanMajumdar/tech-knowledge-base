package com.arpan.spark.util

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


object FileUtils {
    fun recursivelyDeleteDirectory(dirPath: String) {
        val outputDir = Paths.get(dirPath)
        if (Files.exists(outputDir)) {
            Files.walk(outputDir).map(Path::toFile).forEach { file: File -> file.delete() }
            Files.deleteIfExists(outputDir)
        }
    }
}


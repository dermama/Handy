package com.termux.shared

/**
 * Termux shared service - placeholder for termux-shared module.
 * In production, this would be populated from the Termux source:
 * https://github.com/termux/termux-app/tree/master/termux-shared
 *
 * This module handles:
 * - Termux environment setup and bootstrap
 * - Package management utilities
 * - Shared constants and preferences
 * - Notification channel management
 * - File system operations
 */
object TermuxConstants {
    const val PREFIX_DIR = "/data/data/com.edge.integrated/files/termux/usr"
    const val HOME_DIR = "/data/data/com.edge.integrated/files/termux/home"
    const val PACKAGE_NAME = "com.edge.integrated"
    const val FILES_DIR = "/data/data/com.edge.integrated/files"
}

class TermuxEnvironment {
    val environment: Map<String, String> = mapOf(
        "TERMUX_PREFIX" to TermuxConstants.PREFIX_DIR,
        "TERMUX_HOME" to TermuxConstants.HOME_DIR,
        "PREFIX" to TermuxConstants.PREFIX_DIR,
        "HOME" to TermuxConstants.HOME_DIR,
        "TERM" to "xterm-256color",
        "SHELL" to "${TermuxConstants.PREFIX_DIR}/bin/bash",
        "PATH" to "${TermuxConstants.PREFIX_DIR}/bin:/system/bin:/system/xbin",
        "LD_LIBRARY_PATH" to TermuxConstants.PREFIX_DIR,
        "LANG" to "en_US.UTF-8"
    )

    fun isInstalled(): Boolean {
        val prefixDir = java.io.File(TermuxConstants.PREFIX_DIR)
        return prefixDir.exists() && prefixDir.listFiles()?.isNotEmpty() == true
    }
}

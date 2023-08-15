/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
#include "JWindowsFileDialog.h"

void _throwException(JNIEnv* env, const char* msg, const char* file, const int line) {
    std::string error(msg);
    error = error.append(" - ").append(file).append(" at line ").append(std::to_string(line));
    env->ThrowNew(env->FindClass("com/github/jacksonbrienen/jwfd/JNIException"), error.c_str());
}
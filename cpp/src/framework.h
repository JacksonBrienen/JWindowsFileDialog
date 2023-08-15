/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */

/*!
 * @file framework.h
 * @brief The needed imports for all files used.
 */
#pragma once

#define WIN32_LEAN_AND_MEAN

#include <windows.h>
#include <shobjidl.h>

// Java AWT and JNI Header files
#include <jni.h>
#include <jawt_md.h>

// Other required Header files
#include <string>
#include <vector>
#include "JException.h"
#include "JHWND.h"
#include "WindowsFileDialog.h"
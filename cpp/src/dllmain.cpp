/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */

/*!
 * @mainpage JWindowsFileDialog
 * The JNI code used to run within the JWindowsFileDialog.jar allowing for native file dialogs. <br>
 * ## Files:
 * @ref framework.h <br>
 * @ref WindowsFileDialog.h <br>
 * @ref JException.h <br>
 * @ref JHWND.h <br>
 * @ref JWindowsFileDialog.h <br>
 */


#include "JWindowsFileDialog.h"

BOOL APIENTRY DllMain(HMODULE hModule, DWORD  ul_reason_for_call, LPVOID lpReserved) {
    return TRUE;
} 
/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
#include "JWindowsFileDialog.h"

byte getHWND(HWND& hwnd, JNIEnv* env, jobject frame) {
    if (frame == nullptr) {
        hwnd = NULL;
        return 0;
    }

    JAWT_DrawingSurface* ds = awt.GetDrawingSurface(env, frame);
    if (ds == nullptr) {
        throwException(env, "Failed to get awt drawing surface");
        return HWND_DRAWING_SURFACE_FAIL;
    }

    jint lock = ds->Lock(ds);
    if ((lock & JAWT_LOCK_ERROR) != 0) {
        throwException(env, "Failed to lock awt drawing surface");
        awt.FreeDrawingSurface(ds);
        return HWND_DRAWING_SURFACE_LOCK_FAIL;
    }

    JAWT_DrawingSurfaceInfo* dsi = ds->GetDrawingSurfaceInfo(ds);
    if (dsi == nullptr) {
        throwException(env, "Failed to get awt drawing surface info");
        ds->Unlock(ds);
        awt.FreeDrawingSurface(ds);
        return HWND_DRAWING_SURFACE_INFO_FAILED;
    }

    JAWT_Win32DrawingSurfaceInfo* win32Dsi = (JAWT_Win32DrawingSurfaceInfo*)dsi->platformInfo;
    if (win32Dsi == nullptr) {
        throwException(env, "Failed to get awt win32 drawing surface info");
        ds->FreeDrawingSurfaceInfo(dsi);
        ds->Unlock(ds);
        awt.FreeDrawingSurface(ds);
        return HWND_DRAWING_SURFACE_INFO_FAILED;
    }
    hwnd = win32Dsi->hwnd;
    ds->FreeDrawingSurfaceInfo(dsi);
    ds->Unlock(ds);
    awt.FreeDrawingSurface(ds);
    return 0;
}
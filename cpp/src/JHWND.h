/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */

/*!
  * @file JHWND.h
  * @brief Provides error codes and ability to get an HWND from a Frame or JFrame
  * @author Jackson Brienen
  * @version 0.8
  */
#pragma once
#include "framework.h"

/// The HWND was retrieved and returned successfully
#define HWND_SUCCESS 0x0
/// JAWT failed to get the drawing surface of the passed frame
#define HWND_DRAWING_SURFACE_FAIL 0x1
/// JAWT failed to lock the drawing surface
#define HWND_DRAWING_SURFACE_LOCK_FAIL 0x2
/// JAWT failed to recieve the drawing surface info
#define HWND_DRAWING_SURFACE_INFO_FAILED 0x4
/// JAWT failed to get win32 drawing surface info
#define HWND_DRAWING_SURFACE_WIN32_INFO_FAILED 0x8

/*! \fn getHWND
 * With a given JFrame or Frame returns the HWND associated with it.
 * @param[out] hwnd Location for the retrieved hwnd to be stored.
 * @param[in] env The Java environment this function is being called from.
 * @param[in] frame A Java Frame or JFrame object
 * @returns HWND_SUCCESS or an error code if the HWND could not be retrieved
 */
byte getHWND(HWND& hwnd, JNIEnv* env, jobject frame);
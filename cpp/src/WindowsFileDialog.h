/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */

/*!
 * @file WindowsFileDialog.h
 * @brief Provides functions to open a native file dialog.
 * @author Jackson Brienen
 * @version 0.8
 */
#pragma once
#include "framework.h"

/// The dialog was successfully opened and the response was successfully stored
#define DIALOG_SUCCESS 0x0
/// The CoInitialize() function failed
#define COINIT_FAIL 0x1
/// The CoCreateInstance() function failed
#define COCREATE_FAIL 0x2
/// The FileDialog::GetOptions() function failed
#define GET_OPTIONS_FAIL 0x4
/// The FileDialog::SetOptions() function failed
#define SET_OPTIONS_FAIL 0x8
/// The FileDialog::SetOptions() function failed
#define SET_TITLE_FAIL 0x10
/// A ShellItem failed to parse a string path
#define PARSE_FILE_FAIL 0x20
/// The FileDialog::SetFolder() function failed
#define SET_DIR_FAIL 0x40
/// The FileDialog::SetFileTypes() function failed
#define SET_FILTER_FAIL 0x80
/// The FileDialog::SetDefaultExtension() function failed
#define SET_DEF_EXT_FAIL 0x100
/// The FileOpenDialog::GetResults() function failed
#define GET_RESULTS_FAIL 0x200
/// The IShellItemArray::GetCount() function failed
#define GET_RESULTS_SIZE_FAIL 0x400
/// The IShellItemArray::GetItemAt() function failed
#define GET_ARRAY_ITEM_FAIL 0x800
/// The FileDialog::GetResult() function failed
#define GET_RESULT_FAIL 0x1000

/*!
 * Opens a Windows Open File Dialog
 * @param[in] env The Java enviornment which called this function.
 * @param[out] path The location where the derived path should be placed.
 * @param[in] hwnd The handler for the frame this dialog should use as its parent for modality.
 * @param[in] title The title to be displayed on the dialog.
 * @param[in] startingDir The location where the dialog should start in.
 * @param[in] filters An array of filters which are used to filter the results of the dialog. This may be null only if filterSize is set to 0.
 * @param[in] filterSize The size of the filters array.
 * @returns DIALOG_SUCCESS if successfully dialog creation or an error code if not
 */
unsigned short openFileDialog(JNIEnv* env, jstring& path, HWND hwnd, const wchar_t* title, const wchar_t* startingDir, COMDLG_FILTERSPEC* filters, long filterSize);

/*!
 * Opens a Windows Save File Dialog
 * @param[in] env The Java enviornment which called this function.
 * @param[out] path The location where the derived path should be placed.
 * @param[in] hwnd The handler for the frame this dialog should use as its parent for modality.
 * @param[in] title The title to be displayed on the dialog.
 * @param[in] startingDir The location where the dialog should start in.
 * @param[in] filters An array of filters which are used to filter the results of the dialog. This may be null only if filterSize is set to 0.
 * @param[in] filterSize The size of the filters array.
 * @returns DIALOG_SUCCESS if successfully dialog creation or an error code if not
 */
unsigned short openSaveDialog(JNIEnv* env, jstring& path, HWND hwnd, const wchar_t* title, const wchar_t* startingDir, COMDLG_FILTERSPEC* filters, long filterSize);

/*!
 * Opens a Windows Open Multiple Files Dialog
 * @param[in] env The Java enviornment which called this function.
 * @param[out] paths The location where the resulting array should be placed
 * @param[in] hwnd The handler for the frame this dialog should use as its parent for modality.
 * @param[in] title The title to be displayed on the dialog.
 * @param[in] startingDir The location where the dialog should start in.
 * @param[in] filters An array of filters which are used to filter the results of the dialog. This may be null only if filterSize is set to 0.
 * @param[in] filterSize The size of the filters array.
 * @returns DIALOG_SUCCESS if successfully dialog creation or an error code if not
 */
unsigned short openMultiDialog(JNIEnv* env, jobjectArray& paths, HWND hwnd, const wchar_t* title, const wchar_t* startingDir, COMDLG_FILTERSPEC* filters, long filterSize);

/*!
 * Opens a Windows Open Folder Dialog
 * @param[in] env The Java enviornment which called this function.
 * @param[out] path The location where the derived path should be placed.
 * @param[in] hwnd The handler for the frame this dialog should use as its parent for modality.
 * @param[in] title The title to be displayed on the dialog.
 * @param[in] startingDir The location where the dialog should start in.
 * @returns DIALOG_SUCCESS if successfully dialog creation or an error code if not
 */
unsigned short openDirDialog(JNIEnv* env, jstring& path, HWND hwnd, const wchar_t* title, const wchar_t* startingDir);
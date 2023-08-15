/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */

/*!
 * @file JWindowsFileDialog.h
 * @brief Provides the JNI access functions to interface with Java
 * @author Jackson Brienen
 * @version 0.8
 */

#pragma once
#include "framework.h"

/// global awt instance
extern JAWT awt;

// C linker function names needed for Java to interface
#ifdef __cplusplus
extern "C" {
#endif

	/*!
	 * Load JVM awt data into the global awt instance.
	 * @param[in] env The Java enviornment which called this function.
	 * @param[in] obj The Java object or class which called this function.
	 * @return true if this is successful.
	 * @warning This function should not be called from this code, only to be called through java JNI access.
	 */
	JNIEXPORT jboolean JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_initAWT(JNIEnv *env, jclass obj);

	/*!
	 * Opens a native Windows file dialog to open a single file.
	 * @param[in] env The Java enviornment which called this function.
	 * @param[in] obj The Java object or class which called this function.
	 * @param[in] frame A Frame or JFrame object that will be used for the dialogs modality. May be null.
	 * @param[in] title The title of the displayed dialog.
	 * @param[in] startingDir The starting directory for the displayed dialog.
	 * @param[in] filters Passed as a cstring style array in form description, extensions.
	 * @return the path of the selected file, or null if no path is selected.
	 * @warning This function should not be called from this code, only to be called through java JNI access.
	 */
	JNIEXPORT jstring JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_openFileDialog0(JNIEnv *env, jclass obj, jobject frame, jstring title, jstring startingDir, jstring filters);

	/*!
	 * Opens a native Windows file dialog to save a single file.
	 * @param[in] env The Java enviornment which called this function.
	 * @param[in] obj The Java object or class which called this function.
	 * @param[in] frame A Frame or JFrame object that will be used for the dialogs modality. May be null.
	 * @param[in] title The title of the displayed dialog.
	 * @param[in] startingDir The starting directory for the displayed dialog.
	 * @param[in] filters Passed as a cstring style array in form description, extensions.
	 * @return the path of the selected file, or null if no path is selected.
	 * @warning This function should not be called from this code, only to be called through java JNI access.
	 */
	JNIEXPORT jstring JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_openSaveDialog0(JNIEnv *env, jclass, jobject frame, jstring title, jstring startingDir, jstring filters);

	/*!
	 * Opens a native Windows file dialog to open multiple files.
	 * @param[in] env The Java enviornment which called this function.
	 * @param[in] obj The Java object or class which called this function.
	 * @param[in] frame A Frame or JFrame object that will be used for the dialogs modality. May be null.
	 * @param[in] title The title of the displayed dialog.
	 * @param[in] startingDir The starting directory for the displayed dialog.
	 * @param[in] filters Passed as a cstring style array in form description, extensions.
	 * @return an array of selected paths, or null if no paths are selected.
	 * @warning This function should not be called from this code, only to becalled through java JNI access.
	 */
	JNIEXPORT jobjectArray JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_openMultipleDialog0(JNIEnv *env, jclass obj, jobject frame, jstring title, jstring startingDir, jstring filters);

	/*!
	 * Opens a native Windows file dialog to open a folder.
	 * @param[in] env The Java enviornment which called this function.
	 * @param[in] obj The Java object or class which called this function.
	 * @param[in] frame A Frame or JFrame object that will be used for the dialogs modality. May be null.
	 * @param[in] title The title of the displayed dialog.
	 * @param[in] startingDir The starting directory for the displayed dialog.
	 * @return the path of the selected file, or null if no path is selected.
	 * @warning This function should not be called from this code, only to be called through java JNI access.
	 */
	JNIEXPORT jstring JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_openDirDialog0(JNIEnv *env, jclass obj, jobject frame, jstring title, jstring startingDir);

#ifdef __cplusplus
}
#endif
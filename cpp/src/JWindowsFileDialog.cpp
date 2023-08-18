/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
#include "JWindowsFileDialog.h"

JAWT awt;
JNIEXPORT jboolean JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_initAWT(JNIEnv *env, jclass obj) {
    awt.version = JAWT_VERSION_1_3;
    return JAWT_GetAWT(env, &awt);
}

void createJavaFilter(JNIEnv* env, jstring jfilter, const wchar_t*& wfilter, COMDLG_FILTERSPEC*& comFilters, long &filterSize) {
    // This way is a bit "cute" and might get removed in the future in favor of 2d jarray traversal
    wfilter = (const wchar_t*)env->GetStringChars(jfilter, JNI_FALSE);
    filterSize = 1;

    jsize len = env->GetStringLength(jfilter);
    for (int i = 0; i < len; i++)
        if (*(wfilter + i) == 0)
            filterSize++;

    comFilters = new COMDLG_FILTERSPEC[filterSize / 2];

    int index = 0;
    for (int i = 0; i < filterSize; i++) {
        if (i % 2 == 0)
            comFilters[i / 2].pszName = wfilter + index;
        else
            comFilters[i / 2].pszSpec = wfilter + index;

        if (i + 1 < filterSize)
            while (*(wfilter + index++) != 0);
    }

    filterSize /= 2;
}

void freeJavaFilter(JNIEnv* env, jstring jfilter, const wchar_t* wfilter, COMDLG_FILTERSPEC* comFilters) {
    env->ReleaseStringChars(jfilter, (const jchar *)wfilter);
    delete[] comFilters;
}

JNIEXPORT jstring JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_openFileDialog0(JNIEnv* env, jclass obj, jobject frame, jstring title, jstring startingDir, jstring filters) {
    HWND hwnd;
    if (getHWND(hwnd, env, frame) != HWND_SUCCESS)
        return nullptr;
   
    const wchar_t* wtitle = (const wchar_t*)env->GetStringChars(title, JNI_FALSE);
    const wchar_t* wstartingDir = (const wchar_t*)env->GetStringChars(startingDir, JNI_FALSE);

   
    long filterSize = 0;
    const wchar_t* wfilter = nullptr;
    COMDLG_FILTERSPEC* comFilters = nullptr;

    if (filters != nullptr) {
        createJavaFilter(env, filters, wfilter, comFilters, filterSize);
    }

    jstring value;
    unsigned short code = openFileDialog(env, value, hwnd, wtitle, wstartingDir, comFilters, filterSize);
    freeJavaFilter(env, filters, wfilter, comFilters);
    env->ReleaseStringChars(title, (const jchar *)wtitle);
    env->ReleaseStringChars(startingDir, (const jchar*)wstartingDir);
    if (code != DIALOG_SUCCESS)
        return nullptr;

    return value;
}

JNIEXPORT jstring JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_openSaveDialog0(JNIEnv* env, jclass, jobject frame, jstring title, jstring startingDir, jstring filters) {
    HWND hwnd;
    if (getHWND(hwnd, env, frame) != HWND_SUCCESS)
        return nullptr;

    const wchar_t* wtitle = (const wchar_t*)env->GetStringChars(title, JNI_FALSE);
    const wchar_t* wstartingDir = (const wchar_t*)env->GetStringChars(startingDir, JNI_FALSE);


    long filterSize = 0;
    const wchar_t* wfilter = nullptr;
    COMDLG_FILTERSPEC* comFilters = nullptr;

    if (filters != nullptr) {
        createJavaFilter(env, filters, wfilter, comFilters, filterSize);
    }

    jstring value;
    unsigned short code = openSaveDialog(env, value, hwnd, wtitle, wstartingDir, comFilters, filterSize);
    freeJavaFilter(env, filters, wfilter, comFilters);
    env->ReleaseStringChars(title, (const jchar*)wtitle);
    env->ReleaseStringChars(startingDir, (const jchar*)wstartingDir);
    if (code != DIALOG_SUCCESS)
        return nullptr;

    return value;
}

JNIEXPORT jobjectArray JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_openMultipleDialog0(JNIEnv* env, jclass obj, jobject frame, jstring title, jstring startingDir, jstring filters) {
    HWND hwnd;
    if (getHWND(hwnd, env, frame) != HWND_SUCCESS)
        return nullptr;

    const wchar_t* wtitle = (const wchar_t*)env->GetStringChars(title, JNI_FALSE);
    const wchar_t* wstartingDir = (const wchar_t*)env->GetStringChars(startingDir, JNI_FALSE);


    long filterSize = 0;
    const wchar_t* wfilter = nullptr;
    COMDLG_FILTERSPEC* comFilters = nullptr;

    if (filters != nullptr) {
        createJavaFilter(env, filters, wfilter, comFilters, filterSize);
    }

    jobjectArray value;
    unsigned short code = openMultiDialog(env, value, hwnd, wtitle, wstartingDir, comFilters, filterSize);
    freeJavaFilter(env, filters, wfilter, comFilters);
    env->ReleaseStringChars(title, (const jchar*)wtitle);
    env->ReleaseStringChars(startingDir, (const jchar*)wstartingDir);
    if (code != DIALOG_SUCCESS) 
        return nullptr;

    return value;
}

JNIEXPORT jstring JNICALL Java_com_github_jacksonbrienen_jwfd_WindowsFileDialog_openDirDialog0(JNIEnv* env, jclass obj, jobject frame, jstring title, jstring startingDir) {
    HWND hwnd;
    if (getHWND(hwnd, env, frame) != HWND_SUCCESS)
        return nullptr;

    const wchar_t* wtitle = (const wchar_t*)env->GetStringChars(title, JNI_FALSE);
    const wchar_t* wstartingDir = (const wchar_t*)env->GetStringChars(startingDir, JNI_FALSE);


    long filterSize = 0;

    jstring value;
    if (openDirDialog(env, value, hwnd, wtitle, wstartingDir) != DIALOG_SUCCESS)
        value = nullptr;

    env->ReleaseStringChars(title, (const jchar*)wtitle);
    env->ReleaseStringChars(startingDir, (const jchar*)wstartingDir);

    return value;
}
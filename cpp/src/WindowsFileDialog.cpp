/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
#include "JWindowsFileDialog.h"

enum DIALOG_TYPE {
	SAVE, OPEN, OPEN_MULTIPLE, OPEN_DIR
};

unsigned short showDialog(JNIEnv* env, void *&path, HWND hwnd, const wchar_t* title, const wchar_t* startingDir, COMDLG_FILTERSPEC* filters, long filterSize, DIALOG_TYPE type) {
	path = nullptr;

	IFileDialog* dialog;
	if (FAILED(CoInitializeEx(nullptr, COINIT_APARTMENTTHREADED | COINIT_DISABLE_OLE1DDE))) {
		throwException(env, "Could not CoInitialize");
		return COINIT_FAIL;
	}

	_GUID clsid = (type == SAVE) ? CLSID_FileSaveDialog : CLSID_FileOpenDialog;
	_GUID iid = (type == SAVE) ? IID_IFileSaveDialog : IID_IFileOpenDialog;
	if (FAILED(CoCreateInstance(clsid, nullptr, CLSCTX_ALL, iid, (void**)&dialog))) {
		throwException(env, "Could not CoCreateInstance");
		CoUninitialize();
		return COCREATE_FAIL;
	}

	unsigned long options;
	if (FAILED(dialog->GetOptions(&options))) {
		throwException(env, "Could not Get Dialog Options");
		dialog->Release();
		CoUninitialize();
		return GET_OPTIONS_FAIL;
	}

	switch (type) {
	case SAVE:
	case OPEN:
		break;
	case OPEN_MULTIPLE:
		options |= FOS_ALLOWMULTISELECT;
		break;
	case OPEN_DIR:
		options |= FOS_PICKFOLDERS;
		break;
	}
	
	if (FAILED(dialog->SetOptions(options))) {
		throwException(env, "Could not Set Dialog Options");
		dialog->Release();
		CoUninitialize();
		return SET_OPTIONS_FAIL;
	}

	if (FAILED(dialog->SetTitle(title))) {
		throwException(env, "Could not Set Dialog Title");
		dialog->Release();
		CoUninitialize();
		return SET_TITLE_FAIL;
	}
	
	IShellItem *shellStartingDir;
	if (FAILED(SHCreateItemFromParsingName(startingDir, nullptr, IID_IShellItem, (void**)&shellStartingDir))) {
		throwException(env, "Could not parse starting dir into shell item");
		dialog->Release();
		CoUninitialize();
		return PARSE_FILE_FAIL;
	}

	if (FAILED(dialog->SetFolder(shellStartingDir))) {
		throwException(env, "Could not set starting dir");
		shellStartingDir->Release();
		dialog->Release();
		CoUninitialize();
		return SET_DIR_FAIL;
	}

	if (filterSize > 0) {
		if (FAILED(dialog->SetFileTypes(filterSize, filters))) {
			throwException(env, "Could not set filters");
			shellStartingDir->Release();
			dialog->Release();
			CoUninitialize();
			return SET_FILTER_FAIL;
		}
	}

	if (type == SAVE) {
		if (FAILED(dialog->SetDefaultExtension(L""))) {
			throwException(env, "Could not set default extension");
			shellStartingDir->Release();
			dialog->Release();
			CoUninitialize();
			return SET_DEF_EXT_FAIL;
		}
	}

	EnableWindow(hwnd, false);

	if (SUCCEEDED(dialog->Show(hwnd))) {
		if (type == OPEN_MULTIPLE) {

			IShellItemArray *itemArr;
			if (FAILED(((IFileOpenDialog*)dialog)->GetResults(&itemArr))) {
				throwException(env, "Could not get result from dialog");
				shellStartingDir->Release();
				dialog->Release();
				CoUninitialize();
				return GET_RESULTS_FAIL;
			}

			unsigned long size;
			if (FAILED(itemArr->GetCount(&size))) {
				throwException(env, "Could not get result size");
				itemArr->Release();
				shellStartingDir->Release();
				dialog->Release();
				CoUninitialize();
				return GET_RESULTS_SIZE_FAIL;
			}
			
			path = env->NewObjectArray((long)size, env->FindClass("java/lang/String"), nullptr);

			for (unsigned long i = 0; i < size; i++) {
				IShellItem *item;
				if (FAILED(itemArr->GetItemAt(i, &item))) {
					throwException(env, "Could not get shell item from array");
					itemArr->Release();
					shellStartingDir->Release();
					dialog->Release();
					CoUninitialize();
					return GET_ARRAY_ITEM_FAIL;
				}
				wchar_t* filePath;
				if (FAILED(item->GetDisplayName(SIGDN_FILESYSPATH, &filePath))) {
					throwException(env, "Could not parse file path from shell item");
					item->Release();
					itemArr->Release();
					shellStartingDir->Release();
					dialog->Release();
					CoUninitialize();
					return PARSE_FILE_FAIL;
				}
				env->SetObjectArrayElement((jobjectArray)path, i, env->NewString((const jchar*) filePath, lstrlenW(filePath)));
				CoTaskMemFree(filePath);
				item->Release();
			}
			itemArr->Release();
		}
		else {
			IShellItem* item;
			if (FAILED(dialog->GetResult(&item))) {
				shellStartingDir->Release();
				dialog->Release();
				CoUninitialize();
				return GET_RESULT_FAIL;
			}
			wchar_t* filePath;
			if (FAILED(item->GetDisplayName(SIGDN_FILESYSPATH, &filePath))) {
				item->Release();
				shellStartingDir->Release();
				dialog->Release();
				CoUninitialize();
				return PARSE_FILE_FAIL;
			}
			
			path = env->NewString((const jchar *)filePath, lstrlenW(filePath));
			CoTaskMemFree(filePath);
			item->Release();
		}
	}

	EnableWindow(hwnd, true);

	shellStartingDir->Release();
	dialog->Release();
	CoUninitialize();
	return DIALOG_SUCCESS;
}

unsigned short openFileDialog(JNIEnv* env, jstring& path, HWND hwnd, const wchar_t* title, const wchar_t* startingDir, COMDLG_FILTERSPEC* filters, long filterSize) {
	return showDialog(env, (void*&)path, hwnd, title, startingDir, filters, filterSize, OPEN);
}

unsigned short openSaveDialog(JNIEnv* env, jstring& path, HWND hwnd, const wchar_t* title, const wchar_t* startingDir, COMDLG_FILTERSPEC* filters, long filterSize) {
	return showDialog(env, (void*&)path, hwnd, title, startingDir, filters, filterSize, SAVE);
}

unsigned short openMultiDialog(JNIEnv* env, jobjectArray& paths, HWND hwnd, const wchar_t* title, const wchar_t* startingDir, COMDLG_FILTERSPEC* filters, long filterSize) {
	return showDialog(env, (void*&)paths, hwnd, title, startingDir, filters, filterSize, OPEN_MULTIPLE);
}

unsigned short openDirDialog(JNIEnv* env, jstring& path, HWND hwnd, const wchar_t* title, const wchar_t* startingDir) {
	return showDialog(env, (void*&)path, hwnd, title, startingDir, nullptr, 0, OPEN_DIR);
}
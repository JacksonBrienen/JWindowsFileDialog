/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
#pragma once
#include "framework.h"

/*!
  * @file JException.h
  * @brief Provides a way to throw a java exception via the JNIException class.
  * @author Jackson Brienen
  * @version 0.8
  */

/*! 
 * \def throwException
 * Throws a JNIException in java with the given message.
 * @param[in] env The java enviornment this function was called from. Should be a JNIEnv*.
 * @param[in] msg A message to be given with this exception. Should be a const char*.
 */
#define throwException(env,msg) _throwException(env, msg, __FILE__, __LINE__)

void _throwException(JNIEnv* env, const char* msg, const char* file, const int line);
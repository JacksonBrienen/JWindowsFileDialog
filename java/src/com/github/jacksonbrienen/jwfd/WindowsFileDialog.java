/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
package com.github.jacksonbrienen.jwfd;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.net.URL;

/**
 * A wrapper class to load and call functions in JWindowsFileDialog.dll
 * @author Jackson Brienen
 * @version 0.8
 */
final class WindowsFileDialog {
      // disable default constructor
      private WindowsFileDialog(){}

      static final boolean IS_WINDOWS;
      static {
            String os = System.getProperty("os.name");
            // ensure the operating system
            // the windows libraries used do not support anything below vista or server 2008,
            // so we exclude the three jdk 8 supported OSs that don't meet that requirement
            if(os.contains("Windows") && !(os.contains("Windows 2000") || os.contains("Windows XP") || os.contains("Windows Server 2003"))) {
                  new Frame().dispose(); // On JDK 8_101 this loads the awt dll through the private call of Toolkit.loadLibraries();
                  // it's a sad workaround, but without being able to checked loaded libraries in later jdks it is the only option

                  // Older (1.8) JDKs don't load jawt while later JDKs do, therefore a try catch must be used in case of prior loading
                  try {
                        System.loadLibrary("jawt");
                  } catch (UnsatisfiedLinkError ignored) {}

                  // if the libraries can be loaded, and JAWT can be initialized, we can use the library
                  // to display the native file dialog
                  IS_WINDOWS = loadLibraries() && safeInitAWT();
            } else
                  IS_WINDOWS = false;
      }

      /**
       * Loads the local JWindowsFileDialog.dll to a temporary file that is subsequently deleted on program completion.
       * @return true if the DLL was successfully loaded.
       */
      private static boolean loadLibraries() {
            // the dll title to be used
            String title;

            // get correct dll from lib package based on jdk architecture
            URL dllLocal;
            switch(System.getProperty("sun.arch.data.model")) {
                  case "32":
                        dllLocal = WindowsFileDialog.class.getResource("/com/github/jacksonbrienen/jwfd/lib/JWindowsFileDialog x86.dll");
                        title = "JWindowsFileDialog x86.dll";
                        break;
                  case "64":
                        dllLocal = WindowsFileDialog.class.getResource("/com/github/jacksonbrienen/jwfd/lib/JWindowsFileDialog x64.dll");
                        title = "JWindowsFileDialog x64.dll";
                        break;
                  default:
                        // if the architecture is "unknown" return false
                        return false;
            }

            final Path dllPath = Paths.get(System.getProperty("java.io.tmpdir"),"/", title);

            if(Files.exists(dllPath)) {
                  try {
                        System.load(dllPath.toString());
                        return true;
                  } catch (Exception e) {
                        if(!dllPath.toFile().delete())
                              return false;
                  }
            }

            try {
                  Files.createFile(dllPath);
            } catch (IOException n) {
                  return false;
            }
            final File dllFile = dllPath.toFile();

            try(InputStream dllIn = dllLocal.openStream(); OutputStream dllOut = Files.newOutputStream(dllPath)) {
                  byte[] buffer = new byte[4096];
                  int bytesRead;
                  while((bytesRead = dllIn.read(buffer)) != -1)
                        dllOut.write(buffer, 0, bytesRead);
            } catch(IOException e){
                  // if copying the dll fails, mark the temp file for deletion
                  if(!dllFile.delete())
                        dllFile.deleteOnExit();
                  return false;
            }

            System.load(dllFile.getAbsolutePath());
            return true;
      }

      /**
       * This function must be called before any other native functions here.
       * @return true if JNI awt could be initialized.
       */
      private static native boolean initAWT();

      /**
       * This is a wrapper for the native initAWT function,
       * Since the library has yet to be confirmed working we ensure it with a try catch.
       * @return true if JNI awt could be initialized.
       */
      private static boolean safeInitAWT() {
            boolean b = false;
            try {
                  b = initAWT();
            } catch (Exception e) {
                  return false;
            }
            return b;
      }

      // the native function called by the openFileDialog wrapper function
      private static native String openFileDialog0(Frame frame, String title, String path, String filters) throws JNIException, UnsatisfiedLinkError;

      /**
       * Opens a windows native open file dialog with the given parameters.
       * @param frame The parent frame used as a relative component for modality.
       * @param title The title of the shown dialog window.
       * @param path The starting directory for the dialog.
       * @param filters A list of filters to filter the files shown. These should be in the Windows filter format, and be a non-null value.
       * @return The path of a selected file, null if no file is selected.
       * @throws JNIException If an error occurs while opening the File Dialog
       */
      static String openFileDialog(Frame frame, String title, String path, String filters) throws JNIException, UnsatisfiedLinkError {
            frame = validateFrame(frame);
            title = validateTitle(title);
            path = validatePath(path);
            return openFileDialog0(frame, title, path, filters);
      }

      // the native function called by the openSaveDialog wrapper function
      private static native String openSaveDialog0(Frame frame, String title, String path, String filters) throws JNIException, UnsatisfiedLinkError;

      /**
       * Opens a windows native save file dialog with the given parameters.
       * @param frame The parent frame used as a relative component for modality.
       * @param title The title of the shown dialog window.
       * @param path The starting directory for the dialog.
       * @param filters A list of filters to filter the files shown. These should be in the Windows filter format, and be a non-null value.
       * @return The path of a selected file, null if no file is selected.
       * @throws JNIException If an error occurs while opening the File Dialog
       */
      static String openSaveDialog(Frame frame, String title, String path, String filters) throws JNIException, UnsatisfiedLinkError {
            frame = validateFrame(frame);
            title = validateTitle(title);
            path = validatePath(path);
            return openSaveDialog0(frame, title, path, filters);
      }

      // the native function called by the openSaveDialog wrapper function
      private static native String[] openMultipleDialog0(Frame frame, String title, String path, String filters) throws JNIException, UnsatisfiedLinkError;

      /**
       * Opens a windows native open multiple file dialog with the given parameters.
       * @param frame The parent frame used as a relative component for modality.
       * @param title The title of the shown dialog window.
       * @param path The starting directory for the dialog.
       * @param filters A list of filters to filter the files shown. These should be in the Windows filter format, and be a non-null value.
       * @return An array of the selected paths, null if no paths are selected.
       * @throws JNIException If an error occurs while opening the File Dialog
       */
      static String[] openMultipleDialog(Frame frame, String title, String path, String filters) throws JNIException, UnsatisfiedLinkError {
            frame = validateFrame(frame);
            title = validateTitle(title);
            path = validatePath(path);
            return openMultipleDialog0(frame, title, path, filters);
      }

      private static native String openDirDialog0(Frame frame, String title, String path) throws JNIException, UnsatisfiedLinkError;

      /**
       * Opens a windows native open directory dialog with the given parameters.
       * @param frame The parent frame used as a relative component for modality.
       * @param title The title of the shown dialog window.
       * @param path The starting directory for the dialog.
       * @return The path of a selected file, null if no file is selected.
       * @throws JNIException If an error occurs while opening the File Dialog
       */
      static String openDirDialog(Frame frame, String title, String path) throws JNIException, UnsatisfiedLinkError {
            frame = validateFrame(frame);
            title = validateTitle(title);
            path = validatePath(path);
            return openDirDialog0(frame, title, path);
      }

      // Validation functions to check if values are in a valid state.

      // Frames can be null, but if they are not they must be visible.
      private static Frame validateFrame(Frame frame){
            if(frame == null || !frame.isVisible())
                  return null;
            return frame;
      }

      // Titles cannot be null, but Windows will automatically add a title if the passed title is empty.
      private static String validateTitle(String title){
            if(title == null)
                  return "";
            return title;
      }

      // Paths must exist and be non-null.
      private static String validatePath(String path){
            File fpath;
            if(path == null || !(fpath = new File(path)).exists())
                  return System.getProperty("user.home");
            if(!fpath.isDirectory())
                  return fpath.getParentFile().getAbsolutePath();
            return fpath.getAbsolutePath();
      }

}

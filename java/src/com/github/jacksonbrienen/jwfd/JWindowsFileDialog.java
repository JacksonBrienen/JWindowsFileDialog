/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
package com.github.jacksonbrienen.jwfd;

import static com.github.jacksonbrienen.jwfd.WindowsFileDialog.IS_WINDOWS;
import java.awt.*;

/**
 * A File Dialog that can be used for opening files, multiple files, saving files, and opening Directories.<br>
 * The File Dialog will open as the native Windows Dialog on compatible systems, or as a <code>JFileChooser</code> on incompatible systems.
 * @author Jackson Brienen
 * @version 0.8
 * @see javax.swing.JFileChooser
 */
public final class JWindowsFileDialog {

    // disable default constructor
    private JWindowsFileDialog() {}

    /**
     * Opens a basic open file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     */
    public static String showOpenDialog(Frame frame){
        return showOpenDialog(frame, FileExtension.ALL);
    }

    /**
     * Opens a basic open file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param filters an array of <code>FileExtension</code>s that filter the shown results by this dialog.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     */
    public static String showOpenDialog(Frame frame, FileExtension... filters){
        return showOpenDialog(frame, null, filters);
    }

    /**
     * Opens a basic open file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param title a <code>String</code> representing the title displayed by the dialog window.
     * @param filters an array of <code>FileExtension</code>s that filter the shown results by this dialog.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     */
    public static String showOpenDialog(Frame frame, String title, FileExtension... filters){
        return showOpenDialog(frame, title, null, filters);
    }

    /**
     * Opens a basic open file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param title a <code>String</code> representing the title displayed by the dialog window.
     * @param path a <code>String</code> representing the starting directory for this dialog.
     * @param filters an array of <code>FileExtension</code>s that filter the shown results by this dialog.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     */
    public static String showOpenDialog(Frame frame, String title, String path, FileExtension... filters){
        if(IS_WINDOWS) {
            try {
                return WindowsFileDialog.openFileDialog(frame, title, path, toWindowsFilter(filters));
            } catch(JNIException | UnsatisfiedLinkError ignored) {}
        }
        return JFileDialog.showOpenDialog(frame, title, path, filters);
    }

    /**
     * Opens a basic save file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     * If the selected path does not have an extension a default extension based on the selected filter will be appended.
     */
    public static String showSaveDialog(Frame frame){
        return showSaveDialog(frame, FileExtension.ALL);
    }

    /**
     * Opens a basic save file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param filters an array of <code>FileExtension</code>s that filter the shown results by this dialog.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     * If the selected path does not have an extension a default extension based on the selected filter will be appended.
     */
    public static String showSaveDialog(Frame frame, FileExtension... filters){
        return showSaveDialog(frame, null, filters);
    }

    /**
     * Opens a basic save file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param title a <code>String</code> representing the title displayed by the dialog window.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     * If the selected path does not have an extension a default extension based on the selected filter will be appended.
     */
    public static String showSaveDialog(Frame frame, String title, FileExtension... filters){
        return showSaveDialog(frame, title, null, filters);
    }

    /**
     * Opens a basic save file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param title a <code>String</code> representing the title displayed by the dialog window.
     * @param path a <code>String</code> representing the starting directory for this dialog.
     * @param filters an array of <code>FileExtension</code>s that filter the shown results by this dialog.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     * If the selected path does not have an extension a default extension based on the selected filter will be appended.
     */
    public static String showSaveDialog(Frame frame, String title, String path, FileExtension... filters){
        if(IS_WINDOWS) {
            try {
                return WindowsFileDialog.openSaveDialog(frame, title, path, toWindowsFilter(filters));
            } catch(JNIException | UnsatisfiedLinkError ignored) {}
        }
        return JFileDialog.showSaveDialog(frame, title, path, filters);
    }

    /**
     * Opens an open multi file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @return a <code>String[]</code> representing the selected paths, or null if no paths are selected.
     */
    public static String[] showMultiDialog(Frame frame){
        return showMultiDialog(frame, FileExtension.ALL);
    }

    /**
     * Opens an open multi file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param filters an array of <code>FileExtension</code>s that filter the shown results by this dialog.
     * @return a <code>String[]</code> representing the selected paths, or null if no paths are selected.
     */
    public static String[] showMultiDialog(Frame frame, FileExtension... filters){
        return showMultiDialog(frame, null, filters);
    }

    /**
     * Opens an open multi file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param title a <code>String</code> representing the title displayed by the dialog window.
     * @param filters an array of <code>FileExtension</code>s that filter the shown results by this dialog.
     * @return a <code>String[]</code> representing the selected paths, or null if no paths are selected.
     */
    public static String[] showMultiDialog(Frame frame, String title, FileExtension... filters){
        return showMultiDialog(frame, title, null, filters);
    }

    /**
     * Opens an open multi file dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param title a <code>String</code> representing the title displayed by the dialog window.
     * @param path a <code>String</code> representing the starting directory for this dialog.
     * @param filters an array of <code>FileExtension</code>s that filter the shown results by this dialog.
     * @return a <code>String[]</code> representing the selected paths, or null if no paths are selected.
     */
    public static String[] showMultiDialog(Frame frame, String title, String path, FileExtension... filters){
        if(IS_WINDOWS) {
            try {
                return WindowsFileDialog.openMultipleDialog(frame, title, path, toWindowsFilter(filters));
            } catch(JNIException | UnsatisfiedLinkError ignored) {}
        }
        return JFileDialog.showMultiDialog(frame, title, path, filters);
    }

    /**
     * Opens an open folder dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     */
    public static String showDirectoryDialog(Frame frame){
        return showDirectoryDialog(frame, null);
    }

    /**
     * Opens an open folder dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param title a <code>String</code> representing the title displayed by the dialog window.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     */
    public static String showDirectoryDialog(Frame frame, String title){
        return showDirectoryDialog(frame, title, null);
    }

    /**
     * Opens an open folder dialog.
     * @param frame The parent frame used as a relative component for modality.
     * @param title a <code>String</code> representing the title displayed by the dialog window.
     * @param path a <code>String</code> representing the starting directory for this dialog.
     * @return a <code>String</code> representing the selected path, or null if no path is selected.
     */
    public static String showDirectoryDialog(Frame frame, String title, String path){
        if(IS_WINDOWS) {
            try {
                return WindowsFileDialog.openDirDialog(frame, title, path);
            } catch(JNIException | UnsatisfiedLinkError ignored) {}
        }
        return JFileDialog.showDirDialog(frame, title, path);
    }

    // function that turns a FileExtension[] into a String[][] in windows filter form that the JNI functions can use
    private static String toWindowsFilter(FileExtension... filter) {
        if(filter == null || filter.length == 0)
            return FileExtension.ALL.getWindowsFilter();
        StringBuilder bld = new StringBuilder(filter[0].getWindowsFilter());
        for(int i = 1; i < filter.length; i++)
            bld.append('\0').append(filter[i].getWindowsFilter());
        return bld.toString();
    }

}
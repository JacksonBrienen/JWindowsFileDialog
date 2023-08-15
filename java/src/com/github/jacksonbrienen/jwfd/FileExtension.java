/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
package com.github.jacksonbrienen.jwfd;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * A Class representing a file extension used for filtering results of <code>JWindowsFileDialog</code>.
 * @author Jackson Brienen
 * @version 0.8
 * @see JWindowsFileDialog
 */
public final class FileExtension extends FileFilter {

    /**
     * The All Files Extension, simply allows for selection of any file with any extension.
     */
    public static final FileExtension ALL = new FileExtension();

    private final String[] extensions;
    private final String windowsFilter;
    private final String description;

    /**
     * Internal constructor used to make the ALL extension.
     */
    private FileExtension() {
        extensions = new String[]{""};
        windowsFilter = "All" + "\0" + "*.*";
        description = "All (*.*)";
    }

    /**
     * Constructs a new <code>FileExtension</code> with the given description and extensions.<br>
     * This is used in parallel with <code>JWindowsFileDialog</code> to filter results shown by both windows and java file dialogs.
     * @param description A basic user-friendly description of what the extension is. For example "Text Document" for the .txt extension.
     * @param extensions An array of Strings representing the extensions used by this file extension.
     *                   These should not include anything but the direct characters for the extension.
     *                   For example for a description "JPG Images" a list of extensions would be "jpg" and "jpeg".
     * @throws IllegalArgumentException If the description or extensions parameters are null, or if the extensions array has a size of 0.
     * @see JWindowsFileDialog
     */
    public FileExtension(String description, String... extensions) throws IllegalArgumentException {
        if(description == null)
            throw new IllegalArgumentException("The description cannot be null");
        if(extensions == null)
            throw new IllegalArgumentException("The extensions array cannot be null");
        if(extensions.length == 0)
            throw new IllegalArgumentException("The extensions array must have a minimum length of 1");

        this.extensions = addExtensionIdentifier(extensions);
        String winExtensions = arrayToWindowsFilter(extensions);
        windowsFilter = description + "\0" + winExtensions;
        this.description = description + " (" + winExtensions + ")";
    }

    /**
     * The Windows Filter which should be passed to the <code>WindowsFileDialog</code> when creating a native dialog.
     * @return a String representing a cstring array of length 2
     * @see WindowsFileDialog
     */
    String getWindowsFilter(){
        return windowsFilter;
    }

    /**
     * @return an array of extensions in the form ".ext" where ext is the extension.
     */
    String[] getExtensions() {
        return extensions;
    }

    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
            return true;
        for(String extension: extensions)
            if(f.getName().toLowerCase().endsWith(extension.toLowerCase()))
                return true;
        return false;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString(){
        return description;
    }

    // used to append a . at the front of all extensions in an extension array
    private static String[] addExtensionIdentifier(String[] extensions) {
        for(int i = 0; i < extensions.length; i++)
            extensions[i] = "." + extensions[i];
        return extensions;
    }

    // turns an array of extensions to a single String in the windows format
    // this must be called after addExtensionIdentifier in order to produce the correct result
    private static String arrayToWindowsFilter(String[] extensions){
        StringBuilder builder = new StringBuilder();
        builder.append("*").append(extensions[0]);
        for(int i = 1; i < extensions.length; i++)
            builder.append(";*").append(extensions[i]);
        return builder.toString();
    }
}

/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
package com.github.jacksonbrienen.jwfd;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * Internal class used to display a <code>JFileChooser</code> in more of a dialog style. Providing file checking,
 * and extension appending which by default <code>JFileChooser</code> does not offer.<br>
 * This is used as a backup for if <code>WindowsFileDialog</code> cannot be used for either JNI problems or Incompatible Operating Systems.
 * @author Jackson Brienen
 * @version 0.8
 * @see JFileChooser
 * @see WindowsFileDialog#IS_WINDOWS
 */
final class JFileDialog {

    // disable default constructor
    private JFileDialog() {}

    // abstract class to provide different ways of approving files based on if the dialog
    // is saving, opening a file, opening multiple files, or opening a directory.
    private static abstract class SelectionApprover {
        private final Runnable approve;
        public SelectionApprover(Runnable approve) {
            this.approve = approve;
        }

        public abstract void approveSelection();

        public final void approve(){
            approve.run();
        }
    }

    // A JFileChooser that uses the SelectionApprover to ensure selected files are in a proper state (writable/readable) or if they exist.
    // This depends on the state of the JFileChooser.
    private static final class MutableAcceptanceFileChooser extends JFileChooser {
        private final SelectionApprover openFileApprover = new SelectionApprover(super::approveSelection) {
            public void approveSelection() {
                File f = getSelectedFile();
                if(!f.exists())
                    JOptionPane.showMessageDialog(MutableAcceptanceFileChooser.this, String.format("%s\nThe file name is not valid.", f.getName()), getDialogTitle(), JOptionPane.WARNING_MESSAGE);
                else if(!Files.isReadable(f.toPath()))
                    JOptionPane.showMessageDialog(MutableAcceptanceFileChooser.this, String.format("%s\nThis file is in use.\nEnter a new name or close the file that's open in another program.", f.getName()), getDialogTitle(), JOptionPane.WARNING_MESSAGE);
                else
                    approve();
            }
        };

        private final SelectionApprover saveFileApprover = new SelectionApprover(super::approveSelection) {
            public void approveSelection() {
                // Make sure the saved file has one of the possible selected extensions
                String name = getSelectedFile().getAbsolutePath();
                // if the filter is set to all ignore this set
                if(getFileFilter() != FileExtension.ALL) {
                    String[] extensions = ((FileExtension) getFileFilter()).getExtensions();
                    boolean addExtension = true;
                    for(String s : extensions)
                        if(name.endsWith(s)) {
                            addExtension = false;
                            break;
                        }
                    if(addExtension) {
                        // if none of the extensions are correct, set the first possible extension
                        name += extensions[0];
                        // and update the selected file
                        setSelectedFile(new File(name));
                    }
                }

                if(getSelectedFile().exists())
                    // if the file already exists prompt the user if they want to replace it
                    if(JOptionPane.showConfirmDialog(MutableAcceptanceFileChooser.this, String.format("%s already exists.\nDo you want to replace it?", name), "Confirm Save As", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        if(!Files.isWritable(getSelectedFile().toPath())) { // check if the file is writable, if not it is being used by another program
                            // tell the user they cannot save to this location, and don't approve the selection
                            JOptionPane.showMessageDialog(MutableAcceptanceFileChooser.this, String.format("%s\nThis file is in use.\nEnter a new name or close the file that's open in another program.", getSelectedFile().getName()), getDialogTitle(), JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }else
                        return; // if no is selected don't approve the selection
                try {
                    // attempt to create the saved file
                    // if false is returned the file already exists, we don't need to delete it
                    // if true is returned we successfully created an empty file in the location
                    if(getSelectedFile().createNewFile())
                        getSelectedFile().delete(); // delete the empty file created
                    approve(); // if all the checks are successful approve the selection, this will close the dialog
                } catch (IOException e) {
                    // if an exception is thrown the user does not have permissions to create a file in that location
                    JOptionPane.showMessageDialog(MutableAcceptanceFileChooser.this, String.format("%s\nYou don't have permission to save in this location.\nContact the administrator to obtain permission.", getSelectedFile().getAbsolutePath()), getDialogTitle(), JOptionPane.WARNING_MESSAGE);
                }
            }
        };

        private final SelectionApprover folderApprover = new SelectionApprover(super::approveSelection) {
            public void approveSelection() {
                if(!getSelectedFile().exists()) {
                    if(JOptionPane.showConfirmDialog(MutableAcceptanceFileChooser.this, String.format("The folder %s does not exist. Do you want to create it?", getSelectedFile().getName()), "Create Folder", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION)
                        return;
                    if(!getSelectedFile().mkdirs()){
                        JOptionPane.showMessageDialog(MutableAcceptanceFileChooser.this, String.format("Error creating folder:\n%s\n\nMake sure you have permissions to create folders in this directory.", getSelectedFile().getAbsolutePath()), "Could not create Folder", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                approve();
            }
        };

        private final SelectionApprover multiApprover = new SelectionApprover(super::approveSelection) {
            @Override
            public void approveSelection() {
                // Essentially a copy of the single open file approver, but for a list of files instead
                File[] files = getSelectedFiles();
                boolean exist = true;
                for(File f: files)
                    if(!f.exists()) {
                        exist = false;
                        break;
                    }
                if(!exist) {
                    JOptionPane.showMessageDialog(MutableAcceptanceFileChooser.this, "One or more of the selected files don't exist.", "File Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean readable = true;
                for(File f: files)
                    if(!Files.isReadable(f.toPath())) {
                        readable = false;
                        break;
                    }
                if(!readable) {
                    JOptionPane.showMessageDialog(MutableAcceptanceFileChooser.this, "One or more of the select files are\nbeing used by another program.", "File Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                approve();
            }
        };

        private SelectionApprover approver;

        public MutableAcceptanceFileChooser() {
            super(".");
        }

        public void approveSelection() {
            if(approver != null)
                approver.approveSelection();
            else
                super.approveSelection();
        }

        public int showOpenDialog(Component parent) {
            approver = openFileApprover;
            setFileSelectionMode(FILES_ONLY);
            setMultiSelectionEnabled(false);
            return super.showOpenDialog(parent);
        }

        public int showMultiDialog(Component parent) {
            approver = multiApprover;
            setFileSelectionMode(FILES_ONLY);
            setMultiSelectionEnabled(true);
            return super.showOpenDialog(parent);
        }

        public int showDirDialog(Component parent) {
            approver = folderApprover;
            setFileSelectionMode(DIRECTORIES_ONLY);
            setMultiSelectionEnabled(false);
            resetChoosableFileFilters();
            return super.showOpenDialog(parent);
        }

        public int showSaveDialog(Component parent) {
            approver = saveFileApprover;
            setFileSelectionMode(FILES_ONLY);
            setMultiSelectionEnabled(false);
            return super.showSaveDialog(parent);
        }

        // uses a FileExtension array to set the Filter for this Chooser
        public void setFilters(FileExtension... filters) {
            resetChoosableFileFilters();
            removeChoosableFileFilter(getFileFilter());
            if(filters != null && filters.length > 0) {
                for(FileExtension f: filters)
                    addChoosableFileFilter(f);
            }else {
                addChoosableFileFilter(FileExtension.ALL);
            }
        }
    }

    /**
     * Shows a basic open file dialog that is thread blocking.
     * @param frame The parent frame used as a relative component for modality.
     * @param title The title of the shown dialog window.
     * @param path The starting directory for the dialog.
     * @param filters A list of filters to filter the files shown.
     * @return a String representing the selected path. Will return null if no file is selected or the dialog is canceled.
     */
    static String showOpenDialog(Frame frame, String title, String path, FileExtension... filters){
        MutableAcceptanceFileChooser chooser = new MutableAcceptanceFileChooser();
        chooser.getActionMap().get("viewTypeDetails").actionPerformed(null);

        if(title != null)
            chooser.setDialogTitle(title);
        else
            chooser.setDialogTitle("Open");

        if(path != null)
            chooser.setCurrentDirectory(new File(path));
        else
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        chooser.setFilters(filters);

        if(chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile().getAbsolutePath();
        return null;
    }

    /**
     * Shows a dialog to open multiple files this is thread blocking.
     * @param frame The parent frame used as a relative component for modality.
     * @param title The title of the shown dialog window.
     * @param path The starting directory for the dialog.
     * @param filters A list of filters to filter the files shown.
     * @return a String array representing the files selected. Will return null if no files are selected or the dialog is canceled.
     */
    static String[] showMultiDialog(Frame frame, String title, String path, FileExtension... filters){
        MutableAcceptanceFileChooser chooser = new MutableAcceptanceFileChooser();
        chooser.getActionMap().get("viewTypeDetails").actionPerformed(null);

        if(title != null)
            chooser.setDialogTitle(title);
        else
            chooser.setDialogTitle("Open");

        if(path != null)
            chooser.setCurrentDirectory(new File(path));
        else
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        chooser.setFilters(filters);

        if(chooser.showMultiDialog(frame) == JFileChooser.APPROVE_OPTION)
            return Arrays.stream(chooser.getSelectedFiles()).map(File::getAbsolutePath).toArray(String[]::new);
        return null;
    }

    /**
     * Shows a basic save file dialog that is thread blocking.
     * @param frame The parent frame used as a relative component for modality.
     * @param title The title of the shown dialog window.
     * @param path The starting directory for the dialog.
     * @param filters A list of filters to filter the files shown.
     * @return a String representing the selected path, if a selected path does not have an extension the first extension of the selected filter will be appended.
     * Will return null if no file is selected or the dialog is canceled.
     */
    static String showSaveDialog(Frame frame, String title, String path, FileExtension... filters){
        MutableAcceptanceFileChooser chooser = new MutableAcceptanceFileChooser();
        chooser.getActionMap().get("viewTypeDetails").actionPerformed(null);

        if(title != null)
            chooser.setDialogTitle(title);
        else
            chooser.setDialogTitle("Save As");

        if(path != null)
            chooser.setCurrentDirectory(new File(path));
        else
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        chooser.setFilters(filters);

        if(chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile().getAbsolutePath();
        return null;
    }

    /**
     * Shows an open file dialog to select a folder this is thread blocking.
     * @param frame The parent frame used as a relative component for modality.
     * @param title The title of the shown dialog window.
     * @param path The starting directory for the dialog.
     * @return a String representing the selected path. Will return null if no file is selected or the dialog is canceled.
     */
    static String showDirDialog(Frame frame, String title, String path) {
        MutableAcceptanceFileChooser chooser = new MutableAcceptanceFileChooser();
        chooser.getActionMap().get("viewTypeDetails").actionPerformed(null);

        if(title != null)
            chooser.setDialogTitle(title);
        else
            chooser.setDialogTitle("Open Folder");
        if(path != null)
            chooser.setCurrentDirectory(new File(path));
        else
            chooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        if(chooser.showDirDialog(frame) == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile().getAbsolutePath();
        return null;
    }

}

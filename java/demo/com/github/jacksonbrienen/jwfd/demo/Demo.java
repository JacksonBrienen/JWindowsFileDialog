/*
 * Content Protected VIA GPL-2.0-only
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 * Copyright (c) 2023 Jackson Nicholas Brienen
 * https://github.com/JacksonBrienen/JWindowsFileDialog
 *
 * **PLEASE DO NOT REMOVE THIS HEADER**
 */
package com.github.jacksonbrienen.jwfd.demo;

import com.github.jacksonbrienen.jwfd.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

// This is a good way to test if the library is working on a specific system or architecture
public class Demo {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Demo");
        FileExtension[] filters = new FileExtension[] {
                FileExtension.ALL,
                new FileExtension("PNG Image", "png"),
                new FileExtension("JPG Image", "jpg", "jpeg"),
                new FileExtension("Text Document", "txt"),
                new FileExtension("HTML Document", "htm", "html")
        };

        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JLabel resultLabel = new JLabel("[]");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane resultContainer = new JScrollPane(resultLabel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        resultContainer.setBorder(BorderFactory.createEmptyBorder());
        resultContainer.setPreferredSize(new Dimension(0, resultLabel.getPreferredSize().height * 2 + 3));
        frame.add(Box.createVerticalStrut(2));

        Box winRow = new Box(BoxLayout.X_AXIS);
        winRow.add(Box.createHorizontalStrut(2));

        ((JButton)winRow.add(new JButton("Open"))).addActionListener((e) ->
                resultLabel.setText(String.format("[%s]", JWindowsFileDialog.showOpenDialog(frame, filters))));

        winRow.add(Box.createHorizontalStrut(5));
        ((JButton)winRow.add(new JButton("Open Multi"))).addActionListener((e) ->
                resultLabel.setText(String.format("%s", Arrays.toString(JWindowsFileDialog.showMultiDialog(frame, filters)).replace("null", "[null]"))));

        winRow.add(Box.createHorizontalStrut(5));
        ((JButton)winRow.add(new JButton("Open Folder"))).addActionListener((e) ->
                resultLabel.setText(String.format("[%s]", JWindowsFileDialog.showDirectoryDialog(frame))));

        winRow.add(Box.createHorizontalStrut(5));
        ((JButton)winRow.add(new JButton("Save"))).addActionListener((e) ->
                resultLabel.setText(String.format("[%s]", JWindowsFileDialog.showSaveDialog(frame, filters))));

        winRow.add(Box.createHorizontalStrut(2));
        frame.add(winRow);

        frame.add(Box.createVerticalStrut(2));

        Box resultBox = new Box(BoxLayout.X_AXIS);
        resultBox.add(Box.createHorizontalStrut(1));
        resultBox.add(resultContainer);
        resultBox.add(Box.createHorizontalStrut(1));
        frame.add(resultBox);
        frame.add(Box.createVerticalStrut(2));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

<h1 style="margin: 0;">JWindowsFileDialog</h1>
<p>Windows native file dialog library for Java AWT and Swing. Allowing the use of the native file dialog on Windows, and using JFileChooser on non-compatible operating systems.</p>
<p>❗<strong>This project is in the early stages of development, expect changes and breaking features. If you find a bug please report it under the issues section. I suck at spelling, so if there is an issue with documentation please report that as well.</strong>❗</p>
<h3 style="margin: 0;">Usage</h3>

```java
// Create a File Extension you wish to filter for, for example jpg, pngs, and an all filter for other image formats
FileExtension jpg = new FileExtension("JPG Images", "jpg", "jpeg");
FileExtension png = new FileExtension("PNG Images", "png", "png");

// pass either a Frame or JFrame in the first argument to have the dialog act as a modal, null can be passed
String selection = JWindowsFileDialog.showOpenDialog(null, "Select an Image", jpg, png, FileExtension.ALL);

if(selection == null)
	System.out.println("The user did not select an image");
else
	System.out.println("The selected image is: " + selection);
```
Documentation is provided for all methods, and a larger example is available under demo.

<h3 style="margin: 0;">Compatibility</h3>
<p style="margin: 1;">Compiled with JDK 8u101, tested up to JDK 20.0.2. 32-bit and 64-bit is support.</p>
<h5 style="margin: 0;">OS Support</h5>
<p style="font-size: 0.8em;">Key: ✔️ (Working), ❌ (Not Working/ Unsupported), ⚠️ (Untested expected to work)</p>

| JDK 8 Compatible<br>Operating Systems  | Status |
| ----------------------- | ------------ |
| Windows 11              | <center>✔️  |
| Windows 10              | <center>✔️  |
| Windows 8.1             | <center>✔️  |
| Windows 8               | <center>✔️  |
| Windows 7               | <center>✔️  |
| Windows Vista           | <center>✔️  |
| Windows Server 2022     | <center>⚠️  |
| Windows Server 2019     | <center>⚠️  |
| Windows Server 2016     | <center>⚠️  |
| Windows Server 2012 R2  | <center>⚠️  |
| Windows Server 2012     | <center>⚠️  |
| Windows Server 2008 R2  | <center>⚠️  |
| Windows Server 2008     | <center>⚠️  |
| Windows Server 2003     | <center>❌  |
| Windows XP              | <center>❌  |
| Windows 2000            | <center>❌  |
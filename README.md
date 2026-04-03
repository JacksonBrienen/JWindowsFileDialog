> [!IMPORTANT]
> I have not updated this in a long time, though it should be pretty bullet proof on the supported systems. I would recommend [FlatLaf's System File Chooser](https://www.formdev.com/flatlaf/system-file-chooser/) as this is going to be better supported and works well with one of the most common LAFs.

<h1 style="margin: 0;">JWindowsFileDialog</h1>

<p>Windows native file dialog library for Java AWT and Swing. Allowing the use of the native file dialog on Windows, and using JFileChooser on non-compatible operating systems.</p>
<p>❗<strong>While most issues have been sorted out, ARM architecture is not supported and the fallback methods have not been tested.</strong>❗</p>
<h3 style="margin: 0;">Usage</h3>

```java
// Create a File Extension you wish to filter for, for example jpg, pngs, and an all filter for other image formats
FileExtension jpg = new FileExtension("JPG Images", "jpg", "jpeg");
FileExtension png = new FileExtension("PNG Images", "png", "png");

// pass either a Frame or JFrame in the first argument to have the dialog act as a modal, null can be passed
String selection = JWindowsFileDialog.showOpenDialog(null, "Select an Image", jpg, png, FileExtension.ALL);

if(selection == null) {
	System.out.println("The user did not select an image");
} else {
	System.out.println("The selected image is: " + selection);
}
```
Documentation is provided for all methods, and a working demo is available under jwfd-demo.

<h3 style="margin: 1;">Importing</h3>

<h5 style="margin: 0;">Maven</h5>

```xml
<dependency>
    <groupId>io.github.jacksonbrienen</groupId>
    <artifactId>jwfd</artifactId>
    <version>0.9.0</version>
</dependency>
```

<h5 style="margin: 0;">Gradle</h5>

```gradle
implementation group: 'io.github.jacksonbrienen', name: 'jwfd', version: '0.9.0'
```

<h5 style="margin: 0;">Gradle(Kotlin)</h5>

```gradle.kts
implementation("io.github.jacksonbrienen:jwfd:0.9.0")
```
Other imports can be found on the [maven central page](https://central.sonatype.com/artifact/io.github.jacksonbrienen/jwfd).
<h3 style="margin: 0;">Compatibility</h3>
<p style="margin: 1;">Compiled with JDK 8u101, tested up to JDK 22.0.2. 32-bit and 64-bit is supported, ARM is not supported at this time.</p>
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

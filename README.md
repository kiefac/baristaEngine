# Barista engine
Barista game engine for Java, based off of javidx9's olcConsoleGameEngine/olcPixelGameEngine.

## Example video
[Add video after uploading]

## Using the engine in Eclipse (tl;dr)
1. Clone the repository.
2. Put the contents of the eclipse folder in your workspace.
3. Import Existing Project in Eclipse.
4. Rename baristaExample, kiefac.barista, and BaristaExample.java.
5. Replace code in your renamed BaristaExample.java under onUserCreate and onUserUpdate.

## Using the engine in Eclipse (for the absolute beginner)
1. Download the .zip of the repository.
2. Copy the contents of the "eclipse" folder into your Eclipse workspace folder (Windows default: "C:\Users\username\eclipse-workspace").
3. Open Eclipse.
4. Go to File -> Import -> General -> Existing Projects into Workspace.
5. Click Next. 
6. Select "Select root directory:", click Browse, and find the folder in your workspace folder.
7. Click "Select Folder".
8. Select "baristaEngine" under "Projects".
9. Click Finish.
10. Expand baristaEngine in the package explorer.
11. Expand src.
12. Expand kiefac.barista.
13. Select BaristaExample.java and press F2.
14. Type in your new class name and press Enter.
15. Open your renamed class file.
16. Select kiefac.barista and press F2.
17. Type in your new package name and press Enter.
18. Select the baristaEngine project and press F2.
19. Type in your new project name and press Enter.
20. Delete everything in onUserCreate except `return true;`.
21. Delete everything in onUserUpdate except `return true;`.
22. Go to main and setup the constructFrame with your screen width, screen height, pixel width, and pixel height.
23. Start writing code!

## Missing functions?
Functions are named similarly to the olcConsoleGameEngine/olcPixelGameEngine. If I'm missing a function you need, DM me on Twitter (@kiefac), message me on Discord (kiefac.#6114), or add an issue here on GitHub!

## License
I'm releasing this under the WTFPL (http://www.wtfpl.net/txt/copying/). However, I would appreciate it if you leave a link to this page if you release anything using it! (javidx9, if you want me to change this to the OLC-3, let me know!)

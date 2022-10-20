set olddir=%CD%
cd /d "C:\Users\Lee\Desktop\SharedFolder2"
rmdir "." /s /q
cd /d "C:\Users\Lee\Desktop\sf3"
rmdir "." /s /q
cd /d "C:\Users\Lee\Desktop\sf4"
rmdir "." /s /q
cd /d "%olddir%"

xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard\target\client-jar-with-dependencies.jar" "C:\Users\Lee\Desktop\SharedFolder2\" /y
xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard\target\client-jar-with-dependencies.jar" "C:\Users\Lee\Desktop\sf3\" /y
xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard\target\client-jar-with-dependencies.jar" "C:\Users\Lee\Desktop\sf4\" /y


xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard\target\server-jar-with-dependencies.jar" "C:\Users\Lee\Desktop\SharedFolder2\" /y
xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard\target\server-jar-with-dependencies.jar" "C:\Users\Lee\Desktop\sf3\" /y
xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard\target\server-jar-with-dependencies.jar" "C:\Users\Lee\Desktop\sf4\" /y
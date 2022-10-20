set olddir=%CD%
cd /d "C:\Users\Lee\Desktop\SharedFolder2"
rmdir "." /s /q
cd /d "C:\Users\Lee\Desktop\sf3"
rmdir "." /s /q
cd /d "C:\Users\Lee\Desktop\sf4"
rmdir "." /s /q
cd /d "%olddir%"

xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard" "C:\Users\Lee\Desktop\SharedFolder2\" /s /e /y
xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard" "C:\Users\Lee\Desktop\sf3\" /s /e /y
xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard" "C:\Users\Lee\Desktop\sf4\" /s /e /y


xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard" "C:\Users\Lee\Desktop\SharedFolder2\" /s /e /y
xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard" "C:\Users\Lee\Desktop\sf3\" /s /e /y
xcopy "G:\SharedFolder_Ubuntu\OnlineWhiteBoard" "C:\Users\Lee\Desktop\sf4\" /s /e /y
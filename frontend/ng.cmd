set EXEC_DIR=%CD%
cd "%EXEC_DIR%"
ECHO "%EXEC_DIR%"
node\node.exe node_modules\@angular\cli\bin\ng  %*

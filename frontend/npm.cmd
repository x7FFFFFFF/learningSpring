set EXEC_DIR=%CD%
cd "%EXEC_DIR%"
ECHO "%EXEC_DIR%"
node\node.exe node\node_modules\npm\bin\npm-cli.js %*

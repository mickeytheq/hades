IF %1.==. GOTO NoVersion

rem - set the version in hades-metadata file
ECHO version = %1 > %~dp0\src\main\resources\hades-metadata

@echo off

rem - Get the date and time and update the eons-plugin file with it
For /f "tokens=1-3 delims=/ " %%a in ('date /t') do (set date=%%c-%%b-%%a)
For /f "tokens=1-2 delims=/:" %%a in ('time /t') do (set time=%%a-%%b-00-00)

sed --in-place=.backup -r "s/(id = CATALOGUEID\{.*:).*(})/\1%date%-%time%\2/" %~dp0src\main\resources\eons-plugin

CALL build.bat

copy c:\Users\micha\AppData\Roaming\StrangeEons3\plug-ins\hades-dev.seext "D:\Dropbox\Games\Arkham horror LCG\Hades\hades-%1.seext"

GOTO :End

:NoVersion
  ECHO no version provided

:End
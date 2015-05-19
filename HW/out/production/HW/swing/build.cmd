@echo off
for /d %%a in (*) do (
    pushd %%a
    if exist build.cmd call build.cmd
    popd %%a
)
# Virtual environment

## Creating a virtual environment

1. Install virtual environment
```bash
pip install virtualenv
```

2. Create virtual environment

Windows systems
```bash
python -m venv <folder_name>
``` 

Linux/OSX
```bash
virtualenv <folder_name>
```

## Using virtual environment

Windows
```bash
# cmd.exe
<folder_name>\Scripts\Activate.bat

# Powershell
<folder_name>\Scripts\Activate.ps1

# bash shell
. ./<folder_name>/bin/activate
```

Linux/OSX
```bash
source <folder_name>/bin/activate
```
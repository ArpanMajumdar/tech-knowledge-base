from pathlib import Path

# Current working directory
cwd = Path.cwd()
print(f'Current working directory:\n{cwd}')

# Create full pathname by joining path and filename
new_file_path = Path.joinpath(cwd, 'new_file.txt')
print(f'\nNew file full path:\n{new_file_path}')

# Check if file exists
does_file_exist = Path.exists(new_file_path)
print(f'\nDoes the new file exist?\n{does_file_exist}')

# Working with directories
print(f'\n\n{"=" * 20} Working with directories {"=" * 20}\n')

# Get parent directory
parent_dir = cwd.parent

# Check if a pathname is a directory
is_dir = Path.is_dir(parent_dir)
print(f'\nIs path \"{parent_dir}\" a directory?\n{is_dir}')

# Check if a pathname is a file
file_path = Path.joinpath(cwd, 'managing_files_demo.py')
is_dir = Path.is_dir(file_path)
print(f'\nIs path \"{file_path}\" a directory?\n{is_dir}')

# List child directories
print('\nCWD contents:')
for child in cwd.iterdir():
    print(child)

# Working with files
print(f'\n\n{"=" * 20} Working with files {"=" * 20}\n')

current_file = Path.joinpath(cwd, 'managing_files_demo.py')

# Get filename
current_file_name = current_file.name
print(f'\nCurrent file name:\n{current_file_name}')

# Get file extension
current_file_ext = current_file.suffix
print(f'\nCurrent file extension:\n{current_file_ext}')

# Get the folder
current_file_folder_name = current_file.parent.name
print(f'\nCurrent file folder name:\n{current_file_folder_name}')

# Get the size
current_file_size = current_file.stat().st_size
print(f'\nCurrent file size:\n{current_file_size}')

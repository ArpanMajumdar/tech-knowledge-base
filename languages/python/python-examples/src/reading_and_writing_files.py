from pathlib import Path

# Modes
# - `r` - Read(default)
# - `w` - Truncate and write
# - `a` - Append if file exists
# - `x` - Write, fail if file exists
# - `+` - Updating(read/write)
#
# File types
# - `t` - Text file(default)
# - `b` - Binary file

input_file_path = Path.joinpath(Path.cwd().parent, 'input.txt')
output_file_path = Path.joinpath(Path.cwd().parent, 'output.txt')

# Reading from a file

stream = open(str(input_file_path))

if stream.readable():
    print(f'Reading {input_file_path.name} ...')

    # Read single character
    print(stream.read(1))

    # Read single line
    print(stream.readline())

# Close the stream
stream.close()

# Writing to a file

stream = open(str(output_file_path), 'wt')

# Write a single character
print(f'Writing to {output_file_path.name} ...')
stream.write('H')
stream.writelines(['ello', ' ', 'World'])
stream.write('\n')
print('Writing completed.')

# Close the stream
stream.close()

# Managing the stream

stream = open(str(output_file_path), 'wt')
stream.write('demo!')

# Move the cursor to a specific position
stream.seek(0)

stream.write('cool')

# Flushes the data from stream to file, at this point if somebody else opens the file,
# he will be able to read the current data
stream.flush()

stream.close()

# Closing the resorce using with
with open(str(output_file_path), 'wt') as stream:
    stream.write('Hello world !')



# Python examples

## Working with files

### Opening a file
``` python
stream = open(file_name, mode, buffer_size)
```

Modes
- `r` - Read(default)
- `w` - Truncate and write
- `a` - Append if file exists
- `x` - Write, fail if file exists
- `+` - Updating(read/write)

File types
- `t` - Text file(default)
- `b` - Binary file
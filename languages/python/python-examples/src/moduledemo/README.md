# Modules

### Import module as namespace
```python
import helpers
helpers.display('Not a warning')
```

### Import all into current namespace
```python
from helpers import *
display('Not a warning')
```

### Import specific items into current namespace
```python
from helpers import display
display('Not a warning')
```

# Packages

### Install an individual package
```bash
pip install pandas
```

### Install from a list of packages
```bash
pip install -r requirements.txt
```


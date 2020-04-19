# ES6 refresher

## Imports and exports

1. default export
``` js
import person from './person.js' // This works if there is a default export
import prs from './person.js'
```

2. named export
``` js
import { smth } from './utility.js'         // Importing a named property/function
import { smth as sm } from './utility.js'   // Using alias for an import
import * as bundled from './utility.js'     // Import all exported properties as object
```


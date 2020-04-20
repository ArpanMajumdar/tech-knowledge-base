## Using a development workflow

### Why?
- Optimize code
- Use next-gen(ES6 or above) language features
- Be more productive

### How?
- Use **dependency management** tool - **npm** or **yarn**
- Use **Bundler** - **Webpack**
- Use **Compiler**(compiles newer versions of JS to older versions for targeting wider range of browsers) - **Babel** + Presets
- Use a **development server**

## JSX
Write writing code in React, we often use JSX syntax which is like HTML in javascript.
``` jsx
<div className="App">
    <h1>Hi, this is a React App</h1>
</div>
```
However, it is not HTML and it is transpiled to JS code which which looks like this.
``` js
React.createElement('div', { className: 'App' },
    React.createElement('h1', null, "Hi, this is a React App"));
```
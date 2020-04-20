import React, { Component } from 'react';
import './App.css';
import Person from './components/Person/Person'

class App extends Component {

  state = {
    persons: [
      {
        name: "Joey",
        age: 35
      },
      {
        name: "Chandler",
        age: 36
      },
      {
        name: "Monica",
        age: 33
      }
    ]
  }

  switchNamesHandler = (newName) => {
    console.log("Switch names clicked.")

    // This merges all the data with the original state and replaces wht has changed
    this.setState({
      persons: [
        {
          name: newName,
          age: 35
        },
        {
          name: "Chandler Bing",
          age: 36
        },
        {
          name: "Monica Geller",
          age: 33
        }
      ]
    });
  }

  nameChangedHandler = (event) => {
    this.setState({
      persons: [
        {
          name: "Joey",
          age: 35
        },
        {
          name: event.target.value,
          age: 36
        },
        {
          name: "Monica",
          age: 33
        }
      ]
    });
  }

  render() {
    // Inline styling using JS
    const buttonStyle = {
      backgroundColor: "white",
      font: "inherit",
      border: '1px solid blue',
      padding: '8px',
      cursor: 'pointer'
    };

    return (
      <div className="App">
        <h1>Hi, this is a React App</h1>
        <p>This is really cool !</p>
        <button
          style={buttonStyle}
          onClick={this.switchNamesHandler.bind(this, "Joey Tribbiani")}>
          Switch names
        </button>
        <Person
          name={this.state.persons[0].name}
          age={this.state.persons[0].age}
          click={this.switchNamesHandler.bind(this, "Joey !!")} />
        <Person
          name={this.state.persons[1].name}
          age={this.state.persons[1].age}
          inputChanged={this.nameChangedHandler}>
          Hobbies: Making jokes
        </Person>
        <Person
          name={this.state.persons[2].name}
          age={this.state.persons[2].age} />
      </div>
    );
  }
}

export default App;

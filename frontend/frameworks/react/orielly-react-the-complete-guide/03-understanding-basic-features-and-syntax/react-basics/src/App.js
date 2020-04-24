import React, { Component } from 'react';
import './App.css';
import Person from './components/Person/Person'

class App extends Component {

  state = {
    persons: [
      {
        id: '1',
        name: "Joey",
        age: 35
      },
      {
        id: '2',
        name: "Chandler",
        age: 36
      },
      {
        id: '3',
        name: "Monica",
        age: 33
      }
    ],
    showPersons: false
  }

  switchNamesHandler = (newName) => {
    console.log("Switch names clicked.")

    // This merges all the data with the original state and replaces wht has changed
    this.setState({
      persons: [
        {
          id: '1',
          name: newName,
          age: 35
        },
        {
          id: '2',
          name: "Chandler Bing",
          age: 36
        },
        {
          id: '3',
          name: "Monica Geller",
          age: 33
        }
      ]
    });
  }

  nameChangedHandler = (event, id) => {
    const persons = [...this.state.persons]
    const listIndex = persons.findIndex(p => {
      return p.id === id;
    })

    const personOnFocus = persons[listIndex];
    personOnFocus.name = event.target.value;

    this.setState({
      persons: persons
    });
  }

  togglePersonsHandler = () => {
    const doesShow = this.state.showPersons
    this.setState({ showPersons: !doesShow })
  }

  deletePersonHandler = (index) => {
    console.log("Delete person called");

    // Copy persons into new array
    const persons = [...this.state.persons];
    persons.splice(index, 1);
    this.setState({ persons: persons })
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

    let persons = null;
    if (this.state.showPersons) {
      persons = (
        <div>
          {this.state.persons.map((person, index) =>
            <Person
              click={() => this.deletePersonHandler(index)}
              name={person.name}
              age={person.age}
              //This id should be something unique in the list
              key={person.id}
              inputChanged={(event) => this.nameChangedHandler(event, person.id)}
            />
          )}
        </div>
      );
    }

    return (
      <div className="App">
        <h1>Hi, this is a React App</h1>
        <p>This is really cool !</p>
        <button
          style={buttonStyle}
          onClick={this.togglePersonsHandler}>
          Toggle persons
        </button>
        {persons}
      </div>
    );
  }
}

export default App;

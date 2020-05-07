import React, { useState } from 'react';
import './App.css';
import Person from './components/Person/Person'

const App = props => {

    const [personsState, setPersonState] = useState({
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
    });

    const [someOtherState, setSomeOtherState] = useState({
        "otherState": "Some other value"
    })

    const switchNamesHandler = () => {
        console.log("Switch names clicked.")
        console.log(personsState)
        console.log(someOtherState)

        // This merges all the data with the original state and replaces wht has changed
        setPersonState({
            persons: [
                {
                    name: "Joey Tribbiani",
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

    return (
        <div className="App">
            <h1>Hi, this is a React App</h1>
            <p>This is really cool !</p>
            <button onClick={switchNamesHandler}>Switch names</button>
            <Person name={personsState.persons[0].name} age={personsState.persons[0].age} />
            <Person name={personsState.persons[1].name} age={personsState.persons[1].age}>Hobbies: Making jokes</Person>
            <Person name={personsState.persons[2].name} age={personsState.persons[2].age} />
        </div>
    );
}

export default App;

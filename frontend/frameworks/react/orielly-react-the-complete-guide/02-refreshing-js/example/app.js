console.log("Hello world");

// let and const
let name1 = "Arpan";
const name2 = "Vivek";

console.log("Name1 =", name1);
console.log("Name2 =", name2);

// Arrow function
function myName(name) {
    console.log(name1);
}

const myNameArrowFn = (name) => {
    console.log(name);
}

// They give the same output
myName("Arpan");
myNameArrowFn("Arpan");

// Classes

class Human {
    constructor() {
        this.gender = "Male";
    }
}

class Person extends Human {
    constructor() {
        super();
        this.name = "Arpan";
    }

    getName() {
        return this.name;
    }

    getGender() {
        return this.gender;
    }
}

const person = new Person();
console.log("Name = ", person.getName(), " Gender = ", person.getGender());

//  Spread operator

const numbers = [1, 2, 3];
const newnumbers = [...numbers, 4, 5];
console.log(newnumbers);

const person2 = {
    name: 'Naruto'
};

const newPerson = {
    ...person2,
    age: 30
}

console.log(newPerson);

// Varargs
const filterEvenNumbers = (...args) => {
    return args.filter(el => el % 2 === 0);
}

console.log(filterEvenNumbers(1, 2, 3, 4, 5));

// Destructuring
[m, n] = [10, 5];
console.log("m = ", m);
console.log("n = ", n);
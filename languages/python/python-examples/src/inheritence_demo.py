class Person:
    def __init__(self, name: str):
        self.name = name

    def say_hello(self) -> str:
        return "Hello " + self.name + " !!"

    def __str__(self):
        return self.name


class Student(Person):
    def __init__(self, name: str, college: str):
        # Call to super-class constructor should be the first call
        super().__init__(name)
        self.college = college

    def say_college_name(self) -> str:
        return "I've studied at " + self.college

    def say_hello(self) -> str:
        return super().say_hello() + " I'm rather tired."

    def __str__(self):
        return f'{self.name} from {self.college}'


student = Student("Arpan", "NIT Warangal")
print(student.say_hello())
print(student.say_college_name())
print(f'Is student instance of Student? : {isinstance(student, Student)}')
print(f'Is student instance of Person? : {isinstance(student, Person)}')
print(f'Is Student class subclass of Person class? : {issubclass(Student, Person)}')
print(student)

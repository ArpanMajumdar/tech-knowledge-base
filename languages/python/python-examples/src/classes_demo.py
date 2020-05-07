# Class names must be in PascalCase
class Presenter:
    # Constructor method
    def __init__(self, name: str) -> None:
        # Always use property if you have defined, don't use the actual variable (__name) here to set property
        self.name = name
        # Avoid using the properties starting with _ (single underscore) unless you know what you are doing
        self._company = "Target"

    # Custom getter
    @property
    def name(self):
        print("Inside getter")
        return self.__name

    # Custom setter
    @name.setter
    def name(self, presenter_name: str):
        # Perform custom validation here
        print("Inside setter")
        self.__name = presenter_name

    def say_hello(self) -> str:
        """
        Returns hello message
        :return: Hello message
        """
        return self.__get_msg()

    # Do not use properties or methods starting with __ (double underscore). These are equivalent to private properties
    def __get_msg(self) -> str:
        return "Hello " + self.name + " !!"


presenter = Presenter("Arpan")
presenter.name = "Arpan Majumdar"
msg = presenter.say_hello()
print(msg)

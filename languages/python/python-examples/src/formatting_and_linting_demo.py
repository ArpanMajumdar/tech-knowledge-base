# Type hints
def print_hello(name: str) -> str:
    """
    Returns a greeting message
    :param name: Name of person
    :return: Hello message
    """
    msg = "Hello " + name + " !"
    print(msg)
    return msg


print_hello("Arpan")

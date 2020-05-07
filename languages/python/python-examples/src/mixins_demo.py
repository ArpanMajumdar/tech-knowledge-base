class Loggable:
    def __init__(self):
        self.logger_name = ''

    def log(self):
        print(f'Log message from {self.logger_name}')


class Connection:
    def __init__(self):
        self.server = ''

    def connect(self):
        print(f'Connecting to server {self.server}')


class SqlDatabase(Loggable, Connection):

    def __init__(self, server: str):
        super().__init__()
        self.logger_name = "SqlDatabaseLogger"
        self.server = server


def framework(item):
    if isinstance(item, Loggable):
        item.log()

    if isinstance(item, Connection):
        item.connect()


sql_database = SqlDatabase("localhost:8080")
framework(sql_database)

class Point:

    def __init__(self, init_x: int, init_y: int):
        """
        @param init_x: Initial x coordinate
        @param init_y: Initial y coordinate
        """
        self.x = init_x
        self.y = init_y

    def get_x(self) -> int:
        """
        Returns x coordinate
        @return: x coordinate
        """
        return self.x

    def get_y(self) -> int:
        """
        Returns y coordinate
        @return: y coordinate
        """
        return self.y

    def distance_from_origin(self) -> float:
        return (self.x ** 2 + self.y ** 2) ** 0.5


point1 = Point(5, 10)
point2 = Point(1, 2)

print("Point1: ", point1.get_x(), ",", point1.get_y())
print("Point2: ", point2.get_x(), ",", point2.get_y())
print("Distance of point 1 from origin: ", point1.distance_from_origin())

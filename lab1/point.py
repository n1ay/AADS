import numpy as np


class Point:
    def __init__(self, x, y):
        self.set_coordinates(x, y)

    def set_coordinates(self, x, y):
        self.x = x
        self.y = y
        self.update_polar()

    def update_polar(self):
        self.r = np.sqrt(self.x ** 2 + self.y ** 2)
        self.phi = np.arctan2(self.y, self.x)

    def __str__(self):
        return '({0}, {1})'.format(self.x, self.y)

    __repr__ = __str__
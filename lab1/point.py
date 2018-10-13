import numpy as np


class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def convert_polar(self):
        return PointPolar(np.sqrt(self.x**2 + self.y**2), np.arctan2(self.y, self.x))

    def __str__(self):
        return '({0}, {1})'.format(self.x, self.y)

    __repr__ = __str__


class PointPolar():
    def __init__(self, r, phi):
        self.r = r
        self.phi = phi

    def convert_cartesian(self):
        return Point(self.r * np.cos(self.phi), self.r * np.sin(self.phi))

    def __str__(self):
        return '({0}, {1})'.format(self.r, self.phi)

    __repr__ = __str__
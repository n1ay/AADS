import matplotlib.pyplot as plt
from point import Point
from operator import attrgetter
from stack import Stack


def draw_points(points_all, points_hull):
    pax, pay = [p.x for p in points_all], [p.y for p in points_all]
    phx, phy = [p.x for p in points_hull], [p.y for p in points_hull]
    plt.plot(pax, pay, 'b', linewidth=0, marker='o')
    plt.plot(phx, phy, 'r', linewidth=0, marker='o')
    plt.show()


def ccw(p1, p2, p3):
    return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x)


def transform_points(points):
    min_y = min(points, key=lambda x: x.y).y
    ref_point = min(filter(lambda p: p.y == min_y, points), key=attrgetter('x'))
    vector = Point(-ref_point.x, -ref_point.y)

    points_ref = [Point(p.x + vector.x, p.y + vector.y) for p in points]

    for p in points_ref:
        if p.x == 0 and p.y == 0:
            ref_point = p
            break

    point_tmp = Point(points_ref[0].x, points_ref[0].y)
    points_ref[0].x = ref_point.x
    points_ref[0].y = ref_point.y

    ref_point.x = point_tmp.x
    ref_point.y = point_tmp.y

    points_polar = [p.convert_polar() for p in points_ref]
    points_polar_sorted = sorted(points_polar, key=lambda p: (p.phi, p.r))
    points_cartesian_sorted = [p.convert_cartesian() for p in points_polar_sorted]
    return [Point(p.x - vector.x, p.y - vector.y) for p in points_cartesian_sorted]


def graham_hull(points):
    points = transform_points(points)

    print(points)

    stack = Stack()
    stack.append(points[0])
    stack.append(points[1])
    stack.append(points[2])

    for i in range(3, len(points)):
        while len(stack) > 1 and ccw(stack.one_below_top(), stack.top(), points[i]) < 0:
            stack.pop()
        stack.append(points[i])

    return stack


def main():
    points = [
        Point(0, -1),
        Point(0, 0),
        Point(0, 1),

        Point(1, -1),
        Point(1, 0),
        Point(1, 1),

        Point(2, -1),
        Point(2, 0),
        Point(2, 1),

        Point(3, -1),
        Point(3, 0),
        Point(3, 1)
    ]

    points.reverse()
    points_hull = graham_hull(points)
    draw_points(points, points_hull)


if __name__ == '__main__':
    main()

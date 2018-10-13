
class Stack(list):
    def __init__(self):
        super(list, self).__init__()

    def top(self):
        return self[len(self)-1]

    def one_below_top(self):
        return self[len(self) - 2]
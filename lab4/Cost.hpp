#ifndef _COST_HPP_
#define _COST_HPP_

#include <list>
#include <ostream>

class Cost {
    public:
    std::list<unsigned> seq;
    unsigned long cost;

    Cost();
    Cost(std::list<unsigned> seq, unsigned long cost);
    friend std::ostream & operator<<(std::ostream& str, const Cost & cost) {
        str << "Sequence: ";
        for (auto it = cost.seq.begin(); it != cost.seq.end(); it++) {
            str << *it << " ";
        }
        str << std::endl << "Cost: " << cost.cost;
        return str;
    }

};

#endif

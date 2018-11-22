#ifndef _COST_HPP_
#define _COST_HPP_

#include <list>
#include <vector>
#include <ostream>
#include <utility>
#include <armadillo>
#include <algorithm>

class Cost {
    public:
    std::list<std::pair<unsigned, unsigned> > seq;
    unsigned long cost;

    Cost();
    Cost(std::list<std::pair<unsigned, unsigned> > seq, unsigned long cost);
    
    unsigned getRows(std::vector<arma::Mat<double> > arrayVector);
    unsigned getCols(std::vector<arma::Mat<double> > arrayVector);
    
    friend std::ostream & operator<<(std::ostream& str, const Cost & cost) {
        str << "Sequence: ";
        for (auto it = cost.seq.begin(); it != cost.seq.end(); it++) {
            str << (*it).first << " " << (*it).second;
        }
        str << std::endl << "Cost: " << cost.cost;
        return str;
    }

};

#endif

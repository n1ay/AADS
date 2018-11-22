#include "Cost.hpp"

Cost::Cost() {}
Cost::Cost(std::list<std::pair<unsigned, unsigned> > seq, unsigned long cost): seq(seq), cost(cost) {}

unsigned Cost::getCols(std::vector<arma::Mat<double> > arrayVector) {
    auto it = std::min_element(seq.begin(), seq.end(),
            [](const std::pair<unsigned, unsigned> & p1, const std::pair<unsigned, unsigned> & p2) {
                    unsigned p1_min = p1.first >= p1.second ? p1.first : p1.second;
                    unsigned p2_min = p2.first >= p2.second ? p2.first : p2.second;
                    return p1_min < p2_min;
                }
            );
    unsigned min = (*it).first > (*it).second ? (*it).second : (*it).first;
    return arrayVector[min].n_cols;
}

unsigned Cost::getRows(std::vector<arma::Mat<double> > arrayVector) {
    auto it = std::max_element(seq.begin(), seq.end(),
             [](const std::pair<unsigned, unsigned> & p1, const std::pair<unsigned, unsigned> & p2) {
                     unsigned p1_max = p1.first >= p1.second ? p1.first : p1.second;
                     unsigned p2_max = p2.first >= p2.second ? p2.first : p2.second;
                     return p1_max < p2_max;
                 }
             );
    unsigned max = (*it).first >= (*it).second ? (*it).first : (*it).second;
    return arrayVector[max].n_rows;

}

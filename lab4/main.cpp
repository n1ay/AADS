#include <cstdio>
#include <algorithm>
#include <unistd.h>
#include <utility>
#include <thread>
#include <chrono>
#include "Cost.hpp"

#define SLEEPTIME_NS 1

std::list<arma::Mat<double>> readData(const std::string & filename); 
arma::Mat<double> computeSequential(const std::list<arma::Mat<double> > & arrayList);
arma::Mat<double> computeParallel(const std::list<arma::Mat<double> > & arrayList);
void computeSequentialWorker(const std::list<arma::Mat<double> > & arrayList, arma::Mat<double> & setResult);
std::vector<std::vector<Cost> > calcCostTable (const std::list<arma::Mat<double> > & arrayList);
Cost calcCost (const std::vector<arma::Mat<double> > & arrayList, const std::vector<std::vector<Cost> > & costs, unsigned indexh, unsigned indexv);

int main() {
    auto matrices = readData("data.txt");
    /*std::chrono::time_point<std::chrono::steady_clock> start = std::chrono::steady_clock::now();
    std::cout<<computeSequential(matrices)<<std::endl;
    std::chrono::time_point<std::chrono::steady_clock> stop = std::chrono::steady_clock::now();
    std::chrono::duration<double, std::milli> elapsed = stop - start;
    std::cout<<"Sequential: " << elapsed.count() << " ms"<<std::endl;


    start = std::chrono::steady_clock::now();
    std::cout<<computeParallel(matrices)<<std::endl;
    stop = std::chrono::steady_clock::now();
    elapsed = stop - start;
    std::cout<<"Parallel: " << elapsed.count() << " ms"<<std::endl;
*/
    auto costs = calcCostTable(matrices);
    for (unsigned i = 1; i < matrices.size(); i++) {
        std::cout<<costs[i][i-1]<<std::endl;
    }
    return 0;
}

arma::Mat<double> computeSequential(const std::list<arma::Mat<double> > & arrayList) {
    auto it = arrayList.begin();
    arma::Mat<double> result = *it;
    it++;
    while(it != arrayList.end()) {
        result = result * (*it);
        std::this_thread::sleep_for(std::chrono::nanoseconds(SLEEPTIME_NS));
        it++;
    }

    return result;
}

void computeSequentialWorker(const std::list<arma::Mat<double> > & arrayList, arma::Mat<double> & setResult) {
    setResult = computeSequential(arrayList);
}

arma::Mat<double> computeParallel(const std::list<arma::Mat<double> > & arrayList) {
    const unsigned numThreads = sysconf(_SC_NPROCESSORS_ONLN);
    arma::Mat<double> result;

    std::vector<std::list<arma::Mat<double> > > arrays(numThreads);

    auto it = arrayList.begin();
    for(unsigned i = 0; i < numThreads; i++) {
        arrays[i] = std::list<arma::Mat<double> >();
        for(unsigned j = 0; j < arrayList.size() / numThreads; j++, it++) {
            arrays[i].push_back(*it);
        }
    }
    while (it != arrayList.end()) {
        arrays[numThreads-1].push_back(*it);
        it++;
    }

    std::vector<std::thread> threads(numThreads);
    std::vector<arma::Mat<double> > results(numThreads);
    for (unsigned i = 0; i < numThreads; i++) {
        results[i] = arma::Mat<double>();
        threads[i] = std::thread(computeSequentialWorker, arrays[i], std::ref(results[i]));
    }

    std::list<arma::Mat<double> > resultsList;
    for (unsigned i = 0; i < numThreads; i++) {
        threads[i].join();
        resultsList.push_back(results[i]);
    }
    
    return computeSequential(resultsList);
}

std::vector<std::vector<Cost> > calcCostTable(const std::list<arma::Mat<double> > & arrayList) {
    std::vector<arma::Mat<double> > arrayVector;
    arrayVector.reserve(arrayList.size());
    std::copy(arrayList.begin(), arrayList.end(), std::back_inserter(arrayVector));
    std::vector<std::vector<Cost> > costs(arrayList.size());
    for (unsigned i = 0; i < arrayList.size(); i++) {
        costs[i] = std::vector<Cost>(i + 1);
    }
    
    unsigned i = 1;
    for (auto it = arrayList.begin(), itNext = ++(arrayList.begin()); itNext != arrayList.end(); it++, itNext++, i++) {
        costs[i][i-1] = Cost({std::make_pair(i-1, i)}, (*it).n_rows*(*it).n_cols*(*itNext).n_cols);
    }


    for (i = 2; i < arrayList.size(); i++) {
        std::cout<<"Debug: "<< i << " " <<std::endl;
        for (unsigned j = 0; j < i; j++) {
            costs[i][j] = calcCost(arrayVector, costs, i, j);
        }
    }

    return costs;
}

Cost calcCost(const std::vector<arma::Mat<double> > & arrayVector, const std::vector<std::vector<Cost> > & costs,  unsigned indexh, unsigned indexv) {
    Cost costLeftGroup = costs[indexh - 1][indexv];
    unsigned costValueLeft = costLeftGroup.cost + costLeftGroup.getRows(arrayVector) * arrayVector[indexh].n_cols * arrayVector[indexh].n_rows;
    
    Cost costRightGroup = costs[indexh][indexv + 1];
    unsigned costValueRight = costRightGroup.cost + costRightGroup.getRows(arrayVector) * arrayVector[indexv].n_cols * arrayVector[indexv].n_rows;

    if (costValueRight > costValueLeft) {
        costRightGroup.seq.push_back(std::make_pair(indexv, indexv + 1));
        costRightGroup.cost = costValueRight;
        return costRightGroup;
    }

    costLeftGroup.seq.push_back(std::make_pair(indexh - 1, indexh));
    costLeftGroup.cost = costValueLeft;
    return costLeftGroup;
}


std::list<arma::Mat<double> > readData(const std::string & filename) {
    std::list<arma::Mat<double>> matrices;
    std::ifstream ifs;
    std::string line;
    ifs.open(filename);
    while (std::getline(ifs, line)) {
        matrices.push_back(arma::Mat<double>(line));
    }

    return matrices;
}

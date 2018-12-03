#include <stdio.h>
#include <string.h>
#include <libxsmm.h>
#include <time.h>

#define MAX_DIM 9
#define MATRICES 10000

double* transpose(double* matrix, int rows, int cols);
void read_data(char* filename, double*** matrices, int** rows, int** cols);

int main() {
    
    libxsmm_init();

    double* C1 = malloc(MAX_DIM * MAX_DIM * sizeof(double));
    double* C2 = malloc(MAX_DIM * MAX_DIM * sizeof(double));

    int* rows;
    int* cols;
    double** matrices;

    read_data("data.txt", &matrices, &rows, &cols);

    for (int i = 0; i < MATRICES; i++) {
        double* tmp = matrices[i];
        matrices[i] = transpose(matrices[i], rows[i], cols[i]);
        free(tmp);
    }

    memcpy(C1, matrices[0], rows[0] * cols[0] * sizeof(double));
    
    double alpha = 1, beta = 0;
    int m = rows[0];

    clock_t start = clock();

    for (int i = 1; i < MATRICES; i++) {
        int n = cols[i], k = rows[i];

        if (i % 2) {
        libxsmm_dgemm(NULL/*transa*/, NULL/*transb*/,
            &m/*required*/, &n/*required*/, &k/*required*/,
            &alpha/*alpha*/, C1/*required*/, NULL/*lda*/,
            matrices[i]/*required*/, NULL/*ldb*/,
            &beta/*beta*/, C2/*required*/, NULL/*ldc*/);
        } else {
        libxsmm_dgemm(NULL/*transa*/, NULL/*transb*/,
            &m/*required*/, &n/*required*/, &k/*required*/,
            &alpha/*alpha*/, C2/*required*/, NULL/*lda*/,
            matrices[i]/*required*/, NULL/*ldb*/,
            &beta/*beta*/, C1/*required*/, NULL/*ldc*/);
        }
    }

    clock_t stop = clock();

    for (int i = 0; i < MAX_DIM * MAX_DIM; i++) {
        printf("%lf ", C2[i]);
    }

    printf("\nTime: %lf ms", ((double)(stop - start))/CLOCKS_PER_SEC * 1000);

    libxsmm_finalize();

    free(C1);
    free(C2);

    for(int i = 0; i < MATRICES; i++) {
        free(matrices[i]);
    }
    free(matrices);
    free(rows);
    free(cols);

    return 0;
}

void read_data(char* filename, double*** matrices, int** rows, int** cols) {
    *matrices = malloc(MATRICES * sizeof(double*));
    *rows = malloc(MATRICES * sizeof(int));
    *cols = malloc(MATRICES * sizeof(int));
    
    int index = 0;
    FILE* fp = fopen(filename, "r");
    char* line = NULL;
    size_t len = 0;
    ssize_t read;

    while((read = getline(&line, &len, fp)) != -1) {
        int dim1, dim2;
        dim1 = atoi(line);
        (*rows)[index] = dim1;

        read = getline(&line, &len, fp);
        dim2 = atoi(line);
        (*cols)[index] = dim2;

        (*matrices)[index] = malloc(dim1 * dim2 * sizeof(double));
        for (int i = 0; i < dim1 * dim2; i++) {
            read = getline(&line, &len, fp);
            (*matrices)[index][i] = atof(line);
        }
        index++;
    }

    fclose(fp);
    if (line)
        free(line);
}

double* transpose(double* matrix, int rows, int cols) {
    double* result = malloc(rows * cols * sizeof(double));
    int k = 0;
    for (int j = 0; j < cols; j++) {
        for (int i = 0; i < rows; i++, k++) {
            result[k] = matrix[j + i * cols];
        }
    }

    return result;
}

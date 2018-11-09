#ifndef _BM_H_ //bm.h 
#define _BM_H_ //boyer-moore algorithm

#define CHAR_SIZE 256

#include <string.h>
#include <stdlib.h>

typedef struct occ_table_info {
    unsigned int* occ_table;
    int occ_table_length;
} occ_table_info;

int last_pos(char character, char* pattern, int pattern_length);
int* get_pos_table(char* pattern, int pattern_length);
occ_table_info get_occ_table(char* text, char* pattern);

#endif

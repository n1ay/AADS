#ifndef _BM_H_ //bm.h
#define _BM_H_ //boyer-moore algorithm

#define CHAR_SIZE 256

#include <string.h>
#include <stdlib.h>

typedef struct array {
    void* data;
    unsigned long length;
} array;

int last_pos(char character, char* pattern, int pattern_length);
int* get_pos_table(char* pattern, int pattern_length);
array get_occ_table(char* text, unsigned long text_length, char* pattern);

#endif

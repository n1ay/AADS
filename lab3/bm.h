#ifndef _BM_H_ //bm.h
#define _BM_H_ //boyer-moore algorithm

#define CHAR_SIZE 256
#define BUFSIZE 8096

#include <string.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>

typedef struct array {
    void* data;
    unsigned long length;
} array;

int last_pos(char character, char* pattern, int pattern_length);
int* get_pos_table(char* pattern, int pattern_length);
array get_occ_table(int fd, char* pattern);
int read_block(int fd, char* buf, int pattern_length);
unsigned long count_occurences(int fd, char* pattern);
#endif

#include "bm.h"

int get_last_pos(char character, char* pattern, int pattern_length) {
    for (int i = pattern_length - 1; i >= 0; i--) {
        if (pattern[i] == character)
            return i;
    }

    return -1;
}
 
int* get_pos_table(char* pattern, int pattern_length) {
    int* pos_table = malloc(CHAR_SIZE * sizeof(int));
    for (int c = 0; c < CHAR_SIZE; c++) {
        pos_table[c] = get_last_pos(c, pattern, pattern_length);
    }

    return pos_table;
}

int read_block(int fd, char* buf, int pattern_length) {
    memcpy(buf, buf + BUFSIZE - pattern_length + 1, pattern_length - 1);
    return read(fd, buf + pattern_length - 1, BUFSIZE - pattern_length + 1);
}

unsigned long count_occurences(int fd, char* pattern) {
    int pattern_length = strlen(pattern);
    int* pos_table = get_pos_table(pattern, pattern_length);
    char* text_cmp_mem = malloc(pattern_length + 1);
    text_cmp_mem[pattern_length] = 0;
    int occ_counter = 0;

    char* buf = malloc(BUFSIZE * sizeof(char));
    int read_bytes = read(fd, buf, BUFSIZE);

    for(int i = pattern_length - 1; i < read_bytes; i += pattern_length) {
        int j = pos_table[buf[i]];
        if ((j >= 0) && ((i - j - 1 + pattern_length) < read_bytes)) {
            memcpy(text_cmp_mem, buf + i - j, pattern_length);
            if (!strcmp(text_cmp_mem, pattern)) {
                occ_counter++;
                i += pattern_length - j;
            }
        }
    }


    while ((read_bytes = read_block(fd, buf, pattern_length))) {
        for(int i = pattern_length - 1; i < read_bytes + pattern_length - 1; i += pattern_length) {
            int j = pos_table[buf[i]];
            if ((j >= 0) && ((i - j - 1 + pattern_length) < read_bytes + pattern_length - 1)) {
                memcpy(text_cmp_mem, buf + i - j, pattern_length);
                if (!strcmp(text_cmp_mem, pattern)) {
                    occ_counter++;
                    i += pattern_length - j;
                }
            }
        }
    }

    free(pos_table);
    free(text_cmp_mem);
    free(buf);

    return occ_counter;
}

array get_occ_table(int fd, char* pattern) {
    int pattern_length = strlen(pattern);
    int* pos_table = get_pos_table(pattern, pattern_length);
    char* text_cmp_mem = malloc(pattern_length + 1);
    text_cmp_mem[pattern_length] = 0;
    int occ_counter = 0;
    size_t occ_table_size = 256;
    unsigned long* occ_table = malloc(occ_table_size * sizeof(unsigned long));

    char* buf = malloc(BUFSIZE * sizeof(char));
    int read_bytes = read(fd, buf, BUFSIZE);
    unsigned long index = pattern_length - 1;

    for(int i = pattern_length - 1; i < read_bytes; i += pattern_length, index += pattern_length) {
        int j = pos_table[buf[i]];
        if ((j >= 0) && ((i - j - 1 + pattern_length) < read_bytes)) {
            memcpy(text_cmp_mem, buf + i - j, pattern_length);
            if (!strcmp(text_cmp_mem, pattern)) {
                occ_table[occ_counter++] = index - j;
                i += pattern_length - j;
                index += pattern_length - j;
            }

            if (occ_counter == occ_table_size) {
                occ_table_size *= 2;
                occ_table = realloc(occ_table, occ_table_size * sizeof(unsigned long));
            }
        }
    }

    index = read_bytes;
    unsigned long index_prev = index;
    while ((read_bytes = read_block(fd, buf, pattern_length))) {
        for(int i = pattern_length - 1; i < read_bytes + pattern_length - 1; i += pattern_length, index += pattern_length) {
            int j = pos_table[buf[i]];
            if ((j >= 0) && ((i - j - 1 + pattern_length) < read_bytes + pattern_length - 1)) {
                memcpy(text_cmp_mem, buf + i - j, pattern_length);
                if (!strcmp(text_cmp_mem, pattern)) {
                    occ_table[occ_counter++] = index - j;
                    i += pattern_length - j;
                    index += pattern_length - j;
                }

                if (occ_counter == occ_table_size) {
                    occ_table_size *= 2;
                    occ_table = realloc(occ_table, occ_table_size * sizeof(unsigned long));
                }
            }
        }
        index = index_prev + read_bytes;
        index_prev = index;
    }

    free(pos_table);
    free(text_cmp_mem);
    free(buf);

    array table_info;
    table_info.data = occ_table;
    table_info.length = occ_counter;

    return table_info;
}

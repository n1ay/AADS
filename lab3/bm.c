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

array get_occ_table(char* text, int text_length, char* pattern) {
    int pattern_length = strlen(pattern);
    int* pos_table = get_pos_table(pattern, pattern_length);

    char* text_cmp_mem = malloc(pattern_length + 1);
    text_cmp_mem[pattern_length] = 0;
    int occ_counter = 0;
    int occ_table_size = 256;
    unsigned int* occ_table = malloc(occ_table_size * sizeof(unsigned int));


    for(int i = pattern_length - 1; i < text_length; i += pattern_length) {
        int j = pos_table[text[i]];
        if ((j >= 0) && ((i - j - 1 + pattern_length) < text_length)) {
            memcpy(text_cmp_mem, text + i - j, pattern_length);
            if (!strcmp(text_cmp_mem, pattern)) {
                occ_table[occ_counter++] = i - j;
                i += pattern_length - j;
            }

            if (occ_counter == occ_table_size) {
                occ_table_size *= 2;
                occ_table = realloc(occ_table, occ_table_size);
            }
        }
    }

    free(pos_table);
    free(text_cmp_mem);

    array table_info;
    table_info.data = occ_table;
    table_info.length = occ_counter;

    return table_info;
}

#include <stdio.h>
#include "bm.h"

int main(int argc, char** argv) {
    char * input = "aabcdefghijklmnopqrstuvwxyzbcggggbc";
    
    printf("%s\n", input);
    occ_table_info table_info = get_occ_table(input, "bc");

    for(int i = 0; i < table_info.occ_table_length; i++) {
        printf("%d\n", table_info.occ_table[i]);
    }

    free(table_info.occ_table);
    return 0;
}

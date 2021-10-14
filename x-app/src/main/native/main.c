#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "include/lzoconf.h"
#include "include/flzo.h"

int main(void) {
    if (lzo_init() != LZO_E_OK) {
        printf("lzo_init() failed!!!\n");
        return 3;
    }
    FILE *file = fopen("./test.AppImage", "r");
    FILE *out = fopen("./out.AppImage", "wb");
    if (file == NULL) {
        printf("目标文件不存在");
        return 3;
    }

    if (out == NULL) {
        printf("tttt");
    }
    //1MB
    size_t as = 1024 * 1024;
    size_t size = sizeof(char);
    size_t temp;
    do {
        char arr[as];

        temp = fread(&arr, size, as, file);
        if (temp == 0) {
            int code = ferror(file);
            if (code != 0) {
                printf("err_code=%d\n", code);
                exit(code);
            }
        } else {
            lzo_uint i_len = 0;
            lzo_bytep o_arr = NULL;
            lzo_bytep i_arr = NULL;
            lzo_uint in_len = temp * size;
            printf("成功读取:%ld字节数据\n", in_len);
            compress((unsigned char *) &arr, &i_arr, in_len, &i_len);
            decompress(i_arr, i_len, &o_arr, &in_len);
            fwrite(o_arr, size, in_len, out);
            free(i_arr);
            free(o_arr);
        }
    } while (temp > 0);

    fclose(out);
    fclose(file);
}

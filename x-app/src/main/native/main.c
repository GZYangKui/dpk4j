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
    FILE *file = fopen("./test.txt", "r");
    FILE *out = fopen("./out.txt","wb");
    if (file == NULL) {
        printf("目标文件不存在");
        return 3;
    }

    if (out == NULL){
        printf("tttt");
    }

    size_t as = 5;
    size_t size = sizeof(char);

    do {
        char arr[as];

        size_t temp =  fread(&arr,size,as,file);
        if (temp == 0) {
            int code = ferror(file);
            printf("err_code=%d\n",code);
            exit(code);
        }else{
            printf("成功读取:%ld块数据\n",temp);
            lzo_bytep i_arr = compress((unsigned char *) &arr);
            lzo_bytep o_arr  = decompress(i_arr);
            fwrite(o_arr,size,as,out);
            free(i_arr);
            free(o_arr);
        }
    } while (size >= 0);

    fclose(out);
    fclose(file);
}

//
// Created by yangkui on 2021/10/15.
//
#include <stdlib.h>
#include <string.h>

#include "include/dpk4j/flzo.h"
#include "include/FileArchive.h"
#include "include/jni/cn_navclub_dkp4j_platform_tool_FileArchive.h"

/*
 * Class:     cn_navclub_dkp4j_platform_tool_FileArchive
 * Method:    compress
 * Signature: (Ljava/io/File;Ljava/io/File;)I
 */
JNIEXPORT jint JNICALL Java_cn_navclub_dkp4j_platform_tool_FileArchive_compress
        (JNIEnv *env, jclass clazz, jobject src, jobject dest) {
    if (src == NULL || dest == NULL) {
        return NULL_POINTER;
    }
    jclass sClazz = (*env)->GetObjectClass(env, src);
    jclass dClass = (*env)->GetObjectClass(env, dest);


    jmethodID sMdId = (*env)->GetMethodID(env, sClazz, "getAbsolutePath", "()Ljava/lang/String;");
    jmethodID dMdId = (*env)->GetMethodID(env, dClass, "getAbsolutePath", "()Ljava/lang/String;");

    jstring srStr = (*env)->CallObjectMethod(env, src, sMdId);
    jstring dsStr = (*env)->CallObjectMethod(env, dest, dMdId);

    jboolean isCopy = JNI_TRUE;

    const char *srPath = (*env)->GetStringUTFChars(env, srStr, &isCopy);
    const char *dsPath = (*env)->GetStringUTFChars(env, dsStr, &isCopy);

    //手动释放局部变量
    (*env)->DeleteLocalRef(env, sClazz);
    (*env)->DeleteLocalRef(env, dClass);
    (*env)->DeleteLocalRef(env, srStr);
    (*env)->DeleteLocalRef(env, dsStr);

    return (jint)do_compress(srPath, dsPath);
}

/*
 * Class:     cn_navclub_dkp4j_platform_tool_FileArchive
 * Method:    decompress
 * Signature: (Ljava/io/File;Ljava/io/File;)I
 */
JNIEXPORT jint JNICALL Java_cn_navclub_dkp4j_platform_tool_FileArchive_decompress
        (JNIEnv *env, jclass clazz, jobject src, jobject dest) {

}


extern uint do_compress(const char *srcPath, const char *dsPath) {
    FILE *in = fopen(srcPath, "rb");
    FILE *out = fopen(dsPath, "w");

    //目标文件不存在
    if (in == NULL) {
        return INPUT_FILE_NOT_FOUND;
    }
    size_t num;
    lzo_bytep buffer = (lzo_bytep) malloc(CHUNK_NUM * CHUNK_SIZE);
    do {
        memset(buffer, 0, CHUNK_NUM * CHUNK_SIZE);
        num = fread(buffer, CHUNK_SIZE, CHUNK_NUM, in);
        if (num == 0) {
            uint code;
            if ((code = ferror(in)) == 0) {
                break;
            }
            printf("读取文件过程发生异常:%d", code);
            return IO_HAPPENED_EXCEPT;
        }
        lzo_uint out_len = 0;
        lzo_bytep out_buf = NULL;
        lzo_uint in_len = num * CHUNK_SIZE;
        printf("开始压缩\n");
        uint rs;
        printf("开始压缩:%d\n", rs);
        rs = dkp4j_compress(buffer, &out_buf, in_len, &out_len);
        printf("结束压缩:%d\n", rs);
        if (rs != 0) {
            printf("压缩数据失败:%d", rs);
            return CD_COM_HAPPENED_EXCEPT;
        }
        size_t _num = fwrite(out_buf, CHUNK_SIZE, out_len, out);
        if (_num == 0) {
            uint code = ferror(out);
            if (code != 0) {
                printf("写入文件过程发生异常:%d", code);
                return IO_HAPPENED_EXCEPT;
            }
        }
    } while (num > 0);
    free(buffer);
    fclose(in);
    fclose(out);
    buffer = NULL;

    return CD_OK;
}
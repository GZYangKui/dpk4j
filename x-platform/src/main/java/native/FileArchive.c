//
// Created by yangkui on 2021/10/15.
//
#include <stdlib.h>
#include <string.h>

#include "include/tool.h"
#include "include/dpk4j/flzo.h"
#include "include/FileArchive.h"
#include "include/jni/cn_navclub_dkp4j_platform_tool_FileArchive.h"


JNIEXPORT jint JNICALL Java_cn_navclub_dkp4j_platform_tool_FileArchive_fileEncode
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

    return (jint) do_compress(srPath, dsPath);
}


JNIEXPORT jint JNICALL Java_cn_navclub_dkp4j_platform_tool_FileArchive_fileDecode
        (JNIEnv *env, jclass clazz, jobject src, jobject dest) {

}


JNIEXPORT jbyteArray JNICALL Java_cn_navclub_dkp4j_platform_tool_FileArchive_byteEncode
        (JNIEnv *env, jclass clazz, jbyteArray array) {
    //待压缩数组为空=>抛出异常
    if (array == NULL) {
        (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointer"), "待压缩数组不能为空!");
        return NULL;
    }
    jsize size = (*env)->GetArrayLength(env, array);

    //返回空数组
    if (size == 0) {
        return (*env)->NewByteArray(env, 0);
    }

    lzo_uint out_len = 0;
    lzo_bytep buffer = NULL;

    //将java字节数组转换为无符号字节
    lzo_bytep in = dkp4j_jbyte_to_cbyte(env, array);

    uint rs = dkp4j_compress(in, &buffer, size, &out_len);

    if (rs != C_D_SUCCESS) {
        (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeExcept"), "压缩过程发生错误!");
        return NULL;
    }

    //将c字节数组转换为java字节数组
    jbyteArray c_arr = dkp4j_cbyte_to_jbyte(env, buffer, (jsize) out_len);

    // 释放资源
    free(buffer);

    return c_arr;
}


JNIEXPORT jbyteArray JNICALL Java_cn_navclub_dkp4j_platform_tool_FileArchive_byteDecode
        (JNIEnv *env, jclass clazz, jbyteArray array) {
    if (array == NULL) {
        (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/NullPointer"), "待解码数据不能为空!");
        return NULL;
    }
    jsize size = (*env)->GetArrayLength(env, array);
    if (size == 0) {
        return array;
    }
    lzo_uint out_len = 0;
    lzo_bytep out = NULL;
    lzo_bytep in = dkp4j_jbyte_to_cbyte(env, array);
    lzo_uint rs = dkp4j_decompress(in, size, &out, &out_len);
    if (rs != C_D_SUCCESS) {
        (*env)->ThrowNew(env, (*env)->FindClass(env, "java/lang/RuntimeExcept"), "解压过程发生错误!");
        return NULL;
    }
    jbyteArray arr = dkp4j_cbyte_to_jbyte(env, out, (jsize) out_len);
    //释放资源
    free(in);
    free(out);
    return arr;
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
        u_int rs = dkp4j_compress(buffer, &out_buf, in_len, &out_len);
        if (rs != C_D_SUCCESS) {
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
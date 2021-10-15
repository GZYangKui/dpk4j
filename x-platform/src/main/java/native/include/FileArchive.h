//
// Created by yangkui on 2021/10/15.
//

#ifndef JNIDKG4J_FILEARCHIVE_H
#define JNIDKG4J_FILEARCHIVE_H

//默认数据块为1M
#define CHUNK_NUM (1024*1024)
#define CHUNK_SIZE (sizeof(char))
#define CD_OK (0)
//空指针
#define NULL_POINTER (-1)
//输入文件不存在
#define INPUT_FILE_NOT_FOUND (-2)
//IO异常
#define IO_HAPPENED_EXCEPT (-3)
//压缩/解压过程出现错误
#define CD_COM_HAPPENED_EXCEPT (-4)


extern uint do_compress(const char *srcPath,const char *dsPath);


#endif //JNIDKG4J_FILEARCHIVE_H

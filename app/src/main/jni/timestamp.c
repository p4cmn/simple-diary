#include <jni.h>
#include <stdio.h>
#include <time.h>

JNIEXPORT jlong JNICALL
Java_com_artem_records_utils_TimeUtils_getCurrentTimestamp(
        __attribute__((unused)) JNIEnv *env,
        __attribute__((unused)) jobject obj
        ) {
    time_t seconds = time(NULL);
    return (jlong)seconds * 1000;
}

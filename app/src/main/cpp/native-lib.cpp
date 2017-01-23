#include <jni.h>
#include <string>

extern "C"
jstring
Java_capstone_textblock_textblock_get_coordinates_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

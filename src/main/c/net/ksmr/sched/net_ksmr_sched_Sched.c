#define _GNU_SOURCE

#include <jni.h>
#include <sched.h>
#include <errno.h>
#include <string.h>

int handle_EINVAL(JNIEnv * env) {
  jclass null_pointer_exception_clazz = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
  return (*env)->ThrowNew(env, null_pointer_exception_clazz, "The affinity bit mask contains no processors that are currently physically on the system.");
}

/*
 * Class:     net_ksmr_sched_Sched
 * Method:    linux_sched_setaffinity
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_ksmr_sched_Sched_linux_1sched_1setaffinity
  (JNIEnv * env, jclass clazz, jlong mask)
{
  int err = sched_setaffinity(0, sizeof(long), (const cpu_set_t *) &mask);

  if (err) {
    switch (errno) {
    case EINVAL:
      handle_EINVAL(env);
      return;
    }
  }
}

/*
 * Class:     net_ksmr_sched_Sched
 * Method:    linux_sched_getaffinity
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_net_ksmr_sched_Sched_linux_1sched_1getaffinity
  (JNIEnv * env, jclass clazz)
{
  cpu_set_t mask;

  sched_getaffinity(0, sizeof(cpu_set_t), &mask);

  jlong retmask;
  memcpy(&retmask, &mask, sizeof(jlong));

  return retmask;
}

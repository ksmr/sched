#define _GNU_SOURCE

#include <jni.h>
#include <sched.h>
#include <errno.h>
#include <string.h>

int handle_EINVAL(JNIEnv * env) {
  jclass null_pointer_exception_clazz = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
  return (*env)->ThrowNew(env, null_pointer_exception_clazz, "The affinity bit mask contains no processors that are currently physically on the system and permitted to the thread.");
}

/*
 * Class:     net_ksmr_sched_Sched
 * Method:    linux_sched_setaffinity
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_net_ksmr_sched_Sched_linux_1sched_1setaffinity
  (JNIEnv *env, jclass clazz, jlong mask)
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
 * Method:    linux_sched_setaffinity_dynamic
 * Signature: ([J)V
 */
JNIEXPORT void JNICALL Java_net_ksmr_sched_Sched_linux_1sched_1setaffinity_1dynamic
  (JNIEnv *env, jclass clazz, jlongArray mask)
{
  size_t size = (size_t) (*env)->GetArrayLength(env, mask);
  int num_cpus = size * sizeof(jlong);

  cpu_set_t *set = CPU_ALLOC(num_cpus);
  size_t setsize = CPU_ALLOC_SIZE(num_cpus);
  CPU_ZERO_S(setsize, set);

  jlong *array = (*env)->GetLongArrayElements(env, mask, NULL);
  memcpy(set, array, setsize);
  (*env)->ReleaseLongArrayElements(env, mask, array, JNI_ABORT);

  int err = sched_setaffinity(0, setsize, set);
  CPU_FREE(set);

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
  (JNIEnv *env, jclass clazz)
{
  cpu_set_t mask;

  sched_getaffinity(0, sizeof(cpu_set_t), &mask);

  jlong retmask;
  memcpy(&retmask, &mask, sizeof(jlong));

  return retmask;
}

/*
 * Class:     net_ksmr_sched_Sched
 * Method:    linux_sched_getaffinity_dynamic
 * Signature: ()[J
 */
JNIEXPORT jlongArray JNICALL Java_net_ksmr_sched_Sched_linux_1sched_1getaffinity_1dynamic
  (JNIEnv *env, jclass clazz)
{
  int num_cpus = 512;
  int err;
  cpu_set_t *set;
  size_t setsize;
  do {
    num_cpus *= 2;
    set = CPU_ALLOC(num_cpus);
    setsize = CPU_ALLOC_SIZE(num_cpus);
    CPU_ZERO_S(setsize, set);
    err = sched_getaffinity(0, setsize, set);
  } while (err && errno == EINVAL);

  if (!err) {
    jlongArray retarr = (*env)->NewLongArray(env, 1 + (num_cpus / sizeof(jlong)));
    jlong *els = (*env)->GetLongArrayElements(env, retarr, NULL);

    if (retarr != NULL) {
      memcpy(els, set, setsize);
      CPU_FREE(set);
      (*env)->ReleaseLongArrayElements(env, retarr, els, 0);
      return retarr;
    }
  }

  CPU_FREE(set);
  return NULL;
}

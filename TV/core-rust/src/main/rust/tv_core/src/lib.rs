mod player;
mod decoder;
mod cache;
mod network;

use jni::objects::{JClass, JString, JObject, JValue};
use jni::JNIEnv;
use jni::sys::{jlong, jboolean, jint, jbyteArray, jobjectArray};
use once_cell::sync::OnceCell;
use std::ptr;

static CORE: OnceCell<Core> = OnceCell::new();

struct Core {
    cache: cache::CacheManager,
    network: network::NetworkStack,
}

impl Core {
    fn new() -> Self {
        Self {
            cache: cache::CacheManager::new(256 * 1024 * 1024),
            network: network::NetworkStack::new(),
        }
    }

    fn get() -> &'static Core {
        CORE.get_or_init(Core::new)
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativeInit(
    _env: JNIEnv,
    _class: JClass,
) {
    let _ = Core::get();
    log::info!("TvCore native initialized");
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativeDecodeAes(
    mut env: JNIEnv,
    _class: JClass,
    data: JString,
    key: JString,
    iv: JString,
) -> jbyteArray {
    let data_str: String = match env.get_string(&data) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };
    let key_str: String = match env.get_string(&key) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };
    let iv_str: String = match env.get_string(&iv) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };

    match decoder::aes_decrypt(&data_str, &key_str, &iv_str) {
        Ok(result) => {
            let arr = env.new_byte_array(result.len() as jint).unwrap();
            env.set_byte_array_region(&arr, 0, unsafe { std::mem::transmute::<&[u8], &[i8]>(&result) }).unwrap();
            arr
        }
        Err(_) => std::ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativeDecodeBase64(
    mut env: JNIEnv,
    _class: JClass,
    data: JString,
) -> jbyteArray {
    let data_str: String = match env.get_string(&data) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };

    match decoder::base64_decode(&data_str) {
        Ok(result) => {
            let arr = env.new_byte_array(result.len() as jint).unwrap();
            env.set_byte_array_region(&arr, 0, unsafe { std::mem::transmute::<&[u8], &[i8]>(&result) }).unwrap();
            arr
        }
        Err(_) => std::ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativeCachePut(
    mut env: JNIEnv,
    _class: JClass,
    key: JString,
    data: jbyteArray,
) -> jboolean {
    let key_str: String = match env.get_string(&key) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };
    let data_vec: Vec<u8> = match env.convert_byte_array(data) {
        Ok(v) => v,
        Err(_) => return 0,
    };

    let core = Core::get();
    core.cache.put(&key_str, &data_vec);
    1
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativeCacheGet(
    mut env: JNIEnv,
    _class: JClass,
    key: JString,
) -> jbyteArray {
    let key_str: String = match env.get_string(&key) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };

    let core = Core::get();
    match core.cache.get(&key_str) {
        Some(data) => {
            let arr = env.new_byte_array(data.len() as jint).unwrap();
            env.set_byte_array_region(&arr, 0, unsafe { std::mem::transmute::<&[u8], &[i8]>(&data) }).unwrap();
            arr
        }
        None => std::ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativeNetworkGet(
    mut env: JNIEnv,
    _class: JClass,
    url: JString,
    headers: JString,
    timeout_ms: jint,
) -> jbyteArray {
    let url_str: String = match env.get_string(&url) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };
    let headers_str: String = match env.get_string(&headers) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };

    let core = Core::get();
    let rt = tokio::runtime::Runtime::new().unwrap();
    match rt.block_on(core.network.get(&url_str, &headers_str, timeout_ms as u64)) {
        Ok(data) => {
            let arr = env.new_byte_array(data.len() as jint).unwrap();
            env.set_byte_array_region(&arr, 0, unsafe { std::mem::transmute::<&[u8], &[i8]>(&data) }).unwrap();
            arr
        }
        Err(_) => std::ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativeNetworkPost(
    mut env: JNIEnv,
    _class: JClass,
    url: JString,
    body: JString,
    headers: JString,
    timeout_ms: jint,
) -> jbyteArray {
    let url_str: String = match env.get_string(&url) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };
    let body_str: String = match env.get_string(&body) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };
    let headers_str: String = match env.get_string(&headers) {
        Ok(s) => s.into(),
        Err(_) => return std::ptr::null_mut(),
    };

    let core = Core::get();
    let rt = tokio::runtime::Runtime::new().unwrap();
    match rt.block_on(core.network.post(&url_str, &body_str, &headers_str, timeout_ms as u64)) {
        Ok(data) => {
            let arr = env.new_byte_array(data.len() as jint).unwrap();
            env.set_byte_array_region(&arr, 0, unsafe { std::mem::transmute::<&[u8], &[i8]>(&data) }).unwrap();
            arr
        }
        Err(_) => std::ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativePlayerCreate(
    _env: JNIEnv,
    _class: JClass,
) -> jlong {
    let player = player::Player::new();
    Box::into_raw(Box::new(player)) as jlong
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativePlayerRelease(
    _env: JNIEnv,
    _class: JClass,
    handle: jlong,
) {
    if handle != 0 {
        unsafe {
            let _ = Box::from_raw(handle as *mut player::Player);
        }
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_rust_TvCoreNative_nativeSnifferMatch(
    mut env: JNIEnv,
    _class: JClass,
    url: JString,
    patterns: jobjectArray,
) -> jboolean {
    let url_str: String = match env.get_string(&url) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };

    let len = env.get_array_length(&patterns).unwrap_or(0);
    let mut pattern_list = Vec::new();
    for i in 0..len {
        let elem = env.get_object_array_element(&patterns, i).unwrap();
        let s: String = env.get_string(&elem.into()).unwrap().into();
        pattern_list.push(s);
    }

    if player::sniffer_match(&url_str, &pattern_list) { 1 } else { 0 }
}

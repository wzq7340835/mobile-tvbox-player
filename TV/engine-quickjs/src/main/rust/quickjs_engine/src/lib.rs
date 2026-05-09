use jni::objects::{JClass, JString, JValue, JObject};
use jni::JNIEnv;
use jni::sys::{jlong, jboolean, jint, jstring, jbyteArray, jobjectArray};
use once_cell::sync::OnceCell;
use parking_lot::Mutex;
use rquickjs::{Context, Ctx, Function, Module, Runtime, Value};
use std::collections::HashMap;
use std::ptr;

static ENGINE: OnceCell<Mutex<QuickJsEngine>> = OnceCell::new();

struct SpiderInstance {
    runtime: Runtime,
    context: Context,
    module_loaded: bool,
}

struct QuickJsEngine {
    spiders: HashMap<String, SpiderInstance>,
}

impl QuickJsEngine {
    fn new() -> Self {
        Self {
            spiders: HashMap::new(),
        }
    }

    fn get() -> &'static Mutex<QuickJsEngine> {
        ENGINE.get_or_init(|| Mutex::new(QuickJsEngine::new()))
    }

    fn create_spider(&mut self, spider_key: &str, js_code: &str) -> Result<(), String> {
        let runtime = Runtime::new().map_err(|e| format!("runtime error: {:?}", e))?;
        let context = Context::full(&runtime).map_err(|e| format!("context error: {:?}", e))?;

        context.with(|ctx| {
            ctx.eval::<(), _>(js_code).map_err(|e| format!("eval error: {:?}", e))
        })?;

        self.spiders.insert(spider_key.to_string(), SpiderInstance {
            runtime,
            context,
            module_loaded: true,
        });

        Ok(())
    }

    fn call_spider_method(&mut self, spider_key: &str, method: &str, args: &str) -> Result<String, String> {
        let spider = self.spiders.get_mut(spider_key)
            .ok_or_else(|| format!("spider not found: {}", spider_key))?;

        let result = spider.context.with(|ctx| {
            let code = if args.is_empty() {
                format!("JSON.stringify({}())", method)
            } else {
                format!("JSON.stringify({}({}))", method, args)
            };
            ctx.eval::<String, _>(&code).map_err(|e| format!("call error: {:?}", e))
        })?;

        Ok(result)
    }

    fn remove_spider(&mut self, spider_key: &str) {
        self.spiders.remove(spider_key);
    }

    fn clear(&mut self) {
        self.spiders.clear();
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_engine_QuickJsNative_nativeInit(
    _env: JNIEnv,
    _class: JClass,
) {
    let _ = QuickJsEngine::get();
    log::info!("QuickJsEngine native initialized");
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_engine_QuickJsNative_nativeCreateSpider(
    mut env: JNIEnv,
    _class: JClass,
    spider_key: JString,
    js_code: JString,
) -> jboolean {
    let key: String = match env.get_string(&spider_key) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };
    let code: String = match env.get_string(&js_code) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };

    let engine = QuickJsEngine::get();
    let mut engine = engine.lock();
    match engine.create_spider(&key, &code) {
        Ok(()) => 1,
        Err(e) => {
            log::error!("create spider error: {}", e);
            0
        }
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_engine_QuickJsNative_nativeCallMethod(
    mut env: JNIEnv,
    _class: JClass,
    spider_key: JString,
    method: JString,
    args: JString,
) -> jstring {
    let key: String = match env.get_string(&spider_key) {
        Ok(s) => s.into(),
        Err(_) => return ptr::null_mut(),
    };
    let method_name: String = match env.get_string(&method) {
        Ok(s) => s.into(),
        Err(_) => return ptr::null_mut(),
    };
    let args_str: String = match env.get_string(&args) {
        Ok(s) => s.into(),
        Err(_) => return ptr::null_mut(),
    };

    let engine = QuickJsEngine::get();
    let mut engine = engine.lock();
    match engine.call_spider_method(&key, &method_name, &args_str) {
        Ok(result) => match env.new_string(&result) {
            Ok(s) => s.into_raw(),
            Err(_) => ptr::null_mut(),
        },
        Err(e) => {
            log::error!("call method error: {}", e);
            ptr::null_mut()
        }
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_engine_QuickJsNative_nativeRemoveSpider(
    mut env: JNIEnv,
    _class: JClass,
    spider_key: JString,
) {
    let key: String = match env.get_string(&spider_key) {
        Ok(s) => s.into(),
        Err(_) => return,
    };

    let engine = QuickJsEngine::get();
    let mut engine = engine.lock();
    engine.remove_spider(&key);
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_engine_QuickJsNative_nativeEval(
    mut env: JNIEnv,
    _class: JClass,
    js_code: JString,
) -> jstring {
    let code: String = match env.get_string(&js_code) {
        Ok(s) => s.into(),
        Err(_) => return ptr::null_mut(),
    };

    let engine = QuickJsEngine::get();
    let mut engine = engine.lock();

    let runtime = Runtime::new().unwrap();
    let context = Context::full(&runtime).unwrap();

    let result = context.with(|ctx| {
        ctx.eval::<String, _>(&code).unwrap_or_default()
    });

    match env.new_string(&result) {
        Ok(s) => s.into_raw(),
        Err(_) => ptr::null_mut(),
    }
}

#[no_mangle]
pub extern "system" fn Java_com_fongmi_android_tv_engine_QuickJsNative_nativeClear(
    _env: JNIEnv,
    _class: JClass,
) {
    let engine = QuickJsEngine::get();
    let mut engine = engine.lock();
    engine.clear();
}

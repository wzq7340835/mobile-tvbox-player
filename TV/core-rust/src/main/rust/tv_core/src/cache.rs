use dashmap::DashMap;
use lru::LruCache;
use parking_lot::Mutex;
use std::num::NonZeroUsize;
use std::time::{Duration, Instant};

struct CacheEntry {
    data: Vec<u8>,
    expire_at: Option<Instant>,
}

pub struct CacheManager {
    memory: Mutex<LruCache<String, CacheEntry>>,
    disk_path: Option<String>,
}

impl CacheManager {
    pub fn new(max_size: usize) -> Self {
        let capacity = NonZeroUsize::new(max_size.min(10000)).unwrap_or(NonZeroUsize::new(1000).unwrap());
        Self {
            memory: Mutex::new(LruCache::new(capacity)),
            disk_path: None,
        }
    }

    pub fn put(&self, key: &str, data: &[u8]) {
        let entry = CacheEntry {
            data: data.to_vec(),
            expire_at: Some(Instant::now() + Duration::from_secs(3600)),
        };
        let mut cache = self.memory.lock();
        cache.put(key.to_string(), entry);
    }

    pub fn get(&self, key: &str) -> Option<Vec<u8>> {
        let mut cache = self.memory.lock();
        if let Some(entry) = cache.get(key) {
            if let Some(expire) = entry.expire_at {
                if Instant::now() > expire {
                    cache.pop(key);
                    return None;
                }
            }
            return Some(entry.data.clone());
        }
        None
    }

    pub fn remove(&self, key: &str) {
        let mut cache = self.memory.lock();
        cache.pop(key);
    }

    pub fn clear(&self) {
        let mut cache = self.memory.lock();
        cache.clear();
    }

    pub fn size(&self) -> usize {
        let cache = self.memory.lock();
        cache.len()
    }
}

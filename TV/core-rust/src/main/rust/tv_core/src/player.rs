use regex::Regex;
use std::collections::HashMap;
use std::sync::atomic::{AtomicI64, AtomicBool, Ordering};
use std::sync::Arc;

pub struct Player {
    position: Arc<AtomicI64>,
    duration: Arc<AtomicI64>,
    playing: Arc<AtomicBool>,
    speed: Arc<AtomicI64>,
    headers: HashMap<String, String>,
}

impl Player {
    pub fn new() -> Self {
        Self {
            position: Arc::new(AtomicI64::new(0)),
            duration: Arc::new(AtomicI64::new(0)),
            playing: Arc::new(AtomicBool::new(false)),
            speed: Arc::new(AtomicI64::new(1000)),
            headers: HashMap::new(),
        }
    }

    pub fn set_headers(&mut self, headers: HashMap<String, String>) {
        self.headers = headers;
    }

    pub fn get_headers(&self) -> &HashMap<String, String> {
        &self.headers
    }

    pub fn set_position(&self, pos: i64) {
        self.position.store(pos, Ordering::SeqCst);
    }

    pub fn get_position(&self) -> i64 {
        self.position.load(Ordering::SeqCst)
    }

    pub fn set_duration(&self, dur: i64) {
        self.duration.store(dur, Ordering::SeqCst);
    }

    pub fn get_duration(&self) -> i64 {
        self.duration.load(Ordering::SeqCst)
    }

    pub fn set_playing(&self, playing: bool) {
        self.playing.store(playing, Ordering::SeqCst);
    }

    pub fn is_playing(&self) -> bool {
        self.playing.load(Ordering::SeqCst)
    }

    pub fn set_speed(&self, speed: i64) {
        self.speed.store(speed, Ordering::SeqCst);
    }

    pub fn get_speed(&self) -> i64 {
        self.speed.load(Ordering::SeqCst)
    }
}

pub fn sniffer_match(url: &str, patterns: &[String]) -> bool {
    for pattern in patterns {
        if let Ok(re) = Regex::new(pattern) {
            if re.is_match(url) {
                return true;
            }
        }
    }
    false
}

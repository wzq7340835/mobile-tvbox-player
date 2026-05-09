use reqwest::{Client, Method};
use serde_json::Value;
use std::collections::HashMap;
use std::time::Duration;

pub struct NetworkStack {
    client: Client,
}

impl NetworkStack {
    pub fn new() -> Self {
        let client = Client::builder()
            .connect_timeout(Duration::from_secs(10))
            .timeout(Duration::from_secs(30))
            .redirect(reqwest::redirect::Policy::limited(10))
            .danger_accept_invalid_certs(true)
            .build()
            .unwrap_or_else(|_| Client::new());

        Self { client }
    }

    pub async fn get(&self, url: &str, headers_json: &str, timeout_ms: u64) -> Result<Vec<u8>, String> {
        let headers = parse_headers(headers_json);
        let mut req = self.client.request(Method::GET, url);
        for (k, v) in headers {
            req = req.header(&k, &v);
        }
        req = req.timeout(Duration::from_millis(timeout_ms));

        let resp = req.send().await.map_err(|e| e.to_string())?;
        let bytes = resp.bytes().await.map_err(|e| e.to_string())?;
        Ok(bytes.to_vec())
    }

    pub async fn post(&self, url: &str, body: &str, headers_json: &str, timeout_ms: u64) -> Result<Vec<u8>, String> {
        let headers = parse_headers(headers_json);
        let mut req = self.client.request(Method::POST, url);
        for (k, v) in headers {
            req = req.header(&k, &v);
        }
        req = req.body(body.to_string());
        req = req.timeout(Duration::from_millis(timeout_ms));

        let resp = req.send().await.map_err(|e| e.to_string())?;
        let bytes = resp.bytes().await.map_err(|e| e.to_string())?;
        Ok(bytes.to_vec())
    }
}

fn parse_headers(json: &str) -> HashMap<String, String> {
    if json.is_empty() {
        return HashMap::new();
    }
    serde_json::from_str(json).unwrap_or_default()
}

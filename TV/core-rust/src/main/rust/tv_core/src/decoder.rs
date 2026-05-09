use ring::aead::{Aad, LessSafeKey, Nonce, UnboundKey, AES_128_CBC};
use ring::block::Block;
use base64::{Engine as _, engine::general_purpose};

pub fn aes_decrypt(data: &str, key: &str, iv: &str) -> Result<Vec<u8>, String> {
    let encrypted = general_purpose::STANDARD.decode(data).map_err(|e| e.to_string())?;
    let key_bytes = key.as_bytes();
    let iv_bytes = iv.as_bytes();

    let mut in_out = encrypted.clone();
    let unbound_key = UnboundKey::new(&AES_128_CBC, &key_bytes[..16]).map_err(|e| format!("key error: {:?}", e))?;
    let less_safe_key = LessSafeKey::new(unbound_key);
    let nonce = Nonce::assume_unique_for_key(iv_bytes[..12].try_into().map_err(|_| "iv length error")?);

    less_safe_key.open_in_place(nonce, Aad::empty(), &mut in_out)
        .map_err(|e| format!("decrypt error: {:?}", e))?;

    let plaintext = pkcs7_unpad(&in_out);
    Ok(plaintext)
}

pub fn base64_decode(data: &str) -> Result<Vec<u8>, String> {
    general_purpose::STANDARD.decode(data).map_err(|e| e.to_string())
}

pub fn base64_encode(data: &[u8]) -> String {
    general_purpose::STANDARD.encode(data)
}

fn pkcs7_unpad(data: &[u8]) -> Vec<u8> {
    if data.is_empty() {
        return Vec::new();
    }
    let pad_len = *data.last().unwrap() as usize;
    if pad_len > data.len() {
        return data.to_vec();
    }
    data[..data.len() - pad_len].to_vec()
}

pub fn md5(data: &[u8]) -> String {
    use ring::digest::{digest, MD5};
    let hash = digest(&MD5, data);
    hex_encode(hash.as_ref())
}

pub fn sha256(data: &[u8]) -> String {
    use ring::digest::{digest, SHA256};
    let hash = digest(&SHA256, data);
    hex_encode(hash.as_ref())
}

fn hex_encode(data: &[u8]) -> String {
    data.iter().map(|b| format!("{:02x}", b)).collect()
}

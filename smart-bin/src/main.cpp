#include <WiFi.h>
#include <HTTPClient.h>
#include "ESP32QRCodeReader.h" // nadal używamy do konfiguracji kamery
#include "esp_camera.h"

// --- Konfiguracja WiFi ---
const char* ssid = "Red";
const char* password = "12345678";

// --- Adres serwera do POST ---
const char* serverUrl = "http://192.168.237.109:5000/qr";

// --- Twoje custom piny ---
CameraPins myPins = {
  .PWDN_GPIO_NUM = -1,
  .RESET_GPIO_NUM = -1,
  .XCLK_GPIO_NUM = 15,
  .SIOD_GPIO_NUM = 4,
  .SIOC_GPIO_NUM = 5,
  .Y9_GPIO_NUM = 16,
  .Y8_GPIO_NUM = 17,
  .Y7_GPIO_NUM = 18,
  .Y6_GPIO_NUM = 12,
  .Y5_GPIO_NUM = 10,
  .Y4_GPIO_NUM = 8,
  .Y3_GPIO_NUM = 9,
  .Y2_GPIO_NUM = 11,
  .VSYNC_GPIO_NUM = 6,
  .HREF_GPIO_NUM = 7,
  .PCLK_GPIO_NUM = 13
};

camera_config_t camera_config;

void setup() {
  Serial.begin(115200);
  delay(1000);

  Serial.println("Łączenie z WiFi...");
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nPołączono z WiFi!");

  camera_config.ledc_channel = LEDC_CHANNEL_0;
  camera_config.ledc_timer = LEDC_TIMER_0;
  camera_config.pin_d0 = myPins.Y2_GPIO_NUM;
  camera_config.pin_d1 = myPins.Y3_GPIO_NUM;
  camera_config.pin_d2 = myPins.Y4_GPIO_NUM;
  camera_config.pin_d3 = myPins.Y5_GPIO_NUM;
  camera_config.pin_d4 = myPins.Y6_GPIO_NUM;
  camera_config.pin_d5 = myPins.Y7_GPIO_NUM;
  camera_config.pin_d6 = myPins.Y8_GPIO_NUM;
  camera_config.pin_d7 = myPins.Y9_GPIO_NUM;
  camera_config.pin_xclk = myPins.XCLK_GPIO_NUM;
  camera_config.pin_pclk = myPins.PCLK_GPIO_NUM;
  camera_config.pin_vsync = myPins.VSYNC_GPIO_NUM;
  camera_config.pin_href = myPins.HREF_GPIO_NUM;
  camera_config.pin_sscb_sda = myPins.SIOD_GPIO_NUM;
  camera_config.pin_sscb_scl = myPins.SIOC_GPIO_NUM;
  camera_config.pin_pwdn = myPins.PWDN_GPIO_NUM;
  camera_config.pin_reset = myPins.RESET_GPIO_NUM;
  camera_config.xclk_freq_hz = 10000000;
  camera_config.pixel_format = PIXFORMAT_JPEG;

  camera_config.frame_size = FRAMESIZE_VGA;
  camera_config.jpeg_quality = 30;  // wysoka jakość
  camera_config.fb_count = 2;

  if (esp_camera_init(&camera_config) != ESP_OK) {
    Serial.println("Błąd inicjalizacji kamery");
    while (true) delay(1000);
  }

  Serial.println("Kamera gotowa.");
}

void loop() {
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("Brak połączenia WiFi");
    delay(1000);
    return;
  }

  camera_fb_t* fb = esp_camera_fb_get();
  if (!fb) {
    Serial.println("Nie udało się pobrać ramki");
    delay(1000);
    return;
  }

  Serial.printf("Zrobiono zdjęcie (%d bajtów), wysyłanie...\n", fb->len);

  WiFiClient client;
  if (!client.connect("192.168.237.109", 5000)) {
    Serial.println("Błąd połączenia z serwerem");
    esp_camera_fb_return(fb);
    delay(3000);
    return;
  }

  String boundary = "----WebKitFormBoundary";
  String head = "--" + boundary + "\r\n";
  head += "Content-Disposition: form-data; name=\"image\"; filename=\"frame.jpg\"\r\n";
  head += "Content-Type: image/jpeg\r\n\r\n";

  String tail = "\r\n--" + boundary + "--\r\n";

  int contentLength = head.length() + fb->len + tail.length();

  // Wysyłanie nagłówka HTTP
  client.printf("POST /qr HTTP/1.1\r\n");
  client.printf("Host: 192.168.237.109\r\n");
  client.printf("Content-Type: multipart/form-data; boundary=%s\r\n", boundary.c_str());
  client.printf("Content-Length: %d\r\n", contentLength);
  client.printf("Connection: close\r\n\r\n");

  // Wysyłanie ciała zapytania
  client.print(head);
  client.write(fb->buf, fb->len);
  client.print(tail);

  // Pobranie odpowiedzi
  while (client.connected()) {
    String line = client.readStringUntil('\n');
    if (line == "\r") break;
  }

  String response = client.readString();
  Serial.println("Odpowiedź serwera:");
  Serial.println(response);

  esp_camera_fb_return(fb);
  client.stop();
  delay(1000);
}

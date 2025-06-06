// #include <WebServer.h>
// #include <WiFi.h>
// #include <esp32cam.h>

// const char* WIFI_SSID = "Red";
// const char* WIFI_PASS = "12345678";

// WebServer server(80);

// static auto hiRes = esp32cam::Resolution::find(800, 600);

// class FrameBuffer {
//   public:
//     std::vector<uint8_t> data;
//     int width = 0;
//     int height = 0;
//     portMUX_TYPE mux = portMUX_INITIALIZER_UNLOCKED;

//     void update(const uint8_t* buf, size_t size, int w, int h) {
//       portENTER_CRITICAL(&mux);
//       data.assign(buf, buf + size);
//       width = w;
//       height = h;
//       portEXIT_CRITICAL(&mux);
//     }

//     std::vector<uint8_t> getData() {
//       portENTER_CRITICAL(&mux);
//       auto copy = data;
//       portEXIT_CRITICAL(&mux);
//       return copy;
//     }
// };

// FrameBuffer frameBuffer;

// void captureTask(void* pvParameters) {
//   while (true) {
//     auto frame = esp32cam::capture();
//     if (frame != nullptr) {
//       frameBuffer.update(frame->data(), frame->size(), frame->getWidth(), frame->getHeight());
//     }
//     vTaskDelay(100 / portTICK_PERIOD_MS); // 10 FPS
//   }
// }
// void handleJpg() {
//   auto data = frameBuffer.getData();
//   if (data.empty()) {
//     server.send(503, "text/plain", "No frame");
//     return;
//   }
//   server.setContentLength(data.size());
//   server.send(200, "image/jpeg");
//   WiFiClient client = server.client();
//   client.write(data.data(), data.size());
// }

// void setup() {
//   Serial.begin(115200);
//   using namespace esp32cam;

//   Pins myPins;
//   myPins.PWDN = -1;
//   myPins.RESET = -1;
//   myPins.XCLK = 15;
//   myPins.SDA = 4;
//   myPins.SCL = 5;
//   myPins.D7 = 16;
//   myPins.D6 = 17;
//   myPins.D5 = 18;
//   myPins.D4 = 12;
//   myPins.D3 = 10;
//   myPins.D2 = 8;
//   myPins.D1 = 9;
//   myPins.D0 = 11;
//   myPins.VSYNC = 6;
//   myPins.HREF = 7;
//   myPins.PCLK = 13;

//   Config cfg;
//   cfg.setPins(myPins);
//   cfg.setResolution(hiRes);
//   cfg.setBufferCount(2);
//   cfg.setJpeg(60);

//   bool ok = Camera.begin(cfg);
//   Serial.println(ok ? "CAMERA OK" : "CAMERA FAIL");

//   WiFi.persistent(false);
//   WiFi.mode(WIFI_STA);
//   WiFi.begin(WIFI_SSID, WIFI_PASS);
//   while (WiFi.status() != WL_CONNECTED) {
//     delay(500);
//     Serial.print(".");
//   }
//   Serial.println("\nPołączono z WiFi!");
//   Serial.print("http://");
//   Serial.println(WiFi.localIP());
//   Serial.println("  /cam.jpg");

//   server.on("/cam.jpg", handleJpg);
//   server.begin();

//   xTaskCreatePinnedToCore(captureTask, "captureTask", 4096, NULL, 1, NULL, 1); // Core 1
// }

// void loop() {
//   server.handleClient();
// }

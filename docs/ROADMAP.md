# OM Mobile Roadmap

## 0.1 — Safe launcher prototype

### Alpha 1: Foundation

- [x] Android home-app registration
- [x] OM visual system
- [x] Clock and status surface
- [x] Command bar
- [x] Searchable installed-app launcher
- [x] Safe intents for core phone applications
- [x] Network overview
- [x] Sensor inventory
- [x] Automated debug APK build

### Alpha 2: Live Field Tools

- [ ] Live accelerometer and gyroscope readouts
- [ ] Compass with calibration guidance
- [ ] Barometer, light, and proximity readouts
- [ ] GPS page with explicit, just-in-time location permission
- [ ] Connection detail and DNS diagnostics
- [ ] Safe ping/latency test
- [ ] Shareable diagnostic report with sensitive fields redacted by default

### Alpha 3: Launcher usability

- [ ] Pinned favorites
- [ ] Recent applications
- [ ] Workspaces: Home, Comms, Field, System
- [ ] User-defined commands and aliases
- [ ] Theme presets and wallpaper controls
- [ ] Notification summary
- [ ] Accessibility and large-text pass

### Alpha 4: Daily-driver trial

- [ ] Crash reporting that stores reports locally
- [ ] Backup and restore OM settings
- [ ] Battery-use review
- [ ] Offline behavior review
- [ ] One-week field test on Android 14

## 0.2 — LineageOS trial

Only after 0.1 proves useful and the phone is retired from primary duty:

- Back up and verify all phone data.
- Verify OEM unlocking availability and exact carrier/device status.
- Record baseline calls, SMS, mobile data, Wi-Fi, Bluetooth, NFC, GPS, camera, audio, charging, and sensor behavior.
- Unlock and flash only from the official device instructions current at that time.
- Install OM Mobile as the default home environment.
- Repeat the baseline hardware test and document regressions.

## Later experiments

- Deeper LineageOS/SystemUI integration.
- OM control center and lock-screen experiments.
- A separate mobile-Linux research branch when `bramble` hardware support is mature enough.

The governing rule remains: phone first, Linux second. Preserve reliable calling, camera, connectivity, charging, and recovery before pursuing deeper control.

# OM Mobile Roadmap

The long-term direction is a local-first, modular field computer: the Pixel supplies the interface, sensors, storage, maps, and ordinary connectivity; optional BLE, USB, and TCP modules supply radio capabilities that stock Android cannot expose.

See [Modular Field Computer Concept](MODULAR_FIELD_COMPUTER.md).

## 0.1 — Safe launcher and field-tools prototype

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
- [x] Modular field-computer concept and commercial guardrails

### Alpha 2: Live Field Tools

- [ ] Live accelerometer and gyroscope readouts
- [ ] Compass with calibration guidance
- [ ] Barometer, light, and proximity readouts
- [ ] GPS page with explicit, just-in-time location permission
- [ ] Connection detail and DNS diagnostics
- [ ] Safe ping and latency tests
- [ ] Shareable diagnostic report with sensitive fields redacted by default
- [ ] Permission education screen with useful permission-free fallbacks
- [ ] Foreground session state with clear start, stop, and data-retention controls

### Alpha 2.5: Field Observatory

- [ ] Manual Wi-Fi access-point survey within Android scan limits
- [ ] Time-bounded BLE survey with nearby-device permission
- [ ] Cellular environment and signal-history screen
- [ ] Android unknown-tracker safety guidance
- [ ] First-seen, last-seen, RSSI, and confidence display
- [ ] Explicit distinction between observed, matched, suspicious, and confirmed
- [ ] Battery and scan-throttling measurements on the Pixel 4a (5G)

### Alpha 2.6: Privacy and evidence

- [ ] Common observation record shared by phone and companion modules
- [ ] Local session database
- [ ] Optional location per session
- [ ] Identifier hashing and redacted exports by default
- [ ] Evidence mode for deliberate raw-data retention
- [ ] JSON, CSV, and GeoJSON exports
- [ ] Tool, firmware, and signature-version metadata in reports
- [ ] Per-session and full-history deletion
- [ ] No background or stealth collection by default

### Alpha 2.7: Companion Bridge

- [ ] Adapter interface for BLE, USB, and TCP modules
- [ ] Simulated module for repeatable tests
- [ ] Android USB-host discovery and permission flow
- [ ] Line-delimited JSON ingestion
- [ ] Module health, firmware, power, and disconnect handling
- [ ] First ESP32-S3 OM Field Node prototype
- [ ] Flock-You USB JSON compatibility prototype
- [ ] Confidence-tier visualization and signature-pack versioning

### Alpha 2.8: Communications and specialist tools

- [ ] Meshtastic launch and connection-status integration
- [ ] Evaluate native Meshtastic adapter without copying GPL client code
- [ ] Rayhunter dashboard shortcut and report-import experiment
- [ ] PCAP attachment and external-tool handoff
- [ ] Research USB SDR and environmental-sensor modules
- [ ] Document verified, experimental, and unsupported hardware tiers

### Alpha 3: Launcher usability

- [ ] Pinned favorites
- [ ] Recent applications
- [ ] Workspaces: Home, Comms, Field, System
- [ ] User-defined commands and aliases
- [ ] Commands for `survey`, `cell`, `mesh`, `rayhunter`, and `report`
- [ ] Theme presets and wallpaper controls
- [ ] Notification summary
- [ ] Accessibility and large-text pass

### Alpha 4: Daily-driver trial

- [ ] Crash reporting that stores reports locally
- [ ] Backup and restore OM settings
- [ ] Battery-use review
- [ ] Offline behavior review
- [ ] One-week field test on Android 14
- [ ] Permission-denial and recovery testing
- [ ] External-module attach, detach, and low-power testing

## 0.2 — LineageOS trial

Only after 0.1 proves useful and the phone is retired from primary duty:

- Back up and verify all phone data.
- Verify OEM unlocking availability and exact carrier/device status.
- Record baseline calls, SMS, mobile data, Wi-Fi, Bluetooth, NFC, GPS, camera, audio, charging, and sensor behavior.
- Unlock and flash only from the official device instructions current at that time.
- Install OM Mobile as the default home environment.
- Repeat the baseline hardware test and document regressions.
- Evaluate privileged integration only when it produces a measurable benefit.
- Do not assume a custom ROM grants Wi-Fi monitor mode or protected baseband access.

## Commercial validation track

This track runs beside technical development and does not delay the safe prototype.

- [ ] Select an original working product name after trademark screening
- [ ] Choose an explicit OMobileOS license before accepting outside contributions
- [ ] Inventory and document all third-party licenses and attribution
- [ ] Measure OM Field Node bill of materials and assembly time
- [ ] Create repeatable capability and false-positive tests
- [ ] Conduct a small technical-user pilot
- [ ] Interview potential technician, privacy, expedition, and education users
- [ ] Validate willingness to pay before setting a retail price
- [ ] Define warranty, returns, support, privacy, and safe-use policies
- [ ] Review radio-equipment, battery, and product-liability obligations before hardware sales
- [ ] Make only advertising claims supported by recorded test evidence

## Later experiments

- Deeper LineageOS/SystemUI integration
- OM control center and lock-screen experiments
- Opt-in on-device IP traffic inspection using `VpnService`
- Additional ESP32, Meshtastic, Rayhunter, SDR, and environmental modules
- A separate mobile-Linux research branch when `bramble` hardware support is mature enough
- A module SDK after the observation record and adapter contract are stable

The governing rule remains: **phone first, Linux second; evidence before claims; usefulness before complexity.**

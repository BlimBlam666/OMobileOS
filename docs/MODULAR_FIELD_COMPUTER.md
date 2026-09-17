# OMobileOS Modular Field Computer Concept

## Vision

OMobileOS is an Omarchy-inspired, command-first mobile environment that turns an ordinary Android phone into a modular field computer: a pocket instrument for sensing, diagnostics, communications, privacy awareness, and technical support.

The design is inspired by the usefulness of a science-fiction tricorder, but the product must use an original name, visual identity, and terminology. "OMobileOS" is the project name; "OM Field Console" and "OM Field Node" are working component names until a trademark search and brand decision are completed.

OMobileOS begins as a safe Android launcher and application suite on the Google Pixel 4a (5G), codename `bramble`. It does not require root, an unlocked bootloader, or a custom ROM. Deeper system integration remains a later option after the phone is retired from primary duty.

## Governing principles

1. **Phone first, Linux second.** Calling, camera, connectivity, charging, recovery, and ordinary Android use must remain dependable.
2. **Modular by design.** Use the phone's APIs where they are sufficient and external radios where Android or the hardware cannot observe the needed signal.
3. **Local first.** Observations, maps, reports, and settings remain on the device unless the user deliberately exports them.
4. **Permission by purpose.** Request a sensitive permission only when the corresponding tool is opened and explain the benefit in plain language.
5. **Evidence, not certainty theater.** Report observation source, method, freshness, and confidence. Never present a heuristic as proof of surveillance, compromise, or tracking.
6. **Passive by default.** Active network tests must be deliberate and limited to systems the user is authorized to test.
7. **Useful without accessories.** Core sensors, network diagnostics, app launching, and device status must work on the phone alone.
8. **Replaceable modules.** Hardware and protocol adapters must be isolated so a new board or project can be added without rebuilding the entire application.

## System architecture

### 1. OM Home and Command

- Minimal home screen and status surface
- Searchable application launcher
- Command palette and aliases
- Workspaces for Home, Comms, Field, and System
- Fast commands such as `sensors`, `survey`, `cell`, `mesh`, `rayhunter`, and `report`

### 2. OM Field Tools

Phone-native instruments:

- accelerometer, gyroscope, compass, barometer, light, and proximity
- GPS position, heading, speed, altitude, and accuracy
- Wi-Fi access-point survey within Android scan limits
- Bluetooth Low Energy survey
- cellular environment and signal history
- DNS, route, latency, gateway, and connection diagnostics
- Android unknown-tracker safety guidance
- optional, user-approved IP traffic inspection through Android `VpnService`

Relevant Android boundaries:

- Wi-Fi scans expose access-point results but are permission-gated and throttled: <https://developer.android.com/develop/connectivity/wifi/wifi-scan>
- BLE scanning requires nearby-device permission and time-bounded scan behavior: <https://developer.android.com/develop/connectivity/bluetooth/ble/find-ble-devices>
- cellular APIs expose serving and neighboring cell information, not raw baseband control traffic: <https://developer.android.com/reference/android/telephony/TelephonyManager>
- `VpnService` can observe routed IP packets but not raw 802.11 frames or cellular control messages: <https://developer.android.com/reference/android/net/VpnService>

### 3. OM Companion Bridge

A shared adapter layer connects external instruments over BLE, USB, or TCP.

Each adapter converts its source data into a common observation record:

- timestamp and source module
- protocol and observation type
- ephemeral or redacted identifier
- signal strength, channel, and frequency when available
- optional location and accuracy
- matched signature and signature version
- confidence tier and reason
- raw-source reference when retained
- user notes and export state

Android USB host support makes phone-to-accessory communication practical: <https://developer.android.com/develop/connectivity/usb/host>

### 4. OM Field Node

A small ESP32-S3 accessory handles radio work that stock Android cannot perform reliably.

Initial target:

- passive 2.4 GHz Wi-Fi management-frame observation
- BLE advertising observation
- local status LED or buzzer
- USB serial JSON stream to OMobileOS
- standalone short-term storage
- signed, versioned signature packs
- no transmission or network authentication in the default privacy-audit firmware

The first integration candidate is the MIT-licensed Flock-You project, which already performs passive ESP32-S3 detection and emits confidence-tiered JSON over USB: <https://github.com/colonelpanichacks/flock-you>

OMobileOS should ingest and visualize these observations rather than claim the Pixel can duplicate promiscuous-mode reception through ordinary Android Wi-Fi APIs.

### 5. Specialist modules

**Rayhunter companion**

EFF Rayhunter runs on supported cellular hotspot hardware with access to cellular control traffic. OMobileOS can display status, retrieve reports and packet captures, attach notes, and maintain a local incident timeline. It cannot turn the protected Pixel baseband into a Rayhunter device.

Project: <https://github.com/EFForg/rayhunter>

**Meshtastic companion**

A Meshtastic LoRa node provides off-grid messaging and telemetry. OMobileOS can initially launch or coordinate with the official Android client, then add a native adapter if licensing and maintenance costs are acceptable.

Project: <https://github.com/meshtastic/Meshtastic-Android>

**Future modules**

- USB software-defined radio receivers
- environmental sensor boards
- air-quality and weather instruments
- thermal or infrared sensors
- NFC and RFID readers that expose documented, lawful interfaces
- authorized network-test adapters
- emergency and expedition communication modules

A module enters the supported list only after capability, power use, Android compatibility, recovery behavior, license, and safe-use boundaries are documented.

## Privacy and evidence model

- No cloud account is required for core operation.
- Background collection is off by default.
- Location collection is explicit per session.
- Stable hardware identifiers are hashed or redacted in ordinary reports.
- Raw identifiers and packet captures require a deliberate evidence-mode choice.
- Exports include tool version, signature version, permissions, scan limitations, and confidence explanations.
- Detection rules are versioned and testable.
- The user can delete individual sessions or all local observations.
- No stealth-monitoring feature is permitted.

## Product and revenue concept

The project should validate usefulness before attempting custom phone manufacturing.

### Community product

- free or open core launcher
- basic sensors and diagnostics
- documented companion protocol
- community hardware compatibility list
- reproducible builds and local data ownership

### Sellable products

1. **OM Field Node** — an assembled, tested, and preconfigured ESP32-S3 companion.
2. **OM Field Kit** — Field Node, cables, compact power solution, mounting or case components, and a verified OMobileOS build.
3. **OM Technician Edition** — polished diagnostic reports, saved test profiles, adapter management, and professional support while remaining local-first.
4. **Training and workshops** — practical wireless-awareness, field diagnostics, Linux, and authorized network-analysis instruction.
5. **Integration and support** — paid compatibility work for organizations, researchers, technicians, and community groups.
6. **Later hardware partnerships** — verified bundles with Meshtastic, Rayhunter-compatible, sensor, or SDR hardware without relabeling third-party capabilities as our own.

The best early revenue path is assembled hardware, documentation, training, and support—not charging for unvalidated detector promises.

## Commercial guardrails

- Use an original commercial name and artwork; do not market the product using Star Trek branding.
- Complete a professional trademark search before accepting money under a final product name.
- Preserve third-party attribution and license boundaries. Flock-You is MIT-licensed; Rayhunter and Meshtastic Android use GPL-3.0. Launching or interoperating with GPL applications is cleaner than copying their clients into a differently licensed product.
- Treat radio-equipment authorization, battery safety, product liability, returns, warranty, and privacy disclosures as release gates before selling assembled hardware.
- Performance and safety claims require objective evidence. The FTC states that advertising must be truthful, non-deceptive, and supported by evidence: <https://www.ftc.gov/business-guidance/resources/advertising-faqs-guide-small-business>
- Detection interfaces must distinguish observed, matched, suspicious, and confirmed. OMobileOS should never advertise that it guarantees detection of every camera, tracker, or cell-site simulator.

## Validation path

1. Prove the phone-only Field Tools on the existing Pixel without root or flashing.
2. Run a one-week daily-driver test and measure crashes, battery use, permission friction, and diagnostic usefulness.
3. Build the common observation model and a simulated companion adapter.
4. Integrate one real ESP32-S3 Field Node over USB.
5. Conduct repeatable bench and field tests with known devices and record false positives and false negatives.
6. Pilot with a small group of technical users.
7. Price only after bill of materials, assembly time, support burden, failure rate, and willingness to pay are measured.
8. Expand to Rayhunter and Meshtastic only after the core adapter system is stable.

## Current product statement

> OMobileOS is a local-first, modular field computer that combines an Android phone's sensors and connectivity with optional external instruments for diagnostics, communication, and privacy awareness.

This statement describes what the project can honestly become without promising capabilities that the phone or accessory has not yet proved.

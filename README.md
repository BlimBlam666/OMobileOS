# OM Mobile 0.1 — Bramble

An Omarchy-inspired, command-first Android home environment for the Google Pixel 4a (5G), device codename `bramble`.

OM Mobile 0.1 is deliberately a launcher, not a custom ROM. It can be installed and removed like a normal Android application. It does not require an unlocked bootloader, root access, a factory reset, or replacing Android 14.

## Direction: modular field computer

OMobileOS is growing toward a local-first, modular field computer: a pocket instrument for sensing, diagnostics, communication, privacy awareness, and technical support.

The Pixel provides the interface, ordinary connectivity, sensors, maps, storage, and reports. Optional BLE, USB, and TCP accessories provide capabilities that stock Android cannot safely expose, including raw Wi-Fi observation, supported Rayhunter cellular analysis, Meshtastic LoRa communication, and future environmental or SDR instruments.

The system remains useful without accessories and preserves the rule: **phone first, Linux second; evidence before claims; usefulness before complexity.**

Read the full [Modular Field Computer Concept](docs/MODULAR_FIELD_COMPUTER.md) and [development roadmap](docs/ROADMAP.md).

## Alpha 1 checkpoint

The current source includes:

- A minimal OM Home screen with clock, date, network link, local IPv4 address, internet validation, battery, and charging status.
- A command bar that opens applications and recognizes OM commands.
- A searchable launcher using the phone's installed launchable applications.
- Quick access to browser, phone, terminal, camera, files, and Field Tools.
- A Field Tools foundation that inventories the sensors Android reports on the device.
- A system screen with device, power, network, app, and sensor summaries.
- A GitHub Actions workflow that builds a downloadable debug APK after every push to `main`.

## Commands

Enter these in the OM command bar:

| Command | Result |
| --- | --- |
| `apps` or `launch` | Open the application launcher |
| `browser` or `web` | Open the default browser |
| `web example.com` | Open a specific site |
| `phone` or `call` | Open the phone dialer |
| `camera` | Open the camera |
| `messages` or `sms` | Open messaging |
| `files` | Open the Android document picker |
| `terminal` or `term` | Open Termux if installed |
| `field`, `tools`, or `sensors` | Open Field Tools |
| `system`, `status`, or `ip` | Open system status |
| `wifi` | Open Android Wi-Fi settings |
| `settings` | Open Android settings |
| any exact app name | Launch that installed app |

## Build with GitHub

1. Create an empty GitHub repository and add this project to it.
2. Push the project to the `main` branch.
3. Open the repository's **Actions** tab and select **Build OM Mobile APK**.
4. Open the successful run and download the `om-mobile-0.1-debug` artifact.
5. Unzip the artifact to get `app-debug.apk`.

## Install safely on the Pixel

1. Keep your important phone data backed up as usual. This launcher should not erase anything, but backups remain wise.
2. Transfer `app-debug.apk` to the Pixel.
3. Open it and allow **Install unknown apps** for the file-handling app Android identifies.
4. Install OM Mobile.
5. Press Home. Android should ask which home app to use.
6. Choose **OM Mobile** and initially select **Just once** while testing.

To return to the existing launcher, open **Settings → Apps → Default apps → Home app** and select the previous launcher. OM Mobile can also be uninstalled normally.

## Local Android Studio build

Open the repository root in a current Android Studio release with Android SDK 35 installed. Allow Gradle to sync, select the `app` configuration, and run it on the attached Pixel or an Android emulator.

The project uses JDK 17, Gradle 8.9, Android Gradle Plugin 8.7.3, Kotlin 2.0.21, and Jetpack Compose.

## Permission posture

Alpha 1 asks only for network-state and internet access. It does not request location, contacts, phone-call, SMS, camera, microphone, storage, Bluetooth, NFC, or notification permissions. Existing Android applications continue performing those jobs.

Future Field Tools features will introduce permissions one at a time, with a plain-language explanation and a useful permission-free fallback wherever possible. Background or stealth collection is not part of the design.

## Project boundary

OM Mobile is an independent, experimental project inspired by command-driven Linux desktop ideas and the usefulness of a science-fiction field instrument. It is not an official Omarchy or Star Trek project and does not use their branding.

OM Mobile does not yet modify LineageOS, Android SystemUI, the bootloader, or the Pixel firmware. Detection features must report their source, method, limitations, and confidence rather than presenting a heuristic as certainty.

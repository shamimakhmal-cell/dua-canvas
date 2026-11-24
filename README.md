# DuaCanvas - Islamic Dua & Poster Maker

DuaCanvas is a premium, offline-first Android application built with Jetpack Compose for browsing authentic Islamic duas, multilingual search (Arabic, Bangla, English, transliteration), saving custom collections, and designing/exporting professional Islamic posters (PNG and A4 PDF).

## App Information
- **App Name**: DuaCanvas
- **Package Name**: `com.duacanvas.app`
- **Min SDK**: 24 (Android 7.0+)
- **Target SDK**: 36

---

## Features
1. **Explore & Categories**: Daily Duas, Morning & Evening, Salah, Rizq & Debt, Protection, Forgiveness, Family, Ramadan, Travel, Sleep, Quranic Duas.
2. **Authentic Dua Details**: Arabic typography (RTL), Bangla pronunciation, Bangla meaning, English meaning, source reference, authenticity badges (`✓ Quran`, `✓ Sahih Hadith`, `✓ Hasan Hadith`, `✦ User Provided`), and Fazilat (virtues).
3. **Multilingual Search**: Instant search across Arabic, Bangla Unicode, English, and Bangla transliteration.
4. **Poster Studio & AI Modes**:
   - Templates (12 professional Islamic designs)
   - Fonts & Text controls
   - Format & Layout (A4 Portrait, A4 Landscape, 1080×1080, 1080×1350, 1080×1920)
   - ✨ AI Design & Customize control panel
   - 🎨 AI Poster Generator ("Create with AI" natural language poster design)
5. **Export & Sharing**: High-resolution PNG export, A4 PDF generation (300 DPI equivalent), and Print Preview.
6. **Offline First**: Room database pre-populated with verified duas, favorites, and custom collections.

---

## Build Instructions (Generate APK)

To build the APKs using Gradle:

### 1. Build Debug APK
Run the following command in the project root:
```bash
gradle assembleDebug
```
The generated Debug APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

### 2. Build Release APK
Run the following command in the project root:
```bash
gradle assembleRelease
```
The generated Release APK will be located at:
`app/build/outputs/apk/release/app-release.apk`

---

## Installation on Android Phone
1. Transfer `app-debug.apk` or `app-release.apk` to your Android device.
2. Enable "Install from unknown sources" in your device settings.
3. Tap the APK file to install **DuaCanvas**.

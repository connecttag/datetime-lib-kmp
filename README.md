# ConnectTag DateTime Library (datetime-lib-kmp)

[![JitPack](https://jitpack.io/v/connecttag/datetime-lib-kmp.svg)](https://jitpack.io/#connecttag/datetime-lib-kmp)

مكتبة معالجة وصياغة التواريخ والأوقات والفترات الزمنية والمناطق الزمنية الموجهة لجميع منصات **Kotlin Multiplatform**:
- **Android** (API 24+)
- **Desktop JVM** (Windows, macOS, Linux)
- **iOS** (Arm64, SimulatorArm64)
- **Web / WasmJs**

---

## 📦 التثبيت والإضافة للمشروع (Installation via JitPack)

### 1. إضافة مستودع JitPack في `settings.gradle.kts`:
```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### 2. إضافة التبعية في `build.gradle.kts`:
```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // For Kotlin Multiplatform:
            implementation("com.github.connecttag.datetime-lib-kmp:datetime-lib-kmp:1.0.0")
        }
    }
}
```

الحزمة الأساسية للمكتبة: `org.connecttag.lib.datetime.*`

---

## 🌟 الميزات الأساسية

1. **`PlatformDateTimeMappers` & `EpochTime`:**
   - تحويل ذكي بين الطوابع الزمنية (Epoch Milliseconds / Seconds) وصيغ ISO-8601.
   - دعم التنسيقات المرنة وتنسيقات السيرفر والتقويم المائل (`YYYY/MM/DD`).
2. **`UiDateTimeFormatter`:**
   - صياغة التواريخ والوقت لواجهات المستخدم وفق لغة التطبيق وموقعه الجغرافي (Locale-aware).
   - صياغة الفترات الزمنية النسبية الذكية (Relative Time Formatting: "الآن"، "منذ دقيقة"، "أمس"، إلخ).
3. **`GreetingTimePolicy` & `HourRangePolicy`:**
   - تحديد فترات اليوم والتحيات التلقائية (صباح الخير، مساء الخير) حسب التوقيت والموقع.

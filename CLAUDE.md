# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Coding Guidelines

Follow the conventions in @Coding_Guidelines.md.

## Project

Tidings (a fork of Fossify Messages): an offline SMS/MMS app for Android (`com.rajatroy.tidings`), single Gradle module `:app`, Kotlin + View Binding (no Compose; the Compose detekt rules are only there as a lint plugin). Depends on the shared `org.fossify:commons` library (`libs.fossify.commons`) for base activities, dialogs, theming and helpers, and `org.fossify:mmslib` for MMS sending. Versions live in `gradle/libs.versions.toml`; `VERSION_NAME`/`VERSION_CODE`/`APP_ID` live in `gradle.properties`. minSdk 26, Java/Kotlin target 17.

## Commands

```
./gradlew assembleFossDebug          # build debug APK (flavors: foss, gplay; debug adds ".debug" appId suffix)
./gradlew assembleGplayDebug
./gradlew detekt                     # static analysis (config: detekt.yml, baseline: app/detekt-baseline.xml)
./gradlew lintFossDebug              # Android lint (config: lint.xml, baseline: app/lint-baseline.xml)
./gradlew clean
```

- There are no unit or instrumented tests in the repo (`app/src/test` and `app/src/androidTest` don't exist). PR CI is delegated to the shared workflow `FossifyOrg/.github/.github/workflows/pr.yml`.
- Release builds are minified and signed only if `keystore.properties` (see `keystore.properties_sample`) or the `SIGNING_*` env vars are present; otherwise they are unsigned.
- Formatting: `.editorconfig` (4 spaces, max line length 160, LF). Detekt uses `buildUponDefaultConfig`, so new code must not add findings beyond the baseline.
- `CHANGELOG.md` follows Keep a Changelog; add user-facing changes under `## [Unreleased]`. `.tidings/release-marker.txt` is auto-generated, don't edit it.
- Translations (`values-*/strings.xml`) are updated by the translation service (`chore(l10n)` commits); only edit the default `values/strings.xml`.
- `foss` and `gplay` flavors differ only in `src/{foss,gplay}/res/values/bools.xml`; there is no flavor-specific Kotlin.

## Architecture

All code is under `app/src/main/kotlin/com/rajatroy/tidings/`.

**Two sources of truth.** The Android system SMS/MMS provider (`content://sms`, `content://mms`) is the canonical message store; the app reads and writes it directly (`extensions/Context.kt` has `getMessages`, `getConversations`, `getThreadParticipants`, etc.; `helpers/MessagesReader.kt`/`MessagesWriter.kt` are used for backup import/export). A Room database (`databases/MessagesDatabase.kt`, DB name `conversations.db`, DAOs in `interfaces/`) is a local cache/extension layer: cached conversations, messages, attachments, drafts, and the recycle bin (`RecycleBinMessage`) and archive state, which the system provider has no notion of. Accessors are `Context.conversationsDB` / `messagesDB` etc. in `extensions/Context.kt`. When changing a Room entity, bump `version` and add a `MIGRATION_x_y` in `MessagesDatabase.getInstance`; note `fallbackToDestructiveMigration()` is on, so a missing migration silently wipes the cache instead of crashing.

**Sending pipeline** (`messaging/`): `Context.sendMessageCompat` in `Messaging.kt` decides SMS vs MMS (attachments, long text, or group with the group-MMS setting → MMS). SMS goes through `SmsSender`/`SmsManager` wrappers; MMS goes through `mmslib` (`getSendMessageSettings()` is marked deprecated pending a rewrite). `SendingErrors.kt`/`SmsException` map failures to user-facing messages. Scheduled messages are in `ScheduledMessage.kt` and fired via `ScheduledMessageReceiver` / `RescheduleAlarmsReceiver`.

**Receiving / system integration** (`receivers/`, `services/`): `SmsReceiver`, `MmsReceiver`, the `*Sent`/`*Delivered` status receivers, and notification action receivers (`DirectReplyReceiver`, `MarkAsReadReceiver`, `DeleteSmsReceiver`) are manifest-registered; `HeadlessSmsSendService` handles respond-via-message for default-SMS-app behavior. Incoming handling shares logic in `helpers/ReceiverUtils.kt`; notifications are in `helpers/NotificationHelper.kt`. Blocked numbers/keywords filtering happens on receive.

**UI** (`activities/`, `adapters/`, `dialogs/`): `MainActivity` (conversation list + search), `ThreadActivity` (a conversation; by far the largest file), plus archived/recycle-bin/new-conversation/settings/details/vCard activities. Most extend `SimpleActivity` → commons' base activity. Activities and background work communicate via **EventBus** (`models/Events.kt` defines the event types; activities register on `EventBus.getDefault()` while visible), so a data change in a receiver/background thread refreshes the UI by posting an event, not by observing Room. Background work uses commons' `ensureBackgroundThread`, not coroutines/ViewModels.

**Caching** (`helpers/MessagingCache.kt`): in-memory LRU caches for contact name/photo and thread participants. Participants entries are validated against the recipient number list; call `remove`/`evictAll` when contacts or threads change.

**Settings**: `helpers/Config.kt` (accessed as `context.config`) wraps SharedPreferences on top of commons' `BaseConfig`. Constants (intents extras, request codes, limits) are in `helpers/Constants.kt`.

**Backup/import-export**: `models/*Backup.kt` + `ExportedMessage`/`MessagesBackup` are kotlinx-serialization models for the JSON message backup; `MessagesImporter`/`MessagesReader`/`MessagesWriter` handle it. Blocked keywords have their own `BlockedKeywordsExporter`/`Importer`.

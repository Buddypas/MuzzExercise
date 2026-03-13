# ChatApp

A single-screen chat application for Android, built as a technical exercise. It showcases **Jetpack Compose**, **MVI architecture**, and **Clean Architecture** principles.

The app simulates a two-user messaging interface with message grouping, time separators, and delivery indicators — closely replicating the feel of a real chat experience. It uses a local Room database as the data source, keeping the scope focused on UI, state management, and architecture.

## Features

- **Real-time chat UI** — sent/received message bubbles with custom styling
- **Smart message grouping** — messages from the same sender are visually grouped, with bubble tails shown based on timing and sender changes
- **Time separators** — automatically inserted when there's a gap of more than 1 hour between messages
- **User switching** — tap the menu icon to swap between two users and send messages as either one
- **Persistent storage** — all messages are saved locally with Room and survive app restarts
- **Delivery indicators** — checkmark icons on sent messages

## Architecture

The project follows **Clean Architecture** with three distinct layers and an **MVI** (Model-View-Intent) pattern for unidirectional data flow.

A key design choice is the separation of **ViewModel State** (the actual data state of the screen) from **UI State** (the mapped representation consumed by Compose). This concept, inspired by Google's architecture samples, keeps the ViewModel focused on business logic while the UI layer receives only what it needs to render.

A generic `BaseViewModel<State, UiState, UiEvent>` enforces the MVI contract, providing:
- Internal state management
- State-to-UI-state mapping
- A typed event sink for user interactions

```
presentation/         UI components, ViewModel, MVI contracts
├── ChatActivity      Single Activity entry point
├── ChatViewModel     State management with MVI pattern
├── ChatScreen        Main composable
├── MessageList       Chat bubble composables
└── MessageInputField Text input with send button

domain/               Pure Kotlin business logic
├── models/           User, Message
└── repositories/     Repository interfaces

data/                 Room database, DAOs, DTOs, mappers
├── database/         Room DB with seed data callback
├── entities/         UserDto, MessageDto
├── mappers/          DTO ↔ Domain model extensions
└── repositories/     Repository implementations
```

## Tech Stack

| Layer | Technology |
|-------|------------|
| UI | Jetpack Compose, Material Design 3 |
| Architecture | MVI (Model-View-Intent), Clean Architecture |
| DI | Hilt |
| Database | Room |
| Image Loading | Coil |
| Async | Kotlin Coroutines + Flow |
| Date/Time | kotlinx-datetime |

## Design Decisions

- **ViewModel State vs UI State** — separating these two concerns prevents the UI from depending on internal implementation details and makes the ViewModel easier to test.
- **Generic BaseViewModel** — enforces a consistent MVI contract across the app, reducing boilerplate and making it easy to add new screens with the same pattern.

## Building

Open the project in Android Studio and run it on a device or emulator (API 24+).

```bash
./gradlew assembleDebug
```

### Requirements

- Android Studio Ladybug or newer
- JDK 21
- Min SDK 24 (Android 7.0)

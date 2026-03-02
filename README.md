# Muzz Chat Screen

A chat screen implementation for Android, built in 2 days as a technical exercise as a part of a hiring process. 
The app simulates a two-user messaging interface with message grouping, time separators, and delivery indicators — closely replicating the feel of a real dating app chat experience.
I used it to play around with the concept of a ViewModel State: the actual state of the data of a screen which then gets mapped to a UI state.
This idea was found in one of Google's samples (possibly Jetcaster but not sure).

## Features

- **Real-time chat UI** with sent/received message bubbles and custom styling
- **Smart message grouping** — messages from the same sender are visually grouped, with bubble tails shown based on timing and sender changes
- **Time separators** — automatically inserted when there's a gap of more than 1 hour between messages
- **User switching** — tap the menu icon to swap between two users (John and Sarah) and send messages as either one
- **Persistent storage** — all messages are saved locally with Room and survive app restarts
- **Delivery indicators** — checkmark icons on sent messages

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose, Material Design 3 |
| Architecture | MVI (Model-View-Intent), Clean Architecture |
| DI | Hilt |
| Database | Room |
| Image Loading | Coil |
| Async | Kotlin Coroutines + Flow |
| Date/Time | kotlinx-datetime |

## Architecture

The project follows Clean Architecture with three distinct layers:

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

A generic `BaseViewModel<State, UiState, UiEvent>` enforces the MVI contract across the app, providing unidirectional data flow through internal state, mapped UI state, and a user event sink.

## Building

Open the project in Android Studio and run it on a device or emulator (API 24+).

```bash
./gradlew assembleDebug
```

## Requirements

- Android Studio Ladybug or newer
- JDK 21
- Min SDK 24 (Android 7.0)

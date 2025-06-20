# Flix Train Maintenance Tracker App

A task management app for tracking and syncing train maintenance tasks using clean architecture and MVVM in Kotlin.

<table align="center">
  <tr>
    <td align="center">
      <div><strong>Dark Mode</strong></div>
      <img src="![OnlineMode.png](OnlineMode.png)" width="300" alt="Online Mode" />
    </td>
    <td align="center">
      <div><strong>Light Mode</strong></div>
      <img src="![OfflineMode.png](OfflineMode.png)" width="300" alt="Offline Mode" />
    </td>
  </tr>
</table>

## 🧱 Architecture

- **MVVM Pattern**
- **Clean Architecture**
    - `data`: Local DB (Room), Remote API, Repository implementation
    - `domain`: Business logic, models, use cases
    - `presentation`: UI, ViewModels, Navigation

## 🔧 Tech Stack

- Kotlin
- Jetpack Compose
- Room Database
- Retrofit
- Hilt (DI)
- Coroutines + Flows

## 📁 Key Modules

- **domain/**
    - `model`, `repository`, `usecase`
- **data/**
    - `local`, `remote`, `repository`, `di`
- **presentation/**
    - `ui`, `viewmodel`, `navigation`, `common`

## 🚀 Features

- View all tasks
- View task details
- Sync tasks with server
- Offline support

Let me know if you want to add images, badges, or usage instructions.
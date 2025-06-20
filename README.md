# Flix Train Maintenance Tracker App

A task management app for tracking and syncing train maintenance tasks using clean architecture and MVVM in Kotlin.

  <div align="center">

  <h3>📹 Watch the Demo Video</h3>

  <a href="https://github.com/FahadKhalid/Flix-Train/blob/main/Flix.webm" target="_blank">
    <img src="https://img.shields.io/badge/Click%20to%20Watch-Video-blue?style=for-the-badge&logo=github" alt="Watch Video">
  </a>

</div>



<table align="center">
  
  <tr>
    <td align="center">
      <div><strong>Online Mode</strong></div>
      <img src="https://github.com/FahadKhalid/Flix-Train/blob/main/Online.png" width="300" alt="Light Mode" />
    </td>
    <td align="center">
      <div><strong>Offline Mode</strong></div>
      <img src="https://github.com/FahadKhalid/Flix-Train/blob/main/Offline.png" width="300" alt="Dark Mode" />
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
- Sync tasks with the server
- Offline support

Thnak you!

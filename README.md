# Handy 🧰

**Handy** is an all-in-one Android application that integrates **Google AI Edge Gallery**, **Termux**, **Winlator**, and **OpenCode AI** into a unified platform — combining on-device AI, a full Linux terminal, Windows app compatibility, and an AI coding agent in a single APK.

---

## ✨ Features

| Component | Role | Source |
|-----------|------|--------|
| **🤖 AI Chat** | On-device LLM inference via LiteRT | [Google AI Edge Gallery](https://github.com/google-ai-edge/gallery) |
| **💻 Terminal** | Full Linux terminal environment | [Termux](https://github.com/termux/termux-app) |
| **🪟 Winlator** | Run Windows x86/x64 apps on Android | [Winlator](https://github.com/brunodev85/winlator) |
| **🧠 OpenCode AI** | AI coding agent (cloud or local) | [OpenCode](https://github.com/anomalyco/opencode) |

### AI Chat (Edge Gallery)
- Run LLMs entirely on-device (Gemma, Llama, Phi, etc.)
- Download models from HuggingFace
- Chat with images, audio, and text
- **Local mode** (Edge Gallery LiteRT) or **Cloud mode** (OpenCode, 75+ providers)

### Terminal (Termux)
- Full Linux environment with bash, Python, Node.js, etc.
- Execute scripts, compile code, install packages via APT
- Multiple terminal sessions
- Integrated with OpenCode for AI-assisted coding

### Winlator
- Run Windows applications on Android
- Wine 9.0 + Box86/Box64 for x86 emulation
- DXVK/VKD3D for DirectX translation
- Customizable container settings

### OpenCode AI
- AI coding agent running in Termux Node.js environment
- Default: **Cloud mode** (Claude, GPT, Gemini, 75+ providers)
- Fallback: **Local mode** via Edge Gallery models when offline
- Code writing, editing, debugging, and task automation

---

## 🏗 Architecture

```
┌─────────────────────────────────────────────────────┐
│                 Handy (App Shell)                     │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌────────┐ │
│  │ AI Chat  │ │ Terminal │ │ Winlator │ │OpenCode│ │
│  │ (Compose)│ │ (Compose)│ │ (Compose)│ │(Compose)│ │
│  └────┬─────┘ └────┬─────┘ └────┬─────┘ └───┬────┘ │
│       │            │            │            │       │
│  ┌────▼────┐ ┌────▼────┐ ┌────▼────┐ ┌─────▼─────┐ │
│  │  Model  │ │ Termux  │ │Winlator │ │ OpenCode   │ │
│  │  Bridge │ │ Bridge  │ │ Bridge  │ │ Bridge     │ │
│  └────┬────┘ └────┬────┘ └────┬────┘ └─────┬─────┘ │
│       │           │           │             │        │
│  ┌────▼───────────▼───────────▼─────────────▼─────┐ │
│  │              Services Layer                     │ │
│  │  (ModelDownload, Termux, Winlator, OpenCode)    │ │
│  └─────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────┘
```

---

## 🔧 Build

### Prerequisites
- Android SDK 35
- NDK 27.0.12077973
- Java 17
- CMake 3.22.1

### Local Build
```bash
git clone https://github.com/dermama/Handy.git
cd Handy
./gradlew :app:assembleDebug
```

### GitHub Actions
The project includes a complete CI/CD pipeline (`.github/workflows/build.yml`):
- Builds for `arm64-v8a`, `armeabi-v7a`, `x86_64`
- Downloads Termux bootstraps and Winlator assets
- Runs lint and tests
- Signs and releases on tag push

---

## 🗂 Project Structure

```
Handy/
├── app/                          # Main application (Edge Gallery fork)
│   ├── bridge/                   # Integration bridges
│   ├── service/                  # Foreground services
│   ├── ui/                       # Jetpack Compose UI screens
│   ├── model/                    # Data models
│   └── di/                       # Hilt DI modules
├── termux-module/                # Termux modules
│   ├── terminal-emulator/
│   ├── terminal-view/
│   └── termux-shared/
├── winlator-core/                # Winlator core engine
│   └── src/main/cpp/             # Native code (CMake)
├── opencode-runtime/             # OpenCode runtime scripts
│   ├── scripts/install-opencode.sh
│   ├── scripts/start-server.sh
│   └── config/opencode.default.json
└── gradle/libs.versions.toml     # Version catalog (60+ deps)
```

---

## 📱 Screens

| Screen | Description |
|--------|-------------|
| **AI Chat** | Multi-turn chat with local/cloud models, image & audio support |
| **Terminal** | Full Linux terminal with multiple sessions |
| **Winlator** | Windows container management and launching |
| **OpenCode AI** | AI coding agent with task execution |
| **Models** | Browse, download, and manage AI models |
| **Settings** | Configure all components |

---

## 📄 License

This project integrates open-source components under their respective licenses:

| Component | License |
|-----------|---------|
| Google AI Edge Gallery | [Apache 2.0](https://github.com/google-ai-edge/gallery/blob/main/LICENSE) |
| Termux | [GPL-3.0](https://github.com/termux/termux-app/blob/master/LICENSE) |
| Winlator | [LGPL-2.1](https://github.com/brunodev85/winlator/blob/main/LICENSE) |
| OpenCode | [MIT](https://github.com/anomalyco/opencode/blob/dev/LICENSE) |

---

## 🌐 Links

- **Repository**: [github.com/dermama/Handy](https://github.com/dermama/Handy)
- **Edge Gallery**: [github.com/google-ai-edge/gallery](https://github.com/google-ai-edge/gallery)
- **Termux**: [github.com/termux/termux-app](https://github.com/termux/termux-app)
- **Winlator**: [github.com/brunodev85/winlator](https://github.com/brunodev85/winlator)
- **OpenCode**: [github.com/anomalyco/opencode](https://github.com/anomalyco/opencode)

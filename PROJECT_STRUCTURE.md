# Project structure

```text
markdown-editor/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/jp/example/markdowneditor/
│   │   │   │   ├── data/       # Document model and local DataStore repository
│   │   │   │   ├── editor/     # ViewModel and Markdown formatting operations
│   │   │   │   ├── ui/         # Editor and preview screens
│   │   │   │   └── MainActivity.kt
│   │   │   └── res/            # Android manifest, strings, and theme resources
│   │   └── test/               # Formatting and repository unit tests
│   └── build.gradle.kts
├── gradle/
│   └── wrapper/
└── README.md
```

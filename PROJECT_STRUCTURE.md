# Project structure

```text
markdown-editor/
├── app/
│   └── src/
│       ├── main/
│       │   ├── java/jp/example/markdowneditor/
│       │   │   ├── data/       # Local document storage
│       │   │   ├── editor/     # Text editing and formatting controls
│       │   │   ├── preview/    # Markdown preview
│       │   │   └── ui/         # Shared UI components
│       │   └── res/
│       │       ├── drawable/
│       │       └── values/
│       ├── androidTest/
│       │   └── java/jp/example/markdowneditor/
│       └── test/
│           └── java/jp/example/markdowneditor/
└── README.md
```

This is a directory scaffold only. It contains no app implementation or build configuration.

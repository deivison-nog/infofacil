# InfoFácil — Informática para todos

App Android nativo em Kotlin com Jetpack Compose para alfabetização digital básica, focado em adultos maiores e pessoas com pouca familiaridade com tecnologia.

## Funcionalidades

- 📖 **6 módulos** com conteúdo educativo real em português
- 🎓 **30 aulas** detalhadas com objetivo, conteúdo, bloco visual e dica prática
- 🧩 **Quiz** ao final de cada módulo com 5 questões e explicações
- 📊 **Acompanhamento de progresso** completo por módulo e geral
- 🏆 **Sistema de conquistas** motivacional
- 📱 **100% offline** — sem login, sem internet necessária
- 💾 Progresso salvo localmente com **DataStore**
- 🎨 Interface com **Material 3**, design acessível e legível

## Módulos disponíveis

| # | Módulo | Emoji |
|---|--------|-------|
| 1 | Primeiros Passos | 🖥️ |
| 2 | Mouse e Teclado | 🖱️ |
| 3 | Arquivos e Pastas | 📁 |
| 4 | Internet Básica | 🌐 |
| 5 | E-mail | ✉️ |
| 6 | Segurança Digital | 🔒 |

## Stack técnica

- **Linguagem**: Kotlin
- **UI**: Jetpack Compose + Material 3
- **Navegação**: Navigation Compose
- **Estado**: StateFlow + ViewModel (AndroidViewModel)
- **Persistência**: DataStore Preferences
- **Mínimo SDK**: 24 (Android 7.0)
- **Target SDK**: 35

## Estrutura do projeto

```
app/src/main/java/com/info85/infofacil/
├── MainActivity.kt
├── data/
│   ├── ProgressRepository.kt
│   └── content/
│       └── AppContent.kt          # Todo o conteúdo educacional
├── model/
│   ├── Module.kt
│   ├── Lesson.kt
│   ├── QuizQuestion.kt
│   └── UserProgress.kt
├── navigation/
│   ├── Routes.kt
│   └── AppNavGraph.kt
└── ui/
    ├── InfoFacilApp.kt
    ├── components/
    │   ├── AccentColors.kt
    │   └── BottomNavBar.kt
    ├── home/
    │   ├── HomeScreen.kt
    │   └── HomeViewModel.kt
    ├── lesson/
    │   ├── LessonScreen.kt
    │   └── LessonViewModel.kt
    ├── modules/
    │   ├── ModuleDetailScreen.kt
    │   ├── ModulesScreen.kt
    │   └── ModulesViewModel.kt
    ├── onboarding/
    │   └── OnboardingScreen.kt
    ├── progress/
    │   ├── ProgressScreen.kt
    │   └── ProgressViewModel.kt
    ├── quiz/
    │   ├── QuizScreen.kt
    │   └── QuizViewModel.kt
    ├── splash/
    │   └── SplashScreen.kt
    └── theme/
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

## Build

### Pré-requisitos

- Android Studio Hedgehog (2023.1.1) ou superior
- JDK 17
- Android SDK com API 35

### Compilar e instalar

```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

### Rodar testes

```bash
./gradlew test
```

## Package

`com.info85.infofacil`

## Logo

- `app/src/main/res/drawable/infofacil_logo.svg`
- `app/src/main/res/drawable/infofacil_logo_foreground.xml`


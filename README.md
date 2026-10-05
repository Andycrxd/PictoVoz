# CIMA Prototype

Prototipo funcional de una aplicación de Comunicación Aumentativa y Alternativa
(CAA) para tablet Android, desarrollado en Kotlin con Jetpack Compose y Material 3.

## Qué incluye

- Barra superior de construcción de frase con límite de 5 pictogramas.
- Agregar pictogramas desde categorías.
- Eliminar pictogramas individuales tocándolos en la barra.
- Botón para borrar toda la frase.
- Generación temporal de frase con `RuleBasedSentenceBuilder`.
- Reproducción de voz en español mediante Android Text-to-Speech.
- Repetición de la última frase.
- Categorías: Necesidades, Emociones, Familia, Acciones, Objetos y Personas.
- Frases rápidas de un toque.
- Recursos locales de pictogramas en `res/drawable`.
- Arquitectura sencilla tipo MVVM, preparada para reemplazar el generador por IA local después.

## Cómo abrir en Android Studio

1. Abre Android Studio.
2. Selecciona **File > Open**.
3. Elige la carpeta `CIMAPrototype`.
4. Acepta la sincronización de Gradle.
5. Si Android Studio lo solicita, instala Android SDK 35.
6. Ejecuta la app en una tablet física o emulador Android API 26 o superior.

La aplicación no requiere Internet durante la ejecución. La primera sincronización
de Gradle puede requerir Internet si Android Studio no tiene las dependencias en caché.

## Estructura principal

```text
app/src/main/java/com/example/cima
├── data
│   ├── model
│   └── repository
├── domain
├── ui
│   ├── components
│   ├── screen
│   └── theme
└── viewmodel
```

## Archivos clave

- `MainActivity.kt`: arranque de la app e inyección sencilla de dependencias.
- `CommunicationViewModel.kt`: estado y comportamiento principal.
- `SentenceBuilder.kt`: contrato para sustituir el generador temporal por IA local.
- `TextToSpeechManager.kt`: administración de Text-to-Speech.
- `HomeScreen.kt`: pantalla principal del prototipo.
- `InMemoryPictogramRepository.kt`: catálogo local inicial sin base de datos.

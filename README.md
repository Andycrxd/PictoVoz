# PictoVoz

PictoVoz es una aplicación Android de Comunicación Aumentativa y Alternativa (CAA) diseñada para facilitar la comunicación mediante pictogramas y síntesis de voz.

## ¿A quién está dirigida?

La aplicación está pensada para personas que presentan dificultades para comunicarse mediante el habla, incluyendo:

- Personas autistas.
- Personas con dificultades del habla.
- Personas con discapacidad de comunicación.
- Personas que requieren apoyo temporal o permanente para expresarse.

## Características

- Construcción de frases mediante pictogramas.
- Límite de 5 pictogramas por frase.
- Síntesis de voz en español.
- Categorías de pictogramas.
- Frases rápidas de un toque.
- Eliminación individual de pictogramas.
- Opción para borrar toda la frase.
- Reproducción de la última frase.
- Interfaz adaptada para tablet Android.
- Funcionamiento sin conexión a Internet durante el uso.

## Tecnologías

- Kotlin
- Android
- Jetpack Compose
- Material 3
- Android Text-to-Speech
- Arquitectura MVVM

## Estado del proyecto

Prototipo funcional desarrollado para una aplicación de Comunicación Aumentativa y Alternativa (CAA).

## Captura de la aplicación

La aplicación permite seleccionar pictogramas, construir frases y reproducirlas mediante síntesis de voz.
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

### 🔑 Configuración de la API Key de Gemini para que funcione la IA

Para habilitar la generación de oraciones mediante IA en la nube, debes configurar tu clave de API localmente:

1. Obten una clave gratuita en [Google AI Studio](https://aistudio.google.com/).
2. En la raíz del proyecto, abre el archivo `local.properties` (si no existe, créalo).
3. Agrega la siguiente línea sustituyendo el valor por tu clave (sin comillas):
   ```properties
   GEMINI_API_KEY=tu_api_key_aqui

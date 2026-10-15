package com.example.cima.data.repository

import com.example.cima.R
import com.example.cima.data.model.Categoria
import com.example.cima.data.model.FraseRapida
import com.example.cima.data.model.Pictogram

class InMemoryPictogramRepository : PictogramRepository {

    private val categories = listOf(
        Categoria("needs", "Necesidades"),
        Categoria("emotions", "Emociones"),
        Categoria("family", "Familia"),
        Categoria("actions", "Acciones"),
        Categoria("objects", "Objetos"),
        Categoria("people", "Personas")
    )

    private val pictograms = listOf(
        // NECESIDADES (needs)
        Pictogram("needs_water", "Agua", "needs", R.drawable.ic_water),
        Pictogram("needs_food", "Comida", "needs", R.drawable.ic_food),
        Pictogram("needs_bathroom", "Baño", "needs", R.drawable.ic_bathroom),
        Pictogram("needs_sleep", "Dormir", "needs", R.drawable.ic_sleep),
        Pictogram("needs_help", "Ayuda", "needs", R.drawable.ic_help),
        Pictogram("needs_pain", "Dolor", "needs", R.drawable.ic_pain),
        Pictogram("needs_rest", "Descanso", "needs", R.drawable.ic_sleep),
        Pictogram("needs_medicine", "Medicina", "needs", R.drawable.ic_medicine),

        // EMOCIONES (emotions)
        Pictogram("emotions_happy", "Feliz", "emotions", R.drawable.ic_happy),
        Pictogram("emotions_sad", "Triste", "emotions", R.drawable.ic_sad),
        Pictogram("emotions_angry", "Enojado", "emotions", R.drawable.ic_angry),
        Pictogram("emotions_tired", "Cansado", "emotions", R.drawable.ic_tired),
        Pictogram("emotions_scared", "Asustado", "emotions", R.drawable.ic_scared),
        Pictogram("emotions_sick", "Enfermo", "emotions", R.drawable.ic_sick),
        Pictogram("emotions_disgust", "Asco", "emotions", R.drawable.ic_disgust),
        Pictogram("emotions_love", "Amor", "emotions", R.drawable.ic_love),
        Pictogram("emotions_pain", "Dolor", "emotions", R.drawable.ic_pain),

        // FAMILIA (family)
        Pictogram("family_me", "Yo", "family", R.drawable.ic_me),
        Pictogram("family_mom", "Mamá", "family", R.drawable.ic_mom),
        Pictogram("family_dad", "Papá", "family", R.drawable.ic_dad),
        Pictogram("family_brother", "Hermano", "family", R.drawable.ic_brother),
        Pictogram("family_sister", "Hermana", "family", R.drawable.ic_sister),
        Pictogram("family_grandma", "Abuela", "family", R.drawable.ic_grandma),
        Pictogram("family_grandpa", "Abuelo", "family", R.drawable.ic_grandpa),

        // ACCIONES (actions)
        Pictogram("actions_eat", "Comer", "actions", R.drawable.ic_eat),
        Pictogram("actions_drink", "Beber", "actions", R.drawable.ic_drink),
        Pictogram("actions_sleep", "Dormir", "actions", R.drawable.ic_sleep),
        Pictogram("actions_play", "Jugar", "actions", R.drawable.ic_play),
        Pictogram("actions_wanna", "quiero", "actions", R.drawable.ic_wanna),
        Pictogram("actions_paint", "Dibujar", "actions", R.drawable.ic_paint),
        Pictogram("actions_wash", "Bañarse", "actions", R.drawable.ic_wash),
        Pictogram("actions_listen", "Escuchar", "actions", R.drawable.ic_hear),
        Pictogram("actions_watch", "Ver", "actions", R.drawable.ic_watch),

        // OBJETOS (objects)
        Pictogram("objects_water", "Agua", "objects", R.drawable.ic_water),
        Pictogram("objects_food", "Comida", "objects", R.drawable.ic_food),
        Pictogram("objects_ball", "Pelota", "objects", R.drawable.ic_ball),
        Pictogram("objects_house", "Casa", "objects", R.drawable.ic_house),
        Pictogram("objects_toy", "Juguete", "objects", R.drawable.ic_toy),
        Pictogram("objects_book", "Libro", "objects", R.drawable.ic_book),
        Pictogram("objects_clothes", "Ropa", "objects", R.drawable.ic_clothes),

        // PERSONAS (people)
        Pictogram("people_mom", "Mamá", "people", R.drawable.ic_mom),
        Pictogram("people_dad", "Papá", "people", R.drawable.ic_dad),
        Pictogram("people_teacher", "Maestro", "people", R.drawable.ic_teacher),
        Pictogram("people_therapist", "Terapeuta", "people", R.drawable.ic_therapist),
        Pictogram("people_friend", "Amigo", "people", R.drawable.ic_brother),
        Pictogram("people_doctor", "Doctor", "people", R.drawable.ic_therapist)
    ) + listOf(
        // Nuevos pictogramas: símbolos emoji del sistema, disponibles sin conexión.
        // NECESIDADES (32)
        emojiPictogram("needs_hunger", "Hambre", "needs", "🤤"),
        emojiPictogram("needs_thirst", "Sed", "needs", "💦"),
        emojiPictogram("needs_cold", "Frío", "needs", "🥶"),
        emojiPictogram("needs_hot", "Calor", "needs", "🥵"),
        emojiPictogram("needs_fever", "Fiebre", "needs", "🤒"),
        emojiPictogram("needs_cough", "Tos", "needs", "😷"),
        emojiPictogram("needs_nausea", "Náuseas", "needs", "🤢"),
        emojiPictogram("needs_dizzy", "Mareo", "needs", "😵‍💫"),
        emojiPictogram("needs_headache", "Dolor de cabeza", "needs", "🤕"),
        emojiPictogram("needs_stomachache", "Dolor de estómago", "needs", "😖"),
        emojiPictogram("needs_sore_throat", "Dolor de garganta", "needs", "🗣️"),
        emojiPictogram("needs_toothache", "Dolor de dientes", "needs", "🦷"),
        emojiPictogram("needs_earache", "Dolor de oído", "needs", "👂"),
        emojiPictogram("needs_itch", "Picazón", "needs", "🖐️"),
        emojiPictogram("needs_vomit", "Vómito", "needs", "🤮"),
        emojiPictogram("needs_toilet", "Ir al baño", "needs", "🚽"),
        emojiPictogram("needs_wash_hands", "Lavarse las manos", "needs", "🧼"),
        emojiPictogram("needs_brush_teeth", "Cepillarse los dientes", "needs", "🪥"),
        emojiPictogram("needs_get_dressed", "Vestirse", "needs", "👕"),
        emojiPictogram("needs_undress", "Desvestirse", "needs", "🩲"),
        emojiPictogram("needs_sit", "Sentarse", "needs", "🪑"),
        emojiPictogram("needs_stand", "Levantarse", "needs", "🧍"),
        emojiPictogram("needs_privacy", "Privacidad", "needs", "🚪"),
        emojiPictogram("needs_diaper_change", "Cambiar pañal", "needs", "👶"),
        emojiPictogram("needs_wipe", "Limpiarse", "needs", "🧻"),
        emojiPictogram("needs_blow_nose", "Sonarme la nariz", "needs", "🤧"),
        emojiPictogram("needs_doctor", "Ir al doctor", "needs", "🩺"),
        emojiPictogram("needs_take_medicine", "Tomar medicina", "needs", "💊"),
        emojiPictogram("needs_drink_water", "Tomar agua", "needs", "🥛"),
        emojiPictogram("needs_eat_something", "Comer algo", "needs", "🍽️"),
        emojiPictogram("needs_drink_something", "Beber algo", "needs", "🥤"),
        emojiPictogram("needs_pee", "Hacer pipí", "needs", "💧"),

        // EMOCIONES (16)
        emojiPictogram("emotions_excited", "Emocionado", "emotions", "🤩"),
        emojiPictogram("emotions_surprised", "Sorprendido", "emotions", "😲"),
        emojiPictogram("emotions_confused", "Confundido", "emotions", "🤔"),
        emojiPictogram("emotions_worried", "Preocupado", "emotions", "😟"),
        emojiPictogram("emotions_nervous", "Nervioso", "emotions", "😬"),
        emojiPictogram("emotions_frustrated", "Frustrado", "emotions", "😤"),
        emojiPictogram("emotions_bored", "Aburrido", "emotions", "😑"),
        emojiPictogram("emotions_embarrassed", "Avergonzado", "emotions", "😳"),
        emojiPictogram("emotions_jealous", "Celoso", "emotions", "😒"),
        emojiPictogram("emotions_proud", "Orgulloso", "emotions", "😎"),
        emojiPictogram("emotions_lonely", "Solo", "emotions", "🧍"),
        emojiPictogram("emotions_calm", "Tranquilo", "emotions", "😌"),
        emojiPictogram("emotions_relieved", "Aliviado", "emotions", "😮‍💨"),
        emojiPictogram("emotions_disappointed", "Decepcionado", "emotions", "😞"),
        emojiPictogram("emotions_impatient", "Impaciente", "emotions", "⏳"),
        emojiPictogram("emotions_guilty", "Culpable", "emotions", "😓"),

        // FAMILIA (13)
        emojiPictogram("family_aunt", "Tía", "family", "👩‍🦰"),
        emojiPictogram("family_uncle", "Tío", "family", "👨‍🦰"),
        emojiPictogram("family_cousin_boy", "Primo", "family", "🧒"),
        emojiPictogram("family_cousin_girl", "Prima", "family", "👭"),
        emojiPictogram("family_baby", "Bebé", "family", "👶"),
        emojiPictogram("family_son", "Hijo", "family", "👦"),
        emojiPictogram("family_daughter", "Hija", "family", "👧"),
        emojiPictogram("family_stepfather", "Padrastro", "family", "👨‍👦"),
        emojiPictogram("family_stepmother", "Madrastra", "family", "👩‍👧"),
        emojiPictogram("family_husband", "Esposo", "family", "🤵"),
        emojiPictogram("family_wife", "Esposa", "family", "👰"),
        emojiPictogram("family_partner", "Pareja", "family", "❤️"),
        emojiPictogram("family_guardian", "Tutor o tutora", "family", "🧑‍🧒"),

        // ACCIONES (26)
        emojiPictogram("actions_walk", "Caminar", "actions", "🚶"),
        emojiPictogram("actions_run", "Correr", "actions", "🏃"),
        emojiPictogram("actions_jump", "Saltar", "actions", "🤸"),
        emojiPictogram("actions_sit", "Sentarse", "actions", "🪑"),
        emojiPictogram("actions_stand", "Levantarse", "actions", "🧍"),
        emojiPictogram("actions_open", "Abrir", "actions", "📂"),
        emojiPictogram("actions_close", "Cerrar", "actions", "🔒"),
        emojiPictogram("actions_give", "Dar", "actions", "🎁"),
        emojiPictogram("actions_take", "Tomar", "actions", "🤲"),
        emojiPictogram("actions_go", "Ir", "actions", "➡️"),
        emojiPictogram("actions_come", "Venir", "actions", "👋"),
        emojiPictogram("actions_help", "Ayudar", "actions", "🤝"),
        emojiPictogram("actions_wait", "Esperar", "actions", "⏳"),
        emojiPictogram("actions_stop", "Parar", "actions", "🛑"),
        emojiPictogram("actions_start", "Empezar", "actions", "▶️"),
        emojiPictogram("actions_finish", "Terminar", "actions", "🏁"),
        emojiPictogram("actions_read", "Leer", "actions", "📖"),
        emojiPictogram("actions_write", "Escribir", "actions", "✍️"),
        emojiPictogram("actions_painting", "Pintar", "actions", "🎨"),
        emojiPictogram("actions_sing", "Cantar", "actions", "🎤"),
        emojiPictogram("actions_dance", "Bailar", "actions", "💃"),
        emojiPictogram("actions_speak", "Hablar", "actions", "🗣️"),
        emojiPictogram("actions_laugh", "Reír", "actions", "😆"),
        emojiPictogram("actions_cry", "Llorar", "actions", "😢"),
        emojiPictogram("actions_washing", "Lavar", "actions", "🧼"),
        emojiPictogram("actions_brush", "Cepillarse", "actions", "🪥"),

        // OBJETOS (28)
        emojiPictogram("objects_phone", "Teléfono", "objects", "📱"),
        emojiPictogram("objects_tablet", "Tableta", "objects", "📲"),
        emojiPictogram("objects_computer", "Computadora", "objects", "💻"),
        emojiPictogram("objects_pencil", "Lápiz", "objects", "✏️"),
        emojiPictogram("objects_paper", "Papel", "objects", "📄"),
        emojiPictogram("objects_notebook", "Cuaderno", "objects", "📓"),
        emojiPictogram("objects_backpack", "Mochila", "objects", "🎒"),
        emojiPictogram("objects_chair", "Silla", "objects", "🪑"),
        emojiPictogram("objects_table", "Mesa", "objects", "🪵"),
        emojiPictogram("objects_bed", "Cama", "objects", "🛏️"),
        emojiPictogram("objects_pillow", "Almohada", "objects", "🛌"),
        emojiPictogram("objects_blanket", "Manta", "objects", "🧣"),
        emojiPictogram("objects_shoes", "Zapatos", "objects", "👟"),
        emojiPictogram("objects_socks", "Calcetines", "objects", "🧦"),
        emojiPictogram("objects_pants", "Pantalón", "objects", "👖"),
        emojiPictogram("objects_shirt", "Camisa", "objects", "👕"),
        emojiPictogram("objects_coat", "Abrigo", "objects", "🧥"),
        emojiPictogram("objects_glass", "Vaso", "objects", "🥛"),
        emojiPictogram("objects_plate", "Plato", "objects", "🍽️"),
        emojiPictogram("objects_spoon", "Cuchara", "objects", "🥄"),
        emojiPictogram("objects_fork", "Tenedor", "objects", "🍴"),
        emojiPictogram("objects_bottle", "Botella", "objects", "🍼"),
        emojiPictogram("objects_toothbrush", "Cepillo dental", "objects", "🪥"),
        emojiPictogram("objects_toothpaste", "Pasta dental", "objects", "🧴"),
        emojiPictogram("objects_soap", "Jabón", "objects", "🧼"),
        emojiPictogram("objects_towel", "Toalla", "objects", "🧻"),
        emojiPictogram("objects_door", "Puerta", "objects", "🚪"),
        emojiPictogram("objects_window", "Ventana", "objects", "🪟"),

        // PERSONAS (19)
        emojiPictogram("people_me", "Yo", "people", "🙋"),
        emojiPictogram("people_you", "Tú", "people", "🫵"),
        emojiPictogram("people_he", "Él", "people", "👨"),
        emojiPictogram("people_she", "Ella", "people", "👩"),
        emojiPictogram("people_male_classmate", "Compañero", "people", "👨‍🤝‍👨"),
        emojiPictogram("people_female_classmate", "Compañera", "people", "👭"),
        emojiPictogram("people_professor", "Profesor", "people", "🧑‍🏫"),
        emojiPictogram("people_professora", "Profesora", "people", "👩‍🏫"),
        emojiPictogram("people_director", "Director", "people", "🧑‍💼"),
        emojiPictogram("people_directora", "Directora", "people", "👩‍💼"),
        emojiPictogram("people_nurse_male", "Enfermero", "people", "🧑‍⚕️"),
        emojiPictogram("people_nurse_female", "Enfermera", "people", "👩‍⚕️"),
        emojiPictogram("people_dentist", "Dentista", "people", "🦷"),
        emojiPictogram("people_police", "Policía", "people", "👮"),
        emojiPictogram("people_firefighter", "Bombero", "people", "🧑‍🚒"),
        emojiPictogram("people_salesperson", "Vendedor", "people", "🛍️"),
        emojiPictogram("people_driver", "Conductor", "people", "🚘"),
        emojiPictogram("people_neighbor_male", "Vecino", "people", "🏠"),
        emojiPictogram("people_neighbor_female", "Vecina", "people", "🏡")
    )

    private fun emojiPictogram(
        id: String,
        name: String,
        categoryId: String,
        emoji: String
    ) = Pictogram(
        id = id,
        nombre = name,
        categoriaId = categoryId,
        imageResId = null,
        visualEmoji = emoji
    )

    private val quickPhrases = listOf(
        FraseRapida("quick_bathroom", "Quiero ir al baño", R.drawable.ic_bathroom),
        FraseRapida("quick_thirsty", "Tengo sed", R.drawable.ic_water),
        FraseRapida("quick_hungry", "Tengo hambre", R.drawable.ic_food),
        FraseRapida("quick_rest", "Quiero descansar", R.drawable.ic_sleep),
        FraseRapida("quick_help", "Ayuda por favor", R.drawable.ic_help),
        FraseRapida("quick_play", "Quiero jugar", R.drawable.ic_play)
    )

    override fun getCategories(): List<Categoria> = categories

    override fun getPictograms(): List<Pictogram> = pictograms

    override fun getQuickPhrases(): List<FraseRapida> = quickPhrases

    override fun getPictogramsByCategory(categoryId: String): List<Pictogram> {
        return pictograms.filter { it.categoriaId == categoryId }
    }
}



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
        Pictogram("needs_water", "Agua", "needs", R.drawable.ic_water),
        Pictogram("needs_food", "Comida", "needs", R.drawable.ic_food),
        Pictogram("needs_bathroom", "Baño", "needs", R.drawable.ic_bathroom),
        Pictogram("needs_sleep", "Dormir", "needs", R.drawable.ic_sleep),
        Pictogram("needs_help", "Ayuda", "needs", R.drawable.ic_help),

        Pictogram("emotions_happy", "Feliz", "emotions", R.drawable.ic_happy),
        Pictogram("emotions_sad", "Triste", "emotions", R.drawable.ic_sad),
        Pictogram("emotions_angry", "Enojado", "emotions", R.drawable.ic_angry),
        Pictogram("emotions_tired", "Cansado", "emotions", R.drawable.ic_tired),

        Pictogram("family_mom", "Mamá", "family", R.drawable.ic_mom),
        Pictogram("family_dad", "Papá", "family", R.drawable.ic_dad),
        Pictogram("family_brother", "Hermano", "family", R.drawable.ic_brother),
        Pictogram("family_sister", "Hermana", "family", R.drawable.ic_sister),

        Pictogram("actions_eat", "Comer", "actions", R.drawable.ic_eat),
        Pictogram("actions_drink", "Beber", "actions", R.drawable.ic_drink),
        Pictogram("actions_sleep", "Dormir", "actions", R.drawable.ic_sleep),
        Pictogram("actions_play", "Jugar", "actions", R.drawable.ic_play),
        Pictogram("actions_go", "Ir", "actions", R.drawable.ic_go),

        Pictogram("objects_water", "Agua", "objects", R.drawable.ic_water),
        Pictogram("objects_food", "Comida", "objects", R.drawable.ic_food),
        Pictogram("objects_ball", "Pelota", "objects", R.drawable.ic_ball),
        Pictogram("objects_house", "Casa", "objects", R.drawable.ic_house),

        Pictogram("people_mom", "Mamá", "people", R.drawable.ic_mom),
        Pictogram("people_dad", "Papá", "people", R.drawable.ic_dad),
        Pictogram("people_teacher", "Maestro", "people", R.drawable.ic_teacher),
        Pictogram("people_therapist", "Terapeuta", "people", R.drawable.ic_therapist)
    )

    private val quickPhrases = listOf(
        FraseRapida("quick_bathroom", "Quiero ir al baño", R.drawable.ic_bathroom),
        FraseRapida("quick_thirsty", "Tengo sed", R.drawable.ic_water),
        FraseRapida("quick_hungry", "Tengo hambre", R.drawable.ic_food),
        FraseRapida("quick_rest", "Quiero descansar", R.drawable.ic_sleep),
        FraseRapida("quick_help", "Ayuda por favor", R.drawable.ic_help)
    )

    override fun getCategories(): List<Categoria> = categories

    override fun getPictograms(): List<Pictogram> = pictograms

    override fun getQuickPhrases(): List<FraseRapida> = quickPhrases

    override fun getPictogramsByCategory(categoryId: String): List<Pictogram> {
        return pictograms.filter { it.categoriaId == categoryId }
    }
}
